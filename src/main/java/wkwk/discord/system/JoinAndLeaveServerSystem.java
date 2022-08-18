package wkwk.discord.system;

import wkwk.discord.dao.JoinAndLeaveServerDAO;
import wkwk.discord.system.core.SystemMaster;

public class JoinAndLeaveServerSystem extends SystemMaster {
    // madeW

    public JoinAndLeaveServerSystem() {
        JoinAndLeaveServerDAO dao = new JoinAndLeaveServerDAO();
        api.addServerJoinListener(event -> dao.addServerData(event.getServer().getId()));

        api.addServerLeaveListener(event -> dao.deleteServerData(event.getServer().getId()));
    }
}
