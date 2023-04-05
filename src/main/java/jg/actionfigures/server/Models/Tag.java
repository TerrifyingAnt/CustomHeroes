package jg.actionfigures.server.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tag_table")
public class Tag {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="tag_id")
    private Integer id;

    @Column(name="tag_title")
    private String title;

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    
    
}
