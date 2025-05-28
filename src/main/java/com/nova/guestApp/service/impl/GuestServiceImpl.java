package com.nova.guestApp.service.impl;

import com.nova.guestApp.dtos.request.CheckoutRequest;
import com.nova.guestApp.dtos.request.GuestRequest;
import com.nova.guestApp.dtos.response.AllGuestResponse;
import com.nova.guestApp.dtos.response.CustomResponse;
import com.nova.guestApp.dtos.response.GuestResponse;
import com.nova.guestApp.enums.Status;
import com.nova.guestApp.model.Guest;
import com.nova.guestApp.repository.GuestRepository;
import com.nova.guestApp.repository.UserRepository;
import com.nova.guestApp.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomResponse checkIn(GuestRequest request) {
        // Find the most recent guest with the same tagId who is still CHECKED_IN
        Guest guestFind = guestRepository.findFirstByTagIdAndStatusOrderByCheckInTimeDesc(request.getTagId(), Status.CHECKED_IN);

        if (guestFind != null) {
            return CustomResponse.builder()
                    .responseCode("000")
                    .responseMessage("Guest with tag ID already checked-in")
                    .response(null)
                    .build();
        }

        Guest guest = Guest.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .staffName(request.getStaffName())
                .tagId(request.getTagId())
                .purposeOfVisit(request.getPurposeOfVisit())
                .email(request.getEmail())
                .status(Status.CHECKED_IN)
                .checkedOutBy(null)
                .checkOutTime(null)
                .checkInTime(request.getCheckInTime())
                .build();

        Guest savedGuest = guestRepository.save(guest);

        return CustomResponse.builder()
                .responseCode("002")
                .responseMessage("Guest checked-in successfully")
                .response(GuestResponse.builder()
                        .id(savedGuest.getId())
                        .name(savedGuest.getName())
                        .phoneNumber(savedGuest.getPhoneNumber())
                        .staffName(savedGuest.getStaffName())
                        .tagId(savedGuest.getTagId())
                        .purposeOfVisit(savedGuest.getPurposeOfVisit())
                        .email(savedGuest.getEmail())
                        .status(savedGuest.getStatus())
                        .checkedOutBy(null)
                        .checkInTime(String.valueOf(savedGuest.getCheckInTime()))
                        .checkOutTime(null)
                        .build()
                )
                .build();
    }


    @Override
    public CustomResponse checkOut(CheckoutRequest request, int id) {
        Guest findGuest = guestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(findGuest.getStatus() == Status.CHECKED_IN){
           findGuest.setCheckedOutBy(request.getName());
           findGuest.setCheckOutTime(request.getCheckOutTime());
           findGuest.setStatus(Status.CHECKED_OUT);

           Guest savedGuest = guestRepository.save(findGuest);

           return CustomResponse.builder()
                   .responseCode("002")
                   .responseMessage("Checkout successful")
                   .response(GuestResponse.builder()
                           .checkOutTime(String.valueOf(request.getCheckOutTime()))
                           .id(savedGuest.getId())
                           .name(savedGuest.getName())
                           .phoneNumber(savedGuest.getPhoneNumber())
                           .staffName(savedGuest.getStaffName())
                           .tagId(savedGuest.getTagId())
                           .purposeOfVisit(savedGuest.getPurposeOfVisit())
                           .email(savedGuest.getEmail())
                           .status(savedGuest.getStatus())
                           .checkedOutBy(savedGuest.getCheckedOutBy())
                           .checkInTime(String.valueOf(savedGuest.getCheckInTime()))
                           .build())
                   .build();
        }
        return CustomResponse.builder()
                .responseCode("000")
                .responseMessage("Already checked out")
                .response(null)
                .build();
    }

    @Override
    public AllGuestResponse getAllGuest() {
        List<Guest> guests = guestRepository.findAll();

        List<GuestResponse> response = new ArrayList<>();

        for(Guest guest: guests){
            GuestResponse res = new GuestResponse();
            res.setId(guest.getId());
            res.setName(guest.getName());
            res.setPhoneNumber(guest.getPhoneNumber());
            res.setStaffName(guest.getStaffName());
            res.setTagId(guest.getTagId());
            res.setStatus(guest.getStatus());
            res.setPurposeOfVisit(guest.getPurposeOfVisit());
            res.setEmail(guest.getEmail());
            res.setCheckedOutBy(guest.getCheckedOutBy());
            res.setCheckInTime(String.valueOf(guest.getCheckInTime()));
            response.add(res);
        }
        return AllGuestResponse.builder()
                .responseCode("004")
                .responseMessage("Guests gotten successfully")
                .response(response)
                .build();
    }
}
