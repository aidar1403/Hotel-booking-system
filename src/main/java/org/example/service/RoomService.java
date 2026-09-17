package org.example.service;

import java.util.List;
import org.example.model.Room;
import org.example.model.RoomStatus;
import org.example.model.RoomType;
import org.example.repository.RoomRepository;

public class RoomService {
private final RoomRepository repository;

public RoomService (RoomRepository repository) {
    this.repository = repository;
}

public void addRooms (int startNumber, int endNumber, RoomType Type,int Capacity) {
    repository.addRooms(startNumber,endNumber,Type,Capacity);
}

public List<Room> getAllRooms() {
    return repository.getRooms();
}
public Room findRoomById(int id){
    return repository.findRoomById(id);
}

public List<Room> findAvailableRoomsByType(RoomType type) {
    return repository.findAvailableByType(type);
}

    public void updateRoom(int roomId, RoomType newType, int newCapacity) {
        Room room = repository.findRoomById(roomId);
        if (room == null) {
            System.out.println("Комната с ID " + roomId + " не найдена");
            return;
        }
        room.setType(newType);
        room.setCapacity(newCapacity);
        repository.updateRoom(room);
        System.out.println("Комната " + room.getRoomNumber() + " обновлена");
    }

    public void deleteRoom(int roomId) {
        Room room = repository.findRoomById(roomId);
        if (room == null) {
            System.out.println("Комната с ID " + roomId + " не найдена");
            return;
        }
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            System.out.println("Нельзя удалить комнату " + room.getRoomNumber() +
                    " (статус: " + room.getStatus() + ")");
            return;
        }
        repository.deleteRoom(roomId);
        System.out.println("Комната " + room.getRoomNumber() + " удалена");
    }
}
