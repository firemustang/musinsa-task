package com.musinsa.api_task.api.service;

import com.musinsa.api_task.api.dto.request.BrandRequest;
import com.musinsa.api_task.api.dto.response.*;
import com.musinsa.api_task.common.exception.BaseException;
import com.musinsa.api_task.common.exception.code.ErrorCode;
import com.musinsa.api_task.domain.dto.CategoryPriceDto;
import com.musinsa.api_task.domain.dto.PriceInfoDto;
import com.musinsa.api_task.domain.entity.Brand;
import com.musinsa.api_task.domain.entity.ProductPrice;
import com.musinsa.api_task.domain.enums.Category;
import com.musinsa.api_task.domain.repository.BrandRepository;
import com.musinsa.api_task.domain.repository.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final ProductPriceRepository productPriceRepository;

    @Transactional(readOnly = true)
    public BrandLowestPriceResponse findLowestPricePerCategory() {
        List<ProductPrice> allProductPrices = productPriceRepository.findAll();

        if (allProductPrices.isEmpty()) {
            throw new BaseException(ErrorCode.NO_BRANDS_FOUND);
        }

        Map<Category, ProductPriceResponse> lowestPrices = new EnumMap<>(Category.class);

        for (ProductPrice productPrice : allProductPrices) {
            Category category = productPrice.getCategory();
            int price = productPrice.getPrice();
            String brandName = productPrice.getBrand().getName();
            ProductPriceResponse currentLowestProduct = lowestPrices.get(category);

            if (currentLowestProduct == null || price < currentLowestProduct.price()) {
                lowestPrices.put(category, new ProductPriceResponse(productPrice.getId(), brandName, category.getDisplayName(), price));
            }
        }

        int totalAmount = lowestPrices.values().stream()
                .mapToInt(ProductPriceResponse::price)
                .sum();

        Map<String, ProductPriceResponse> sortedLowestPrices = lowestPrices.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getDisplayName(),
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return new BrandLowestPriceResponse(sortedLowestPrices, totalAmount);
    }


    @Transactional(readOnly = true)
    public BrandLowestTotalPriceResponse findLowestTotalPriceBrand() {
        Optional<Brand> optionalBrand = brandRepository.findAllByOrderByIdDesc().stream()
                .min(Comparator.comparingInt(brand -> productPriceRepository.findByBrandIdOrderByCategoryAsc(brand.getId()).stream()
                        .mapToInt(ProductPrice::getPrice).sum()));

        if (optionalBrand.isEmpty()) {
            throw new BaseException(ErrorCode.NO_BRANDS_FOUND);
        }

        Brand brand = optionalBrand.get();

        List<CategoryPriceDto> categories = productPriceRepository.findByBrandIdOrderByCategoryAsc(brand.getId()).stream()
                .map(price -> new CategoryPriceDto(price.getCategory().getDisplayName(), price.getPrice()))
                .sorted(Comparator.comparing(dto -> Category.fromDisplayName(dto.category()).ordinal()))
                .collect(Collectors.toList());

        int totalPrice = categories.stream()
                .mapToInt(CategoryPriceDto::price)
                .sum();

        return new BrandLowestTotalPriceResponse(brand.getName(), categories, totalPrice);
    }

    @Transactional(readOnly = true)
    public PriceInfoDto findLowestPriceByCategory(final String categoryName) {
        Category category = Category.fromDisplayName(categoryName);
        List<ProductPrice> prices = productPriceRepository.findByCategory(category);

        if (prices.isEmpty()) {
            throw new BaseException(ErrorCode.NO_PRODUCTS_FOUND);
        }

        Optional<ProductPrice> lowestPrice = prices.stream()
                .min(Comparator.comparingInt(ProductPrice::getPrice));

        return lowestPrice
                .map(price -> new PriceInfoDto(price.getId(), price.getBrand().getName(), price.getPrice()))
                .orElseThrow(() -> new BaseException(ErrorCode.NO_PRODUCTS_FOUND));
    }

    @Transactional(readOnly = true)
    public PriceInfoDto findHighestPriceByCategory(final String categoryName) {
        Category category = Category.fromDisplayName(categoryName);
        List<ProductPrice> prices = productPriceRepository.findByCategory(category);

        if (prices.isEmpty()) {
            throw new BaseException(ErrorCode.NO_PRODUCTS_FOUND);
        }

        Optional<ProductPrice> highestPrice = prices.stream()
                .max(Comparator.comparingInt(ProductPrice::getPrice));

        return highestPrice
                .map(price -> new PriceInfoDto(price.getId(), price.getBrand().getName(), price.getPrice()))
                .orElseThrow(() -> new BaseException(ErrorCode.NO_PRODUCTS_FOUND));
    }

    @Transactional
    public BrandResponse addOrUpdateBrand(final BrandRequest brandRequest) {

        Brand brand = brandRepository.findByName(brandRequest.name())
                .orElseGet(() -> new Brand(brandRequest.name()));

        List<ProductPrice> updatedProductPrices = brandRequest.products().stream()
                .map(dto -> {
                    try {
                        Category category = Category.fromDisplayName(dto.category());
                        Optional<ProductPrice> existingProduct = brand.getProductPrices().stream()
                                .filter(p -> p.getCategory().equals(category))
                                .findFirst();

                        if (existingProduct.isPresent()) {
                            ProductPrice product = existingProduct.get();
                            return new ProductPrice(brand, product.getCategory(), dto.price());
                        } else {
                            return new ProductPrice(brand, category, dto.price());
                        }
                    } catch (IllegalArgumentException e) {
                        throw new BaseException(ErrorCode.INVALID_CATEGORY);
                    }
                })
                .collect(Collectors.toList());

        brand.setProductPrices(updatedProductPrices);
        Brand savedBrand = brandRepository.save(brand);

        List<ProductPriceResponse> productPriceResponses = savedBrand.getProductPrices().stream()
                .map(productPrice -> new ProductPriceResponse(
                        productPrice.getId(),
                        productPrice.getBrand().getName(),
                        productPrice.getCategory().getDisplayName(),
                        productPrice.getPrice()))
                .collect(Collectors.toList());

        return new BrandResponse(savedBrand.getId(), savedBrand.getName(), productPriceResponses);
    }


    @Transactional
    public DeleteBrandResponse deleteBrand(final Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BaseException(ErrorCode.NO_BRANDS_FOUND));
        brandRepository.delete(brand);
        return new DeleteBrandResponse(brand.getId());
    }

    @Transactional
    public DeleteProductPriceResponse deleteProduct(final Long productId) {
        ProductPrice productPrice = productPriceRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.NO_PRODUCTS_FOUND));
        productPriceRepository.delete(productPrice);
        return new DeleteProductPriceResponse(productPrice.getId());
    }

    @Transactional(readOnly = true)
    public List<AllBrandListResponse> findAllBrands() {
        List<Brand> brands = brandRepository.findAll();

        return brands.stream()
                .map(brand -> new AllBrandListResponse(brand.getId(), brand.getName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<BrandResponse> findAllBrandsWithProducts(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAll(pageable);

        return brands.map(brand -> {
            List<ProductPriceResponse> productPriceResponses = brand.getProductPrices().stream()
                    .map(productPrice -> new ProductPriceResponse(
                            productPrice.getId(),
                            productPrice.getBrand().getName(),
                            productPrice.getCategory().getDisplayName(),
                            productPrice.getPrice()))
                    .collect(Collectors.toList());

            return new BrandResponse(brand.getId(), brand.getName(), productPriceResponses);
        });
    }

}
