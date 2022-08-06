package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BotSendMessageParameters {
    SERVER_ID(ServerPropertyParameters.SERVER_ID.getParam()),
    MESSAGE_ID("MESSAGE_ID"),
    CHANNEL_ID("CHANNEL_ID"),
    USER_ID("USER_ID");
    private final String param;

}
