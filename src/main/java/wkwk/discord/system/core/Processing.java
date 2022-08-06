package wkwk.discord.system.core;

import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;

public class Processing {

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

    public String rePressInfoMessage(User user, boolean hideBy, boolean lockBy) {
        String infoMessage = """
                チャンネル管理者 : &mention&
                表示状態 : &hide&
                ロック状態 : &lock&""";
        return infoMessage.replaceFirst("&mention&", user.getMentionTag())
                .replaceFirst("&hide&", hideBy ? "非表示中" : "表示中")
                .replaceFirst("&lock&", lockBy ? "ロック中" : "アンロック中");
    }
}
