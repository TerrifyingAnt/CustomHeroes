package jg.actionfigures.server.API;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import jg.actionfigures.server.Models.Cassandra.Chat;

public interface CassandraChatRepository extends CassandraRepository<Chat, Integer>{
    
    @AllowFiltering
    List<Chat> findByUser(String user); 

    @AllowFiltering
    List<Chat> findByChatRoomId(Long chatRoomId);
}
