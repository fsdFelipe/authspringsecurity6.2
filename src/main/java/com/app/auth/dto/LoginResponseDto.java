package com.app.auth.dto;

import java.util.Set;

import com.app.auth.enums.Perfil;
import com.app.auth.enums.TokenType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
	@JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_token_expiry")
    private String accessTokenExpiry;

    @JsonProperty("token_type")
    private TokenType tokenType;

    @JsonProperty("email")
    private String email;
    
    @JsonProperty("perfis")
    private Set<Perfil> perfis;
}
