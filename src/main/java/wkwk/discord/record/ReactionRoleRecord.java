package wkwk.discord.record;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReactionRoleRecord {
    // madeW
    private long messageId = -1;
    private long serverId;
    private String link;
    private ArrayList<Long> roleIds;
    private ArrayList<String> emojis;
    private long roleId;
    private String emoji;
}
