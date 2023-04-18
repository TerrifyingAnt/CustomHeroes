package jg.actionfigures.server.Models.Cassandra;

import java.io.Serializable;

public class MessageId implements Serializable{
    private Long username;
    private Long chatRoomId;


    public MessageId(Long username, Long chatRoomId) {
        this.username = username;
        this.chatRoomId = chatRoomId;
    }

    public MessageId() {}


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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((chatRoomId == null) ? 0 : chatRoomId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MessageId other = (MessageId) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (chatRoomId == null) {
            if (other.chatRoomId != null)
                return false;
        } else if (!chatRoomId.equals(other.chatRoomId))
            return false;
        return true;
    }

    

    


}
