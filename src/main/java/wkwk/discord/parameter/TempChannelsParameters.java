package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TempChannelsParameters {
    // madeW
    VOICE_CHANNEL_ID("VOICE_CHANNEL_ID"),
    TEXT_CHANNEL_ID("TEXT_CHANNEL_ID"),
    SERVER_ID(ServerPropertyParameters.SERVER_ID.getParam()),
    HIDE_BY("HIDE_BY"),
    LOCK_BY("LOCK_BY"),
    OWNER_USER_ID("OWNER_USER_ID"),
    INFO_MESSAGE_ID("INFO_MESSAGE_ID");

    private final String param;

}
