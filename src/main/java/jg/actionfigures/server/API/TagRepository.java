package jg.actionfigures.server.API;


import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.PostgerSql.Tag;

public interface TagRepository extends CrudRepository<Tag, Integer>{
    
}