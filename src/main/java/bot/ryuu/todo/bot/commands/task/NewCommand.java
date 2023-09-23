package bot.ryuu.todo.bot.commands.task;

import bot.ryuu.todo.bot.commands.AbstractCommand;
import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.data.server.Server;
import bot.ryuu.todo.data.task.Task;
import bot.ryuu.todo.language.Messages;
import bot.ryuu.todo.theme.Theme;
import bot.ryuu.todo.utility.Wrapping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.Optional;

public class NewCommand extends AbstractCommand {
    public NewCommand(DataCluster cluster) {
        super(cluster);

        setCode("_new_command");
        setCommand(
                Commands.slash("new", "create a new todo task")
                        .addOption(OptionType.STRING, "name", "task title", true)
                        .addOption(OptionType.STRING, "description", "task description", true)
                        .addOptions(
                                new OptionData(OptionType.STRING, "color", "task color")
                                        .addChoice("Blue", "BLUE")
                                        .addChoice("Red", "RED")
                                        .addChoice("Green", "GREEN")
                                        .addChoice("Cyan", "CYAN")
                                        .addChoice("Purple", "PURPLE")
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    public void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Server> server = cluster.getServer(slash);
        Optional<String> name = getOptionString(slash, "name");
        Optional<String> description = getOptionString(slash, "description");
        Optional<String> color = getOptionString(slash, "color");

        if (server.isPresent() && name.isPresent() && description.isPresent()) {
            Wrapping<Color> c = Wrapping.of(Theme.MAIN);
            color.ifPresent(value -> {
                switch (value) {
                    case "RED" -> c.set(Theme.ERROR);
                    case "GREEN" -> c.set(Theme.GREEN);
                    case "CYAN" -> c.set(Theme.CYAN);
                    case "PURPLE" -> c.set(Theme.PURPLE);
                }
            });

            Task.builder()
                    .name(name.get())
                    .color(c.get())
                    .description(description.get())
                    .serverId(server.get().getId())
                    .build().save(cluster);

            slash.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription(Messages.message("NEW", lang(slash))).build()
            ).queue();
        } else
            replyError(slash);
    }
}
