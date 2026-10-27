package org.example.web;

import org.example.model.Booking;
import org.example.repository.BookingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")

public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @GetMapping("{id}")
    public Booking getBookingById(@PathVariable int id){
        return bookingRepository.findById(id);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking){
        return bookingRepository.save(booking);
    }

    @PutMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable int id) {
        Booking booking = bookingRepository.findById(id);
        if (booking != null){
            booking.setStatus(org.example.model.BookingStatus.CANCELLED);
            bookingRepository.update(booking);
            return "Booking "+booking.getId()+" has been cancelled";
        }
        return "Booking "+id+" not found";
    }

}
