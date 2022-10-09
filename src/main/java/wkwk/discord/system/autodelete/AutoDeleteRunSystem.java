package wkwk.discord.system.autodelete;

import wkwk.discord.dao.AutoDeleteRunDAO;
import wkwk.discord.record.DeleteMessageRecord;
import wkwk.discord.system.core.SystemMaster;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletionException;

public class AutoDeleteRunSystem extends SystemMaster {
    // madeW
    TimerTask task;
    Timer timer;

    AutoDeleteRunDAO dao = new AutoDeleteRunDAO();

    public AutoDeleteRunSystem() {
        task = new TimerTask() {
            public void run() {
                Date date = new Date();
                String dates = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                if (dao.CheckIfDeleteMessageDate(dates)) {
                    ArrayList<DeleteMessageRecord> messages = dao.getDeleteMessage(dates);
                    dao.deleteMessage(messages);
                    for (DeleteMessageRecord message : messages) {
                        if (api.getTextChannelById(message.getTextChannelId()).isPresent()) {
                            try {
                                api.getMessageById(message.getMessageId(), api.getTextChannelById(message.getTextChannelId()).get()).join().delete();
                            } catch (CompletionException ignored) {
                            }
                        }
                    }
                }
            }
        };
        timer = new Timer();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        timer.schedule(task, calendar.getTime(), 1000L);
    }
}
