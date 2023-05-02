CREATE KEYSPACE IF NOT EXISTS custom_heroes_chat
  WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

USE custom_heroes_chat;

CREATE TABLE IF NOT EXISTS messages (
  chatRoomId bigint,
  date timestamp,
  userFrom text,
  userTo text,
  content text,
  PRIMARY KEY ((chatRoomId), date)
);

CREATE  table if not EXISTS chats (
  chatRoomId bigint,
  user text,
  PRIMARY KEY ((chatRoomId), user)
);