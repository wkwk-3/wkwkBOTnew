package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoggingParameters {

    SERVER_ID(ServerPropertyParameters.SERVER_ID.getParam()),
    CHANNEL_ID("CHANNEL_ID"),
    LOG_TYPE("LOG_TYPE"),
    TARGET_CHANNEL_ID("TARGET_CHANNEL_ID");

    private final String param;

}
