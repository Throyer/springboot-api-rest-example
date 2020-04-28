package com.github.throyer.common.springboot.api.models.shared;

import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class Pagination {

    private static final int PRIMEIRA_PAGINA = 0;
    
    private static final int TAMANHO_PADRAO = 10;
    private static final int TAMANHO_MINIMO = 1;
    private static final int TAMANHO_MAXIMO = 500;

    private int page = PRIMEIRA_PAGINA;    
    private int size = TAMANHO_PADRAO;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (Objects.nonNull(page) && page >= PRIMEIRA_PAGINA) {
            this.page = page;
        } else {
            page = PRIMEIRA_PAGINA;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (Objects.nonNull(size) && size >= TAMANHO_MINIMO && size <= TAMANHO_MAXIMO) {
            this.size = size;
        } else {
            this.size = TAMANHO_PADRAO;
        }
    }

    public Pageable build() {
        return PageRequest.of(page, size);
    }
}