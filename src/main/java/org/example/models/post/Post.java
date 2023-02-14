package org.example.models.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
public class Post {
    @JsonProperty(value = "id", required = true)
    private String id;
    @JsonProperty(value = "image")
    private String image;
    @JsonProperty(value = "link")
    private URL link;
    @JsonProperty(value = "likes")
    private int likes;
    @JsonProperty(value = "tags")
    private ArrayList<String> tags;
    @JsonProperty(value = "text")
    private String text;
    @JsonProperty(value = "publishDate")
    private Date publishDate;
    @JsonProperty(value = "updatedDate")
    private Date updatedDate;
}
