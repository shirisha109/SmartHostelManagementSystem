package com.smarthostel.model;

public class Room {
    private int roomNumber;
    private String block;
    private int capacity;
    private int occupiedBeds;

    public Room() {
    }

    public Room(int roomNumber, String block, int capacity, int occupiedBeds) {
        this.roomNumber = roomNumber;
        this.block = block;
        this.capacity = capacity;
        this.occupiedBeds = occupiedBeds;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public void setOccupiedBeds(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", block='" + block + '\'' +
                ", capacity=" + capacity +
                ", occupiedBeds=" + occupiedBeds +
                '}';
    }
}
