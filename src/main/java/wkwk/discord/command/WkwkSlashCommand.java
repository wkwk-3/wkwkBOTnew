package wkwk.discord.command;

import org.javacord.api.interaction.*;
import wkwk.discord.system.core.SystemMaster;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WkwkSlashCommand extends SystemMaster {
    // madeW

    public void createCommand() {
        System.out.println("translate");
        SlashCommand.with("translate","Translate the message into the specified language and send it.",
                        Arrays.asList(
                                SlashCommandOption.create(SlashCommandOptionType.STRING,"text", "Text to be translated", true),
                                SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING,"Language", "Language after translation", true,
                                        Arrays.asList(
                                                SlashCommandOptionChoice.create("Bulgarian", "BG"),
                                                SlashCommandOptionChoice.create("Czech", "CS"),
                                                SlashCommandOptionChoice.create("Danish", "DA"),
                                                SlashCommandOptionChoice.create("German", "DE"),
                                                SlashCommandOptionChoice.create("Greek", "EL"),
                                                SlashCommandOptionChoice.create("English (British)", "EN-GB"),
                                                SlashCommandOptionChoice.create("English (American)", "EN-US"),
                                                SlashCommandOptionChoice.create("Spanish", "ES"),
                                                SlashCommandOptionChoice.create("Estonian", "ET"),
                                                SlashCommandOptionChoice.create("Finnish", "FI"),
                                                SlashCommandOptionChoice.create("French", "FR"),
                                                SlashCommandOptionChoice.create("Hungarian", "HU"),
                                                SlashCommandOptionChoice.create("Indonesian", "ID"),
                                                SlashCommandOptionChoice.create("Italian", "IT"),
                                                SlashCommandOptionChoice.create("Japanese", "JA"),
                                                SlashCommandOptionChoice.create("Lithuanian", "LT"),
                                                SlashCommandOptionChoice.create("Latvian", "LV"),
                                                SlashCommandOptionChoice.create("Dutch", "NL"),
                                                SlashCommandOptionChoice.create("Polish", "PL"),
                                                SlashCommandOptionChoice.create("Portuguese (Brazilian)", "PT-BR"),
                                                SlashCommandOptionChoice.create("Romanian", "RO"),
                                                SlashCommandOptionChoice.create("Russian", "RU"),
                                                SlashCommandOptionChoice.create("Turkish", "TR"),
                                                SlashCommandOptionChoice.create("Ukrainian", "UK"),
                                                SlashCommandOptionChoice.create("Chinese (simplified)", "ZH")
                                        ))
                        ))
                .addDescriptionLocalization(DiscordLocale.JAPANESE, "指定した言語に翻訳してメッセージを送信します。").createGlobal(api).join();
        System.out.println("ping");
        SlashCommand.with("ping", "BOTの回線速度を計測").createGlobal(api).join();
        System.out.println("claim");
        SlashCommand.with("claim", "一時通話の権限を獲得").createGlobal(api).join();
        System.out.println("invite");
        SlashCommand.with("invite", "BOT招待リンクを表示").createGlobal(api).join();
        System.out.println("guild");
        SlashCommand.with("guild", "サポートサーバー招待リンクを表示").createGlobal(api).join();
        System.out.println("setup");
        SlashCommand.with("setup", "一時通話作成に必要な要素を生成").createGlobal(api).join();
        System.out.println("add");
        SlashCommand.with("add", "追加",
                Collections.singletonList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "user", "特定のユーザーだけ見えるようにする",
                                Collections.singletonList(
                                        SlashCommandOption.create(SlashCommandOptionType.USER, "selectUser", "対象ユーザー", true)
                                )
                        ))
        ).createGlobal(api).join();
        System.out.println("delete");
        SlashCommand.with("delete", "削除",
                Collections.singletonList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "user", "特定のユーザーだけに見えないように",
                                Collections.singletonList(
                                        SlashCommandOption.create(SlashCommandOptionType.USER, "selectUser", "対象ユーザー", true)
                                )
                        ))
        ).createGlobal(api).join();
        System.out.println("set");
        SlashCommand.with("set", "サーバー設定変更",
                        Arrays.asList(
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "vcat", "一時通話チャンネル作成先カテゴリ変更",
                                        Collections.singletonList(
                                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "category", "対象カテゴリ", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "tcat", "一時テキストチャンネル作成先カテゴリ変更",
                                        Collections.singletonList(
                                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "category", "対象カテゴリ", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "first", "一時チャンネル作成時に入る通話チャンネル変更",
                                        Collections.singletonList(
                                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "voiceChannel", "対象チャンネル", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "mention", "メンション送信先変更",
                                        Collections.singletonList(
                                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "textChannel", "対象チャンネル", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND_GROUP, "enable", "機能有効化切り替え",
                                        Arrays.asList(
                                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "temp", "一時チャンネル作成",
                                                        Collections.singletonList(
                                                                SlashCommandOption.create(SlashCommandOptionType.BOOLEAN, "enable", "有効化切り替え", true)
                                                        )),
                                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "text", "一時テキスト作成",
                                                        Collections.singletonList(
                                                                SlashCommandOption.create(SlashCommandOptionType.BOOLEAN, "enable", "有効化切り替え", true)
                                                        ))
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "size", "通話初期制限人数変更",
                                        Collections.singletonList(
                                                SlashCommandOption.create(SlashCommandOptionType.LONG, "num", "人数", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "role", "リアクションロール",
                                        Arrays.asList(
                                                SlashCommandOption.create(SlashCommandOptionType.STRING, "messageId", "メッセージID", true),
                                                SlashCommandOption.create(SlashCommandOptionType.ROLE, "role", "付与ロール", true),
                                                SlashCommandOption.create(SlashCommandOptionType.STRING, "emoji", "対象絵文字", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "mess", "リアクションロール対象メッセージ",
                                        Arrays.asList(
                                                SlashCommandOption.create(SlashCommandOptionType.STRING, "messageId", "メッセージID", true),
                                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "textChannel", "対象チャンネル", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "namePreset", "チャンネルネーム変更候補追加",
                                        Collections.singletonList(
                                                SlashCommandOption.create(SlashCommandOptionType.STRING, "name", "チャンネル名候補", true)
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND_GROUP, "logging", "ログ設定",
                                        Arrays.asList(
                                                SlashCommandOption.create(SlashCommandOptionType.SUB_COMMAND, "USER", "ユーザー履歴"),
                                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "CHAT", "チャット履歴",
                                                        Collections.singletonList(
                                                                SlashCommandOption.create(SlashCommandOptionType.CHANNEL, "textChannel", "対象チャンネル", true)
                                                        ))
                                        )),
                                SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "stereo", "募集分のテンプレ",
                                        Collections.singletonList(
                                                SlashCommandOption.create(SlashCommandOptionType.STRING, "template", "募集テンプレート", true)
                                        ))
                        ))
                .createGlobal(api)
                .join();
        System.out.println("remove");
        SlashCommand.with("remove", "設定削除",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "role", "リアクションロール削除表示",
                                Collections.singletonList(
                                        SlashCommandOption.create(SlashCommandOptionType.STRING, "messageID", "対象メッセージ", true)
                                )),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "namepreset", "名前候補削除表示"),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "logging", "ログ表示削除表示")
                )).createGlobal(api).join();
        System.out.println("start");
        SlashCommand.with("start", "処理開始",
                Collections.singletonList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "delete", "オートデリート開始",
                                Arrays.asList(
                                        SlashCommandOption.create(SlashCommandOptionType.LONG, "time", "削除までの時間", true),
                                        SlashCommandOption.create(SlashCommandOptionType.STRING, "unit", "時間の単位 s m h d ", true)
                                ))
                )).createGlobal(api).join();
        System.out.println("stop");
        SlashCommand.with("stop", "処理終了",
                Collections.singletonList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "delete", "オートデリート終了")
                )).createGlobal(api).join();
        System.out.println("help");
        SlashCommand.with("help", "ヘルプ").createGlobal(api).join();
        System.out.println("show");
        SlashCommand.with("show", "サーバー情報表示").createGlobal(api).join();
        System.out.println("mess");
        SlashCommand.with("mess", "メッセージ送信",
                Arrays.asList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "text", "メッセージ内容", true),
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "url", "添付画像リンク")
                )
        ).createGlobal(api).join();
        System.out.println("name");
        SlashCommand.with("name", "チャンネル名前変更",
                Collections.singletonList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "name", "チャンネル名", true)
                )
        ).createGlobal(api).join();
        System.out.println("size");
        SlashCommand.with("size", "チャンネル人数変更",
                Collections.singletonList(
                        SlashCommandOption.create(SlashCommandOptionType.LONG, "size", "チャンネル最大人数", true)
                )
        ).createGlobal(api).join();
        System.out.println("men");
        SlashCommand.with("men", "チャンネル募集送信",
                Collections.singletonList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "text", "募集内容")
                )
        ).createGlobal(api).join();
        System.out.println("nameS");
        SlashCommand.with("n", "チャンネル名前変更",
                Collections.singletonList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "name", "チャンネル名", true)
                )
        ).createGlobal(api).join();
        System.out.println("sizeS");
        SlashCommand.with("s", "チャンネル人数変更",
                Collections.singletonList(
                        SlashCommandOption.create(SlashCommandOptionType.LONG, "size", "チャンネル最大人数", true)
                )
        ).createGlobal(api).join();
        System.out.println("menS");
        SlashCommand.with("m", "チャンネル募集送信",
                Collections.singletonList(
                        SlashCommandOption.create(SlashCommandOptionType.STRING, "text", "募集内容")
                )
        ).createGlobal(api).join();
        new MessageContextMenuBuilder()
                .setName("translate")
                .setDefaultEnabledForEveryone()
                .createGlobal(api).join();
    }

    public void allDeleteCommands() {
        List<SlashCommand> commands = api.getGlobalSlashCommands().join();
        for (SlashCommand command : commands) {
            command.deleteGlobal();
        }

        for (MessageContextMenu messageContextMenu : api.getGlobalMessageContextMenus().join()) {
            messageContextMenu.deleteGlobal();
        }
    }

    public void commandShow() {
        List<SlashCommand> commands = api.getGlobalSlashCommands().join();
        for (SlashCommand command : commands) {
            System.out.println(command.getName());
        }
        for (MessageContextMenu messageContextMenu : api.getGlobalMessageContextMenus().join()) {
            System.out.println(messageContextMenu.getName());
        }
    }
}
