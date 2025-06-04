package com.nova.guestApp.service;

import com.nova.guestApp.dtos.request.CheckoutRequest;
import com.nova.guestApp.dtos.request.GuestRequest;
import com.nova.guestApp.dtos.response.AllGuestResponse;
import com.nova.guestApp.dtos.response.CustomResponse;
import org.springframework.stereotype.Service;

@Service
public interface GuestService {
    CustomResponse checkIn(GuestRequest request);

    CustomResponse checkOut(CheckoutRequest request, int id, boolean isTagSubmitted);

    AllGuestResponse getAllGuest();
}
