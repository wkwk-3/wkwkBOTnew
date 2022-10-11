package wkwk.discord.system;

import com.vdurmont.emoji.EmojiManager;
import org.glassfish.hk2.api.MultiException;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.ServerVoiceChannelUpdater;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.SelectMenuOptionBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import wkwk.discord.dao.SlashCommandDAO;
import wkwk.discord.record.*;
import wkwk.discord.system.core.Processing;
import wkwk.discord.system.core.SystemMaster;
import wkwk.discord.system.core.errors.ErrorEmbedCreate;
import wkwk.discord.system.core.errors.ErrorNumber;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CompletionException;

public class SlashCommandSystem extends SystemMaster {
    // madeW
    public SlashCommandSystem() {
        SlashCommandDAO dao = new SlashCommandDAO();
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            String cmd = interaction.getCommandName();
            User sendUser = interaction.getUser();
            // サーバー内
            if (interaction.getServer().isPresent() && interaction.getChannel().isPresent()) {
                Server server = interaction.getServer().get();
                boolean isAdmin = interaction.getServer().get().getAllowedPermissions(interaction.getUser()).contains(PermissionType.ADMINISTRATOR);
                long serverId = server.getId();
                switch (cmd) {
                    case "setup", "set", "remove", "start", "stop", "show", "mess" -> {
                        if (isAdmin) {
                            switch (cmd) {
                                case "set" -> {
                                    if (interaction.getOptionByIndex(0).isPresent()) {
                                        SlashCommandInteractionOption subCommandGroup = interaction.getOptionByIndex(0).get();
                                        switch (subCommandGroup.getName()) {
                                            case "vcat" -> {
                                                if (interaction.getOptionByIndex(0).get().getOptionChannelValueByName("category").isPresent()) {
                                                    long voiceCategoryId = interaction.getOptionByIndex(0).get().getOptionChannelValueByName("category").get().getId();
                                                    if (server.getChannelCategoryById(voiceCategoryId).isPresent()) {
                                                        dao.UpDataVoiceCategory(serverId, voiceCategoryId);
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + voiceCategoryId + ">を一時通話作成先カテゴリに設定しました。").respond();
                                                        break;
                                                    }
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + voiceCategoryId + ">はカテゴリではありません。").respond();
                                                    break;
                                                }
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("カテゴリを入力して下さい。").respond();
                                            }
                                            case "tcat" -> {
                                                if (interaction.getOptionByIndex(0).get().getOptionChannelValueByName("category").isPresent()) {
                                                    long textCategoryId = interaction.getOptionByIndex(0).get().getOptionChannelValueByName("category").get().getId();
                                                    if (server.getChannelCategoryById(textCategoryId).isPresent()) {
                                                        dao.UpDataTextCategory(serverId, textCategoryId);
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + textCategoryId + ">を一時チャット作成先カテゴリに設定しました。").respond();
                                                        break;
                                                    }
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + textCategoryId + ">はカテゴリではありません。").respond();
                                                    break;
                                                }
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("カテゴリを入力して下さい。").respond();
                                            }

                                            case "first" -> {
                                                if (interaction.getOptionByIndex(0).get().getOptionChannelValueByName("voiceChannel").isPresent()) {
                                                    long firstChannelId = interaction.getOptionByIndex(0).get().getOptionChannelValueByName("voiceChannel").get().getId();
                                                    if (server.getVoiceChannelById(firstChannelId).isPresent()) {
                                                        dao.UpDataFirstChannel(serverId, firstChannelId);
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + firstChannelId + ">を一時通話作成用チャンネルに設定しました。").respond();
                                                        break;
                                                    }
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + firstChannelId + ">は通話チャンネルではありません。").respond();
                                                    break;
                                                }
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話チャンネルを入力して下さい。").respond();
                                            }

                                            case "mention" -> {
                                                if (interaction.getOptionByIndex(0).get().getOptionChannelValueByName("textChannel").isPresent()) {
                                                    long mentionChannelId = interaction.getOptionByIndex(0).get().getOptionChannelValueByName("textChannel").get().getId();
                                                    if (server.getTextChannelById(mentionChannelId).isPresent()) {
                                                        dao.UpDataMentionChannel(serverId, mentionChannelId);
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + mentionChannelId + ">を募集送信用チャンネルに設定しました。").respond();
                                                        break;
                                                    }
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + mentionChannelId + ">はテキストチャンネルではありません。").respond();
                                                    break;
                                                }
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("テキストチャンネルを入力して下さい。").respond();
                                            }

                                            case "enable" -> {
                                                if (interaction.getOptionByIndex(0).get().getOptionByIndex(0).isPresent() && interaction.getOptionByIndex(0).get().getOptionByIndex(0).get().getOptionBooleanValueByName("enable").isPresent()) {
                                                    String subCommand = interaction.getOptionByIndex(0).get().getOptionByIndex(0).get().getName();
                                                    boolean enable = interaction.getOptionByIndex(0).get().getOptionByIndex(0).get().getOptionBooleanValueByName("enable").get();
                                                    String flag = enable ? "有効化" : "無効化";
                                                    switch (subCommand) {
                                                        case "temp" -> {
                                                            dao.UpDataTempAll(serverId, enable);
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話作成を" + flag + "しました").respond();
                                                        }
                                                        case "text" -> {
                                                            dao.UpDataTempText(serverId, enable);
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("チャット作成を" + flag + "しました").respond();
                                                        }
                                                    }
                                                }
                                            }

                                            case "size" -> {
                                                if (interaction.getOptionByIndex(0).get().getOptionLongValueByName("num").isPresent()) {
                                                    long size = interaction.getOptionByIndex(0).get().getOptionLongValueByName("num").get();
                                                    if (-1 < size && size < 100) {
                                                        dao.UpDataDefaultSize(serverId, (int) size);
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("デフォルト人数を" + size + "に設定しました。").respond();
                                                        break;
                                                    }
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("0 ~ 99の間で入力して下さい。").respond();
                                                    break;
                                                }
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("人数を入力して下さい。").respond();
                                            }

                                            case "role" -> {
                                                if (interaction.getOptionByIndex(0).get().getOptionStringValueByName("messageId").isPresent()
                                                        && interaction.getOptionByIndex(0).get().getOptionRoleValueByName("role").isPresent()
                                                        && interaction.getOptionByIndex(0).get().getOptionStringValueByName("emoji").isPresent()
                                                        && EmojiManager.isEmoji(interaction.getOptionByIndex(0).get().getOptionStringValueByName("emoji").get().replaceFirst("️", ""))) {
                                                    try {
                                                        long messageId = Long.parseLong(interaction.getOptionByIndex(0).get().getOptionStringValueByName("messageId").get());
                                                        if (dao.CheckIfRole(messageId)) {
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("そのメッセージは対象に登録されていません。").respond();
                                                            break;
                                                        } else if (dao.getReactMessageSize(messageId) > 25) {
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("そのメッセージにはすでに25件のリアクションロールが設定されています。").respond();
                                                            break;
                                                        } else if (interaction.getOptionByIndex(0).get().getOptionRoleValueByName("role").get().getPosition() < api.getYourself().getRoles(server).get(1).getPosition()) { // 指定したロールが自身より上の場合
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("指定したロールがBOTの所持ロールより上位に存在します。\n下記を参照し変更してください。\n`https://www.wkwk.tech/RoleReordering`").respond();
                                                            break;
                                                        }
                                                        Role role = interaction.getOptionByIndex(0).get().getOptionRoleValueByName("role").get();
                                                        String emoji = interaction.getOptionByIndex(0).get().getOptionStringValueByName("emoji").get().replaceFirst("️", "");
                                                        long channelId = dao.getReactChanel(messageId);
                                                        if (server.getTextChannelById(channelId).isPresent()) {
                                                            Message reactionMessage = api.getMessageById(messageId, server.getTextChannelById(channelId).get()).join();
                                                            reactionMessage.addReaction(emoji).join();
                                                            MessageRecord messageRecord = new MessageRecord();
                                                            messageRecord.setMessageId(messageId);
                                                            messageRecord.setTextChannelId(channelId);
                                                            messageRecord.setRole(role);
                                                            messageRecord.setEmoji(emoji);
                                                            messageRecord.setServerId(serverId);
                                                            dao.setReactionRole(messageRecord);
                                                            interaction.createImmediateResponder().setContent(reactionMessage.getLink() + "\nに対してリアクションロール`" + emoji + " to " + role.getName() + "`を設定しました。").setFlags(MessageFlag.EPHEMERAL).respond();
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("数字のみを入力してください。").respond();
                                                    }
                                                }
                                            }

                                            case "mess" -> {
                                                if (interaction.getOptionByIndex(0).isPresent()
                                                        && interaction.getOptionByIndex(0).get().getOptionStringValueByName("messageId").isPresent()
                                                        && interaction.getOptionByIndex(0).get().getOptionChannelValueByName("textChannel").isPresent()) {
                                                    try {

                                                        ServerChannel target = interaction.getOptionByIndex(0).get().getOptionChannelValueByName("textChannel").get();
                                                        long messageId = Long.parseLong(interaction.getOptionByIndex(0).get().getOptionStringValueByName("messageId").get());
                                                        if (dao.getServerReactMessageSize(serverId) > 25) {
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このサーバーは既に25件のリアクションロール先メッセージが設定されています。").respond();
                                                        } else if (dao.CheckIfMessage(messageId)) {
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("既にそのメッセージは設定されています。").respond();
                                                        } else if (!target.getType().isTextChannelType()) {
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("テキストチャンネルを入力して下さい。").respond();
                                                        } else if (target.asTextChannel().isPresent()
                                                                && api.getServerTextChannelById(target.getId()).isPresent()
                                                                && api.getServerTextChannelById(target.getId()).get().getServer().getId() == serverId
                                                                && api.getMessageById(messageId, api.getServerTextChannelById(target.getId()).get()).join().isServerMessage()) {
                                                            MessageRecord messageRecord = new MessageRecord();
                                                            messageRecord.setMessageId(messageId);
                                                            messageRecord.setServerId(serverId);
                                                            messageRecord.setTextChannelId(target.getId());
                                                            messageRecord.setLink(api.getMessageById(messageId, target.asTextChannel().get()).join().getLink().toString());
                                                            dao.setReactMessageData(messageRecord);
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("リアクションメッセージの対象に設定しました。" + api.getMessageById(messageId, api.getServerTextChannelById(target.getId()).get()).join().getLink()).respond();
                                                        }
                                                    } catch (NumberFormatException ignore) {
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("数字のみを入力してください。").respond();
                                                    } catch (CompletionException exception) {
                                                        if ("Unknown Message".equals(exception.getCause().getMessage())) {
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("メッセージが存在しません。").respond();
                                                        }
                                                    }
                                                }
                                            }

                                            case "namepreset" -> {
                                                if (interaction.getOptionByIndex(0).isPresent()
                                                        && interaction.getOptionByIndex(0).get().getOptionStringValueByName("name").isPresent()) {
                                                    String namePreset = interaction.getOptionByIndex(0).get().getOptionStringValueByName("name").get();
                                                    if (dao.CheckIfNamePreset(namePreset)) {
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("その候補はすでに登録済みです。").respond();
                                                    } else if (dao.getNamePresetSize(serverId) > 25) {
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このサーバーは既に25件の名前変更候補が設定されています。").respond();
                                                    } else {
                                                        NamePresetRecord record = new NamePresetRecord();
                                                        record.setServerId(serverId);
                                                        record.setName(namePreset);
                                                        dao.setNamePreset(record);
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("`" + namePreset + "`を候補に登録しました。").respond();
                                                    }
                                                }
                                            }
                                            case "logging" -> {
                                                if (dao.getLoggingSize(serverId) > 25) {
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このサーバーは既に25件の監視項目が設定されています。").respond();
                                                } else {
                                                    SlashCommandInteractionOption sub = interaction.getOptionByIndex(0).get().getOptionByIndex(0).get();
                                                    String subCmd = sub.getName();

                                                    switch (subCmd) {
                                                        case "user" -> {
                                                            LoggingRecord loggingRecord = new LoggingRecord();
                                                            loggingRecord.setServerId(serverId);
                                                            loggingRecord.setTextChannelId(interaction.getChannel().get().getId());
                                                            loggingRecord.setType(subCmd);
                                                            loggingRecord.setTargetChannelId(0);
                                                            if (!(interaction.getChannel().get().getType().isServerChannelType() && interaction.getChannel().get().getType().isTextChannelType())) {
                                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このサーバー内のテキストチャンネルで実行してください。").respond();
                                                            } else if (dao.CheckIfLogging(loggingRecord)) {
                                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このチャンネルは既にユーザーログの対象です。").respond();
                                                            } else {
                                                                dao.setLogging(loggingRecord);
                                                            }
                                                        }
                                                        case "chat" -> {
                                                            if (sub.getOptionChannelValueByName("textChannel").isPresent() && !sub.getOptionChannelValueByName("textChannel").get().getType().isTextChannelType()) {
                                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("監視できるのはテキストチャンネルのみです。").respond();
                                                            } else {
                                                                LoggingRecord loggingRecord = new LoggingRecord();
                                                                loggingRecord.setServerId(serverId);
                                                                loggingRecord.setTextChannelId(interaction.getChannel().get().getId());
                                                                loggingRecord.setType(subCmd);
                                                                loggingRecord.setTargetChannelId(sub.getOptionChannelValueByName("textChannel").get().getId());
                                                                if (!(interaction.getChannel().get().getType().isServerChannelType() && interaction.getChannel().get().getType().isTextChannelType())) {
                                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このサーバー内のテキストチャンネルで実行してください。").respond();
                                                                } else if (dao.CheckIfLogging(loggingRecord)) {
                                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このチャンネルは既にユーザーログの対象です。").respond();
                                                                } else {
                                                                    dao.setLogging(loggingRecord);
                                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("<#" + sub.getOptionChannelValueByName("textChannel").get().getId() + ">の監視を開始します。").respond();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            case "stereo" -> {
                                                if (interaction.getOptionByIndex(0).isPresent()
                                                        && interaction.getOptionByIndex(0).get().getOptionStringValueByName("template").isEmpty()) {
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("募集テンプレートを入力して下さい。").respond();
                                                } else {
                                                    String template = interaction.getOptionByIndex(0).get().getOptionStringValueByName("template").get();
                                                    dao.updateStereoTyped(serverId, template);
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("募集テンプレートを入力して下さい。").respond();
                                                }
                                            }
                                        }
                                    }
                                }
                                case "remove" -> {
                                    if (interaction.getOptionByIndex(0).isPresent()) {
                                        SlashCommandInteractionOption subCommandGroup = interaction.getOptionByIndex(0).get();
                                        switch (subCommandGroup.getName()) {
                                            case "role" -> {
                                                if (subCommandGroup.getOptionStringValueByName("messageID").isPresent()) {
                                                    try {
                                                        long messageId = Long.parseLong(subCommandGroup.getOptionStringValueByName("messageID").get());
                                                        if (dao.CheckIfRole(messageId)) {
                                                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("そのメッセージIDは設定されていません。").respond();
                                                        } else {
                                                            ReactionRoleRecord record = dao.getReactAllData(messageId);
                                                            if (record.getEmojis().size() == 0) {
                                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("リアクションロールは一文字も設定されていません。").respond();
                                                            } else {
                                                                SelectMenuBuilder roleMenuBuilder = new SelectMenuBuilder().setCustomId("removeRole").setPlaceholder("削除したいリアクション").setMaximumValues(1).setMinimumValues(1);
                                                                for (String emoji : record.getEmojis()) {
                                                                    roleMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(emoji).setValue(messageId + "," + emoji).build());
                                                                }
                                                                interaction.createImmediateResponder().respond();
                                                                new MessageBuilder()
                                                                        .setContent("リアクションロール削除")
                                                                        .addComponents(ActionRow.of(roleMenuBuilder.build())).send(interaction.getChannel().get());
                                                            }
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("数字のみを入力してください。").respond();
                                                    }
                                                }
                                            }
                                            case "namepreset" -> {
                                                ArrayList<String> namePreset = dao.getNamePresetAllData(serverId);
                                                if (namePreset.size() == 0) {
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("ネームプリセットは一つも設定されていません。").respond();
                                                } else {
                                                    SelectMenuBuilder nameMenuBuilder = new SelectMenuBuilder().setCustomId("removeName").setPlaceholder("削除したい名前").setMaximumValues(namePreset.size()).setMinimumValues(1);
                                                    for (String name : namePreset) {
                                                        nameMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(name).setValue(name).build());
                                                    }
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("削除用選択肢を送信しました。").respond();
                                                    new MessageBuilder()
                                                            .setContent("リアクションロール削除")
                                                            .addComponents(ActionRow.of(nameMenuBuilder.build())).send(interaction.getChannel().get());
                                                }
                                            }
                                            case "logging" -> {
                                                ArrayList<LoggingRecord> logRecord = dao.getLogging(serverId);
                                                if (logRecord.size() > 0) {
                                                    SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder().setCustomId("removeLogging").setPlaceholder("削除したいlog設定を選んでください").setMaximumValues(logRecord.size()).setMinimumValues(1);
                                                    for (LoggingRecord log : logRecord) {
                                                        if (api.getServerTextChannelById(log.getTargetChannelId()).isPresent() && log.getType().equals("chat")) {
                                                            selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(log.getType() + ":" + api.getServerTextChannelById(log.getTargetChannelId()).get().getName()).setValue(log.getType() + "," + log.getTextChannelId() + "," + log.getTargetChannelId()).build());
                                                        } else if (api.getServerTextChannelById(log.getTargetChannelId()).isPresent() && log.getType().equals("user")) {
                                                            selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(log.getType() + ":" + api.getServerTextChannelById(log.getTargetChannelId()).get().getName()).setValue(log.getType() + "," + log.getTextChannelId()).build());
                                                        } else if (api.getServerTextChannelById(log.getTargetChannelId()).isEmpty()) {
                                                            selectMenuBuilder.addOption(new SelectMenuOptionBuilder().setLabel(log.getType() + ":" + log.getTargetChannelId()).setValue(log.getTextChannelId() + " " + log.getType() + " " + log.getTextChannelId()).build());
                                                        }
                                                    }
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("削除用選択肢を送信しました。").respond();
                                                    new MessageBuilder()
                                                            .setContent("履歴チャンネル削除")
                                                            .addComponents(ActionRow.of(selectMenuBuilder.build())).send(interaction.getChannel().get());
                                                } else {
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("監視項目は一つも設定されていません。").respond();
                                                }
                                            }
                                        }
                                    }
                                }
                                case "start" -> {
                                    if (interaction.getOptionByIndex(0).isPresent()) {
                                        SlashCommandInteractionOption subCommandGroup = interaction.getOptionByIndex(0).get();
                                        if ("delete".equals(subCommandGroup.getName())) {

                                            if (subCommandGroup.getOptionLongValueByName("time").isEmpty()) {
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("削除までの時間を入力して下さい。").respond();
                                            } else if (subCommandGroup.getOptionStringValueByName("unit").isEmpty()) {
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("時間の単位を入力して下さい。").respond();
                                            } else if (
                                                    switch (subCommandGroup.getOptionStringValueByName("unit").get()) {
                                                        case "s", "m", "h", "d" -> false;
                                                        default -> true;
                                                    }) {
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("単位は`s m h d`のいずれかを入力して下さい。").respond();
                                            } else if (!(interaction.getChannel().get().getType().isServerChannelType() && interaction.getChannel().get().getType().isTextChannelType())) {
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このサーバーのテキストチャンネルを選択してください。").respond();
                                            } else if (subCommandGroup.getOptionLongValueByName("time").get() > Integer.MAX_VALUE) {
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("数が大きすぎます").respond();
                                            } else if (subCommandGroup.getOptionLongValueByName("time").get() < 0) {
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("数が小さすぎます").respond();
                                            } else {
                                                DeleteTimeRecord deleteTimeRecord = new DeleteTimeRecord();
                                                deleteTimeRecord.setServerId(serverId);
                                                deleteTimeRecord.setTextChannelId(interaction.getChannel().get().getId());
                                                deleteTimeRecord.setTime(subCommandGroup.getOptionLongValueByName("time").get());
                                                deleteTimeRecord.setUnit(subCommandGroup.getOptionStringValueByName("unit").get());
                                                dao.setDeleteTime(deleteTimeRecord);
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(subCommandGroup.getOptionLongValueByName("time").get() + switch (subCommandGroup.getOptionStringValueByName("unit").get()) {
                                                    case "s" -> "秒";
                                                    case "m" -> "分";
                                                    case "h" -> "時間";
                                                    case "d" -> "日";
                                                    default -> "false";
                                                } + "後にこのチャンネルではチャットが自動削除されます。").respond();
                                            }
                                        } else if ("vote".equals(subCommandGroup.getName())) {
                                            if (subCommandGroup.getOptionStringValueByName("title").isPresent() && subCommandGroup.getOptionStringValueByName("emoji-1").isPresent()) {
                                                VoteDataRecord voteDataRecord = new VoteDataRecord();
                                                ArrayList<VoteRecord> voteRecords = new ArrayList<>();
                                                voteDataRecord.setTitle(subCommandGroup.getOptionStringValueByName("title").get());
                                                voteDataRecord.setServerId(serverId);
                                                String[] options = subCommandGroup.getOptionStringValueByName("emoji-1").get().split(",");
                                                VoteRecord voteRecord = new VoteRecord();
                                                v
                                                for (KnownCustomEmoji emoji : api.getServerById(serverId).get().getCustomEmojis()) {
                                                    emoji.get
                                                }
                                                for (char emoji : options[0].toCharArray()) {
                                                    if (EmojiManager.isEmoji(String.valueOf(emoji))) {

                                                    }
                                                    System.out.println(eomoji);
                                                }
                                                System.out.println(EmojiManager.isEmoji(options[0]));
                                            }
                                        }
                                    }
                                }
                                case "stop" -> {
                                    if (interaction.getOptionByIndex(0).isPresent()) {
                                        SlashCommandInteractionOption subCommandGroup = interaction.getOptionByIndex(0).get();
                                        if ("delete".equals(subCommandGroup.getName())) {
                                            if (!dao.checkIfDeleteTime(interaction.getChannel().get().getId())) {
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このチャンネルは自動削除が設定されていません。").respond();
                                            } else {
                                                dao.deleteDeleteTime(interaction.getChannel().get().getId());
                                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("このチャンネルの自動削除を停止しました。").respond();
                                            }
                                        } else if ("vote".equals(subCommandGroup.getName())) {
                                            // test
                                        }
                                    }
                                }
                                case "show" -> {
                                    StringBuilder namePreset = new StringBuilder();
                                    ArrayList<String> namePresets = dao.getNamePresetAllData(serverId);
                                    for (String namePresetData : namePresets) {
                                        namePreset.append("・").append(namePresetData).append("\n");
                                    }

                                    ArrayList<LoggingRecord> loggingRecords = dao.getLogging(serverId);
                                    StringBuilder userLogging = new StringBuilder();
                                    StringBuilder chatLogging = new StringBuilder();
                                    for (LoggingRecord loggingRecord : loggingRecords) {
                                        switch (loggingRecord.getType()) {
                                            case "user" ->
                                                    userLogging.append("・<#").append(loggingRecord.getTextChannelId()).append(">\n");
                                            case "chat" ->
                                                    chatLogging.append("・<#").append(loggingRecord.getTextChannelId()).append("> `to` <#").append(loggingRecord.getTargetChannelId()).append(">").append("\n");
                                        }
                                    }

                                    StringBuilder deleteTime = new StringBuilder();
                                    ArrayList<DeleteTimeRecord> deleteTimeRecords = dao.getDeleteTimes(serverId);
                                    for (DeleteTimeRecord deleteTimeRecord : deleteTimeRecords) {
                                        deleteTime.append("・<#")
                                                .append(deleteTimeRecord.getTextChannelId())
                                                .append("> : ")
                                                .append(deleteTimeRecord.getTime())
                                                .append(
                                                        switch (deleteTimeRecord.getUnit()) {
                                                            case "s" -> "秒";
                                                            case "m" -> "分";
                                                            case "h" -> "時間";
                                                            case "d" -> "日";
                                                            default -> "アンノウン";
                                                        }
                                                )
                                                .append("後");
                                    }

                                    StringBuilder reactionRole = new StringBuilder();
                                    ArrayList<MessageRecord> reactionMessages = dao.getReactMessages(serverId);
                                    for (MessageRecord messageRecord : reactionMessages) {
                                        ReactionRoleRecord roleRecord = dao.getReactAllData(messageRecord.getMessageId());
                                        ArrayList<String> emojis = roleRecord.getEmojis();
                                        ArrayList<Long> roleIds = roleRecord.getRoleIds();
                                        try {
                                            if (api.getServerTextChannelById(messageRecord.getTextChannelId()).isEmpty()) {
                                                reactionRole.append("・").append(messageRecord.getMessageId()).append("は存在しません。").append("\n");
                                                continue;
                                            }
                                            Message message = api.getMessageById(messageRecord.getMessageId(), api.getServerTextChannelById(messageRecord.getTextChannelId()).get()).join();
                                            reactionRole.append("・").append(message.getLink().toString()).append("\n");
                                        } catch (CompletionException e) {
                                            if (e.getCause().getMessage().equals("Unknown Message")) {
                                                reactionRole.append("・").append(messageRecord.getMessageId()).append("は存在しません。").append("\n");
                                                continue;
                                            }
                                        } catch (MultiException e) {
                                            System.out.println("a");
                                        }
                                        for (int i = 0; i < roleRecord.getRoleIds().size(); i++) {
                                            reactionRole.append("｜・").append(emojis.get(i)).append(" `to` ").append(api.getRoleById(roleIds.get(i)).isPresent() ? api.getRoleById(roleIds.get(i)).get().getName() : "ロールが存在しません").append("\n");
                                        }
                                    }

                                    ServerDataRecord serverDataRecord = dao.getTempSetting(serverId);
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("個人チャットに送信しました。").respond();
                                    String serverDataString = "・メンション送信チャンネルID : " + (serverDataRecord.getMentionChannelId() == -1 ? "保存されていません" : "<#" + serverDataRecord.getMentionChannelId() + ">") + "\n" +
                                            "・一時作成チャネル : " + (serverDataRecord.getFirstChannelId() == -1 ? "保存されていません" : "<#" + serverDataRecord.getFirstChannelId() + ">") + "\n" +
                                            "・通話カテゴリ : " + (serverDataRecord.getVoiceCategoryId() == -1 ? "保存されていません" : "<#" + serverDataRecord.getVoiceCategoryId() + ">") + "\n" +
                                            "・テキストカテゴリ : " + (serverDataRecord.getTextCategoryId() == -1 ? "保存されていません" : "<#" + serverDataRecord.getTextCategoryId() + ">") + "\n" +
                                            "・カスタム募集 : " + (serverDataRecord.getStereoTyped() == null ? "保存されていません" : "<#" + serverDataRecord.getStereoTyped() + ">") + "\n" +
                                            "・通話初期ネーム : " + (serverDataRecord.getDefaultName() == null ? "保存されていません" : "<#" + serverDataRecord.getDefaultName() + ">") + "\n";
                                    new MessageBuilder().setEmbed(new EmbedBuilder()
                                            .setTitle("一覧情報表示 With " + server.getName())
                                            .setAuthor(sendUser)
                                            .addField("一時通話設定", serverDataString)
                                            .addField("名前変更候補", namePreset.toString().equals("") ? "・保存されていません" : namePreset.toString())
                                            .addField("ユーザー入退出", userLogging.toString().equals("") ? "・保存されていません" : userLogging.toString())
                                            .addField("チャット監視", chatLogging.toString().equals("") ? "・保存されていません" : chatLogging.toString())
                                            .addField("自動チャット削除対象", deleteTime.toString().equals("") ? "・保存されていません" : deleteTime.toString())
                                            .addField("リアクションロール", reactionRole.toString().equals("") ? "・保存されていません" : reactionRole.toString())
                                            .setColor(Color.cyan)
                                            .setThumbnail("https://i.imgur.com/nRy1iz3.png")).send(sendUser).join();
                                }
                                case "mess" -> {
                                    if (interaction.getOptionStringValueByName("text").isPresent()) {
                                        String message = interaction.getOptionStringValueByName("text").get().replaceAll(" ", "\n");
                                        MessageBuilder responseMessage = new MessageBuilder().setContent(message);
                                        if (interaction.getOptionStringValueByName("url").isPresent()) {
                                            String[] splitUrls = interaction.getOptionStringValueByName("url").get().split(" ");
                                            for (String url : splitUrls) {
                                                try {
                                                    responseMessage.addAttachment(new URL(url));
                                                } catch (MalformedURLException ex) {
                                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(url + "は画像URLではない。").respond();
                                                }
                                            }
                                        }
                                        responseMessage.send(interaction.getChannel().get()).join();
                                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("代行送信完了。").respond();
                                    }
                                }
                            }
                        } else {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(new ErrorEmbedCreate().create(ErrorNumber.NOT_ADMIN)).respond();
                        }
                    }
                    case "n", "name", "s", "size", "m", "men", "add", "delete", "claim" -> {
                        if (sendUser.getConnectedVoiceChannel(server).isEmpty()) {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話に居る時しか使えません。").respond();
                            break;
                        } else if (!dao.CheckIfChannel(sendUser.getConnectedVoiceChannel(server).get().getId())) {
                            interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("一時通話に居る時しか使えません。").respond();
                            break;
                        }
                        boolean isManage = dao.CheckIfManage(sendUser.getId(), sendUser.getConnectedVoiceChannel(server).get().getId());
                        TempChannelRecord tempChannelRecord = dao.getTempChannelData(sendUser.getConnectedVoiceChannel(server).get().getId());
                        if (isManage) {
                            if (cmd.equals("n") || cmd.equals("name") && interaction.getOptionStringValueByName("name").isPresent()) {
                                sendUser.getConnectedVoiceChannel(server).get().createUpdater().setName(interaction.getOptionStringValueByName("name").get()).update();
                                if (api.getServerTextChannelById(tempChannelRecord.getTextChannelId()).isPresent()) {
                                    api.getServerTextChannelById(tempChannelRecord.getTextChannelId()).get().createUpdater().setName(interaction.getOptionStringValueByName("name").get()).update();
                                }
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話名を変更しました。").respond();
                            } else if (cmd.equals("s") || cmd.equals("size") && interaction.getOptionLongValueByName("size").isPresent()) {
                                if (interaction.getOptionLongValueByName("size").get() > 99 && 0 > interaction.getOptionLongValueByName("size").get()) {
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("0 ~ 99までの数字を入力して下さい。").respond();
                                } else {
                                    sendUser.getConnectedVoiceChannel(server).get().createUpdater().setUserLimit(interaction.getOptionLongValueByName("size").get().intValue()).update();
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("通話上限人数を変更しました。").respond();
                                }
                            } else if (cmd.equals("m") || cmd.equals("men")) {
                                ServerDataRecord serverDataRecord = dao.getMentionData(serverId);
                                if (api.getServerTextChannelById(serverDataRecord.getMentionChannelId()).isEmpty()) {
                                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("メンション送信先が設定されていません。\nサーバー管理者に報告してください。").respond();
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
                            } else if (cmd.equals("claim")) {
                                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("貴方は既に通話管理者です").respond();
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
            }
            switch (cmd) {
                case "ping" -> {
                    long ping = 0L;
                    try {
                        InetAddress address = InetAddress.getByName("8.8.8.8");
                        for (int n = 0; n < 5; n++) {
                            long start = System.currentTimeMillis();
                            boolean ena = address.isReachable(100);
                            long end = System.currentTimeMillis();
                            if (ena) ping += (end - start);
                        }
                        ping /= 5L;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent(ping + "ms").respond();
                }
                case "invite" ->
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("https://wkwk.tech/BT").respond();
                case "guild" ->
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("https://wkwk.tech/GL").respond();
                case "help" ->
                        interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).setContent("https://wkwk.tech/FUNC").respond();
            }
        });
    }
}
