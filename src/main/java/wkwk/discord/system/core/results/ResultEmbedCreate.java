package wkwk.discord.system.core.results;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;

public class ResultEmbedCreate {
    // madeW
    public EmbedBuilder create(int errorNum, String contentText) {
        ArrayList<String> content = getContent(errorNum);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle(content.get(0));
        embedBuilder.setDescription(contentText);
        return embedBuilder;
    }

    public EmbedBuilder create(String contentText) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle("完了");
        embedBuilder.setDescription(contentText);
        return embedBuilder;
    }

    public EmbedBuilder create(int errorNum) {
        ArrayList<String> content = getContent(errorNum);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.RED);
        embedBuilder.setTitle(content.get(0));
        embedBuilder.setDescription(content.get(1));
        return embedBuilder;
    }

    private ArrayList<String> getContent(int idx) {
        ArrayList<String> content = new ArrayList<>();
        content.add("結果");
        switch (idx) {
            case ResultNumber.COMPLETION -> content.add("完了。");
            case ResultNumber.UNFINISHED -> content.add("実行不可。");
            case ResultNumber.UNKNOWN -> content.add("未知のエラーです。BOT管理者に問い合わせてください。");
            default -> content.add("BOT管理者に問い合わせてください。");
        }
        return content;
    }
}
