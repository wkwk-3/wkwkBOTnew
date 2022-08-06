package wkwk.discord.record;

import lombok.Data;

@Data
public class DeleteTimeRecord {
    private long serverId;
    private long textChannelId;
    private long time;
    private String unit;
}
