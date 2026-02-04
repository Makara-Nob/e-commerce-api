package com.internal.feature.supplier.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.supplier.dto.request.CreateSupplierRequestDto;
import com.internal.feature.supplier.dto.request.GetAllSupplierRequestDto;
import com.internal.feature.supplier.dto.request.UpdateSupplierRequestDto;
import com.internal.feature.supplier.dto.response.AllSupplierResponseDto;
import com.internal.feature.supplier.dto.response.SupplierResponseDto;
import com.internal.feature.supplier.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    // ----------------- CRUD -----------------
    @PostMapping
    public ResponseEntity<ApiResponse<SupplierResponseDto>> createSupplier(
            @Valid @RequestBody CreateSupplierRequestDto requestDTO) {
        SupplierResponseDto responseDTO = supplierService.createSupplier(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Supplier created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSupplierRequestDto requestDTO) {
        SupplierResponseDto responseDTO = supplierService.updateSupplier(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> getSupplierById(@PathVariable Long id) {
        SupplierResponseDto responseDTO = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier retrieved successfully", responseDTO));
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllSupplierResponseDto>> getAllSuppliers(@RequestBody GetAllSupplierRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success("Suppliers fetched successfully", supplierService.getAllSuppliers(requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierResponseDto>> deleteSupplier(@PathVariable Long id) {
        SupplierResponseDto supplierDTO = supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", supplierDTO));
    }
}

