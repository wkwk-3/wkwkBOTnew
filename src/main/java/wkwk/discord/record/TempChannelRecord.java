package wkwk.discord.record;

import lombok.Data;

@Data
public class TempChannelRecord {
    private long voiceChannelId;
    private long textChannelId;
    private long serverId;
    private long ownerUserId;
    private long infoMessageId;
    private boolean hideBy;
    private boolean lockBy;
    private boolean existBy = false;
    private long mentionChannelId;
}
