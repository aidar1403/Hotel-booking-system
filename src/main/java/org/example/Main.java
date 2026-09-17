package org.example;
import org.example.repository.InMemoryRoomRepository;
import org.example.repository.RoomRepository;
import org.example.service.BookingService;
import org.example.service.RoomService;
import org.example.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        RoomRepository roomRepository = new InMemoryRoomRepository();
        RoomService roomService = new RoomService(roomRepository);
        BookingService bookingService = new BookingService(roomRepository);

        ConsoleUI ui = new ConsoleUI(roomService, bookingService);
        ui.start();
    }
}