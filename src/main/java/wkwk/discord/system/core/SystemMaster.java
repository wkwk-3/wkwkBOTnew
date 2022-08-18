package wkwk.discord.system.core;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import wkwk.discord.dao.BotDataDAO;

public class SystemMaster {
    // madeW
    protected static DiscordApi api;

    public SystemMaster() {
        if (api == null) {
            api = new DiscordApiBuilder().setToken(new BotDataDAO().BotGetToken()).setAllIntents().login().join();
        }
    }
}
