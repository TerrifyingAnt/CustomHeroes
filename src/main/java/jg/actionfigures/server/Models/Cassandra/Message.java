package jg.actionfigures.server.Models.Cassandra;

import java.util.Date;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;



@Table("messages")
public class Message {

    @PrimaryKey
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("id")
    private Date id;

    @Column("from_user")
    private Long fromUser;

    @Column("to_user")
    private Long toUser;

    @Column("content")
    private String content;


    // Getters and setters
    public Date getId() {
        return id;
    }

    public void setId(Date id) {
        this.id = id;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    public Long getToUser() {
        return toUser;
    }

    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




}
