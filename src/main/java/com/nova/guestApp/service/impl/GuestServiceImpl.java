package com.nova.guestApp.service.impl;

import com.nova.guestApp.dtos.request.CheckoutRequest;
import com.nova.guestApp.dtos.request.EmailDetailsRequest;
import com.nova.guestApp.dtos.request.GuestRequest;
import com.nova.guestApp.dtos.response.AllGuestResponse;
import com.nova.guestApp.dtos.response.CustomResponse;
import com.nova.guestApp.dtos.response.GuestResponse;
import com.nova.guestApp.enums.CardStatus;
import com.nova.guestApp.enums.Status;
import com.nova.guestApp.model.Guest;
import com.nova.guestApp.repository.GuestRepository;
import com.nova.guestApp.repository.UserRepository;
import com.nova.guestApp.service.EmailService;
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

    @Autowired
    private EmailService emailService;

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
                .name(request.getName().toLowerCase())
                .phoneNumber(request.getPhoneNumber())
                .staffName(request.getStaffName().toLowerCase())
                .tagId(request.getTagId())
                .purposeOfVisit(request.getPurposeOfVisit())
                .email(request.getEmail().toLowerCase())
                .status(Status.CHECKED_IN)
                .cardStatus(CardStatus.NOT_SUBMITTED)
                .comment(null)
                .checkedOutBy(null)
                .checkOutTime(null)
                .checkInTime(request.getCheckInTime())
                .build();

        Guest savedGuest = guestRepository.save(guest);

        //send email alert
        EmailDetailsRequest emailDetails = EmailDetailsRequest.builder()
                .recipient(savedGuest.getEmail())
                .subject("Check in alert from Nova bank")
                .messageBody("Hi, " + savedGuest.getName() + " \n" +
                        "You have been checked in with the tag id: " + savedGuest.getTagId() +
                        "\n" +
                        "\n" +
                        "Kindly see the receptionist to checkout to your way out."
                )
                .build();
        emailService.sendEmailAlert(emailDetails);
        //email sent

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
                        .cardStatus(savedGuest.getCardStatus())
                        .checkInTime(savedGuest.getCheckInTime())
                        .checkOutTime(null)
                        .build()
                )
                .build();
    }


    @Override
    public CustomResponse checkOut(CheckoutRequest request, int id, boolean isTagSubmitted) {
        Guest findGuest = guestRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (findGuest.getStatus() == Status.CHECKED_IN) {
            findGuest.setCheckedOutBy(request.getName());
            findGuest.setCheckOutTime(request.getCheckOutTime());
            findGuest.setStatus(Status.CHECKED_OUT);
            if (isTagSubmitted) {
                findGuest.setCardStatus(CardStatus.SUBMITTED);
            } else {
                findGuest.setCardStatus(CardStatus.NOT_SUBMITTED);
                findGuest.setComment(request.getComment());
            }
            Guest savedGuest = guestRepository.save(findGuest);

            //send email alert
            EmailDetailsRequest emailDetails = EmailDetailsRequest.builder()
                    .recipient(savedGuest.getEmail())
                    .subject("Check out alert from Nova bank")
                    .messageBody("Hi, " + savedGuest.getName() + " \n" +
                            "You have been checked out with the tag id: " + savedGuest.getTagId() + " successfully." +
                            "\n" +
                            "\n" +
                            "Thank you for visiting nova bank.🎉"
                    )
                    .build();
            emailService.sendEmailAlert(emailDetails);
            //email sent

            return CustomResponse.builder()
                    .responseCode("002")
                    .responseMessage("Checkout successful")
                    .response(GuestResponse.builder()
                            .checkOutTime(request.getCheckOutTime())
                            .id(savedGuest.getId())
                            .name(savedGuest.getName())
                            .phoneNumber(savedGuest.getPhoneNumber())
                            .staffName(savedGuest.getStaffName())
                            .tagId(savedGuest.getTagId())
                            .purposeOfVisit(savedGuest.getPurposeOfVisit())
                            .email(savedGuest.getEmail())
                            .status(savedGuest.getStatus())
                            .cardStatus(savedGuest.getCardStatus())
                            .checkedOutBy(savedGuest.getCheckedOutBy())
                            .checkInTime(savedGuest.getCheckInTime())
                            .comment(savedGuest.getComment())
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

        for (Guest guest : guests) {
            GuestResponse res = new GuestResponse();
            res.setId(guest.getId());
            res.setName(guest.getName());
            res.setPhoneNumber(guest.getPhoneNumber());
            res.setStaffName(guest.getStaffName());
            res.setTagId(guest.getTagId());
            res.setStatus(guest.getStatus());
            res.setPurposeOfVisit(guest.getPurposeOfVisit());
            res.setEmail(guest.getEmail());
            res.setCardStatus(guest.getCardStatus());
            res.setComment(guest.getComment());
            res.setCheckedOutBy(guest.getCheckedOutBy());
            res.setCheckInTime(guest.getCheckInTime());
            res.setCheckOutTime(guest.getCheckOutTime());
            response.add(res);
        }
        return AllGuestResponse.builder()
                .responseCode("004")
                .responseMessage("Guests gotten successfully")
                .response(response)
                .build();
    }
}
