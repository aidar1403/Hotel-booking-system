package org.example.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String,String> home() {
        return Map.of(
                "status","API is running",
                "message","Welcome to Hotel System Booking API!",
                "rooms_endpoint","/api/rooms",
                "bookings_endpoint","/api/bookings"
        );
    }
}
