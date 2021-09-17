package com.company.task.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
@Getter
@Setter
@EqualsAndHashCode
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 200)
    @Column(name = "username")
    private String username;
    @Size(max = 200)
    @Column(name = "password")
    private String password;
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "surname")
    private String surname;
    @Column(name = "age")
    private Integer age;
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @ManyToOne
    private Role role;
}
