package wkwk.discord.record;

import lombok.Data;

@Data
public class VoteRecord {
    String emojiText;
    long emojiId;
    String emojiType;
    String voteText;
    long votes;
}
