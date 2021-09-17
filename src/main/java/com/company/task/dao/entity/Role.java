package com.company.task.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "role")
@Getter
@Setter
@EqualsAndHashCode
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 200)
    @Column(name = "name")
    private String name;
}
