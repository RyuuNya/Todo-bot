package bot.ryuu.nyan.bot.reaction;

import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import net.dv8tion.jda.api.entities.GuildWelcomeScreen;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SlashCommand {
    @Autowired
    private ServerRepository serverRepository;

    public void execute(SlashCommandInteractionEvent event) {
        switch (event.getFullCommandName()) {
            case "help" -> help(event);
            case "ends" -> ends(event);
        }
    }

    private void ends(@NotNull SlashCommandInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            GuildChannelUnion chanel = event.getOption("chanel").getAsChannel();

            if (chanel.getType() == ChannelType.TEXT) {
                server.get().setChanelEnd(chanel.getId());
                serverRepository.save(server.get());
                event.reply("*The server settings have been successfully changed, nya*").setEphemeral(true).queue();
            } else {
                event.reply("The channel must be of the text type").setEphemeral(true).queue();
            }
        } else {
            event.reply("*there was an error, nyaaa*").setEphemeral(true).queue();
        }
    }


    public void help(@NotNull SlashCommandInteractionEvent event) {
        event.reply("*help, nyaa!!!*").queue();
    }
}