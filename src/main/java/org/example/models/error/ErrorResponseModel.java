package org.example.models.error;

import lombok.Getter;
import lombok.Setter;
import org.example.models.User;

import java.util.List;

@Getter
@Setter
public class ErrorResponseModel extends ObjectError{
    private User data;
}
