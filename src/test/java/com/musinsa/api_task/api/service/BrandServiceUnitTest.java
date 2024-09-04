package com.musinsa.api_task.api.service;

import com.musinsa.api_task.api.dto.request.BrandRequest;
import com.musinsa.api_task.api.dto.request.ProductPriceRequest;
import com.musinsa.api_task.api.dto.response.BrandLowestPriceResponse;
import com.musinsa.api_task.api.dto.response.BrandResponse;
import com.musinsa.api_task.common.exception.BaseException;
import com.musinsa.api_task.common.exception.code.ErrorCode;
import com.musinsa.api_task.domain.dto.PriceInfoDto;
import com.musinsa.api_task.domain.entity.Brand;
import com.musinsa.api_task.domain.entity.ProductPrice;
import com.musinsa.api_task.domain.enums.Category;
import com.musinsa.api_task.domain.repository.BrandRepository;
import com.musinsa.api_task.domain.repository.ProductPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BrandServiceUnitTest {
    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductPriceRepository productPriceRepository;


    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 인서트업데이트테스트() throws Exception {
        //given
        BrandRequest brandRequest = new BrandRequest("testBrand", List.of(new ProductPriceRequest("상의", 1000)));
        Brand testBrand = new Brand("testBrand");
        testBrand.setProductPrices(List.of(new ProductPrice(testBrand, Category.TOP, 1000)));

        when(brandRepository.findByName("testBrand")).thenReturn(Optional.of(testBrand));
        when(brandRepository.save(any(Brand.class))).thenReturn(testBrand);

        //when
        BrandResponse brandResponse = brandService.addOrUpdateBrand(brandRequest);
        //then
        assertEquals("testBrand", brandResponse.name());
        assertEquals(1, brandResponse.products().size());
        verify(brandRepository, times(1)).save(any(Brand.class));
    }


    @Test
    public void 인서트업데이트테스트_실패케이스() throws BaseException {
        //given
        BrandRequest brandRequest = new BrandRequest("BrandA", List.of(
                new ProductPriceRequest("아무거나카테고리", 10000)
        ));

        //when
        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(new Brand("BrandA")));

        //then
        BaseException exception = assertThrows(BaseException.class, () -> {
            brandService.addOrUpdateBrand(brandRequest);
        });

        assertEquals(ErrorCode.INVALID_CATEGORY, exception.getErrorCode());
    }


    //구현1)
    @Test
    public void 카테고리별최저가격브랜드와_상품가격_총액을조회() throws Exception {
        //given
        List<ProductPrice> productPrices = List.of(
                new ProductPrice(new Brand("브랜드11"), Category.TOP, 1111),
                new ProductPrice(new Brand("브랜드22"), Category.TOP, 2222),
                new ProductPrice(new Brand("브랜드33"), Category.TOP, 3333)
        );
        //when
        when(productPriceRepository.findAll()).thenReturn(productPrices);
        //then
        BrandLowestPriceResponse response = brandService.findLowestPricePerCategory();
        assertEquals(1111, response.totalAmount());
        assertEquals("브랜드11", response.lowestPrices().get("상의").brandName());

    }

    //구현2 - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
    @Test
    void 단일브랜드_최저가브랜드_카테고리() throws Exception {
        //given
        Brand testBrand = new Brand("TestBrand");
        List<ProductPrice> productPrices = List.of(
                new ProductPrice(testBrand, Category.HAT, 1000),
                new ProductPrice(testBrand, Category.SNEAKERS, 500),
                new ProductPrice(testBrand, Category.OUTER, 3000)
        );

    }

    @Test
    void 카테코리이름으로최저가() {
        //given
        List<ProductPrice> productPrices = List.of(
                new ProductPrice(new Brand("Brand11"), Category.HAT, 1111),
                new ProductPrice(new Brand("Brand22"), Category.HAT, 2222),
                new ProductPrice(new Brand("Brand33"), Category.HAT, 3333),
                new ProductPrice(new Brand("Brand44"), Category.HAT, 4444)
        );
        when(productPriceRepository.findByCategory(Category.HAT)).thenReturn(productPrices);
        //when
        PriceInfoDto lowestPrice = brandService.findLowestPriceByCategory("모자");

        //then
        assertEquals(1111, lowestPrice.price());
        assertEquals("Brand11", lowestPrice.brandName());

    }

    @Test
    void 카테코리이름으로최고가() {
        // Given
        List<ProductPrice> productPrices = List.of(
                new ProductPrice(new Brand("BrandA"), Category.TOP, 10000),
                new ProductPrice(new Brand("BrandB"), Category.TOP, 9000)
        );

        when(productPriceRepository.findByCategory(Category.TOP)).thenReturn(productPrices);

        // When
        PriceInfoDto highestPrice = brandService.findHighestPriceByCategory("상의");

        // Then
        assertEquals(10000, highestPrice.price());
        assertEquals("BrandA", highestPrice.brandName());
    }

}