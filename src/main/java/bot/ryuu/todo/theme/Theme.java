package bot.ryuu.todo.theme;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class Theme {
    public static Color MAIN = new Color(111, 150, 250);
    public static Color ERROR = new Color(255, 54, 94);

    public static EmbedBuilder main() {
        return new EmbedBuilder().setColor(MAIN);
    }

    public static EmbedBuilder error() {
        return new EmbedBuilder().setColor(ERROR);
    }
}
