package jg.actionfigures.server.API;

import org.springframework.data.repository.CrudRepository;
import jg.actionfigures.server.Models.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByLogin(String login);
}