package com.smarthostel.model;

public class Student extends Person {
    private String gender;
    private String branch;
    private int year;
    private String parentPhone;
    private String hostelBlock;
    private int roomNumber;

    public Student() {
    }

    public Student(int id, String name, String phone, String address,
                   String gender, String branch, int year,
                   String parentPhone, String hostelBlock, int roomNumber) {
        super(id, name, phone, address);
        this.gender = gender;
        this.branch = branch;
        this.year = year;
        this.parentPhone = parentPhone;
        this.hostelBlock = hostelBlock;
        this.roomNumber = roomNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getHostelBlock() {
        return hostelBlock;
    }

    public void setHostelBlock(String hostelBlock) {
        this.hostelBlock = hostelBlock;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", gender='" + gender + '\'' +
                ", branch='" + branch + '\'' +
                ", year=" + year +
                ", parentPhone='" + parentPhone + '\'' +
                ", hostelBlock='" + hostelBlock + '\'' +
                ", roomNumber=" + roomNumber +
                '}';
    }
}
