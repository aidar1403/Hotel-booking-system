package org.example.repository;
import org.example.model.Room;
import org.example.model.RoomType;

import java.util.List;


public interface RoomRepository {
    List<Room> getRooms();
    Room findRoomById(int id);
    void addRooms (int startNumber,int endNumber, RoomType Type,int Capacity);
}

