package wkwk.discord.record;

import lombok.Data;

import java.util.Date;

@Data
public class DeleteMessageRecord {
    private long serverId;
    private long messageId;
    private Date deleteDate;
    private long textChannelId;

}
