package com.nova.guestApp.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse {
    @JsonProperty("code")
    private String responseCode;
    @JsonProperty("message")
    private String responseMessage;
    @JsonProperty("data")
    private GuestResponse response;
}
