package wkwk.discord.system.autodelete;

import wkwk.discord.dao.AutoDeleteRegisterDAO;
import wkwk.discord.record.DeleteMessageRecord;
import wkwk.discord.record.DeleteTimeRecord;
import wkwk.discord.system.core.SystemMaster;

import java.util.Calendar;
import java.util.Date;

public class AutoDeleteRegisterSystem extends SystemMaster {
    // madeW
    public AutoDeleteRegisterSystem() {
        AutoDeleteRegisterDAO dao = new AutoDeleteRegisterDAO();
        api.addMessageCreateListener(event -> {
            if (dao.CheckIfDeleteChannel(event.getChannel().getId())
                    && event.getServer().isPresent()
                    && event.getMessage().getAuthor().asUser().isPresent()
                    && !event.getMessage().getAuthor().asUser().get().isBot()) {
                DeleteTimeRecord deleteTimeRecord = dao.getDeleteTime(event.getChannel().getId());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Date.from(event.getMessage().getCreationTimestamp()));
                switch (deleteTimeRecord.getUnit()) {
                    case "s", "S" -> calendar.add(Calendar.SECOND, (int) deleteTimeRecord.getTime());
                    case "m", "M" -> calendar.add(Calendar.MINUTE, (int) deleteTimeRecord.getTime());
                    case "h", "H" -> calendar.add(Calendar.HOUR, (int) deleteTimeRecord.getTime());
                    case "d", "D" -> calendar.add(Calendar.DAY_OF_MONTH, (int) deleteTimeRecord.getTime());
                }
                DeleteMessageRecord deleteMessageRecord = new DeleteMessageRecord();
                deleteMessageRecord.setServerId(event.getServer().get().getId());
                deleteMessageRecord.setMessageId(event.getMessageId());
                deleteMessageRecord.setTextChannelId(event.getChannel().getId());
                deleteMessageRecord.setDeleteDate(calendar.getTime());
                deleteMessageRecord.setMessageLink(event.getMessageLink().toString());
                dao.addDeleteMessage(deleteMessageRecord);
            }
        });
    }
}
