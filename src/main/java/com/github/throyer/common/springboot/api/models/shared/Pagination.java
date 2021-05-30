package com.github.throyer.common.springboot.api.models.shared;

import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pagination {

    private static final int FIRST_PAGE = 0;
    
    private static final int DEFAULT_SIZE = 10;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 500;

    private int page = FIRST_PAGE;    
    private int size = DEFAULT_SIZE;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (Objects.nonNull(page) && page >= FIRST_PAGE) {
            this.page = page;
        } else {
            page = FIRST_PAGE;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (Objects.nonNull(size) && size >= MIN_SIZE && size <= MAX_SIZE) {
            this.size = size;
        } else {
            this.size = DEFAULT_SIZE;
        }
    }

    public Pageable build() {
        return PageRequest.of(page, size);
    }
}