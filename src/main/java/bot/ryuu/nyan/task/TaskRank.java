package bot.ryuu.nyan.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;

import java.util.List;


@Getter
@AllArgsConstructor
public enum TaskRank {
    C(50, Emoji.fromCustom("rankc", 1120646504491790336L, false)),
    B(75, Emoji.fromCustom("rankb", 1120646481939017750L, false)),
    A(100, Emoji.fromCustom("ranka", 1120646465304399882L, false)),
    S(150, Emoji.fromCustom("ranks", 1120646493129420911L, false));

    public static TaskRank setRank(String r) {
        switch (r.toUpperCase()) {
            case "B" -> {
                return B;
            }
            case "A" -> {
                return A;
            }
            case "S" -> {
                return S;
            }
            default -> {
                return C;
            }
        }
    }

    public static List<SelectOption> getOptionList() {
        return List.of(
                SelectOption.of("Rank S", "rank_s").withEmoji(TaskRank.S.getEmoji()),
                SelectOption.of("Rank A", "rank_a").withEmoji(TaskRank.A.getEmoji()),
                SelectOption.of("Rank B", "rank_b").withEmoji(TaskRank.B.getEmoji()),
                SelectOption.of("Rank C", "rank_c").withEmoji(TaskRank.C.getEmoji())
        );
    }

    public final int point;
    private final CustomEmoji emoji;
}
