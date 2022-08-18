package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WatchingParameters {
    // madeW
    SERVER_ID("SERVER_ID"),
    TEMP_CREATE_ID("TEMP_CREATE");

    private final String param;
}
