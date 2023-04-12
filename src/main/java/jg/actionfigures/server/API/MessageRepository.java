package jg.actionfigures.server.API;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import jg.actionfigures.server.Models.Cassandra.Message;

public interface MessageRepository extends CassandraRepository<Message, Integer>{
    @AllowFiltering
    List<Message> findByFromUser(Long fromUser);

    @AllowFiltering
    List<Message> findByToUser(Long toUser);
    
}
