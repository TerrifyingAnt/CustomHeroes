package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.PostgerSql.Figure;

public interface FigureRepository extends CrudRepository<Figure, Integer>{
    
}
