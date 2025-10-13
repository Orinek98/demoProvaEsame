package com.dsbd.project.controller;

import com.dsbd.project.entity.Trip;
import com.dsbd.project.security.AuthResponse;
import com.dsbd.project.service.ProjectTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/")
public class TripController {

    @Autowired
    ProjectTripService tripService;

    @GetMapping(path = "trips")
    public @ResponseBody Iterable<Trip> getAll(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "departureTime") String sortBy
    ) {
        return tripService.findTrips(id, origin, destination, maxPrice, sortBy);
    }

    @PostMapping(path = "trips")
    public @ResponseBody Trip createTrip(@RequestBody Trip trip) {
        return tripService.addTrip(trip);
    }

    @PostMapping(path = "trips/{tripId}/buy")
    public @ResponseBody AuthResponse buyTrip(
            @PathVariable Integer tripId,
            Authentication authentication
    ) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();

        try {
            tripService.buyTrip(email, tripId);
            return new AuthResponse("Acquisto completato con successo per il viaggio ID: " + tripId);
        } catch (Exception e) {
            return new AuthResponse("Errore nell'acquisto: " + e.getMessage());
        }
    }

}

