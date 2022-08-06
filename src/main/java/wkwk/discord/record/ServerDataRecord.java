package wkwk.discord.record;

import lombok.Data;

@Data
public class ServerDataRecord {
    private long serverId = -1;
    private long mentionChannelId = -1;
    private long firstChannelId = -1;
    private long voiceCategoryId = -1;
    private long textCategoryId = -1;
    private String stereoTyped;
    private String defaultName;
    private boolean tempBy;
    private boolean textBy;
    private int defaultSize = -1;
}