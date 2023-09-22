package bot.ryuu.todo.bot.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Optional;

public interface AbstractInstruments {
    default Optional<String> getOptionString(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) != null)
            return Optional.of(event.getOption(name).getAsString());
        else
            return Optional.empty();
    }

    default Optional<Boolean> getOptionBoolean(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) != null)
            return Optional.of(event.getOption(name).getAsBoolean());
        else
            return Optional.empty();
    }

    default Optional<Role> getOptionRole(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) != null)
            return Optional.of(event.getOption(name).getAsRole());
        else
            return Optional.empty();
    }
}
