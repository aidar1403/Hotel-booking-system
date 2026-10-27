package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Room {
    private static int nextId = 1;
    private int id;
    private String roomNumber;
    private RoomType type;
    private int capacity;
    private RoomStatus status;

    public Room() {
    }

    public Room(String roomNumber, RoomType type, int capacity, RoomStatus status) {
        this.id = nextId++;
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public RoomType getType() { return type; }
    public void setType(RoomType type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
}