package org.example.model;

public class Booking {
    private static int nextId = 1;
    private final int id;
    private String clientName;
    private int numberOfGuests;
    private BookingStatus status;
    private Integer assignedRoom;
    private RoomType requestedRoomType;
    private String startDate;
    private String endDate;

    public Booking(String clientName,int numberOfGuests, RoomType requestedRoomType, String startDate, String endDate){
        this.id = nextId++;
        this.clientName = clientName;
        this.numberOfGuests = numberOfGuests;
        this.requestedRoomType = requestedRoomType;
        this.assignedRoom = null;
        this.status = BookingStatus.CONFIRMED;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId () { return id ;}
    public Integer getAssignedRoom () {return assignedRoom ;}
    public RoomType getRequestedRoomType () {return requestedRoomType ;}
    public String getClientName () {return clientName ;}
    public int getNumberOfGuests () {return numberOfGuests ;}
    public BookingStatus getStatus () {return status ;}
    public String getStartDate () {return startDate ;}
    public String getEndDate () {return endDate ;}

    public void setClientName (String clientName) {this.clientName = clientName;}
    public void setNumberOfGuests (int numberOfGuests) {this.numberOfGuests = numberOfGuests;}
    public void setStatus (BookingStatus status) {this.status = status;}
    public void setAssignedRoom (Integer assignedRoom) {this.assignedRoom = assignedRoom;}
    public void setRequestedRoomType (RoomType requestedRoomType) {this.requestedRoomType = requestedRoomType;}
    public void setStartDate (String startDate) {this.startDate = startDate;}
    public void setEndDate (String endDate) {this.endDate = endDate;}
}
