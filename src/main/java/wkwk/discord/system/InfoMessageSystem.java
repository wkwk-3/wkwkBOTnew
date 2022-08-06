package wkwk.discord.system;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.SelectMenuOptionBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.ButtonInteraction;
import wkwk.discord.dao.InfoMessageDAO;
import wkwk.discord.record.MessageRecord;
import wkwk.discord.record.ServerDataRecord;
import wkwk.discord.record.TempChannelRecord;
import wkwk.discord.system.core.Processing;
import wkwk.discord.system.core.SystemMaster;

import java.util.ArrayList;

public class InfoMessageSystem extends SystemMaster {
    public InfoMessageSystem() {
        api.addButtonClickListener(event -> {
            ButtonInteraction interaction = event.getButtonInteraction();
            String customId = interaction.getCustomId();
            if (interaction.getServer().isPresent() && interaction.getChannel().isPresent()) {
                InfoMessageDAO dao = new InfoMessageDAO();
                TempChannelRecord tempChannelRecord =  dao.getTempChannelData(interaction.getChannel().get().getId());
                Processing processing = new Processing();
                switch (customId) {
                    case "hide" -> {
                        if (!tempChannelRecord.isExistBy()) {
                            interaction.createImmediateResponder().setContent("一時テキストチャンネルの中で押してください。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        User user = interaction.getUser();
                        if (user.getId() != tempChannelRecord.getOwnerUserId()) {
                            interaction.createImmediateResponder().setContent("あなたは通話管理者ではありません。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        tempChannelRecord.setHideBy(!tempChannelRecord.isHideBy());
                        Server server = interaction.getServer().get();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).isPresent()) {
                            PermissionsBuilder permissionsBuilder = new PermissionsBuilder();
                            if (tempChannelRecord.isLockBy()) {
                                permissionsBuilder.setDenied(PermissionType.CONNECT);
                            } else {
                                permissionsBuilder.setUnset(PermissionType.CONNECT);
                            }
                            if (tempChannelRecord.isHideBy()) {
                                server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(server.getEveryoneRole(), permissionsBuilder.setDenied(PermissionType.VIEW_CHANNEL).build()).update();
                                interaction.createImmediateResponder().setContent("通話を非表示にしました").setFlags(MessageFlag.EPHEMERAL).respond();
                            } else {
                                server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(server.getEveryoneRole(), permissionsBuilder.setUnset(PermissionType.VIEW_CHANNEL).build()).update();
                                interaction.createImmediateResponder().setContent("通話を表示しました").setFlags(MessageFlag.EPHEMERAL).respond();
                            }
                        }
                        dao.updateTempData(tempChannelRecord);
                        interaction.getMessage().createUpdater().setContent(processing.rePressInfoMessage(user, tempChannelRecord.isHideBy(), tempChannelRecord.isLockBy())).applyChanges().join();
                    }
                    case "lock" -> {
                        User user = interaction.getUser();
                        if (user.getId() != tempChannelRecord.getOwnerUserId()) {
                            interaction.createImmediateResponder().setContent("あなたは通話管理者ではありません。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        tempChannelRecord.setLockBy(!tempChannelRecord.isLockBy());
                        Server server = interaction.getServer().get();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).isPresent()) {
                            PermissionsBuilder permissionsBuilder = new PermissionsBuilder();
                            if (tempChannelRecord.isHideBy()) {
                                permissionsBuilder.setDenied(PermissionType.VIEW_CHANNEL);
                            } else {
                                permissionsBuilder.setUnset(PermissionType.VIEW_CHANNEL);
                            }
                            if (tempChannelRecord.isLockBy()) {
                                server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(server.getEveryoneRole(), permissionsBuilder.setDenied(PermissionType.CONNECT).build()).update();
                                interaction.createImmediateResponder().setContent("通話をロックにしました").setFlags(MessageFlag.EPHEMERAL).respond();
                            } else {
                                server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(server.getEveryoneRole(), permissionsBuilder.setUnset(PermissionType.CONNECT).build()).update();
                                interaction.createImmediateResponder().setContent("通話をアンロックしました").setFlags(MessageFlag.EPHEMERAL).respond();
                            }
                        }
                        dao.updateTempData(tempChannelRecord);
                        interaction.getMessage().createUpdater().setContent(processing.rePressInfoMessage(user, tempChannelRecord.isLockBy(), tempChannelRecord.isLockBy())).applyChanges().join();
                    }
                    case "name" -> {
                        User user = interaction.getUser();
                        if (user.getId() != tempChannelRecord.getOwnerUserId()) {
                            interaction.createImmediateResponder().setContent("あなたは通話管理者ではありません。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        ArrayList<String> names = dao.getNamePreset(interaction.getServer().get().getId());
                        if (names.size() > 0) {
                            SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("name").setPlaceholder("変更したい名前を設定してください").setMaximumValues(1).setMinimumValues(1);
                            for (String name : names) {
                                selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(name).setValue(name).build());
                            }
                            new MessageBuilder()
                                    .setContent("通話名前変更")
                                    .addComponents(ActionRow.of(selectMenuBuilder.build())).send(interaction.getChannel().get());
                            interaction.createImmediateResponder().respond();
                        } else {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("選択肢が存在しません。\n`/set namePreset name:`\n**name:の後**に選択肢に追加したい文字に変更して実行してください").respond();
                        }
                    }
                    case "next" -> {
                        interaction.getMessage().createUpdater().removeAllComponents().addComponents(
                                ActionRow.of(Button.danger("back", "前の項目"),
                                        Button.success("remove-recruiting", "募集文削除"),
                                        Button.success("hide", "非表示切替"),
                                        Button.success("lock", "参加許可切替"))).applyChanges();
                        interaction.createImmediateResponder().respond();
                    }
                    case "back" -> {
                        interaction.getMessage().createUpdater().removeAllComponents().addComponents(
                                ActionRow.of(Button.success("name", "通話名前変更"),
                                        Button.success("size", "通話人数変更"),
                                        Button.success("send-recruiting", "募集送信"),
                                        Button.success("claim", "通話権限獲得"),
                                        Button.danger("next", "次の項目"))).applyChanges();
                        interaction.createImmediateResponder().respond();
                    }
                    case "size" -> {
                        if (interaction.getUser().getId() != tempChannelRecord.getOwnerUserId()) {
                            interaction.createImmediateResponder().setContent("あなたは通話管理者ではありません。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("size").setPlaceholder("変更したい人数を選択してください").setMaximumValues(1).setMinimumValues(1);
                        selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(Integer.toString(0)).setValue(Integer.toString(0)).build());
                        for (int n = 2; n < 7; n++) {
                            selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(Integer.toString(n)).setValue(Integer.toString(n)).build());
                        }
                        new MessageBuilder()
                                .setContent("通話人数変更")
                                .addComponents(ActionRow.of(selectMenuBuilder.build())).send(interaction.getChannel().get());
                        interaction.createImmediateResponder().respond();
                    }

                    case "send-recruiting" -> { // 募集送信
                        if (interaction.getUser().getId() != tempChannelRecord.getOwnerUserId()) {
                            interaction.createImmediateResponder().setContent("あなたは通話管理者ではありません。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        ServerDataRecord mentionRecord = dao.getMentionData(interaction.getServer().get().getId());
                        if (tempChannelRecord.getOwnerUserId() == interaction.getUser().getId() && interaction.getServer().get().getTextChannelById(mentionRecord.getMentionChannelId()).isPresent()) {
                            ServerTextChannel textChannel = interaction.getServer().get().getTextChannelById(mentionRecord.getMentionChannelId()).get();
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setAuthor(interaction.getUser())
                                    .setTitle("募集");
                            if (api.getServerVoiceChannelById(tempChannelRecord.getVoiceChannelId()).isPresent()) {
                                embed.addField("チャンネル", "<#" + api.getServerVoiceChannelById(tempChannelRecord.getVoiceChannelId()).get().getIdAsString() + ">");
                            }
                            Message message = new MessageBuilder().setEmbed(embed).send(textChannel).join();
                            MessageRecord messageRecord = new MessageRecord();
                            messageRecord.setVoiceChannelId(tempChannelRecord.getVoiceChannelId());
                            messageRecord.setTextChannelId(tempChannelRecord.getTextChannelId());
                            messageRecord.setServerId(interaction.getServer().get().getId());
                            messageRecord.setMessageId(message.getId());
                            messageRecord.setLink(message.getLink().toString());
                            dao.setMentionMessage(messageRecord);
                            interaction.createImmediateResponder().respond();
                        }
                    }
                    case "remove-recruiting" -> {
                        if (interaction.getUser().getId() != tempChannelRecord.getOwnerUserId()) {
                            interaction.createImmediateResponder().setContent("あなたは通話管理者ではありません。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        ServerDataRecord mentionRecord = dao.getMentionData(interaction.getServer().get().getId());
                        if (tempChannelRecord.getOwnerUserId() == interaction.getUser().getId() && interaction.getServer().get().getTextChannelById(mentionRecord.getMentionChannelId()).isPresent()) {
                            ArrayList<MessageRecord> mentionList = dao.getMentionMessage(interaction.getChannel().get().getId());
                            for (MessageRecord messageRecord : mentionList) {
                                if (api.getMessageByLink(messageRecord.getLink()).isPresent()){
                                    api.getMessageByLink(messageRecord.getLink()).get().join().delete();
                                }
                            }
                            dao.deleteMentionMessage(tempChannelRecord.getTextChannelId());
                            interaction.createImmediateResponder().respond();
                        }
                    }
                    case "claim" -> {
                        if (interaction.getUser().getId() == tempChannelRecord.getOwnerUserId()) {
                            interaction.createImmediateResponder().setContent("あなたはすでに通話管理者です。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        Server server = interaction.getServer().get();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).isPresent() && server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).get().getConnectedUserIds().contains(tempChannelRecord.getOwnerUserId())) {
                            interaction.createImmediateResponder().setContent("通話管理者が通話にいらっしゃいます。").setFlags(MessageFlag.EPHEMERAL).respond();
                            break;
                        }
                        dao.updateTempChannelOwner(interaction.getUser().getId(), tempChannelRecord.getVoiceChannelId());
                        interaction.getMessage().createUpdater().setContent(processing.rePressInfoMessage(interaction.getUser(), tempChannelRecord.isHideBy(), tempChannelRecord.isLockBy())).applyChanges().join();
                        if (server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).isPresent()) {
                            server.getVoiceChannelById(tempChannelRecord.getVoiceChannelId()).get().createUpdater().addPermissionOverwrite(interaction.getUser(), processing.getChannelManagePermission()).update();
                        }
                        if (server.getTextChannelById(tempChannelRecord.getTextChannelId()).isPresent()) {
                            server.getTextChannelById(tempChannelRecord.getTextChannelId()).get().createUpdater().addPermissionOverwrite(interaction.getUser(), processing.getChannelManagePermission()).update();
                        }
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話管理者になりました。").respond();
                    }
                }

            }
        });
    }
}
