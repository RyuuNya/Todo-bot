package bot.ryuu.nyan.theme;

import com.sun.tools.javac.Main;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Theme {
    public static final Color MAIN = new Color(162, 210, 255);

    public static EmbedBuilder embed() {
        return new EmbedBuilder()
                .setColor(MAIN);
    }

    public static EmbedBuilder error() {
        return new EmbedBuilder()
                .setDescription(PackEmoji.ERROR.getEmoji().getAsMention() + " An unexpected error occurred")
                .setColor(MAIN);
    }

    public static EmbedBuilder taskCreate() {
        return new EmbedBuilder()
                .setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Your task was created by")
                .setColor(MAIN);
    }

    public static EmbedBuilder noPermission() {
        return new EmbedBuilder()
                .setDescription(PackEmoji.ERROR.getEmoji().getAsMention() + " You don't have enough rights")
                .setColor(MAIN);
    }
}
