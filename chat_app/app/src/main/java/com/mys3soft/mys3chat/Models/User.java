/**
 * this class represent a user
 */
package com.mys3soft.mys3chat.Models;


import java.util.Set;

public class User {
    // password of the user
    private String Password;
    // id of user- mail
    private long ID;
    // mail of the user
    private String Email;
    // first Name
    private String FirstName;
    //last Name
    private String LastName;
    // statues of the user like if he is noe connected
    private String Status;

    private String EntryDate;

    // language the tha user can play with
    private Set<String> Lang;
    // user total score
    private int score;
    // gender
    private String gender;


    public User() {
    }

    public User(String password, long ID, String email, String firstName, String lastName, String status,  Set<String>  lang, int score1,String gender1) {
        this.Password = password;
        this.ID = ID;
        this.Email = email;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Status = status;
        this.Lang= lang;
        this.score = score1;
        this.gender =gender1;
    }

    public String getPassword() {
        return Password;
    }

    public long getID() {
        return ID;
    }

//
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }


    public String getLastName() {
        return LastName;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Set<String> getLang() {
        return Lang;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGender() {
        return gender;
    }

    public void setLang(Set<String> lang) {
        Lang = lang;
    }
}
