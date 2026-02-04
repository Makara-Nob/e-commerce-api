package com.internal.feature.product.service.impl;

import com.internal.enumation.StatusData;
import com.internal.exceptions.error.AlreadyExistException;
import com.internal.exceptions.error.NotFoundException;
import com.internal.feature.inventory.model.Category;
import com.internal.feature.inventory.repository.CategoryRepository;
import com.internal.feature.product.dto.request.CreateProductRequestDto;
import com.internal.feature.product.dto.request.GetAllProductRequestDto;
import com.internal.feature.product.dto.request.UpdateProductRequestDto;
import com.internal.feature.product.dto.response.AllProductResponseDto;
import com.internal.feature.product.dto.response.ProductResponseDto;
import com.internal.feature.product.mapper.ProductMapper;
import com.internal.feature.product.model.Product;
import com.internal.feature.product.repository.ProductRepository;
import com.internal.feature.product.service.ProductService;
import com.internal.feature.supplier.model.Supplier;
import com.internal.feature.supplier.repository.SupplierRepository;
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
import com.internal.feature.product.specification.ProductSpecification;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public AllProductResponseDto getAllProducts(GetAllProductRequestDto requestDto) {
        log.debug("Fetching all products with filter: {}", requestDto);

        Specification<Product> spec = Specification
                .where(ProductSpecification.search(requestDto.getSearch()))
                .and(ProductSpecification.hasStatus(requestDto.getStatus()));

        Pageable pageable = PageRequest.of(requestDto.getPageNo() - 1, requestDto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Product> products = productRepository.findAll(spec, pageable);

        log.info("Fetched {} products (page {} of {})",
                products.getNumberOfElements(), products.getNumber() + 1, products.getTotalPages());

        List<ProductResponseDto> productDTOList = productMapper.mapToListDto(products);
        return productMapper.mapToPaginateDto(productDTOList, products);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        log.debug("Fetching product by ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", id);
                    return new NotFoundException("Product not found with ID: " + id);
                });

        log.debug("Fetched product: id={}, name='{}'", product.getId(), product.getName());
        return productMapper.toDto(product);
    }

    @Override
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto) {
        log.info("Request to create product: name='{}', sku='{}'", requestDto.getName(), requestDto.getSku());

        try {
            validateCreateProduct(requestDto);

            // Fetch Category
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> {
                        log.warn("Category not found with ID: {}", requestDto.getCategoryId());
                        return new NotFoundException("Category not found with ID: " + requestDto.getCategoryId());
                    });

            // Fetch Supplier (optional)
            Supplier supplier = null;
            if (requestDto.getSupplierId() != null) {
                supplier = supplierRepository.findById(requestDto.getSupplierId())
                        .orElseThrow(() -> {
                            log.warn("Supplier not found with ID: {}", requestDto.getSupplierId());
                            return new NotFoundException("Supplier not found with ID: " + requestDto.getSupplierId());
                        });
            }

            Product product = productMapper.toEntity(requestDto);
            product.setCategory(category);
            product.setSupplier(supplier);

            if (product.getQuantity() == null) {
                product.setQuantity(0);
            }
            if (product.getMinStock() == null) {
                product.setMinStock(5);
            }
            if (product.getStatus() == null) {
                product.setStatus(StatusData.ACTIVE);
            }

            Product savedProduct = productRepository.save(product);

            log.info("Product created successfully: id={}, name='{}', category='{}', supplier='{}'",
                    savedProduct.getId(), savedProduct.getName(),
                    category.getName(), supplier != null ? supplier.getName() : "none");
            return productMapper.toDto(savedProduct);

        } catch (Exception e) {
            log.error("Error creating product: name='{}', sku='{}', message={}",
                    requestDto.getName(), requestDto.getSku(), e.getMessage(), e);
            throw e;
        }
    }

    private void validateCreateProduct(CreateProductRequestDto requestDto) {
        if (productRepository.existsBySku(requestDto.getSku())) {
            log.warn("Product creation failed — duplicate SKU: {}", requestDto.getSku());
            throw new AlreadyExistException("Product with SKU '" + requestDto.getSku() + "' already exists");
        }
    }

    @Override
    public ProductResponseDto updateProduct(Long id, UpdateProductRequestDto requestDto) {
        log.info("Request to update product: id={}, name='{}', sku='{}'", id, requestDto.getName(), requestDto.getSku());

        try {
            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Product not found for update: id={}", id);
                        return new NotFoundException("Product not found with ID: " + id);
                    });

            validateUpdateProduct(id, requestDto, existingProduct);

            // Update Category if provided
            if (requestDto.getCategoryId() != null) {
                Category category = categoryRepository.findById(requestDto.getCategoryId())
                        .orElseThrow(() -> {
                            log.warn("Category not found with ID: {}", requestDto.getCategoryId());
                            return new NotFoundException("Category not found with ID: " + requestDto.getCategoryId());
                        });
                existingProduct.setCategory(category);
            }

            // Update Supplier if provided
            if (requestDto.getSupplierId() != null) {
                Supplier supplier = supplierRepository.findById(requestDto.getSupplierId())
                        .orElseThrow(() -> {
                            log.warn("Supplier not found with ID: {}", requestDto.getSupplierId());
                            return new NotFoundException("Supplier not found with ID: " + requestDto.getSupplierId());
                        });
                existingProduct.setSupplier(supplier);
            }

            productMapper.updateEntity(requestDto, existingProduct);
            Product updatedProduct = productRepository.save(existingProduct);

            log.info("Product updated successfully: id={}, name='{}'", updatedProduct.getId(), updatedProduct.getName());
            return productMapper.toDto(updatedProduct);

        } catch (Exception e) {
            log.error("Error updating product id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    private void validateUpdateProduct(Long id, UpdateProductRequestDto requestDto, Product existingProduct) {
        if (requestDto.getSku() != null
                && !existingProduct.getSku().equals(requestDto.getSku())
                && productRepository.existsBySkuAndIdNot(requestDto.getSku(), id)) {
            log.warn("Product update failed — duplicate SKU: {}", requestDto.getSku());
            throw new AlreadyExistException("Product with SKU '" + requestDto.getSku() + "' already exists");
        }
    }

    @Override
    public ProductResponseDto deleteProduct(Long id) {
        log.info("Request to delete product: id={}", id);

        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Product not found for deletion: id={}", id);
                        return new NotFoundException("Product not found with ID: " + id);
                    });

            productRepository.delete(product);
            log.info("Product deleted successfully: id={}, name='{}'", id, product.getName());

            return productMapper.toDto(product);

        } catch (Exception e) {
            log.error("Error deleting product id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}