package ru.codeportfolio.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public record User(@Id
                   Long id,
                   @Column (name = "name")
                   String name,
                   @Column (name = "age")
                   int age) {
}
