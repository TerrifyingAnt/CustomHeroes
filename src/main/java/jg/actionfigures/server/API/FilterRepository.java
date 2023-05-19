package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.PostgerSql.Filter;

public interface FilterRepository extends CrudRepository<Filter, Integer>{
    
}
