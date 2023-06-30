package bot.ryuu.nyan.theme;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;

@Getter
@AllArgsConstructor
public enum PackEmoji {
    TYPE(Emoji.fromCustom("type", 1120643001232924703L, false)),
    TIMER(Emoji.fromCustom("timer", 1120642989593743400L, false)),
    TASK(Emoji.fromCustom("task", 1120642978785022035L, false)),
    TAG(Emoji.fromCustom("tag", 1120642960548171857L, false)),
    SETTING(Emoji.fromCustom("setting", 1120642938033152060L, false)),
    SEARCH(Emoji.fromCustom("search", 1120642917116166225L, false)),
    ROLES(Emoji.fromCustom("roles", 1120642903094595594L, false)),
    RANK(Emoji.fromCustom("rank", 1120642881447788585L, false)),
    PARAMETER(Emoji.fromCustom("parameter", 1120642870123188275L, false)),
    NOTIFICATION(Emoji.fromCustom("notification", 1120642857972273172L, false)),
    FULLTASK(Emoji.fromCustom("fulltask", 1120642833947316295L, false)),
    ERROR(Emoji.fromCustom("error", 1120642823448965231L, false)),
    EDIT(Emoji.fromCustom("edit", 1120642810949935215L, false)),
    CHANNEL(Emoji.fromCustom("channel", 1120642780071460874L, false)),
    BORED(Emoji.fromCustom("bored", 1120642768306442360L, false)),
    BASETASK(Emoji.fromCustom("basetask", 1120642756428185660L, false)),
    BACK(Emoji.fromCustom("back", 1120642743954321438L, false)),
    ACTIVE_LIST(Emoji.fromCustom("activelist", 1120642730159251478L, false)),
    COMPLETE_STAGE(Emoji.fromCustom("completestage", 1120642797695946813L, false)),
    REMOVE_STAGE(Emoji.fromCustom("removestage", 1120642891203743825L, false)),
    ADD_STAGE(Emoji.fromCustom("newstage", 1120642845674569778L, false)),
    STAGE(Emoji.fromCustom("stage", 1120642948988678154L, false));

    private final CustomEmoji emoji;
}
