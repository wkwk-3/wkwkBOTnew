package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DAOParameters {
    // madeW
    CONNECT_STRING("jdbc:mysql://localhost:3306/WKWK_DISCORD_V2"),
    USER_ID("wkwk_v2"),
    PASS_WORD("horizonLuna_2"),
    TABLE_WATCHING("WATCHING"),
    TABLE_BOT_DATA("BOT_DATA"),
    TABLE_SERVER_PROPERTY("SERVER_PROPERTY"),
    TABLE_TEMP_CHANNEL("TEMP_CHANNELS"),
    TABLE_MENTION_MESSAGE("MENTION_MESSAGES"),
    TABLE_REACT_MESSAGE("REACT_MESSAGE"),
    TABLE_REACT_ROLE("REACT_ROLES"),
    TABLE_NAME_PRESET("NAME_PRESET"),
    TABLE_DELETE_TIMES("DELETE_TIMES"),
    TABLE_DELETE_MESSAGES("DELETE_MESSAGES"),
    TABLE_LOGGING("LOGGING"),
    TABLE_BOT_SEND_MESSAGES("BOT_SEND_MESSAGES");


    private final String param;
}
