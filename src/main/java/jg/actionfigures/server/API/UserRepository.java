package jg.actionfigures.server.API;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.PostgerSql.User;

public interface UserRepository extends CrudRepository<User, Integer>{
    User findByLogin(String login);
    Optional<User> findById(Integer id);
}
