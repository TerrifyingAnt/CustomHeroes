package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.Figure;

public interface FigureRepository extends CrudRepository<Figure, Integer>{

    public Figure findByPrice(Double price);
    
}
