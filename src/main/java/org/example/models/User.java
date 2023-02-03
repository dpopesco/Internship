package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.Gender;
import org.example.enums.Title;

import java.net.URL;
import java.util.Date;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Getter
@Setter
public class User {

    @JsonProperty(value = "id", required = true)
    private String id;
    @JsonProperty(value = "firstName", required = true)
    private String firstName;
    @JsonProperty(value = "lastName", required = true)
    private String lastName;
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "title")
    private Title title;
    @JsonProperty(value = "gender")
    private Gender gender;
    @JsonProperty(value = "dateOfBirth")
    private Date dateOfBirth;
    @JsonProperty(value = "registerDate")
    private Date registerDate;
    @JsonProperty(value = "phone")
    private String phone;
    @JsonProperty(value = "picture")
    private URL picture;
    @JsonProperty(value = "location")
    private UserLocation location;

    public User() {
    }

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }


    public static User generateRandomUser() {
        String email = randomAlphanumeric(6) + "@mail.com";
        String firstName = randomAlphabetic(5);
        String lastName = randomAlphabetic(6);

        User newUser = new User(firstName, lastName, email);
        newUser.setTitle(Title.MISS);
        newUser.setGender(Gender.FEMALE);
        return newUser;
    }

    public static User generateAlreadyRegisteredUser() {
        User user = new User("Aron", "Radu", "aron@mail.com");
        user.setTitle(Title.MR);
        user.setGender(Gender.MALE);
        return user;
    }
}
