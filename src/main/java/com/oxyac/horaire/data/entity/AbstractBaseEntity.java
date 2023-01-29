package com.oxyac.horaire.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Аннотация, которая говорит нам, что это суперкласс для всех Entity
// https://vladmihalcea.com/how-to-inherit-properties-from-a-base-class-entity-using-mappedsuperclass-with-jpa-and-hibernate/
@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
public abstract class AbstractBaseEntity {

    // Аннотации, описывающие механизм генерации id - разберитесь в документации каждой!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "global_seq", allocationSize = 1)
    protected Integer id;

    protected AbstractBaseEntity() {
    }
}
