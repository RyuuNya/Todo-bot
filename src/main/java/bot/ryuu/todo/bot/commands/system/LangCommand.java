package bot.ryuu.todo.bot.commands.system;

import bot.ryuu.todo.bot.commands.AbstractCommand;
import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.data.server.Server;
import bot.ryuu.todo.language.LanguageType;
import bot.ryuu.todo.language.Messages;
import bot.ryuu.todo.theme.Theme;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Optional;

public class LangCommand extends AbstractCommand {
    public LangCommand(DataCluster cluster) {
        super(cluster);

        setCode("_lang_command");
        setCommand(
                Commands.slash("lang", "change bot language, can only be changed by the server owner")
                        .addOptions(
                                new OptionData(OptionType.STRING,
                                        "language",
                                        "select a language", true)
                                        .addChoice("Russia", "RU")
                                        .addChoice("English", "EN")
                        )
                        .setGuildOnly(true)
        );
    }

    @Override
    public void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        Optional<Server> server = cluster.getServer(slash);
        Optional<String> language = getOptionString(slash, "language");

        if (server.isPresent() && language.isPresent() && permission(slash)) {
            switch (language.get()) {
                case "RU" -> server.get().setLanguage(LanguageType.RU);
                case "EN" -> server.get().setLanguage(LanguageType.EN);
            }

            server.get().save(cluster.getServerRepository());

            slash.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription(
                                    Messages.message("LANGUAGE", lang(slash))
                            ).build()
            ).queue();
        } else
            replyError(slash);
    }
}
