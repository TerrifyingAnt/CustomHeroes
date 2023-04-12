package jg.actionfigures.server.API;

import org.springframework.data.cassandra.repository.CassandraRepository;

import jg.actionfigures.server.Models.Cassandra.Message;

public interface MessageRepository extends CassandraRepository<Message, Integer>{
    
}
