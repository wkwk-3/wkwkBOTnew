package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReactMessageParameters {

    SERVER_ID(ServerPropertyParameters.SERVER_ID.getParam()),
    TEXT_CHANNEL_ID("TEXT_CHANNEL_ID"),
    MESSAGE_ID("MESSAGE_ID"),
    MESSAGE_LINK("MESSAGE_LINK");

    private final String param;

}
