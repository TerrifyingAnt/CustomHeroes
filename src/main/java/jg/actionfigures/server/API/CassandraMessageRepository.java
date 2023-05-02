package jg.actionfigures.server.API;

import java.util.List;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import jg.actionfigures.server.Models.Cassandra.Message;



public interface CassandraMessageRepository extends CassandraRepository<Message, Integer>{
    @AllowFiltering
    List<Message> findByUserFrom(Long userFrom);

    @AllowFiltering
    List<Message> findByUserTo(Long userTo);

    @AllowFiltering
    List<Message> findAllByChatRoomId(Long chatRoomId);

}
