package jg.actionfigures.server.Models.Cassandra;

import java.util.Date;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("messages")
public class Message {

    @PrimaryKeyColumn(name="username", ordinal = 0,
    type = PrimaryKeyType.PARTITIONED)
    @Column("username")
    private Long username;

    @PrimaryKeyColumn(name="chatroomid", ordinal = 0,
    type = PrimaryKeyType.PARTITIONED)
    @Column("chatroomid")
    private Long chatRoomId;

    @PrimaryKeyColumn(name="date", ordinal = 0,
    type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    @Column("date")
    private Date date;

    @Column("userfrom")
    private Long userFrom;

    @Column("userto")
    private Long userTo;

    @Column("content")
    private String content;

    public Long getUsername() {
        return username;
    }

    public void setUsername(Long username) {
        this.username = username;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getFromUser() {
        return userFrom;
    }

    public void setFromUser(Long userFrom) {
        this.userFrom = userFrom;
    }

    public Long getToUser() {
        return userTo;
    }

    public void setToUser(Long userTo) {
        this.userTo = userTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    

}
