package wkwk.discord.record;

import lombok.Data;

import java.util.ArrayList;

@Data
public class VoteDataRecord {
    long serverId;
    long messageId;
    long allVotes;
    String title;
    String description;
    ArrayList<VoteRecord> voteRecords;
}
