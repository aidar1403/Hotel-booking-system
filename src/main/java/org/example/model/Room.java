package org.example.model;

public class Room {
private static int nextId = 1;
private final int id;
private String RoomNumber;
private RoomType Type;
private int Capacity;
private boolean IsAvailable;

public Room (String RoomNumber,RoomType Type,int Capacity, boolean IsAvailable){
    this.id = nextId++;
    this.RoomNumber = RoomNumber;
    this.Type = Type;
    this.Capacity = Capacity;
    this.IsAvailable = IsAvailable;
}

    public int getId() {return id ;}
    public String getRoomNumber() {return RoomNumber ;}
    public RoomType getType() {return Type ;}
    public int getCapacity() {return Capacity ;}
    public boolean IsAvailable() {return IsAvailable ;}

    public void setRoomNumber(String RoomNumber) {this.RoomNumber = RoomNumber; }
    public void setType(RoomType Type) {this.Type = Type; }
    public void setCapacity(int Capacity) {this.Capacity = Capacity; }
    public void setIsAvailable(boolean IsAvailable) {this.IsAvailable = IsAvailable; }

    }

