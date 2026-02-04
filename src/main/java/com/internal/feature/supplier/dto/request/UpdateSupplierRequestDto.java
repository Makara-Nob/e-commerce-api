package com.internal.feature.supplier.dto.request;

import com.internal.enumation.StatusData;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateSupplierRequestDto {
    private String name;
    private String contactPerson;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String phone;
    private String address;
    private StatusData status;
}

