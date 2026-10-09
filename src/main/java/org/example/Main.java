package org.example;
import org.example.repository.BookingRepository;
import org.example.repository.PostgresBookingRepository;
import org.example.repository.*;
import org.example.service.BookingService;
import org.example.service.RoomService;
import org.example.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        RoomRepository roomRepository = new PostgresRoomRepository();
        RoomService roomService = new RoomService(roomRepository);
        BookingRepository bookingRepository = new PostgresBookingRepository();
        BookingService bookingService = new BookingService(bookingRepository,roomRepository);

        ConsoleUI ui = new ConsoleUI(roomService, bookingService);
        ui.start();
    }
}