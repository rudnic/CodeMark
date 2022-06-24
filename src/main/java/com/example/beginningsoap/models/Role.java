package com.example.beginningsoap.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
public class Role {

    @Id
    private Long id;

    private String name;

    /*@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users;*/
}
