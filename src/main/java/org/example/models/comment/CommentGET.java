package org.example.models.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.models.user.User;

import java.util.Date;

@Getter
@Setter
public class CommentGET extends Comment {
    @JsonProperty(value = "owner")
    private User owner;
    @JsonProperty(value = "publishDate")
    private Date publishDate;
}
