CREATE KEYSPACE IF NOT EXISTS custom_heroes_chat
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

USE custom_heroes_chat;

CREATE TABLE IF NOT EXISTS messages (
  username bigint,
  chatRoomId bigint,
  date timestamp,
  userFrom bigint,
  userTo bigint,
  content text,
  PRIMARY KEY ((username, chatroomid), date)
);
