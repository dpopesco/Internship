package org.example.models.user;

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
    private String title;
    @JsonProperty(value = "gender")
    private String gender;
    @JsonProperty(value = "dateOfBirth")
    private String dateOfBirth;
    @JsonProperty(value = "registerDate")
    private Date registerDate;
    @JsonProperty(value = "updatedDate")
    private Date updatedDate;
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
        newUser.setTitle(Title.MISS.getUserTitle());
        newUser.setGender(Gender.FEMALE.getUserGender());
        return newUser;
    }

    public static User generateAlreadyRegisteredUser() {
        User user = new User("Aron", "Radu", "aron@mail.com");
        user.setTitle(Title.MR.getUserTitle());
        user.setGender(Gender.MALE.getUserGender());
        return user;
    }

    public static User generateUserWithLocation() {
        User user = User.generateRandomUser();
        String street = randomAlphabetic(6);
        String city = randomAlphabetic(5);
        String state = randomAlphabetic(7);
        String country = randomAlphabetic(8);
        String timezone = "+9:00";

        UserLocation userLocation = new UserLocation();

        userLocation.setStreet(street);
        userLocation.setCity(city);
        userLocation.setState(state);
        userLocation.setCountry(country);
        userLocation.setTimezone(timezone);
        user.setLocation(userLocation);
        return user;
    }
}
