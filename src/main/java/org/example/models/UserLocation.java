package org.example.models;

import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Getter
@Setter
public class UserLocation {
    private String street;
    private String city;
    private String state;
    private String country;
    private String timezone;

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
