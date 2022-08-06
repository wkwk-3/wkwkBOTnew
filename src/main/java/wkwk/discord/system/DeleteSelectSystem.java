package wkwk.discord.system;

import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SelectMenuInteraction;
import wkwk.discord.dao.DeleteSelectSystemDAO;
import wkwk.discord.record.LoggingRecord;
import wkwk.discord.record.NamePresetRecord;
import wkwk.discord.record.ReactionRoleRecord;
import wkwk.discord.record.TempChannelRecord;
import wkwk.discord.system.core.SystemMaster;

import java.util.ArrayList;
import java.util.Objects;

public class DeleteSelectSystem extends SystemMaster {
    DeleteSelectSystemDAO dao = new DeleteSelectSystemDAO();

    public DeleteSelectSystem() {
        api.addSelectMenuChooseListener(event -> {
            SelectMenuInteraction interaction = event.getSelectMenuInteraction();
            if (interaction.getServer().isPresent()) {
                Server server = interaction.getServer().get();
                boolean isAdmin = server.getAllowedPermissions(interaction.getUser()).contains(PermissionType.ADMINISTRATOR);
                if (server.getConnectedVoiceChannel(interaction.getUser()).isPresent()) {
                    ServerVoiceChannel targetVoiceChannel = server.getConnectedVoiceChannel(interaction.getUser()).get();
                    TempChannelRecord tempChannelRecord = dao.getTempChannelData(targetVoiceChannel.getId());
                    switch (interaction.getCustomId()) {
                        case "name" -> {
                            if (tempChannelRecord.getOwnerUserId() != interaction.getUser().getId()) {
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方は通話管理者でがございません。").respond();
                                break;
                            }
                            targetVoiceChannel.createUpdater().setName(interaction.getChosenOptions().get(0).getValue()).update();
                            if (server.getTextChannelById(tempChannelRecord.getTextChannelId()).isPresent()) {
                                server.getTextChannelById(tempChannelRecord.getTextChannelId()).get().createUpdater().setName(interaction.getChosenOptions().get(0).getValue()).update();
                            }
                            interaction.createImmediateResponder().respond();
                            interaction.getMessage().delete();
                        }
                        case "size" -> {
                            if (tempChannelRecord.getOwnerUserId() != interaction.getUser().getId()) {
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方は通話管理者でがございません。").respond();
                                break;
                            }
                            targetVoiceChannel.createUpdater().setUserLimit(Integer.parseInt(interaction.getChosenOptions().get(0).getValue())).update();
                            interaction.createImmediateResponder().respond();
                            interaction.getMessage().delete();
                        }
                    }
                }
                switch (interaction.getCustomId()) {
                    case "removeRole" -> {
                        if (!isAdmin) {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方はサーバー管理者ではございません。").respond();
                            break;
                        }
                        ReactionRoleRecord roleRecord = new ReactionRoleRecord();
                        ArrayList<String> emojis = new ArrayList<>();
                        for (SelectMenuOption selectMenuOption : interaction.getChosenOptions()) {
                            String[] values = selectMenuOption.getValue().split(",");
                            if (roleRecord.getMessageId() == -1) roleRecord.setMessageId(Long.parseLong(values[0]));
                            emojis.add(values[1]);
                        }
                        roleRecord.setLink(dao.getReactMessageLink(roleRecord.getMessageId()));
                        if (api.getMessageByLink(roleRecord.getLink()).isPresent()) {
                            Message message = api.getMessageByLink(roleRecord.getLink()).get().join();
                            for (String emoji : emojis) {
                                message.removeReactionsByEmoji(api.getYourself(), emoji).join();
                            }
                        }
                        roleRecord.setEmojis(emojis);
                        dao.deleteReactionRole(roleRecord);
                        interaction.getMessage().delete();
                        StringBuilder content = new StringBuilder(emojis.get(0));
                        for (String emoji : emojis) {
                            if (emojis.indexOf(emoji) == 0) continue;
                            content.append(emoji).append(",");
                        }
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(content + "を削除いたしました。").respond();
                    }
                    case "removeName" -> {
                        if (!isAdmin) {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方はサーバー管理者ではございません。").respond();
                            break;
                        }
                        ArrayList<NamePresetRecord> namePresetRecords = new ArrayList<>();
                        for (SelectMenuOption selectMenuOption : interaction.getChosenOptions()) {
                            NamePresetRecord record = new NamePresetRecord();
                            record.setServerId(server.getId());
                            record.setName(selectMenuOption.getValue());
                            namePresetRecords.add(record);
                        }
                        dao.deleteNamePreset(namePresetRecords); // 削除実行
                        StringBuilder content = new StringBuilder("'").append(namePresetRecords.get(0).getName()).append("`");
                        for (NamePresetRecord record : namePresetRecords) {
                            if (namePresetRecords.indexOf(record) == 0) continue;
                            content.append("\n`").append(record.getName()).append("`");
                        }
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("変更候補 " + content + "を削除しました。").respond();
                    }
                    case "removeLogging" -> {
                        if (!isAdmin) {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方はサーバー管理者ではございません。").respond();
                            break;
                        }
                        ArrayList<LoggingRecord> userLoggingRecords = new ArrayList<>();
                        ArrayList<LoggingRecord> chatLoggingRecords = new ArrayList<>();
                        for (SelectMenuOption selectMenuOption : interaction.getChosenOptions()) {
                            LoggingRecord record = new LoggingRecord();
                            record.setServerId(server.getId());
                            String[] values = selectMenuOption.getValue().split(",");
                            record.setType(values[0]);
                            record.setTextChannelId(Long.parseLong(values[1]));
                            if (Objects.equals(values[0], "chat")) {
                                record.setTargetChannelId(Long.parseLong(values[2]));
                                chatLoggingRecords.add(record);
                                continue;
                            }
                            userLoggingRecords.add(record);
                        }
                        dao.deleteLogging(userLoggingRecords, chatLoggingRecords); // 削除実行
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("選択したログ設定を削除しました。").respond();
                    }
                }
            }
        });
    }
}
