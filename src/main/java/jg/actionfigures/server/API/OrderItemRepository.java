package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.PostgerSql.OrderItem;
import java.util.List;


public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {
    List<OrderItem> findAllByOrderId(Integer id);
    
}
