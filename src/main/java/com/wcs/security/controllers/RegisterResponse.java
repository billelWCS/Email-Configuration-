package com.wcs.security.controllers;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterResponse {
    private String email;
}
