package jg.actionfigures.server.Models.PostgerSql;

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
@Table(name="order_item_table")
public class OrderItem {
    

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="order_item_id")
    private Integer id;

    @Column(name="order_item_count")
    private Integer count;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="order_id")
    private Order order;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="figure_id")
    private Figure figure;

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    
    
    
}
