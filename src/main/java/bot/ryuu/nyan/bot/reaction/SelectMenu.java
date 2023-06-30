package bot.ryuu.nyan.bot.reaction;

import bot.ryuu.nyan.task.Task;
import bot.ryuu.nyan.utility.Combine;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class SelectMenu {
    @Autowired
    private Combine combine;

    public void taskSelect(StringSelectInteractionEvent event) {
        Task task = combine.findTask(event.getSelectedOptions().get(0).getValue());

        if (task != null) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setTitle(task.getName())
                            .setColor(new Color(225, 142, 231))
                            .setDescription(task.getDescription())
                            .build()
            ).queue();
        } else {
            event.reply("*there is no such assignment, nya*").queue();
        }
    }
}
