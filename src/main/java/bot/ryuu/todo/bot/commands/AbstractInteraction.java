package bot.ryuu.todo.bot.commands;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface AbstractInteraction {
    default void slashInteraction(SlashCommandInteractionEvent slash) {

    }

    default void buttonInteraction(ButtonInteractionEvent button) {

    }

    default void stringSelectInteraction(StringSelectInteractionEvent select) {

    }


    default void entitySelectInteraction(EntitySelectInteractionEvent select) {

    }

    default void modalInteraction(ModalInteractionEvent modal) {

    }
}
