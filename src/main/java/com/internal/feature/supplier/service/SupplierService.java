package com.internal.feature.supplier.service;

import com.internal.feature.supplier.dto.request.CreateSupplierRequestDto;
import com.internal.feature.supplier.dto.request.GetAllSupplierRequestDto;
import com.internal.feature.supplier.dto.request.UpdateSupplierRequestDto;
import com.internal.feature.supplier.dto.response.AllSupplierResponseDto;
import com.internal.feature.supplier.dto.response.SupplierResponseDto;

public interface SupplierService {
    AllSupplierResponseDto getAllSuppliers(GetAllSupplierRequestDto requestDto);

    SupplierResponseDto getSupplierById(Long id);

    SupplierResponseDto createSupplier(CreateSupplierRequestDto requestDto);

    SupplierResponseDto updateSupplier(Long id, UpdateSupplierRequestDto requestDto);

    SupplierResponseDto deleteSupplier(Long id);
}

