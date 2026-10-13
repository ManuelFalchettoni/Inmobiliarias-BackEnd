package com.manuel.zaguan_inmobiliarias.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserLoginResponse {
    private String token;
}
