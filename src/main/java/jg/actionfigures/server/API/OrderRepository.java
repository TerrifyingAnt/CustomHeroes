package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>{
    
}
