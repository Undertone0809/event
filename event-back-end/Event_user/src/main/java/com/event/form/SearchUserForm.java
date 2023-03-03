package com.event.form;

import lombok.Data;

@Data
public class SearchUserForm {
    private Integer userId;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
}
