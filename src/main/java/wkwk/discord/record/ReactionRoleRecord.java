package wkwk.discord.record;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReactionRoleRecord {
    private long messageId = -1;
    private long serverId;
    private String link;
    private ArrayList<Long> roleIds;
    private ArrayList<String> emojis;
}
