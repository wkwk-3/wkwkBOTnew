package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MentionMessageParameters {
    // madeW
    SERVER_ID(ServerPropertyParameters.SERVER_ID.getParam()),
    MESSAGE_ID("MESSAGE_ID"),
    TEXT_CHANNEL_ID("TEXT_CHANNEL_ID"),
    VOICE_CHANNEL_ID("VOICE_CHANNEL_ID"),
    MESSAGE_LINK("MESSAGE_LINK"),
    USER_ID("USER_ID");

    private final String param;

}
