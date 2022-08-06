package wkwk.discord.system;

import org.javacord.api.entity.permission.Permissions;
import wkwk.discord.system.core.SystemMaster;

public class BotInfoSystem extends SystemMaster {
    public String getInviteUrl(Permissions permissions) {
        return api != null ? api.createBotInvite(permissions) : null;
    }
}
