package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeleteTimesParameters {
    // madeW
    SERVER_ID(ServerPropertyParameters.SERVER_ID.getParam()),
    TEXT_CHANNEL_ID("TEXT_CHANNEL_ID"),
    DELETE_TIME("DELETE_TIME"),
    TIME_UNIT("TIME_UNIT");

    private final String param;

}
