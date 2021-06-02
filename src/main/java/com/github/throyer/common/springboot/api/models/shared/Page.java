package com.github.throyer.common.springboot.api.models.shared;

import java.util.Collection;

public class Page<T> {
    private Collection<T> content;
    private Integer page;
    private Integer size;  
    private Integer totalPages;  
    private Long totalElements;

    public Page(org.springframework.data.domain.Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

    public Collection<T> getContent() {
        return content;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public Integer getSize() {
        return size;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public static <T> Page<T> of(org.springframework.data.domain.Page<T> page) {
        return new Page<T>(page);
    }
}