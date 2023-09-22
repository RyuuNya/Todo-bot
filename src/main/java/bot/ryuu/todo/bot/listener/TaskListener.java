package bot.ryuu.todo.bot.listener;

import bot.ryuu.todo.bot.commands.AbstractCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class TaskListener extends ListenerAdapter {
    private final List<AbstractCommand> commands;

    public TaskListener(List<AbstractCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        super.onStringSelectInteraction(event);

        for (AbstractCommand command : commands) {
            if (Objects.requireNonNull(event.getSelectMenu().getId()).contains(command.getCommand().getName())) {
                command.execute(event);
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);

        for (AbstractCommand command : commands) {
            if (Objects.requireNonNull(event.getButton().getId()).contains(command.getCommand().getName())) {
                command.execute(event);
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        for (AbstractCommand command : commands) {
            if (event.getName().equals(command.getCommand().getName())) {
                command.execute(event);
            }
        }

    }
}