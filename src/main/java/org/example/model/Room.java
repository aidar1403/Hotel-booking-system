package org.example.model;

public class Room {
private static int nextId = 1;
private final int id;
private String RoomNumber;
private RoomType Type;
private int Capacity;
private RoomStatus Status;

public Room (String RoomNumber,RoomType Type,int Capacity, RoomStatus Status){
    this.id = nextId++;
    this.RoomNumber = RoomNumber;
    this.Type = Type;
    this.Capacity = Capacity;
    this.Status = Status;
}

    public int getId() {return id ;}
    public String getRoomNumber() {return RoomNumber ;}
    public RoomType getType() {return Type ;}
    public int getCapacity() {return Capacity ;}
    public RoomStatus getStatus () {return Status ;}

    public void setRoomNumber(String RoomNumber) {this.RoomNumber = RoomNumber; }
    public void setType(RoomType Type) {this.Type = Type; }
    public void setCapacity(int Capacity) {this.Capacity = Capacity; }
    public void setStatus(RoomStatus Status) {this.Status= Status ; }

    }

