CREATE DATABASE WKWK_DISCORD_V2;

USE WKWK_DISCORD_V2;

create table BOT_DATA
(
    BOT_TOKEN           varchar(100) not null
        primary key,
    BOT_CLIENT_ID       text         null,
    CLIENT_SECRET       text         null,
    ACCESS_TOKEN        text         null,
    ACCESS_TOKEN_SECRET text         null,
    API_KEY             text         null,
    API_SECRET_KEY      text         null
);

create table NAME_PRESET
(
    SERVER_ID   bigint not null,
    NAME_SELECT text   not null
);

create table SERVER_PROPERTY
(
    SERVER_ID          bigint                                           not null
        primary key,
    MENTION_CHANNEL_ID bigint                                           null,
    FIRST_CHANNEL_ID   bigint                                           null,
    VOICE_CATEGORY_ID  bigint                                           null,
    TEXT_CATEGORY_ID   bigint                                           null,
    TEMP_BY            tinyint(1)   default 1                           null,
    TEXT_BY            tinyint(1)   default 1                           null,
    DEFAULT_SIZE       int          default 0                           null,
    STEREO_TYPED       varchar(100) default '&here&/n&text&/n&channel&' null,
    DEFAULT_NAME       varchar(100) default '&user&-Channel'            null
);

create table BOT_SEND_MESSAGES
(
    SERVER_ID    bigint not null,
    MESSAGE_ID   bigint not null,
    MESSAGE_LINK text   null,
    CHANNEL_ID   bigint not null,
    USER_ID      bigint not null,
    primary key (SERVER_ID, MESSAGE_ID, CHANNEL_ID, USER_ID),
    constraint bot_send_messages_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table DELETE_MESSAGES
(
    SERVER_ID       bigint   not null,
    MESSAGE_ID      bigint   not null,
    MESSAGE_LINK    text     null,
    DELETE_TIME     datetime not null,
    TEXT_CHANNEL_ID bigint   not null,
    primary key (SERVER_ID, MESSAGE_ID),
    constraint delete_messages_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table DELETE_TIMES
(
    SERVER_ID       bigint     not null
        primary key,
    TEXT_CHANNEL_ID bigint     not null,
    DELETE_TIME     bigint     not null,
    TIME_UNIT       varchar(1) not null,
    constraint delete_times_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table LOGGING
(
    SERVER_ID         bigint      not null,
    CHANNEL_ID        bigint      not null,
    LOG_TYPE          varchar(15) not null,
    TARGET_CHANNEL_ID bigint      not null,
    primary key (SERVER_ID, CHANNEL_ID, LOG_TYPE, TARGET_CHANNEL_ID),
    constraint logging_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table MENTION_MESSAGES
(
    SERVER_ID        bigint not null,
    MESSAGE_ID       bigint not null,
    TEXT_CHANNEL_ID  bigint not null,
    VOICE_CHANNEL_ID bigint not null,
    MESSAGE_LINK     text   null,
    primary key (TEXT_CHANNEL_ID, MESSAGE_ID),
    constraint mention_messages_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table REACT_MESSAGE
(
    SERVER_ID       bigint not null
        primary key,
    TEXT_CHANNEL_ID bigint not null,
    MESSAGE_ID      bigint not null,
    MESSAGE_LINK    text   null,
    constraint react_message_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table REACT_ROLES
(
    MESSAGE_ID bigint      not null,
    ROLE_ID    bigint      not null,
    EMOJI      varchar(30) not null,
    SERVER_ID  bigint      not null,
    primary key (MESSAGE_ID, EMOJI),
    constraint react_roles_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table TEMP_CHANNELS
(
    VOICE_CHANNEL_ID bigint               not null
        primary key,
    TEXT_CHANNEL_ID  bigint               not null,
    SERVER_ID        bigint               not null,
    OWNER_USER_ID    bigint               not null,
    INFO_MESSAGE_ID  bigint               not null,
    HIDE_BY          tinyint(1) default 0 null,
    LOCK_BY          tinyint(1) default 0 null,
    constraint temp_channels_ibfk_1
        foreign key (SERVER_ID) references SERVER_PROPERTY (SERVER_ID)
);

create table WATCHING
(
    SERVER_ID     bigint null,
    TEMP_CREATE   bigint null,
    SLASH_COMMAND bigint null
);

INSERT INTO BOT_DATA(BOT_TOKEN) VALUES ('0');

CREATE USER 'wkwk_v2' IDENTIFIED BY 'horizonLuna_2';
GRANT ALL ON WKWK_DISCORD_V2.* TO 'wkwk_v2';