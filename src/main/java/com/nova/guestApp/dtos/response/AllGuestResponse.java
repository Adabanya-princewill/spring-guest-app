package com.nova.guestApp.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllGuestResponse {
    @JsonProperty("code")
    private String responseCode;
    @JsonProperty("message")
    private String responseMessage;
    @JsonProperty("data")
    private List<GuestResponse> response;
}

