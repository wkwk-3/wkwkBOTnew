package wkwk.discord.record;

import lombok.Data;

@Data
public class LoggingRecord {
    private long serverId;
    private long textChannelId;
    private String type ;
    private long targetChannelId;
}
