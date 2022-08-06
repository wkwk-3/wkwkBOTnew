package wkwk.discord.record;

import lombok.Data;
import org.javacord.api.entity.permission.Role;

@Data
public class MessageRecord {
    private long serverId;
    private long messageId;
    private long voiceChannelId;
    private long textChannelId;
    private long userId;
    private String link;
    private String emoji;
    private Role role;
}
