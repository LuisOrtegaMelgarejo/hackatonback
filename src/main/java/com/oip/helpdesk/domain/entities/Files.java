package com.oip.helpdesk.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "files")
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_id")
    private Long comment_id;

    @Column(name = "originalname")
    private String originalname;

    @Column(name = "randonname")
    private String randonname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    public String getRandonname() {
        return randonname;
    }

    public void setRandonname(String randonname) {
        this.randonname = randonname;
    }
}
