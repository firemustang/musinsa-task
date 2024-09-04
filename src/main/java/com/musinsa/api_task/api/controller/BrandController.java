package com.musinsa.api_task.api.controller;

import com.musinsa.api_task.api.dto.request.BrandRequest;
import com.musinsa.api_task.api.dto.response.*;
import com.musinsa.api_task.api.service.BrandService;
import com.musinsa.api_task.common.exception.code.ErrorCode;
import com.musinsa.api_task.common.exception.code.SuccessCode;
import com.musinsa.api_task.common.response.CommonResponse;
import com.musinsa.api_task.domain.dto.PriceInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/lowest-price")
    public ResponseEntity<CommonResponse<BrandLowestPriceResponse>> getLowestPricePerCategory() {
        try {
            BrandLowestPriceResponse response = brandService.findLowestPricePerCategory();
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS, response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/lowest-total-price")
    public ResponseEntity<CommonResponse<BrandLowestTotalPriceResponse>> getLowestTotalPriceBrand() {
        try{
            BrandLowestTotalPriceResponse response = brandService.findLowestTotalPriceBrand();
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS, response));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/price-info")
    public ResponseEntity<CommonResponse<PriceInfoResponse>> getPriceInfoByCategory(@RequestParam String categoryName) {
        try {
            PriceInfoDto lowestPrice = brandService.findLowestPriceByCategory(categoryName);
            PriceInfoDto highestPrice = brandService.findHighestPriceByCategory(categoryName);
            PriceInfoResponse response = new PriceInfoResponse(categoryName, lowestPrice, highestPrice);
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS, response));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping
    public ResponseEntity<CommonResponse<BrandResponse>> addOrUpdateBrand(@RequestBody BrandRequest brandRequest) {
        try {
            BrandResponse response = brandService.addOrUpdateBrand(brandRequest);
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS_INSERT, response));
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INVALID_CATEGORY));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/delete-brand/{brandId}")
    public ResponseEntity<CommonResponse<DeleteBrandResponse>> deleteBrand(@PathVariable Long brandId) {
        try {
            DeleteBrandResponse response = brandService.deleteBrand(brandId);
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS_DELETE, response));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<CommonResponse<DeleteProductPriceResponse>> deleteProduct(@PathVariable Long productId) {
        try {
            DeleteProductPriceResponse response = brandService.deleteProduct(productId);
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS_DELETE, response));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/allBrands")
    public ResponseEntity<CommonResponse<List<AllBrandListResponse>>> getAllBrands() {
        try{
            List<AllBrandListResponse> allBrandListResponses = brandService.findAllBrands();
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS, allBrandListResponses));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/brands-with-products")
    public ResponseEntity<CommonResponse<Page<BrandResponse>>> getAllBrandsWithProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            Page<BrandResponse> brandResponses = brandService.findAllBrandsWithProducts(pageable);
            return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS, brandResponses));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }
}
