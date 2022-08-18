package wkwk.discord.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReactRoleParameters {
    // madeW
    SERVER_ID(ReactMessageParameters.SERVER_ID.getParam()),
    MESSAGE_ID(ReactMessageParameters.MESSAGE_ID.getParam()),
    ROLE_ID("ROLE_ID"),
    EMOJI("EMOJI");

    private final String param;

}
