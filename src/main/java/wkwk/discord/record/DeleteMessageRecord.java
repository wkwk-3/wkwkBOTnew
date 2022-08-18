package wkwk.discord.record;

import lombok.Data;

import java.util.Date;

@Data
public class DeleteMessageRecord {
    // madeW
    private long serverId;
    private long messageId;
    private String messageLink;
    private Date deleteDate;
    private long textChannelId;

}
