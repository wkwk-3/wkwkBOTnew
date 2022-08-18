package wkwk.discord.system;

import wkwk.discord.dao.ReactionRoleDAO;
import wkwk.discord.record.ReactionRoleRecord;
import wkwk.discord.system.core.SystemMaster;

public class ReactionRoleSystem extends SystemMaster {
    // madeW
    ReactionRoleDAO dao = new ReactionRoleDAO();
    public ReactionRoleSystem() {
        api.addReactionAddListener(reactionAddEvent -> {
            if (reactionAddEvent.getEmoji().asUnicodeEmoji().isPresent() && reactionAddEvent.getMessageLink().isPresent()
                    && reactionAddEvent.getServer().isPresent() && reactionAddEvent.getUser().isPresent()
                    && !reactionAddEvent.getUser().get().isBot()) {
                String emoji = reactionAddEvent.getEmoji().asUnicodeEmoji().get();
                ReactionRoleRecord reactionRoleRecord = new ReactionRoleRecord();
                reactionRoleRecord.setLink(reactionAddEvent.getMessageLink().get().toString());
                reactionRoleRecord.setServerId(reactionAddEvent.getServer().get().getId());
                reactionRoleRecord.setEmoji(emoji);
                reactionRoleRecord.setMessageId(reactionAddEvent.getMessageId());
                if (dao.checkIfEmoji(reactionRoleRecord)) {
                    reactionRoleRecord.setRoleId(dao.getReactionRoleData(reactionRoleRecord));
                    if (api.getRoleById(reactionRoleRecord.getRoleId()).isPresent()) {
                        api.getRoleById(reactionRoleRecord.getRoleId()).get().addUser(reactionAddEvent.getUser().get()).join();
                    }
                }
            }
        });

        api.addReactionRemoveListener(reactionRemoveEvent -> {
            if (reactionRemoveEvent.getEmoji().asUnicodeEmoji().isPresent() && reactionRemoveEvent.getMessageLink().isPresent()
                    && reactionRemoveEvent.getServer().isPresent() && reactionRemoveEvent.getUser().isPresent()
                    && !reactionRemoveEvent.getUser().get().isBot()) {
                String emoji = reactionRemoveEvent.getEmoji().asUnicodeEmoji().get();
                ReactionRoleRecord reactionRoleRecord = new ReactionRoleRecord();
                reactionRoleRecord.setLink(reactionRemoveEvent.getMessageLink().get().toString());
                reactionRoleRecord.setServerId(reactionRemoveEvent.getServer().get().getId());
                reactionRoleRecord.setEmoji(emoji);
                reactionRoleRecord.setMessageId(reactionRemoveEvent.getMessageId());
                if (dao.checkIfEmoji(reactionRoleRecord)) {
                    reactionRoleRecord.setRoleId(dao.getReactionRoleData(reactionRoleRecord));
                    if (api.getRoleById(reactionRoleRecord.getRoleId()).isPresent()) {
                        api.getRoleById(reactionRoleRecord.getRoleId()).get().removeUser(reactionRemoveEvent.getUser().get()).join();
                    }
                }
            }
        });
    }
}
