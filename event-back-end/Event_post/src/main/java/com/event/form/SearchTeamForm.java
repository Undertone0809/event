package com.event.form;

import lombok.Data;

@Data
public class SearchTeamForm {
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
}
