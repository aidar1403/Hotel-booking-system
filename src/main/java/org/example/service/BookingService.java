package org.example.service;

import org.example.model.Booking;
import org.example.model.BookingStatus;
import org.example.model.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final List <Booking> bookings = new ArrayList<>();

public void createBooking (String clientName, int numberOfGuests,RoomType requestedRoomType,String startDate,String endDate) {
    Booking booking = new Booking (clientName,numberOfGuests,requestedRoomType,startDate,endDate);
    bookings.add(booking);
    System.out.println("Бронь создана! ID: " + booking.getId());
}

public void updateStatuses () {
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (Booking b : bookings) {
        LocalDate start = LocalDate.parse(b.getStartDate(),formatter);
        LocalDate end = LocalDate.parse(b.getEndDate(),formatter);

        if (b.getStatus()==BookingStatus.CANCELLED) {
            continue;
        }

        if (end.isBefore(today)) {
            b.setStatus(BookingStatus.COMPLETED);
            System.out.println("Бронь "+b.getId()+" завершена (выезд)");
        }else if (!start.isAfter(today)&& !end.isBefore(today)) {
            b.setStatus(BookingStatus.ACTIVE);
            System.out.println("Бронь "+b.getId()+" активна (гости в номере)");
        }else if (start.isAfter(today)) {
            b.setStatus(BookingStatus.CONFIRMED);
        }
    }
}


public List <Booking> getAllBookings(){
    return new ArrayList<>(bookings);
}

public Booking findBookingById (int id) {
    for (Booking  c : bookings) {
        if (c.getId()==id) return c;
    }
    return null;
}

public void cancelBooking (int id) {
    Booking booking = findBookingById(id);
    if (booking != null && booking.getStatus() != BookingStatus.CANCELLED) {
        booking.setStatus(BookingStatus.CANCELLED);
        System.out.println("Бронь ID " + id + " отменена");
    }else{
        System.out.println("Бронь не найдена или уже отменена");
    }
}

public void assignRoom(int bookingId, int roomId){
    Booking booking = findBookingById(bookingId);
    if (booking != null) {
        booking.setAssignedRoom(roomId);
        System.out.println("Комната " + roomId + " назначена брони " + bookingId);
    }else{
        System.out.println("Бронь не найдена");
    }
}
}
