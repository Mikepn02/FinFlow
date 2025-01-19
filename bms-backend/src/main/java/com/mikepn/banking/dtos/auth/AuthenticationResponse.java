package com.mikepn.banking.dtos.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mikepn.banking.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("user")
    private User user;
}
