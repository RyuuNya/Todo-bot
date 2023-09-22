package bot.ryuu.todo.bot.commands;

import bot.ryuu.todo.data.DataCluster;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

@Setter
@Getter
public abstract class AbstractCommand implements AbstractInteraction, AbstractInstruments {
    protected String code;
    protected CommandData command;

    protected DataCluster cluster;

    public AbstractCommand(DataCluster cluster) {
        this.cluster = cluster;
    }

    public void execute(Object object) {
        if (object instanceof SlashCommandInteractionEvent slash)
            slashInteraction(slash);
        else if (object instanceof ButtonInteractionEvent button)
            buttonInteraction(button);
        else if (object instanceof StringSelectInteractionEvent string)
            stringSelectInteraction(string);
        else if (object instanceof EntitySelectInteractionEvent entity)
            entitySelectInteraction(entity);
        else if (object instanceof ModalInteractionEvent modal)
            modalInteraction(modal);
    }
}
