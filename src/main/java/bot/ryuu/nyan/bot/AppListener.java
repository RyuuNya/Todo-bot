package bot.ryuu.nyan.bot;

import bot.ryuu.nyan.bot.reaction.SelectMenu;
import bot.ryuu.nyan.bot.reaction.SlashCommand;
import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.service.find.FindManager;
import bot.ryuu.nyan.service.HelpManager;
import bot.ryuu.nyan.service.SettingsManager;
import bot.ryuu.nyan.service.TagManager;
import bot.ryuu.nyan.service.task.TaskManager;
import bot.ryuu.nyan.worker.Worker;
import bot.ryuu.nyan.worker.WorkerRepository;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AppListener extends ListenerAdapter {
    @Autowired
    private SlashCommand slashCommand;
    @Autowired
    private SelectMenu selectMenu;
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SettingsManager settingsManager;
    @Autowired
    private HelpManager helpManager;
    @Autowired
    private TagManager tagManager;
    @Autowired
    private FindManager findManager;

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        super.onGuildJoin(event);


    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        Worker worker = Worker.builder()
                .id(event.getMember().getUser().getId())
                .build();

        workerRepository.save(worker);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        super.onGuildMemberRemove(event);

        Optional<Worker> worker = workerRepository.findById(event.getUser().getId());

        worker.ifPresent(value -> workerRepository.delete(value));
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        super.onButtonInteraction(event);

        if (event.getButton().getId().contains("task"))
            taskManager.execute(event);
        else if (event.getButton().getId().contains("setting"))
            settingsManager.execute(event);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        super.onModalInteraction(event);

        if (event.getModalId().contains("task"))
            taskManager.execute(event);
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        super.onStringSelectInteraction(event);

        if (event.getSelectMenu().getId().contains("task"))
            taskManager.execute(event);
        else if (event.getSelectMenu().getId().contains("setting"))
            settingsManager.execute(event);
        else if (event.getSelectMenu().getId().contains("tag"))
            tagManager.execute(event);
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        super.onEntitySelectInteraction(event);

        if (event.getSelectMenu().getId().contains("task"))
            taskManager.execute(event);
        else if (event.getSelectMenu().getId().contains("setting"))
            settingsManager.execute(event);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);

        switch (event.getName()) {
            case "task", "random" -> taskManager.execute(event);
            case "settings" -> settingsManager.execute(event);
            case "tag" -> tagManager.execute(event);
            case "help" -> helpManager.execute(event);
            case "find" -> findManager.execute(event);
            default -> slashCommand.execute(event);
        }
    }
}
