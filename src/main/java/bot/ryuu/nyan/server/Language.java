package bot.ryuu.nyan.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Language {
    EN("English"), RU("Russian");

    public static List<SelectOption> getSelectOption() {
        return List.of(
                SelectOption.of("English", "english"),
                SelectOption.of("Russian (BETA)", "russian")
        );
    }

    private final String name;
}
