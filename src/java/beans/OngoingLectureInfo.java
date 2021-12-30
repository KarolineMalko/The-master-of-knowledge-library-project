/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.util.ArrayList;

/**
 *
 * @author Karoline Malko
 */
public class OngoingLectureInfo {
    private String lectureSubject;
    private String lectureDate;
    private int roomId;
    private int lectureId;
    private int enrolledVisitorsNum;
    
    

    public OngoingLectureInfo() {
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

    public int getLectureId() {
        return lectureId;
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

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public void setEnrolledVisitorsNum(int enrolledVisitorsNum) {
        this.enrolledVisitorsNum = enrolledVisitorsNum;
    }


    
    
    
}
