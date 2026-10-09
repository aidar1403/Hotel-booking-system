package org.example.repository;

import org.example.model.Booking;
import java.util.List;

public interface BookingRepository {
    Booking save(Booking booking);
    List<Booking> findAll();
    Booking findById(int id);
    void deleteById(int id);
    void update(Booking booking);
}
