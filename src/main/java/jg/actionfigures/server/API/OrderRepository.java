package jg.actionfigures.server.API;

import java.util.List;

import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.PostgerSql.Order;
import jg.actionfigures.server.Models.PostgerSql.User;

public interface OrderRepository extends CrudRepository<Order, Integer>{
    List<Order> findByUserId(Integer id);
    
}
