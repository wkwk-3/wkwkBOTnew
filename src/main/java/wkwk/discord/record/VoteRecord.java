package wkwk.discord.record;

import lombok.Data;

@Data
public class VoteRecord {
    String unicodeEmoji;
    String voteText;
    long votes;
}
