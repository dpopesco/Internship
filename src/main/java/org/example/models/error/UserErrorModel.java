package org.example.models.error;

import lombok.Getter;
import lombok.Setter;
import org.example.models.User;

@Getter
@Setter
public class UserErrorModel extends ErrorModel {
    private User data;
}
