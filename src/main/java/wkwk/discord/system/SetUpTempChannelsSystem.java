package wkwk.discord.system;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommandInteraction;
import wkwk.discord.dao.SetUpDAO;
import wkwk.discord.record.ServerDataRecord;
import wkwk.discord.system.core.SystemMaster;

public class SetUpTempChannelsSystem extends SystemMaster {

    public SetUpTempChannelsSystem() {
        // セットアップCommand
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction =  event.getSlashCommandInteraction();
            if (!interaction.getUser().isBot() && interaction.getServer().isPresent() && interaction.getServer().get().hasPermission(interaction.getUser(), PermissionType.ADMINISTRATOR) && interaction.getCommandName().equals("setup")) {
                Server server = interaction.getServer().get();
                SetUpDAO dao = new SetUpDAO();
                ServerDataRecord old = dao.getServerData(server.getId());
                if (server.getChannelCategoryById(old.getVoiceCategoryId()).isPresent()) {
                    server.getChannelCategoryById(old.getVoiceCategoryId()).get().delete();
                }
                if (server.getChannelCategoryById(old.getTextCategoryId()).isPresent()) {
                    server.getChannelCategoryById(old.getTextCategoryId()).get().delete();
                }
                if (server.getChannelById(old.getMentionChannelId()).isPresent()) {
                    server.getChannelById(old.getMentionChannelId()).get().delete();
                }
                if (server.getChannelById(old.getFirstChannelId()).isPresent()) {
                    server.getChannelById(old.getFirstChannelId()).get().delete();
                }

                ServerDataRecord record = new ServerDataRecord();
                record.setServerId(server.getId());
                record.setVoiceCategoryId(server.createChannelCategoryBuilder().setName("TempVoice").create().join().getId());
                record.setTextCategoryId(server.createChannelCategoryBuilder().setName("TempText").addPermissionOverwrite(server.getEveryoneRole(), new PermissionsBuilder().setDenied(PermissionType.VIEW_CHANNEL).build()).create().join().getId());
                record.setMentionChannelId(server.createTextChannelBuilder().setName("募集送信先").create().join().getId());
                record.setFirstChannelId(server.createVoiceChannelBuilder().setName("一時通話作成").create().join().getId());
                dao.updateServerData(record);
                interaction.createImmediateResponder().setContent("一時通話に必要な要素を作成しました。").setFlags(MessageFlag.EPHEMERAL).respond();
            } else if (interaction.getChannel().isPresent() && !interaction.getChannel().get().getType().isServerChannelType()) {
                interaction.createImmediateResponder().setContent("サーバーのみで使用できます。").setFlags(MessageFlag.EPHEMERAL).respond();
            } else if (!interaction.getServer().get().hasPermission(interaction.getUser(), PermissionType.ADMINISTRATOR)) {
                interaction.createImmediateResponder().setContent("サーバー管理者のみが使用できます。").setFlags(MessageFlag.EPHEMERAL).respond();
            }
        });
    }
}
