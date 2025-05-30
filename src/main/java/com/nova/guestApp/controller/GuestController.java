package com.nova.guestApp.controller;

import com.nova.guestApp.dtos.request.CheckoutRequest;
import com.nova.guestApp.dtos.request.GuestRequest;
import com.nova.guestApp.dtos.response.AllGuestResponse;
import com.nova.guestApp.dtos.response.CustomResponse;
import com.nova.guestApp.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/guest")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/check-in")
    public CustomResponse checkin(@RequestBody GuestRequest request) {
        return guestService.checkIn(request);
    }

    ///  TODO: implement checkout in service impl
    @PostMapping("/check-out/{id}")
    public CustomResponse checkout(@RequestBody CheckoutRequest request, @PathVariable int id) {
        return guestService.checkOut(request, id);
    }

    @GetMapping("/all")
    public AllGuestResponse getAllGuests(){
        return guestService.getAllGuest();
    }
}
