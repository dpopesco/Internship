package org.example.models.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.models.user.User;

@Getter
@Setter
public class PostGET extends Post {

    @JsonProperty(value = "owner")
    private User user;


}
