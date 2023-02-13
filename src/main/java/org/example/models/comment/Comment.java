package org.example.models.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.models.user.User;

import java.util.Date;

@Getter
@Setter
public class Comment {
    @JsonProperty(value = "id", required = true)
    private String id;
    @JsonProperty(value = "message", required = true)
    private String message;
    @JsonProperty(value = "owner")
    private User owner;
    @JsonProperty(value = "post", required = true)
    private String postId;
    @JsonProperty(value = "publishDate")
    private Date publishDate;
}
