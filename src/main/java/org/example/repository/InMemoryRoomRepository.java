package org.example.repository;
import java.util.List;
import java.util.ArrayList;
import org.example.model.Room;
import org.example.model.RoomStatus;
import org.example.model.RoomType;

public class InMemoryRoomRepository implements RoomRepository{
    private final List<Room>rooms;
    public InMemoryRoomRepository(){rooms = new ArrayList<>();}
    @Override
    public List<Room> getRooms () {return new ArrayList<>(rooms);}
    @Override
    public Room findRoomById (int id) {
        for (Room c:rooms) {
            if (c.getId()== id) {
                return c;
            }
        }
        return null;
    }
    @Override
    public void addRooms (int startNumber,int endNumber,RoomType Type, int Capacity) {
        if (startNumber<1 || endNumber<1) {
            System.out.println("Error! Entered numbers can`t low than 1");
            return;
        }
        if (endNumber<startNumber) {
            System.out.println("Error! Last number can`t be low than first");
            return;
        }
        if (Capacity<=0) {
            System.out.println("Error! Capacity can`t be equal or low than zero");
            return;
        }
        if (Type ==null) {
            System.out.println("This field can`t be empty");
            return;
        }

        for (int i = startNumber;i<= endNumber;i++) {
            String RoomNumber = String.valueOf(i);
            Room room = new Room(RoomNumber,Type,Capacity, RoomStatus.AVAILABLE);
            rooms.add(room);
        }
        System.out.println("Rooms added: "+(endNumber-startNumber+1));
    }
    @Override
    public List<Room> findAvailableByType(RoomType Type) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getType() == Type && room.getStatus() == RoomStatus.AVAILABLE) {
                available.add(room);
            }
        }
        return available;
    }
}
