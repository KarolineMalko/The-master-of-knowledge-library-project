/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author Karoline Malko
 */
public class IncomingLecture {
    private int id;
    private String date;
    private String subject;
    private String lecturerFirstName;
    private String lecturerLastName;
    private int roomId;

    public IncomingLecture() {
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getLecturerFirstName() {
        return lecturerFirstName;
    }

    public String getLecturerLastName() {
        return lecturerLastName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLecturerFirstName(String lecturerFirstName) {
        this.lecturerFirstName = lecturerFirstName;
    }

    public void setLecturerLastName(String lecturerLastName) {
        this.lecturerLastName = lecturerLastName;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    
    
}
