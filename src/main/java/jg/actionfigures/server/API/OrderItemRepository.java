package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.OrderItem;

public interface OrderItemRepository extends CrudRepository<OrderItem, Integer>{
    
}
