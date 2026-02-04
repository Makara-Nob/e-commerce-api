package com.internal.feature.supplier.service.impl;

import com.internal.enumation.StatusData;
import com.internal.exceptions.error.NotFoundException;
import com.internal.feature.supplier.dto.request.CreateSupplierRequestDto;
import com.internal.feature.supplier.dto.request.GetAllSupplierRequestDto;
import com.internal.feature.supplier.dto.request.UpdateSupplierRequestDto;
import com.internal.feature.supplier.dto.response.AllSupplierResponseDto;
import com.internal.feature.supplier.dto.response.SupplierResponseDto;
import com.internal.feature.supplier.mapper.SupplierMapper;
import com.internal.feature.supplier.model.Supplier;
import com.internal.feature.supplier.repository.SupplierRepository;
import com.internal.feature.supplier.service.SupplierService;
import com.internal.feature.supplier.specification.SupplierSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional(readOnly = true)
    public AllSupplierResponseDto getAllSuppliers(GetAllSupplierRequestDto requestDto) {
        log.debug("Fetching all suppliers with filter: {}", requestDto);

        Specification<Supplier> spec = Specification
                .where(SupplierSpecification.search(requestDto.getSearch()))
                .and(SupplierSpecification.hasStatus(requestDto.getStatus()));

        Pageable pageable = PageRequest.of(requestDto.getPageNo() - 1, requestDto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Supplier> suppliers = supplierRepository.findAll(spec, pageable);

        log.info("Fetched {} suppliers (page {} of {})",
                suppliers.getNumberOfElements(), suppliers.getNumber() + 1, suppliers.getTotalPages());

        List<SupplierResponseDto> supplierDTOList = supplierMapper.mapToListDto(suppliers);
        return supplierMapper.mapToPaginateDto(supplierDTOList, suppliers);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDto getSupplierById(Long id) {
        log.debug("Fetching supplier by ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Supplier not found with ID: {}", id);
                    return new NotFoundException("Supplier not found with ID: " + id);
                });

        log.debug("Fetched supplier: id={}, name='{}'", supplier.getId(), supplier.getName());
        return supplierMapper.toDto(supplier);
    }

    @Override
    public SupplierResponseDto createSupplier(CreateSupplierRequestDto requestDto) {
        log.info("Request to create supplier: name='{}'", requestDto.getName());

        try {
            Supplier supplier = supplierMapper.toEntity(requestDto);
            if (supplier.getStatus() == null) {
                supplier.setStatus(StatusData.ACTIVE);
            }

            Supplier savedSupplier = supplierRepository.save(supplier);

            log.info("Supplier created successfully: id={}, name='{}'", savedSupplier.getId(), savedSupplier.getName());
            return supplierMapper.toDto(savedSupplier);

        } catch (Exception e) {
            log.error("Error creating supplier: name='{}', message={}",
                    requestDto.getName(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SupplierResponseDto updateSupplier(Long id, UpdateSupplierRequestDto requestDto) {
        log.info("Request to update supplier: id={}, name='{}'", id, requestDto.getName());

        try {
            Supplier existingSupplier = supplierRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Supplier not found for update: id={}", id);
                        return new NotFoundException("Supplier not found with ID: " + id);
                    });

            supplierMapper.updateEntity(requestDto, existingSupplier);
            Supplier updatedSupplier = supplierRepository.save(existingSupplier);

            log.info("Supplier updated successfully: id={}, name='{}'", updatedSupplier.getId(), updatedSupplier.getName());
            return supplierMapper.toDto(updatedSupplier);

        } catch (Exception e) {
            log.error("Error updating supplier id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SupplierResponseDto deleteSupplier(Long id) {
        log.info("Request to delete supplier: id={}", id);

        try {
            Supplier supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Supplier not found for deletion: id={}", id);
                        return new NotFoundException("Supplier not found with ID: " + id);
                    });

            supplierRepository.delete(supplier);
            log.info("Supplier deleted successfully: id={}, name='{}'", id, supplier.getName());

            return supplierMapper.toDto(supplier);

        } catch (Exception e) {
            log.error("Error deleting supplier id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}

