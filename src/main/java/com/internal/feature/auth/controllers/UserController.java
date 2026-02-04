package com.internal.feature.auth.controllers;

import com.internal.config.RequiresRole;
import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.auth.dto.request.*;
import com.internal.feature.auth.dto.response.AllUserResponseDto;
import com.internal.feature.auth.dto.response.UserResponseDto;
import com.internal.feature.auth.service.AuthService;
import com.internal.feature.auth.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Tag(name = "User Management")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ApiResponse<AllUserResponseDto>> getAllUsers(@RequestBody GetAllUserRequestDto request) {
        log.info("Fetching users - page: {}, size: {}, search: '{}', status: {}",
                request.getPageNo(), request.getPageSize(), request.getSearch(), request.getStatus());

        AllUserResponseDto result = userService.getAllUser(request);

        log.info("Successfully retrieved {} users (page {}/{})",
                result.getContent().size(), result.getPageNo(), result.getTotalPages());

        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", result));
    }

    @PostMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserDetail(@PathVariable Long id) {
        log.info("Fetching user details for ID: {}", id);

        UserResponseDto user = userService.getUserById(id);

        return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", user));
    }

    @PostMapping("/create-user")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody RegisterRequestDto registerDto) {
        log.info("Admin user creation request for ID card: {}", registerDto.getUsername());

        UserResponseDto userResponse = authService.createUserByAdmin(registerDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", userResponse));
    }

    @PostMapping("/deleteById/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> deleteUser(@PathVariable Long id) {
        UserResponseDto deletedUser = userService.deleteUserId(id);

        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", deletedUser));
    }

    @PostMapping("/updateById/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDto request) {
        UserResponseDto updatedUser = userService.updateUserId(id, request);

        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<UserResponseDto>> changePassword(@Valid @RequestBody ChangePasswordRequestDto changePasswordDto) {
        UserResponseDto userDto = userService.changePassword(changePasswordDto);

        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", userDto));
    }

    @PostMapping("/change-password-by-admin")
    public ResponseEntity<ApiResponse<UserResponseDto>> changePasswordByAdmin(@Valid @RequestBody ChangePasswordByAdminRequestDto changePasswordDto) {
        UserResponseDto userDto = userService.changePasswordByAdmin(changePasswordDto);

        return ResponseEntity.ok(ApiResponse.success("Password changed by admin successfully", userDto));
    }
}
