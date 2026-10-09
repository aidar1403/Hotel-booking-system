package org.example.service;

import org.example.model.Booking;
import org.example.model.BookingStatus;
import org.example.model.Room;
import org.example.model.RoomStatus;
import org.example.model.RoomType;
import org.example.repository.BookingRepository;
import org.example.repository.RoomRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public void createBooking(String clientName, int numberOfGuests,
                              RoomType requestedRoomType,
                              String startDate, String endDate) {

        List<Room> availableRooms = roomRepository.findAvailableByType(requestedRoomType);
        Booking booking = new Booking(clientName, numberOfGuests,
                requestedRoomType, startDate, endDate);

        if (!availableRooms.isEmpty()) {
            Room room = availableRooms.get(0);
            booking.setAssignedRoom(room.getId());
            booking.setStatus(BookingStatus.CONFIRMED);
            room.setStatus(RoomStatus.OCCUPIED);
            roomRepository.updateRoom(room);
            System.out.println("Бронь создана! Комната " + room.getRoomNumber() + " назначена.");
        } else {
            booking.setStatus(BookingStatus.PENDING);
            System.out.println("Внимание! Нет свободных комнат типа " + requestedRoomType +
                    ". Бронь в статусе ожидания.");
        }

        bookingRepository.save(booking);
        System.out.println("Бронь создана! ID: " + booking.getId());
    }

    public void updateStatuses() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Booking> bookings = bookingRepository.findAll();
        for (Booking b : bookings) {
            LocalDate start = LocalDate.parse(b.getStartDate(), formatter);
            LocalDate end = LocalDate.parse(b.getEndDate(), formatter);

            if (b.getStatus() == BookingStatus.CANCELLED) continue;

            if (end.isBefore(today)) {
                b.setStatus(BookingStatus.COMPLETED);
                Integer roomId = b.getAssignedRoom();
                if (roomId != null) {
                    Room room = roomRepository.findRoomById(roomId);
                    if (room != null && room.getStatus() == RoomStatus.OCCUPIED) {
                        room.setStatus(RoomStatus.PENDING);
                        roomRepository.updateRoom(room);
                        System.out.println("Комната " + room.getRoomNumber() + " ожидает уборки");
                    }
                }
                System.out.println("Бронь " + b.getId() + " завершена (выезд)");
            } else if (!start.isAfter(today) && !end.isBefore(today)) {
                if (b.getStatus() != BookingStatus.ACTIVE) {
                    b.setStatus(BookingStatus.ACTIVE);
                    System.out.println("Бронь " + b.getId() + " активна (гости в номере)");
                }
            } else if (start.isAfter(today)) {
                if (b.getStatus() != BookingStatus.CONFIRMED) {
                    b.setStatus(BookingStatus.CONFIRMED);
                }
            }
            bookingRepository.update(b);
        }
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking findBookingById(int id) {
        return bookingRepository.findById(id);
    }

    public void cancelBooking(int id) {
        Booking booking = bookingRepository.findById(id);
        if (booking != null && booking.getStatus() != BookingStatus.CANCELLED) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.update(booking);
            System.out.println("Бронь ID " + id + " отменена");
        } else {
            System.out.println("Бронь не найдена или уже отменена");
        }
    }

    public void assignRoom(int bookingId, int roomId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking != null) {
            booking.setAssignedRoom(roomId);
            bookingRepository.update(booking);
            System.out.println("Комната " + roomId + " назначена брони " + bookingId);
        } else {
            System.out.println("Бронь не найдена");
        }
    }

    public void cleanRoom(int roomId) {
        Room room = roomRepository.findRoomById(roomId);
        if (room == null) {
            System.out.println("Комната с ID " + roomId + " не найдена");
            return;
        }
        if (room.getStatus() == RoomStatus.PENDING) {
            room.setStatus(RoomStatus.AVAILABLE);
            roomRepository.updateRoom(room);
            System.out.println("Комната " + room.getRoomNumber() + " убрана и готова к заселению");
        } else {
            System.out.println("Комната " + room.getRoomNumber() +
                    " не требует уборки (статус: " + room.getStatus() + ")");
        }
    }
}