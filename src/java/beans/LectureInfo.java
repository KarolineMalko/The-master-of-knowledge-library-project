/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author Karoline Malko
 */
public class LectureInfo {
    private String lectureSubject;
    private String lectureDate;
    private int roomId;
    private int lecturerId;
    private String lecturerPersonNum;

    public void setLecturerPersonNum(String lecturerPersonNum) {
        this.lecturerPersonNum = lecturerPersonNum;
    }

    public String getLecturerPersonNum() {
        return lecturerPersonNum;
    }
    private int enrolledVisitorsNum;
    
    public LectureInfo() {
    }


    public String getLectureSubject() {
        return lectureSubject;
    }

    public String getLectureDate() {
        return lectureDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public int getEnrolledVisitorsNum() {
        return enrolledVisitorsNum;
    }

    public void setLectureSubject(String lectureSubject) {
        this.lectureSubject = lectureSubject;
    }

    public void setLectureDate(String lectureDate) {
        this.lectureDate = lectureDate;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setLecturerId(int lectureId) {
        this.lecturerId = lecturerId;
    }

    public void setEnrolledVisitorsNum(int enrolledVisitorsNum) {
        this.enrolledVisitorsNum = enrolledVisitorsNum;
    }
    
     
}
