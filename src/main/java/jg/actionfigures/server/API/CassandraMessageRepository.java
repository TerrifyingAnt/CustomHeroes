package jg.actionfigures.server.API;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jg.actionfigures.server.Models.Cassandra.Message;



public interface CassandraMessageRepository extends CassandraRepository<Message, Integer>{
    @AllowFiltering
    List<Message> findByUserFrom(Long userFrom);

    @AllowFiltering
    List<Message> findByUserTo(Long userTo);

    @Query("SELECT DISTINCT chatRoomId, username FROM messages WHERE username = ?0 ALLOW FILTERING")
    List<Long> findUniqueChatRoomIdsByUser(Long user);
    
    @AllowFiltering
    List<Message> findByChatRoomIdAndUsername(Long chatRoomId, Long username);

}
