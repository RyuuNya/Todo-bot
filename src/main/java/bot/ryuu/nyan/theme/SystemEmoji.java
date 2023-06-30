package bot.ryuu.nyan.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@Getter
@AllArgsConstructor
public enum SystemEmoji {
    TAKE("<:take:1100111824415887460>", Emoji.fromCustom("take", 1100111824415887460L, false)),

    BORING("<:boring:1100043696612835429>", Emoji.fromCustom("boring", 1100043696612835429L, false)),

    LIST("<:list:1097530942882598994>", Emoji.fromCustom("list", 1097530942882598994L, false)),
    SECURITY("<:security:1097520788229345393>", Emoji.fromCustom("security", 1097520788229345393L, false)),
    SUCCESS("<:success:1100047600486461562>", Emoji.fromCustom("success", 1100047600486461562L, false)),
    TASK("<:task:1100113214487937115>", Emoji.fromCustom("task", 1100113214487937115L, false)),
    TRASH("<:trash:1100112955980394587>", Emoji.fromCustom("trash", 1100112955980394587L, false)),

    BACK("<:back:1100044652947722311>", Emoji.fromCustom("back", 1100044652947722311L, false)),




    ADD("<:add:1115241261155242064>", Emoji.fromCustom("add", 1115241261155242064L, false)),
    NEW_BASE("<:base:1115265402260430948>", Emoji.fromCustom("base", 1115265402260430948L, false)),
    NEW_FULL("<:full:1115265633739870319>", Emoji.fromCustom("full", 1115265633739870319L, false)),
    TYPE("<:type:1115241374338535495>", Emoji.fromCustom("type", 1115241374338535495L, false)),
    CREATE("<:create:1115241500935200820>", Emoji.fromCustom("create", 1115241500935200820L, false)),
    RANK("<:rank:1115266297123577929>", Emoji.fromCustom("rank", 1115266297123577929L, false)),
    TAG("<:tag:1115273636971040888>", Emoji.fromCustom("tag", 1115273636971040888L, false)),
    EDITE("<:edite:1115274183891488818>", Emoji.fromCustom("edite", 1115274183891488818L, false));

    private final String code;
    private final CustomEmoji emoji;
}
