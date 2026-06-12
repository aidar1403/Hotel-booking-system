package org.example.service;

import org.example.model.Booking;
import org.example.model.BookingStatus;
import org.example.model.RoomType;
import org.example.model.RoomStatus;
import org.example.model.Room;
import org.example.repository.RoomRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final List <Booking> bookings = new ArrayList<>();
    private final RoomRepository roomRepository;

    public BookingService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

public void createBooking (String clientName, int numberOfGuests,RoomType requestedRoomType,String startDate,String endDate) {

        var availableRooms = roomRepository.findAvailableByType(requestedRoomType);
        Booking booking;

        if (!availableRooms.isEmpty()) {
            var room = availableRooms.get(0);
            booking = new Booking(clientName, numberOfGuests, requestedRoomType, startDate, endDate);
            booking.setAssignedRoom(room.getId());
            booking.setStatus(BookingStatus.CONFIRMED);
            room.setStatus(RoomStatus.OCCUPIED);
            System.out.println("Бронь создана! Комната " + room.getRoomNumber() + " назначена.");
        }else{
            booking = new Booking(clientName, numberOfGuests, requestedRoomType, startDate, endDate);
            booking.setStatus(BookingStatus.PENDING);
            System.out.println("Внимание! Нет свободных комнат типа "+requestedRoomType+". Бронь в статусе ожидания.");
        }
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

            Integer roomId = b.getAssignedRoom();
            if (roomId != null) {
                Room room = roomRepository.findRoomById(roomId);
                if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
                    room.setStatus(RoomStatus.PENDING);
                    System.out.println("Комната " + room.getRoomNumber() + " ожидает уборки");
                }
            }
            System.out.println("Бронь " + b.getId() + " завершена (выезд)");
        }else if (start.isAfter(today) && !end.isBefore(today)) {
            if (b.getStatus() != BookingStatus.ACTIVE) {
                b.setStatus(BookingStatus.ACTIVE);
                System.out.println("Бронь "+b.getId()+" активна (гости в номере)");
            }
        }else if (start.isAfter(today)) {
            if (b.getStatus() != BookingStatus.CONFIRMED) {
                b.setStatus(BookingStatus.CONFIRMED);
            }
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

public void cleanRoom(int roomId) {
        Room room = roomRepository.findRoomById(roomId);
        if (room == null) {
            System.out.println("Комната с ID "+roomId+" не найдена");
            return;
        }
        if (room.getStatus() == RoomStatus.PENDING) {
            room.setStatus(RoomStatus.AVAILABLE);
            System.out.println("Комната "+ room.getRoomNumber()+" убрана и готова к заселению");
        }else{
            System.out.println("Комната "+room.getRoomNumber()+" не требует уборки (статус: "+room.getStatus()+")");
        }
}
}
