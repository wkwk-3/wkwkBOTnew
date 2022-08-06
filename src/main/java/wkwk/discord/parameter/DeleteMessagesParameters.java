package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeleteMessagesParameters {

    SERVER_ID(DeleteTimesParameters.SERVER_ID.getParam()),
    MESSAGE_ID("MESSAGE_ID"),
    TEXT_CHANNEL_ID("TEXT_CHANNEL_ID"),
    DELETE_TIME("DELETE_TIME");

    private final String param;

}
