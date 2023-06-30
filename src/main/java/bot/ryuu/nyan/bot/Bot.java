package bot.ryuu.nyan.bot;

import bot.ryuu.nyan.server.Language;
import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.server.Settings;
import bot.ryuu.nyan.utility.Combine;
import bot.ryuu.nyan.worker.Worker;
import bot.ryuu.nyan.worker.WorkerRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class Bot {
    private final String token = "MTA4NTY0MTY0OTM5MDEwNDY0Ng.GalB4l.5FZhm9UziCkNsROsw8jMTOc_Dm0qnXeNH5lRXg";
    private JDA client;

    @Autowired
    private AppListener appListener;
    @Autowired
    private Combine combine;

    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @Bean
    private void fun() throws InterruptedException {
        client = JDABuilder
                .createDefault(token)
                .addEventListeners(appListener)
                .setActivity(Activity.playing("MayQueen"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();
    }

    /**Configuration*/
    @Bean
    private void commands() {
        List<CommandData> commands = List.of(
                Commands.slash("help", "help!!!")
                        .setGuildOnly(true)
                        .addOptions(new OptionData(OptionType.STRING, "info", "select a question that interests you")
                                .addChoices(
                                        new Command.Choice("all available commands", "available_commands")
                                )),

                Commands.slash("settings", "settings bot")
                        .setGuildOnly(true)
                        .addOptions(new OptionData(OptionType.STRING, "settings", "select the parameter that you want to change", true)
                                .addChoices(
                                        new Command.Choice("admin role", "admin_role"),
                                        new Command.Choice("notification channel", "notification_channel"),
                                        new Command.Choice("result channel", "result_channel"),
                                        new Command.Choice("editor role", "editor_role"),
                                        new Command.Choice("language", "language")
                                )),

                Commands.slash("random", "random task")
                                .setGuildOnly(true),

                /* task */
                Commands.slash("task", "tasks command")
                        .setGuildOnly(true)
                        .addSubcommands(
                                new SubcommandData("new", "create new task"),
                                new SubcommandData("all", "list all task"),
                                new SubcommandData("active", "list of active tasks"),
                                //new SubcommandData("complete", "complete the task"),
                                new SubcommandData("remove", "remove tasks"),
                                new SubcommandData("give", "issue tasks")
                                        .addOption(OptionType.USER, "user", "the user to whom you want to give the task", true)
                        ),
                Commands.slash("find", "find task")
                        .setGuildOnly(true),

                /* tag */
                Commands.slash("tag", "tag command")
                        .setGuildOnly(true)
                        .addSubcommands(
                            new SubcommandData("new", "create new tag")
                                    .addOption(OptionType.STRING, "name", "name tag", true),
                            new SubcommandData("remove", "delete tags")
                        )
        );
        client.updateCommands().addCommands(commands).queue();
    }

    @Bean
    public void servers() {
        ArrayList<Server> servers = new ArrayList<>();

        for (Guild guild : client.getGuilds()) {
            servers.add(Server.builder()
                    .id(guild.getId())
                    .owner(guild.getOwnerId())
                    .settings(Settings.OPEN)
                    .role(new HashSet<>())
                    .language(Language.EN)
                    .editor(new HashSet<>())
                    .build());
        }

        combine.saveServers(servers);
    }

    @Bean
    public void workers() {
        for (Guild guild : client.getGuilds()) {
            guild.loadMembers(member -> {
                workerRepository.save(Worker.builder()
                        .id(member.getUser().getId())
                        .build());
            });
        }
    }
}
