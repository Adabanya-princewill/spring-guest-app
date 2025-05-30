package com.nova.guestApp.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestRequest {
    private String name;
    private String phoneNumber;
    private String staffName;
    private String tagId;
    private String purposeOfVisit;
    private String email;
    private String checkInTime;
}
