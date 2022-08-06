package wkwk.discord.system;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import wkwk.discord.dao.TempChannelDAO;
import wkwk.discord.record.MessageRecord;
import wkwk.discord.record.ServerDataRecord;
import wkwk.discord.record.TempChannelRecord;
import wkwk.discord.system.core.Processing;
import wkwk.discord.system.core.SystemMaster;

import java.util.ArrayList;
import java.util.concurrent.CompletionException;

public class TempChannelSystem extends SystemMaster {
    TempChannelDAO dao = new TempChannelDAO();
    public TempChannelSystem() {
        // 一時通話作成
        api.addServerVoiceChannelMemberJoinListener(event -> {
            Server server = event.getServer();
            if (!event.getUser().isBot() && event.getChannel().getId() == dao.getFirstChannelId(server.getId())) {
                Processing processing = new Processing();
                ServerDataRecord settingRecord = dao.getTempSetting(server.getId());
                if (server.getOwner().isPresent() && settingRecord.isTempBy() && server.getChannelCategoryById(settingRecord.getVoiceCategoryId()).isEmpty()) {
                    new MessageBuilder().setContent(server.getName() + "通話カテゴリが削除もしくは設定されていません。\n`/set vcat category: `を実行して設定してください。").send(server.getOwner().get()).join();
                } else if (settingRecord.isTempBy() && server.getChannelCategoryById(settingRecord.getVoiceCategoryId()).isPresent()) {
                    TempChannelRecord record = new TempChannelRecord();
                    record.setServerId(server.getId());
                    record.setOwnerUserId(event.getUser().getId());
                    String defaultName = settingRecord.getDefaultName();
                    defaultName = defaultName.replaceAll("&user&", event.getUser().getName());
                    ServerVoiceChannel voiceChannel = new ServerVoiceChannelBuilder(server)
                            .setCategory(server.getChannelCategoryById(settingRecord.getVoiceCategoryId()).get())
                            .setUserlimit(settingRecord.getDefaultSize())
                            .setName(defaultName)
                            .setBitrate(
                                    switch (server.getBoostLevel()) { // サーバーレベルに応じてBITRATEを決定
                                        case NONE -> 96000;
                                        case TIER_1 -> 128000;
                                        case TIER_2 -> 256000;
                                        case TIER_3 -> 384000;
                                        case UNKNOWN -> 64000;
                                    }
                            ).create().join();
                    voiceChannel.createUpdater().addPermissionOverwrite(event.getUser(), processing.getChannelManagePermission()).update();
                    record.setVoiceChannelId(voiceChannel.getId());
                    if (settingRecord.isTextBy() && server.getChannelCategoryById(settingRecord.getTextCategoryId()).isEmpty()) {
                        new MessageBuilder().setContent(server.getName() + "テキストカテゴリが削除もしくは設定されていません。\n`/set tcat category: `を実行して設定してください。").send(server.getOwner().get()).join();
                    } else if (settingRecord.isTextBy() && server.getChannelCategoryById(settingRecord.getTextCategoryId()).isPresent()) {
                        ServerTextChannel textChannel = new ServerTextChannelBuilder(server)
                                .setCategory(server.getChannelCategoryById(settingRecord.getTextCategoryId()).get())
                                .setName(defaultName)
                                .create().join();
                        textChannel.createUpdater().addPermissionOverwrite(event.getUser(), processing.getChannelManagePermission()).update();
                        record.setTextChannelId(textChannel.getId());
                        record.setInfoMessageId(new MessageBuilder().setContent(processing.rePressInfoMessage(event.getUser(), record.isHideBy(), record.isLockBy())).addComponents(
                                ActionRow.of(Button.success("name", "通話名前変更"),
                                        Button.success("size", "通話人数変更"),
                                        Button.success("send-recruiting", "募集送信"),
                                        Button.success("claim", "通話権限獲得"),
                                        Button.danger("next", "次の項目"))).send(textChannel).join().getId());
                    }
                    dao.setTempChannelData(record);
                    try {
                        event.getUser().move(voiceChannel).join();
                    } catch (CompletionException ex) {
                        dao.deleteTempChannelData(record.getVoiceChannelId());
                        if (event.getServer().getTextChannelById(record.getTextChannelId()).isPresent()) {
                            event.getServer().getTextChannelById(record.getTextChannelId()).get().delete();
                        }
                        if (event.getServer().getVoiceChannelById(record.getVoiceChannelId()).isPresent()) {
                            event.getServer().getVoiceChannelById(record.getVoiceChannelId()).get().delete();
                        }
                    }
                }
            }
        });

        api.addServerVoiceChannelMemberJoinListener(event -> {
            if (event.getChannel().getCategory().isPresent()
                    && event.getChannel().getCategory().get().getId() == dao.getVoiceCateGory(event.getServer().getId())) {
                if (event.getChannel().getConnectedUserIds().size() > 1) {
                    long ownerId = dao.getOwnerUserId(event.getChannel().getId());
                    if (ownerId == event.getUser().getId()) {
                        TempChannelRecord record = dao.getTempChannelData(event.getChannel().getId());
                        Server server = event.getServer();
                        Processing processing = new Processing();
                        if (server.getVoiceChannelById(record.getVoiceChannelId()).isPresent()) {
                            server.getVoiceChannelById(record.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(event.getUser(), processing.getChannelManagePermission()).update();
                        }
                        if (server.getTextChannelById(record.getTextChannelId()).isPresent()) {
                            server.getTextChannelById(record.getTextChannelId()).get().createUpdater().addPermissionOverwrite(event.getUser(), processing.getChannelManagePermission()).update();
                        }
                    } else {
                        TempChannelRecord record = dao.getTempChannelData(event.getChannel().getId());
                        Server server = event.getServer();
                        Processing processing = new Processing();
                        if (server.getVoiceChannelById(record.getVoiceChannelId()).isPresent()) {
                            server.getVoiceChannelById(record.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(event.getUser(), processing.getUserPermission()).update();
                        }
                        if (server.getTextChannelById(record.getTextChannelId()).isPresent()) {
                            server.getTextChannelById(record.getTextChannelId()).get().createUpdater().addPermissionOverwrite(event.getUser(), processing.getUserPermission()).update();
                        }
                    }
                }
            }
        });

        api.addServerVoiceChannelMemberJoinListener(event ->{
            ArrayList<TempChannelRecord> temps = dao.getTempChannels(event.getServer().getId());
            ArrayList<Long> deleteTargetVoiceIds = new ArrayList<>();
            for (TempChannelRecord record : temps) {
                if (api.getServerVoiceChannelById(record.getVoiceChannelId()).isPresent()
                        && api.getServerVoiceChannelById(record.getVoiceChannelId()).get().getConnectedUserIds().size() < 1) {
                    deleteTargetVoiceIds.add(record.getVoiceChannelId());
                    api.getServerVoiceChannelById(record.getVoiceChannelId()).get().delete();
                    ArrayList<MessageRecord> messageRecords = dao.getMentionMessage(record.getVoiceChannelId());
                    for (MessageRecord messageRecord : messageRecords) {
                        if (api.getMessageByLink(messageRecord.getLink()).isPresent()){
                            api.getMessageByLink(messageRecord.getLink()).get().join().delete();
                        }
                    }
                    if (api.getServerTextChannelById(record.getTextChannelId()).isPresent()) {
                        api.getServerTextChannelById(record.getTextChannelId()).get().delete();
                    }
                }
            }
            dao.deleteTempChannelDates(deleteTargetVoiceIds);
        });

        api.addServerVoiceChannelMemberLeaveListener(event -> {
            if (event.getChannel().getCategory().isPresent()
                    && event.getChannel().getCategory().get().getId() == dao.getVoiceCateGory(event.getServer().getId())) {
                if (event.getChannel().getConnectedUserIds().size() == 0) {
                    TempChannelRecord record = dao.getTempChannelData(event.getChannel().getId());
                    dao.deleteTempChannelData(event.getChannel().getId());
                    if (event.getServer().getTextChannelById(record.getTextChannelId()).isPresent()) {
                        event.getServer().getTextChannelById(record.getTextChannelId()).get().delete();
                    }
                    if (event.getServer().getVoiceChannelById(record.getVoiceChannelId()).isPresent()) {
                        event.getServer().getVoiceChannelById(record.getVoiceChannelId()).get().delete();
                    }
                    ArrayList<MessageRecord> mentionList = dao.getMentionMessage(event.getChannel().getId());
                    for (MessageRecord messageRecord : mentionList) {
                        if (api.getMessageByLink(messageRecord.getLink()).isPresent()) {
                            api.getMessageByLink(messageRecord.getLink()).get().join().delete();
                        }
                    }
                    dao.deleteMentionMessage(record.getTextChannelId());
                } else if (event.getChannel().getConnectedUserIds().size() > 0) {
                    TempChannelRecord record = dao.getTempChannelData(event.getChannel().getId());
                    Server server =  event.getServer();
                    if (server.getVoiceChannelById(record.getVoiceChannelId()).isPresent()) {
                        server.getVoiceChannelById(record.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(event.getUser(), new PermissionsBuilder().setAllUnset().build()).update();
                    }
                    if (server.getTextChannelById(record.getTextChannelId()).isPresent()) {
                        server.getTextChannelById(record.getTextChannelId()).get().createUpdater().addPermissionOverwrite(event.getUser(), new PermissionsBuilder().setAllUnset().build()).update();
                    }
                }
            }
        });

    }
}
