package org.example.service;

import java.util.List;
import org.example.model.Room;
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
}
