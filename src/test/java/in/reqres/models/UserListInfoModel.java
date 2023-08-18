package in.reqres.models;

import lombok.Data;

import java.util.List;

@Data
public class UserListInfoModel {
    int page, per_page, total, total_pages;
    List<UserData> data;
    Support support;

    @Data
    public static class UserData {
        int id;
        String email, first_name, last_name, avatar;
    }

    @Data
    public static class Support {
        String url, text;
    }
}
