package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostGET extends Post {

    @JsonProperty(value = "owner")
    private User user;


}
