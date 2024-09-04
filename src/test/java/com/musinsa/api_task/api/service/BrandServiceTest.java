//package com.musinsa.api_task.api.service;
//
//import com.musinsa.api_task.api.dto.response.BrandLowestPriceResponse;
//import com.musinsa.api_task.api.dto.response.BrandLowestTotalPriceResponse;
//import com.musinsa.api_task.common.exception.BaseException;
//import com.musinsa.api_task.common.exception.code.ErrorCode;
//import com.musinsa.api_task.domain.dto.CategoryPriceDto;
//import com.musinsa.api_task.domain.dto.PriceInfoDto;
//import com.musinsa.api_task.domain.entity.Brand;
//import com.musinsa.api_task.domain.entity.ProductPrice;
//import com.musinsa.api_task.domain.repository.BrandRepository;
//import com.musinsa.api_task.domain.repository.ProductPriceRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//class BrandServiceTest {
//
//    @Mock
//    private BrandRepository brandRepository;
//
//    @Mock
//    private ProductPriceRepository productPriceRepository;
//
//
//    @InjectMocks
//    private BrandService brandService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testFindLowestPricePerCategory_Success() {
//        // Given
//        Brand brandA = new Brand("A");
//        Brand brandB = new Brand("B");
//
//        ProductPrice productPrice1 = new ProductPrice(brandA, "상의", 10000);
//        ProductPrice productPrice2 = new ProductPrice(brandB, "상의", 9000);
//        ProductPrice productPrice3 = new ProductPrice(brandA, "아우터", 20000);
//        ProductPrice productPrice4 = new ProductPrice(brandB, "아우터", 15000);
//
//        when(productPriceRepository.findAll()).thenReturn(Arrays.asList(productPrice1, productPrice2, productPrice3, productPrice4));
//
//        // When
//        BrandLowestPriceResponse response = brandService.findLowestPricePerCategory();
//
//        // Then
//        assertNotNull(response);
//        assertEquals(2, response.lowestPrices().size());
//        assertEquals("B", response.lowestPrices().get("상의").name());
//        assertEquals("B", response.lowestPrices().get("아우터").name());
//        assertEquals(24000, response.totalAmount());
//    }
//
//    @Test
//    void testFindLowestPricePerCategory_NoProductsFound() {
//        // Given
//        when(productPriceRepository.findAll()).thenReturn(List.of());
//
//        // When & Then
//        BaseException exception = assertThrows(BaseException.class, () -> {
//            brandService.findLowestPricePerCategory();
//        });
//
//        assertEquals("NO_BRANDS_FOUND", exception.getErrorCode());
//    }
//
//    @Test
//    void testFindLowestTotalPriceBrand_Success() {
//        // Given
//        Brand brandA = new Brand("A");
//        Brand brandB = new Brand("B");
//
//        ProductPrice productPrice1 = new ProductPrice(brandA, "상의", 10000);
//        ProductPrice productPrice2 = new ProductPrice(brandA, "아우터", 15000);
//        ProductPrice productPrice3 = new ProductPrice(brandB, "상의", 9000);
//        ProductPrice productPrice4 = new ProductPrice(brandB, "아우터", 14000);
//
//        when(brandRepository.findAllByOrderByIdDesc()).thenReturn(Arrays.asList(brandA, brandB));
//        when(productPriceRepository.findByBrandIdOrderByCategoryAsc(brandA.getId())).thenReturn(Arrays.asList(productPrice1, productPrice2));
//        when(productPriceRepository.findByBrandIdOrderByCategoryAsc(brandB.getId())).thenReturn(Arrays.asList(productPrice3, productPrice4));
//
//        // When
//        BrandLowestTotalPriceResponse response = brandService.findLowestTotalPriceBrand();
//
//        // Then
//        assertNotNull(response);
//        assertEquals("B", response.brandName());
//        assertEquals(2, response.categories().size());
//        assertEquals("상의", response.categories().get(0).category());
//        assertEquals(23000, response.totalPrice());
//    }
//
//    @Test
//    void testFindLowestTotalPriceBrand_NoBrandsFound() {
//        // Given
//        when(brandRepository.findAllByOrderByIdDesc()).thenReturn(List.of());
//
//        // When & Then
//        BaseException exception = assertThrows(BaseException.class, () -> {
//            brandService.findLowestTotalPriceBrand();
//        });
//
//        assertEquals("NO_BRANDS_FOUND", exception.getErrorCode());
//    }
//
//    @Test
//    void testFindLowestPriceByCategory_Success() {
//        // Given
//        Brand brandA = new Brand("A");
//        Brand brandB = new Brand("B");
//
//        ProductPrice productPrice1 = new ProductPrice(brandA, "상의", 10000);
//        ProductPrice productPrice2 = new ProductPrice(brandB, "상의", 9000);
//
//        when(productPriceRepository.findByCategory("상의")).thenReturn(Arrays.asList(productPrice1, productPrice2));
//
//        // When
//        PriceInfoDto response = brandService.findLowestPriceByCategory("상의");
//
//        // Then
//        assertNotNull(response);
//        assertEquals("B", response.brandName());
//        assertEquals(9000, response.price());
//    }
//
//    @Test
//    void testFindHighestPriceByCategory_Success() {
//        // Given
//        Brand brandA = new Brand("A");
//        Brand brandB = new Brand("B");
//
//        ProductPrice productPrice1 = new ProductPrice(brandA, "상의", 10000);
//        ProductPrice productPrice2 = new ProductPrice(brandB, "상의", 9000);
//
//        when(productPriceRepository.findByCategory("상의")).thenReturn(Arrays.asList(productPrice1, productPrice2));
//
//        // When
//        PriceInfoDto response = brandService.findHighestPriceByCategory("상의");
//
//        // Then
//        assertNotNull(response);
//        assertEquals("A", response.brandName());
//        assertEquals(10000, response.price());
//    }
//
//    @Test
//    void testFindPriceByCategory_NoProductsFound() {
//        // Given
//        String nonExistentCategory = "전자기기";
//        when(productPriceRepository.findByCategory(nonExistentCategory)).thenReturn(List.of());
//
//        // When & Then
//        BaseException exception = assertThrows(BaseException.class, () -> {
//            brandService.findLowestPriceByCategory(nonExistentCategory);
//        });
//
//        assertEquals("NO_PRODUCTS_FOUND", exception.getErrorCode());
//    }
//}