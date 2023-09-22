package bot.ryuu.todo.bot.commands.task;

import bot.ryuu.todo.bot.commands.AbstractCommand;
import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.data.server.Server;
import bot.ryuu.todo.data.task.Task;
import bot.ryuu.todo.theme.Theme;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Optional;

public class NewCommand extends AbstractCommand {
    public NewCommand(DataCluster cluster) {
        super(cluster);

        setCode("_new_command");
        setCommand(
                Commands.slash("new", "create a new todo task")
                        .addOption(OptionType.STRING, "name", "task title", true)
                        .addOption(OptionType.STRING, "description", "task description", true)
                        .setGuildOnly(true)
        );
    }

    @Override
    public void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Server> server = cluster.getServer(slash);
        Optional<String> name = getOptionString(slash, "name");
        Optional<String> description = getOptionString(slash, "description");

        if (server.isPresent() && name.isPresent() && description.isPresent()) {
            Task.builder()
                    .name(name.get())
                    .description(description.get())
                    .serverId(server.get().getId())
                    .build().save(cluster);

            slash.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription("The task was successfully created, you can view it using the `/list` command ").build()
            ).queue();
        } else
            slash.deferReply(true).setEmbeds(
                    Theme.error()
                            .setDescription("error occurred").build()
            ).queue();
    }
}
