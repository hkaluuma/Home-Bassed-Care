package com.example.HBC.model;

public class Anime {

    private String fullnames;
    private String comments;
    private String patient_id;
    private String test_status;
    private String location;
    private String age;
    private String phonenumber;
    private String disease;
    private String image_url;

    public Anime(){

    }

    public Anime(String fullnames, String comments, String patient_id, String test_status, String location, String age, String phonenumber, String disease, String image_url ){
        this.fullnames = fullnames;
        this.comments = comments;
        this.patient_id = patient_id;
        this.test_status = test_status;
        this.location = location;
        this.age = age;
        this.phonenumber = phonenumber;
        this.disease = disease;
        this.image_url = image_url;

    }

    public String getFullnames() {
        return fullnames;
    }

    public String getComments() {
        return comments;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getTest_status() {
        return test_status;
    }

    public String getLocation() {
        return location;
    }

    public String getAge() {
        return age;
    }

    public String getDisease() {
        return disease;
    }
    public String getPhonenumber() {
        return phonenumber;
    }

    public String getImage_url() {
        return image_url;
    }

    //setters
    public void setFullnames(String fullnames) {
        this.fullnames = fullnames;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public void setTest_status(String test_status) {
        this.test_status = test_status;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}