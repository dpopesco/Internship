package org.example.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    //urmeaza sa adaug pentru full user
//    private String title;
//    private String gender;
//    private DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//    private String phone;
//    private String picture;
//    private UserLocation location;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
