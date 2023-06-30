package bot.ryuu.nyan.service;

import bot.ryuu.nyan.theme.Theme;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class HelpManager {
    public void execute(SlashCommandInteractionEvent event) {
        String content = "available_commands";

        if (event.getOption("info") != null)
            content = event.getOption("info").getAsString();

        switch (content) {
            case "available_commands" -> availableCommands(event);
        }
    }

    public void availableCommands(SlashCommandInteractionEvent event) {
        String commands = """

                `task all` - a list of all commands available for execution

                `task new` - create a new task *(you need certain rights to use it)*

                `task remove` - delete unnecessary tasks *(you need certain rights to use it)*

                `task find` - search for tasks by keywords and tags

                `task active` - list of active tasks

                `task give` - issue a task to a single user *(you need certain rights to use it)*
                
                """;

        String tags = """

                `tag new` - create a new tag *(you need certain rights to use it)*

                `tag remove` - remove unnecessary tags *(you need certain rights to use it)*

                """;

        String settings = "";

        String help = "";

        event.replyEmbeds(
                Theme.embed()
                        .setTitle("List of all available commands")
                        .addField(new MessageEmbed.Field("Task commands", commands, false, true))
                        .addField(new MessageEmbed.Field("Tag commands", tags, false, true))
                        .build()
        ).setEphemeral(true).queue();
    }
}
