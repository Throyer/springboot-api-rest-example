package com.github.throyer.common.springboot.domain.models.pagination;

import static java.util.Objects.nonNull;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.Entity;

import com.github.throyer.common.springboot.domain.validation.InvalidSortException;
import com.github.throyer.common.springboot.domain.validation.SimpleError;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        if (nonNull(page) && page >= FIRST_PAGE) {
            this.page = page;
        } else {
            page = FIRST_PAGE;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (nonNull(size) && size >= MIN_SIZE && size <= MAX_SIZE) {
            this.size = size;
        } else {
            this.size = DEFAULT_SIZE;
        }
    }

    public Pageable build() {
        return of(page, size);
    }

    public Pageable build(Sort sort) {
        return of(page, size, sort);
    }

    public <T> Pageable build(Sort query, Class<T> entity) {
        if (!entity.isAnnotationPresent(Entity.class)) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Esta Classe não é uma entidade."
            );
        }

        List<SimpleError> errors = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (Order order : query) {
            Optional<Field> optional = fields(entity)
                .filter(field -> belongs(field, order))
                    .findAny();
                    
            if (optional.isPresent()) {
                orders.add(getOrder(order, optional.get()));
            } else {
                errors.add(new SimpleError(order.getProperty(), "Filtro inválido"));
            }
        }

        if (!errors.isEmpty()) {
            throw new InvalidSortException("Filtros inválidos", errors);
        }

        return of(page, size, by(orders));
    }

    private static Boolean belongs(Field field, Order order) {
        var sortable = field.getAnnotation(SortableProperty.class);
        
        var fieldName = field.getName().equals(order.getProperty());
        var name = sortable.name().equals(order.getProperty());

        return name || fieldName;
    }

    private static <T> Stream<Field> fields(Class<T> entity) {
        return Arrays
        .asList(entity.getDeclaredFields())
            .stream()
                .filter(field -> field.isAnnotationPresent(SortableProperty.class));
    }

    private static Order getOrder(Order order, Field field) {
        var fieldName = field.getName();
        var sortable = field.getAnnotation(SortableProperty.class);

        if (!sortable.column().isEmpty()) {
            return new Order(order.getDirection(), sortable.column());
        }

        if (!sortable.name().isEmpty()) {
            return new Order(order.getDirection(), sortable.name());
        }

        return new Order(order.getDirection(), fieldName);
    }
}