package com.company.task.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "task")
@Getter
@Setter
@EqualsAndHashCode
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Lob
    @Size(max = 65535)
    @Column(name = "content")
    private String content;
    @Column(name = "rank")
    private Integer rank;
    @Column(name = "deadline")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "assigned_by", referencedColumnName = "id")
    @ManyToOne
    private User assignedBy;
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    @ManyToOne
    private User assignedTo;
}
