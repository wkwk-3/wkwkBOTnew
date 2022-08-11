CREATE DATABASE WKWK_DISCORD_V2;

USE WKWK_DISCORD_V2;

CREATE TABLE BOT_DATA(
    BOT_TOKEN TEXT PRIMARY KEY NOT NULL,
    BOT_CLIENT_ID TEXT,
    CLIENT_SECRET TEXT,
    ACCESS_TOKEN TEXT,
    ACCESS_TOKEN_SECRET TEXT,
    API_KEY TEXT,
    API_SECRET_KEY TEXT
);

CREATE TABLE SERVER_PROPERTY (
    SERVER_ID BIGINT PRIMARY KEY NOT NULL,
    MENTION_CHANNEL_ID BIGINT,
    FIRST_CHANNEL_ID BIGINT,
    VOICE_CATEGORY_ID BIGINT,
    TEXT_CATEGORY_ID BIGINT,
    TEMP_BY TINYINT(1) DEFAULT 1,
    TEXT_BY TINYINT(1) DEFAULT 1,
    DEFAULT_SIZE INT DEFAULT 0,
    STEREO_TYPED VARCHAR(100) DEFAULT '&here&/n&text&/n&channel&',
    DEFAULT_NAME varchar(100) DEFAULT '&user&-Channel'
);

CREATE TABLE TEMP_CHANNELS (
    VOICE_CHANNEL_ID BIGINT PRIMARY KEY NOT NULL,
    TEXT_CHANNEL_ID BIGINT NOT NULL,
    SERVER_ID BIGINT NOT NULL,
    OWNER_USER_ID BIGINT NOT NULL,
    INFO_MESSAGE_ID BIGINT NOT NULL,
    HIDE_BY TINYINT(1) DEFAULT 0,
    LOCK_BY TINYINT(1) DEFAULT 0,
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

CREATE TABLE MENTION_MESSAGES (
    SERVER_ID BIGINT NOT NULL,
    MESSAGE_ID BIGINT NOT NULL,
    TEXT_CHANNEL_ID BIGINT NOT NULL,
    VOICE_CHANNEL_ID BIGINT NOT NULL,
    MESSAGE_LINK TEXT,
    PRIMARY KEY (TEXT_CHANNEL_ID,MESSAGE_ID),
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

CREATE TABLE REACT_MESSAGE (
    SERVER_ID BIGINT PRIMARY KEY NOT NULL,
    TEXT_CHANNEL_ID BIGINT NOT NULL,
    MESSAGE_ID BIGINT NOT NULL,
    MESSAGE_LINK TEXT,
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

CREATE TABLE REACT_ROLES (
    MESSAGE_ID BIGINT NOT NULL,
    ROLE_ID BIGINT NOT NULL,
    EMOJI VARCHAR(30) NOT NULL,
    SERVER_ID BIGINT NOT NULL,
    PRIMARY KEY (MESSAGE_ID,EMOJI),
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

CREATE TABLE NAME_PRESET (
    SERVER_ID BIGINT NOT NULL,
    NAME_SELECT TEXT NOT NULL
);

CREATE TABLE DELETE_TIMES (
    SERVER_ID BIGINT NOT NULL PRIMARY KEY,
    TEXT_CHANNEL_ID BIGINT NOT NULL,
    DELETE_TIME BIGINT NOT NULL,
    TIME_UNIT VARCHAR(1) NOT NULL,
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

CREATE TABLE DELETE_MESSAGES (
    SERVER_ID BIGINT NOT NULL,
    MESSAGE_ID BIGINT NOT NULL,
    MESSAGE_LINK TEXT,
    DELETE_TIME DATETIME NOT NULL,
    TEXT_CHANNEL_ID BIGINT NOT NULL,
    PRIMARY KEY (SERVER_ID,MESSAGE_ID),
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

CREATE TABLE LOGGING (
    SERVER_ID BIGINT NOT NULL,
    CHANNEL_ID BIGINT NOT NULL,
    LOG_TYPE VARCHAR(15) NOT NULL,
    TARGET_CHANNEL_ID BIGINT NOT NULL,
    PRIMARY KEY (SERVER_ID,CHANNEL_ID,LOG_TYPE,TARGET_CHANNEL_ID),
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

CREATE TABLE BOT_SEND_MESSAGES (
    SERVER_ID BIGINT NOT NULL,
    MESSAGE_ID BIGINT NOT NULL,
    MESSAGE_LINK TEXT,
    CHANNEL_ID BIGINT NOT NULL,
    USER_ID BIGINT NOT NULL,
    PRIMARY KEY (SERVER_ID,MESSAGE_ID,CHANNEL_ID,USER_ID),
    FOREIGN KEY (SERVER_ID) REFERENCES SERVER_PROPERTY(SERVER_ID)
);

INSERT INTO BOT_DATA(BOT_TOKEN) VALUES ('OTkxODc1NDA1NTU4NTgzMzc4.GcnYbE.knt7QOhQbbGfkV6ahSHl0WnNlQ4O0yZaIyQwWU');

CREATE USER 'wkwk_v2' IDENTIFIED BY 'horizonLuna_2';
GRANT ALL ON WKWK_DISCORD_V2.* TO 'wkwk_v2';