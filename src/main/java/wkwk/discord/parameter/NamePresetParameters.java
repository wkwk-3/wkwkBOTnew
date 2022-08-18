package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NamePresetParameters {
    // madeW
    SERVER_ID(ServerPropertyParameters.SERVER_ID.getParam()),
    NAME_SELECT("NAME_SELECT");

    private final String param;

}
