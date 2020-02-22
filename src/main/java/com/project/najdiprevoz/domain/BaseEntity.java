package com.project.najdiprevoz.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Base entity class
 *
 * @param <T> ID class
 */
/**
 * Base entity class
 *
 * @param <T> ID class
 */
@MappedSuperclass
public class BaseEntity<T extends Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        BaseEntity be = (BaseEntity) obj;
        return Objects.equals(getId(), be.getId());
    }

    /**
     * Used for comparison by id with other object of same type
     *
     * @param id The id of the other object
     * @return true if they are equal
     */
    public boolean equalsById(Long id) {
        return Objects.equals(getId(), id);
    }
}