package com.event.form;

import lombok.Data;

@Data
public class SearchPostForm {
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
}
