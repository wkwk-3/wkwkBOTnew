package wkwk.discord.core;

import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import wkwk.deepl.WebSystem;
import wkwk.discord.command.WkwkSlashCommand;
import wkwk.discord.system.*;
import wkwk.discord.system.autodelete.AutoDeleteRegisterSystem;
import wkwk.discord.system.autodelete.AutoDeleteRunSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Start {
    // madeW
    public static void main(String[] args) {

        new WebSystem();
        new JoinAndLeaveServerSystem();
        new SetUpTempChannelsSystem();
        new TempChannelSystem();
        new InfoMessageSystem();
        new SlashCommandSystem();
        new DeleteElementSystem();
        new AutoDeleteRegisterSystem();
        new AutoDeleteRunSystem();
        new WatchingSystem();
        new DeleteSelectSystem();
        new ReactionRoleSystem();
        new TranslateSystem();
        // new testSystem();
        System.out.println(new BotInfoSystem().getInviteUrl(new PermissionsBuilder().setAllowed(PermissionType.ADMINISTRATOR).build()));

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            WkwkSlashCommand wkwkSlashCommand = new WkwkSlashCommand();
            while (true) {
                String cmd = br.readLine();
                switch (cmd) {
                    case "stop" -> {
                        System.out.println("システムを終了します");
                        System.exit(0);
                    }
                    case "reload" -> {

                    }
                    case "ts" -> {

                    }
                    case "tp" -> {

                    }
                    case "tc" -> {
                        wkwkSlashCommand.createTestCommand();
                        System.out.println("テストコマンド作成完了");
                    }
                    case "tcd" -> {
                        wkwkSlashCommand.deleteTestCommand();
                        System.out.println("テストコマンド削除完了");
                    }
                    case "tcs" -> {

                    }

                    case "commandCreate" -> {
                        wkwkSlashCommand.createCommand();
                        System.out.println("Command新規作成完了");
                    }
                    case "AllCommandDelete" -> {
                        wkwkSlashCommand.allDeleteCommands();
                        System.out.println("全削除完了");
                    }
                    case "AllCommandReload" -> {
                        wkwkSlashCommand.allDeleteCommands();
                        System.out.println("全削除完了");
                        wkwkSlashCommand.createCommand();
                        System.out.println("リロード完了");
                    }
                    case "commandShow" -> {
                        System.out.println("\n");
                        wkwkSlashCommand.commandShow();
                    }
                    case "invite" ->
                            System.out.println(new BotInfoSystem().getInviteUrl(new PermissionsBuilder().setAllowed(PermissionType.ADMINISTRATOR).build()));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
