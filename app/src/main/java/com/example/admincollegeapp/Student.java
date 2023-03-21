package com.example.admincollegeapp;

public class Student {

    private String name,email,student_id, mobile_no,status,uid;

    public Student(String name, String email, String student_id, String mobile_no, String status,String uid) {
        this.name = name;
        this.email = email;
        this.student_id = student_id;
        this.mobile_no = mobile_no;
        this.status = status;
        this.uid=uid;
    }

    public Student()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
