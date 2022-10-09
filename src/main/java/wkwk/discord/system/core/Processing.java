package wkwk.discord.system.core;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;

public class Processing {
    // madeW

    public Permissions getUserPermission() {
        return
                new PermissionsBuilder()
                        .setAllowed(
                                PermissionType.VIEW_CHANNEL,
                                PermissionType.SEND_MESSAGES,
                                PermissionType.ADD_REACTIONS,
                                PermissionType.ATTACH_FILE,
                                PermissionType.USE_VOICE_ACTIVITY,
                                PermissionType.STREAM,
                                PermissionType.SPEAK,
                                PermissionType.READ_MESSAGE_HISTORY,
                                PermissionType.CONNECT,
                                PermissionType.CHANGE_NICKNAME
                        )
                        .build();
    }

    public Permissions getChannelManagePermission() {
        return
                new PermissionsBuilder()
                        .setAllowed(
                                PermissionType.VIEW_CHANNEL,
                                PermissionType.SEND_MESSAGES,
                                PermissionType.ADD_REACTIONS,
                                PermissionType.ATTACH_FILE,
                                PermissionType.USE_VOICE_ACTIVITY,
                                PermissionType.STREAM,
                                PermissionType.SPEAK,
                                PermissionType.READ_MESSAGE_HISTORY,
                                PermissionType.CONNECT,
                                PermissionType.CHANGE_NICKNAME,
                                PermissionType.MANAGE_CHANNELS,
                                PermissionType.MANAGE_MESSAGES,
                                PermissionType.USE_APPLICATION_COMMANDS
                        )
                        .build();
    }

    public EmbedBuilder rePressInfoMessage(User user, boolean hideBy, boolean lockBy) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setTitle("通話設定")
                .addInlineField("通話管理者", user.getDiscriminatedName())
                .addInlineField("表示設定", hideBy ? "非表示中" : "表示中")
                .addInlineField("ロック設定", lockBy ? "ロック中" : "アンロック中")
                .setThumbnail(user.getAvatar());
        return embedBuilder;
    }
}
