package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.PostgerSql.Figure;
import jg.actionfigures.server.Models.PostgerSql.Filter;
import java.util.List;


public interface FilterRepository extends CrudRepository<Filter, Integer>{

    public List<Filter> findByFigure(Figure figure);
    
}
