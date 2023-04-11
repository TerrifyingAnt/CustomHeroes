package jg.actionfigures.server.Models.Cassandra;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("messages")
public class Message {

    @PrimaryKey
    @Column("id")
    private UUID id;

    @Column("from_user")
    private UUID fromUser;

    @Column("to_user")
    private UUID toUser;

    @Column("content")
    private String content;

    @Column("created_at")
    private Date createdAt;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getFromUser() {
        return fromUser;
    }

    public void setFromUser(UUID fromUser) {
        this.fromUser = fromUser;
    }

    public UUID getToUser() {
        return toUser;
    }

    public void setToUser(UUID toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
