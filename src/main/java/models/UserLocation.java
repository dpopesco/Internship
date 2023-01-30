package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLocation {
    private String street;
    private String city;
    private String state;
    private String country;
    private String timezone;
}
