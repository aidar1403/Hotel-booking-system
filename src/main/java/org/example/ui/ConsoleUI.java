package org.example.ui;

import org.example.model.Room;
import org.example.model.RoomType;
import org.example.service.RoomService;
import org.example.service.BookingService;
import java.util.Scanner;
import java.util.List;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final RoomService roomService;
    private final BookingService bookingService;

    public ConsoleUI(RoomService roomService,BookingService bookingService) {
        this.roomService = roomService;
        this.bookingService = bookingService;
    }

    public void start() {
        System.out.println("\t\t\t\tHOTEL-BOOKING-SYSTEM\t\t\t\t");
        boolean running = true;
        while (running) {
showMainMenu();
int choice = getIntInput("\nChoose option :");
            switch (choice) {
                case 1:
                    addRooms();
                case 2:
                    showAllRooms();
            }
        }
    }
    private void addRooms() {
        int start = getIntInput("Введите номер первой комнаты :");
        int end = getIntInput("Введите номер последней комнаты :");

        System.out.println("Выберите тип комнаты:");
        System.out.println("1. STANDARD");
        System.out.println("2. LUX");
        System.out.println("3. PRESIDENT");
        int typeChoice = getIntInput("Ваш выбор: ");

        RoomType type;
        switch (typeChoice) {
            case 1 -> type = RoomType.STANDARD;
            case 2 -> type = RoomType.LUX;
            case 3 -> type = RoomType.PRESIDENT;
            default -> {
                System.out.println("Неверный выбор. Устанавливаем STANDARD.");
                type = RoomType.STANDARD;
            }
        }
        int capacity = getIntInput("Введите вместимость комнаты (количество гостей): ");

        roomService.addRooms(start,end,type,capacity);
    }

    private void showMainMenu() {
        System.out.println("1.  Добавить комнаты (Диапозон)");
        System.out.println("2.  Показать все комнаты");
        System.out.println("3.  Показать свободные комнаты по типу");
        System.out.println("4.  Создать бронь");
        System.out.println("5.  Показать все брони");
        System.out.println("6.  Найти бронь по ID");
        System.out.println("7.  Отменить бронь");
        System.out.println("8.  Назначить комнату брони (вручную)");
        System.out.println("9.  Обновить статусы броней (автоматически)");
        System.out.println("10. Отменить комнату как убранную");
        System.out.println("0.  Выход");
    }

    private void showAllRooms(){
        List<Room>rooms = roomService.getAllRooms();

        if (rooms.isEmpty()){
            System.out.println("Не добавлено комнат. Сначала добавьте комнаты");
            return;
        }
        System.out.println("\t\t\tВсе комнаты\t\t\t");
        System.out.printf("%-5s | %-8s | %-10s | %-5s | %-10s%n",
                          "ID", "Номер", "Тип", "Вмест.", "Статус");
        System.out.println("---------------------------------------------");

        for (Room r : rooms) {
            System.out.printf("%-5d | %-8s | %-10s | %-5d | %-10s%n",
                    r.getId(),
                    r.getRoomNumber(),
                    r.getType(),
                    r.getCapacity(),
                    r.getStatus());
        }
        System.out.println("Всего комнат: "+ rooms.size());
    }

private int getIntInput(String prompt) {
        while (true) {
            System.out.println(prompt);
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("Пожалуйста введите корректный номер");
            }
        }
}
    }
