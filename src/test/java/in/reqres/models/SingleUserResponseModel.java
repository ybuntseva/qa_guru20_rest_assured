package in.reqres.models;

import lombok.Data;

@Data
public class SingleUserResponseModel {
    SingleUserData data;
    Support support;

    @Data
    public static class SingleUserData {
        int id;
        String email, first_name, last_name, avatar;
    }

    @Data
    public static class Support {
        String url, text;
    }
}
