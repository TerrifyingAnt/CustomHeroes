package jg.actionfigures.server.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="filter_table")
public class Filter {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="filter_id")
    private Integer id;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="tag_id")
    private Tag tag;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="figure_id")
    private Figure figure;

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    

    
    
}
