package models;

import lombok.Data;
import java.util.Date;

@Data
public class UserResponse {
    private String name;
    private String job;
    private String id;
    private Date createdAt;
    private Date updatedAt;
}

