package com.bigs.forecast.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageInfo {
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
