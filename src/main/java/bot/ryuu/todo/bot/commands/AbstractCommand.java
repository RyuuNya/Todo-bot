package bot.ryuu.todo.bot.commands;

import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.language.LanguageType;
import bot.ryuu.todo.language.Messages;
import bot.ryuu.todo.theme.Theme;
import bot.ryuu.todo.utility.Wrapping;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.concurrent.atomic.AtomicReference;

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

    protected LanguageType lang(SlashCommandInteractionEvent slash) {
        Wrapping<LanguageType> lang = Wrapping.of(LanguageType.EN);

        cluster.getServer(slash).ifPresent(server -> {
            lang.set(server.getLanguage());
        });

        return lang.get();
    }

    protected LanguageType lang(ButtonInteractionEvent button) {
        Wrapping<LanguageType> lang = Wrapping.of(LanguageType.EN);

        cluster.getServer(button).ifPresent(server -> {
            lang.set(server.getLanguage());
        });

        return lang.get();
    }

    protected LanguageType lang(StringSelectInteractionEvent select) {
        Wrapping<LanguageType> lang = Wrapping.of(LanguageType.EN);

        cluster.getServer(select).ifPresent(server -> {
            lang.set(server.getLanguage());
        });

        return lang.get();
    }

    protected void replyError(SlashCommandInteractionEvent slash) {
        slash.deferReply(true).setEmbeds(
                Theme.error()
                        .setDescription(Messages.message("ERROR", lang(slash))).build()
        ).queue();
    }

    protected void replyError(ButtonInteractionEvent button) {
        button.deferReply(true).setEmbeds(
                Theme.error()
                        .setDescription(Messages.message("ERROR", lang(button))).build()
        ).queue();
    }

    protected void replyError(StringSelectInteractionEvent select) {
        select.deferReply(true).setEmbeds(
                Theme.error()
                        .setDescription(Messages.message("ERROR", lang(select))).build()
        ).queue();
    }

    protected boolean permission(SlashCommandInteractionEvent slash) {
        return slash.getUser().getId().equals(slash.getGuild().getOwner().getId());
    }
}
