package wkwk.discord.record;

import lombok.Data;

@Data
public class DeleteTimeRecord {
    // madeW
    private long serverId;
    private long textChannelId;
    private long time;
    private String unit;
}
