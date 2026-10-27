package org.example.web;

import org.example.model.Room;
import org.example.repository.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins="*")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomrepository) {
        this.roomRepository = roomrepository;
    }

    @GetMapping
    public List<Room> getAllRooms(){
        return roomRepository.getRooms();
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable int id){
        return roomRepository.findRoomById(id);
    }

    @PostMapping
    public String createRoom(@RequestBody Room room){
        try{
            int number = Integer.parseInt(room.getRoomNumber());
            roomRepository.addRooms(number, number, room.getType(),room.getCapacity());
            return "Room "+room.getRoomNumber()+" added successfully";
        }catch(NumberFormatException e){
            return "Error, it must be a number";
        }
    }
    @DeleteMapping("/{id}")
    public String deleteRoom(@PathVariable int id){
        roomRepository.deleteRoom(id);
        return "Room "+id+" deleted successfully";
    }
}
