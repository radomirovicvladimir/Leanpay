package org.leanpay.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {

    // Not null and not empty
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
