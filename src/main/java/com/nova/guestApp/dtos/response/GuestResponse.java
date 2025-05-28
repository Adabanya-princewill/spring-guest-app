package com.nova.guestApp.dtos.response;

import com.nova.guestApp.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestResponse {
    private int id;
    private String name;
    private String phoneNumber;
    private String staffName;
    private String tagId;
    private String purposeOfVisit;
    private String email;
    private Status status;
    private String checkedOutBy;
    private String checkInTime;
    private String checkOutTime;
}
