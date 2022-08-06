package wkwk.discord.system;

import wkwk.discord.system.core.SystemMaster;

public class DeleteElementSystem extends SystemMaster {
    public DeleteElementSystem() {
        api.addMessageDeleteListener(messageDeleteEvent -> {
            /*

             */
        });

        api.addServerChannelDeleteListener(serverChannelDeleteEvent -> {
            /*
            一時通話が削除されたらテキストも同時に削除してデータベースからも削除
             */
        });

        api.addRoleDeleteListener(roleDeleteEvent -> {
            /*

             */
        });
    }
}
