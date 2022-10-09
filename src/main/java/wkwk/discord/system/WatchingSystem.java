package wkwk.discord.system;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import wkwk.discord.dao.WatchingSystemDAO;
import wkwk.discord.record.WatchingRecord;
import wkwk.discord.system.core.SystemMaster;

import java.awt.*;

public class WatchingSystem extends SystemMaster {
    // madeW
    WatchingSystemDAO dao = new WatchingSystemDAO();

    WatchingRecord watchingSetting = dao.getWatchingSetting();

    Server wServer;

    public WatchingSystem() {
        if (api.getServerById(watchingSetting.getServerId()).isPresent()) {
            wServer = api.getServerById(watchingSetting.getServerId()).get();
        }

        // 一時通話作成
        api.addServerVoiceChannelMemberJoinListener(serverVoiceChannelMemberJoinEvent -> {
            Server server = serverVoiceChannelMemberJoinEvent.getServer();
            if (!serverVoiceChannelMemberJoinEvent.getUser().isBot() && serverVoiceChannelMemberJoinEvent.getChannel().getId() == dao.getFirstChannelId(server.getId())
                    && server.getTextChannelById(watchingSetting.getCreateWId()).isPresent()) {
                ServerTextChannel wTextChannel = server.getTextChannelById(watchingSetting.getCreateWId()).get();
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("一時通話作成")
                        .addInlineField("サーバネーム", server.getName())
                        .addInlineField("サーバID", server.getIdAsString())
                        .setColor(Color.GREEN);
                wTextChannel.sendMessage(embed);
            }
        });

        // 名前変更
        /*
        api.addSlashCommandCreateListener(slashCommandCreateEvent ->{
            SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            User sendUser = interaction.getUser();
            if (interaction.getServer().isPresent() && interaction.getChannel().isPresent()) {
                Server server = interaction.getServer().get();
                long serverId = server.getId();
                switch (cmd) {
                    case "n", "name", "s", "size", "m", "men", "add", "delete", "claim" -> {
                        if (sendUser.getConnectedVoiceChannel(server).isEmpty()) {
                            // 度の通話にも入っていない
                            break;
                        } else if (!dao.CheckIfChannel(sendUser.getConnectedVoiceChannel(server).get().getId())) {
                            // 一時通話以外の通話に入っている
                            break;
                        }
                        boolean isManage = dao.CheckIfManage(sendUser.getId(), sendUser.getConnectedVoiceChannel(server).get().getId());
                        TempChannelRecord tempChannelRecord = dao.getTempChannelData(sendUser.getConnectedVoiceChannel(server).get().getId());
                        if (isManage) {
                            if (cmd.equals("n") || cmd.equals("name") && interaction.getOptionStringValueByName("name").isPresent()) {
                                // 名前変更完了
                                String name = interaction.getOptionStringValueByName("name").get();
                            } else if (cmd.equals("s") || cmd.equals("size") && interaction.getOptionLongValueByName("size").isPresent()) {
                                if (interaction.getOptionLongValueByName("size").get() > 99 && 0 > interaction.getOptionLongValueByName("size").get()) {
                                    // 人数変更失敗
                                    long size = interaction.getOptionLongValueByName("size").get();
                                } else {
                                    // 人数変更完了
                                    int size = interaction.getOptionLongValueByName("size").get().intValue();
                                }
                            } else if (cmd.equals("m") || cmd.equals("men")) {
                                ServerDataRecord serverDataRecord = dao.getMentionData(serverId);
                                if (api.getServerTextChannelById(serverDataRecord.getMentionChannelId()).isEmpty()) {
                                    // メンション送信先未設定
                                    break;
                                }
                                ServerTextChannel textChannel = api.getServerTextChannelById(serverDataRecord.getMentionChannelId()).get();
                                EmbedBuilder embed = new EmbedBuilder()
                                        .setAuthor(interaction.getUser())
                                        .setTitle("募集");
                                if (interaction.getOptionStringValueByName("text").isPresent()) {
                                    embed.addField("募集内容", interaction.getOptionStringValueByName("text").get());
                                }
                                embed.addField("チャンネル", "<#" + sendUser.getConnectedVoiceChannel(server).get().getId() + ">");
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("募集送信完了。").respond();
                                Message message = new MessageBuilder().setEmbed(embed).send(textChannel).join();
                                MessageRecord messageRecord = new MessageRecord();
                                messageRecord.setVoiceChannelId(sendUser.getConnectedVoiceChannel(server).get().getId());
                                messageRecord.setTextChannelId(tempChannelRecord.getTextChannelId());
                                messageRecord.setServerId(interaction.getServer().get().getId());
                                messageRecord.setMessageId(message.getId());
                                messageRecord.setLink(message.getLink().toString());
                                dao.setMentionMessage(messageRecord);
                            } else if (cmd.equals("add")) {
                                String subCommand = interaction.getOptionByIndex(0).get().getName();
                                if ("user".equals(subCommand)) {
                                    if (interaction.getOptionByIndex(0).get().getOptionUserValueByName("selectUser").isPresent()) {
                                        User user = interaction.getOptionByIndex(0).get().getOptionUserValueByName("selectUser").get();
                                        ServerVoiceChannel voiceChannel = sendUser.getConnectedVoiceChannel(server).get();
                                        ServerVoiceChannelUpdater updater = voiceChannel.createUpdater();
                                        updater.addPermissionOverwrite(user, new Processing().getUserPermission());
                                        updater.update();
                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(user.getName() + "を追加しました。").respond();
                                    } else {
                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("ユーザーを選択してください。").respond();
                                    }
                                }
                            } else if (cmd.equals("delete")) {
                                if (interaction.getOptionByIndex(0).get().getOptionUserValueByName("selectUser").isPresent()) {
                                    User user = interaction.getOptionByIndex(0).get().getOptionUserValueByName("selectUser").get();
                                    ServerVoiceChannel voiceChannel = sendUser.getConnectedVoiceChannel(server).get();
                                    ServerVoiceChannelUpdater updater = voiceChannel.createUpdater();
                                    updater.addPermissionOverwrite(user, new PermissionsBuilder().setDenied(PermissionType.CONNECT, PermissionType.VIEW_CHANNEL).build());
                                    updater.update();
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(user.getName() + "が通話に入れないようにしました。").respond();
                                } else {
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("ユーザーを選択してください").respond();
                                }
                            }
                        } else {
                            if (cmd.equals("claim")) {
                                boolean claimSw = sendUser.getConnectedVoiceChannel(server).get().getConnectedUserIds().contains(tempChannelRecord.getOwnerUserId());
                                if (!claimSw) {
                                    sendUser.getConnectedVoiceChannel(server).get().createUpdater().addPermissionOverwrite(sendUser, new Processing().getChannelManagePermission()).update();
                                    if (api.getServerTextChannelById(tempChannelRecord.getTextChannelId()).isPresent()) {
                                        api.getServerTextChannelById(tempChannelRecord.getTextChannelId()).get().createUpdater().addPermissionOverwrite(sendUser, new Processing().getChannelManagePermission()).update();
                                    }
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(sendUser.getName() + "が新しく通話管理者になりました").respond();
                                } else {
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話管理者が通話にいらっしゃいます").respond();
                                }
                            }
                        }
                    }
                }
        });


        // 管理変更

        // 人数変更

        // 募集送信


        // 開始

        // 終了

        // 追加



         */
    }
}
