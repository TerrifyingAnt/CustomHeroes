package jg.actionfigures.server.Models.Cassandra;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("chats")
public class Chat {
    
    @PrimaryKeyColumn(name="chatroomid", ordinal = 0,
    type = PrimaryKeyType.PARTITIONED)
    @Column("chatroomid")
    private Long chatRoomId;

    @PrimaryKeyColumn(name="user", ordinal = 0,
    type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    @Column("user")
    private String user;

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    
}
