package in.reqres.models;

import lombok.Data;

@Data
public class ListOfUsersResponseModel {

    int page, per_page, total, total_pages, data;

}
