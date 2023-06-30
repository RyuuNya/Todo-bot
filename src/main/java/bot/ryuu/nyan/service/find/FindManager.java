package bot.ryuu.nyan.service.find;

import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.tag.TagRepository;
import bot.ryuu.nyan.task.TaskRepository;
import bot.ryuu.nyan.theme.SystemEmoji;
import bot.ryuu.nyan.theme.Theme;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindManager {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ServerRepository serverRepository;

    public static final String CODE = "_find";

    public void execute(SlashCommandInteractionEvent event) {
        switch (event.getFullCommandName()) {
            case "find" -> find(event);
        }
    }

    public void find(SlashCommandInteractionEvent event) {
        event.replyEmbeds(
                Theme.embed().setDescription("Set up filters to search for").build()
        ).setActionRow(
                Button.secondary(CODE + "_tags", "tags").withEmoji(Emoji.fromCustom(SystemEmoji.TAG.getEmoji())),
                Button.secondary(CODE + "_rank", "ranks").withEmoji(Emoji.fromCustom(Emoji.fromCustom(SystemEmoji.RANK.getEmoji())))
        ).setEphemeral(true).queue();
    }
}
