package wkwk.discord.system;

import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.ButtonInteraction;
import org.javacord.api.interaction.InteractionBase;
import org.javacord.api.interaction.SlashCommandInteraction;
import wkwk.discord.dao.SetUpTempChannelsDAO;
import wkwk.discord.record.ServerDataRecord;
import wkwk.discord.system.core.errors.ErrorEmbedCreate;
import wkwk.discord.system.core.errors.ErrorNumber;
import wkwk.discord.system.core.SystemMaster;

public class SetUpTempChannelsSystem extends SystemMaster {

    SetUpTempChannelsDAO dao = new SetUpTempChannelsDAO();

    public SetUpTempChannelsSystem() {
        // セットアップCommand
        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction =  event.getSlashCommandInteraction();
            if (!interaction.getUser().isBot() && interaction.getServer().isPresent() && interaction.getServer().get().hasPermission(interaction.getUser(), PermissionType.ADMINISTRATOR) && interaction.getCommandName().equals("setup")) {
                if (dao.checkIfTempChannelData(interaction.getServer().get().getId())) {
                    interaction.createImmediateResponder()
                            .addComponents(ActionRow.of(
                                    Button.primary("temp_gene_yes", "YES"),
                                    Button.danger("temp_gene_no", "NO")))
                            .setFlags(MessageFlag.EPHEMERAL).setContent("このサーバーはすでに一時通話作成用の要素が、\n一部もしくは全てが登録されています。\n本当に再生成いたしますか？")
                            .respond();
                } else {
                    new TempChannelCreate().create(interaction);
                }
            } else if (interaction.getChannel().isPresent() && !interaction.getChannel().get().getType().isServerChannelType()) {
                interaction.createImmediateResponder().setContent("サーバーのみで使用できます。").setFlags(MessageFlag.EPHEMERAL).respond();
            } else if (!interaction.getServer().get().hasPermission(interaction.getUser(), PermissionType.ADMINISTRATOR)) {
                interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(new ErrorEmbedCreate().create(ErrorNumber.NOT_ADMIN)).respond();
            }
        });

        api.addButtonClickListener(selectMenuChooseEvent -> {
            ButtonInteraction interaction = selectMenuChooseEvent.getButtonInteraction();
            if ("temp_gene_yes".equals(interaction.getCustomId())) {
                if (interaction.getServer().isPresent() && interaction.getServer().get().getPermissions(interaction.getUser()).getAllowedPermission().contains(PermissionType.ADMINISTRATOR)) {
                    new TempChannelCreate().create(interaction);
                } else {
                    interaction.createImmediateResponder().setFlags(MessageFlag.EPHEMERAL).addEmbed(new ErrorEmbedCreate().create(ErrorNumber.NOT_ADMIN)).respond();
                }
            }
            switch (interaction.getCustomId()) {
                case "temp_gene_no", "temp_gene_yes" -> {
                    interaction.createImmediateResponder().respond();
                    interaction.getMessage().delete();
                }
            }
        });
    }
}
class TempChannelCreate{
    SetUpTempChannelsDAO dao = new SetUpTempChannelsDAO();
    protected void create(InteractionBase interaction) {
        Server server = interaction.getServer().get();
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
    }
}
