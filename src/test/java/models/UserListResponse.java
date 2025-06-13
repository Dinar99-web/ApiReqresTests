package models;

import lombok.Data;
import java.util.List;

@Data
public class UserListResponse {
    private Integer page;
    private Integer per_page;
    private Integer total;
    private Integer total_pages;
    private List<User> data;
    private Support support;
}
