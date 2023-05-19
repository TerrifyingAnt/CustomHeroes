package jg.actionfigures.server.Models.PostgerSql;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="figure_table")
public class Figure {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="figure_id")
    private Integer id;

    @Column(name="figure_name")
    private String title;

    @Column(name="figure_description")
    private String description;
    
    @Column(name="figure_price")
    private Double price;

    @Column(name="figure_rating")
    private Double rating;

    @Column(name="figure_is_movable")
    private Boolean isMovable;

    @Column(name="figure_making_time")
    private String timeOfMaking;

    @Column(name="figure_source_path")
    private String sourcePath;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getIsMovable() {
        return isMovable;
    }

    public void setIsMovable(Boolean isMovable) {
        this.isMovable = isMovable;
    }

    public String getTimeOfMaking() {
        return timeOfMaking;
    }

    public void setTimeOfMaking(String timeOfMaking) {
        this.timeOfMaking = timeOfMaking;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    
    
}
