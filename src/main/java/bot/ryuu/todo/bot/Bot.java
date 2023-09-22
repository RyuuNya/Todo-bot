package bot.ryuu.todo.bot;

import bot.ryuu.todo.TodoApplication;
import bot.ryuu.todo.bot.commands.AbstractCommand;
import bot.ryuu.todo.bot.commands.task.CompleteCommand;
import bot.ryuu.todo.bot.commands.task.ListCommand;
import bot.ryuu.todo.bot.commands.task.NewCommand;
import bot.ryuu.todo.bot.commands.task.RemoveCommand;
import bot.ryuu.todo.bot.listener.TaskListener;
import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.data.server.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Bot {
    private static final String TOKEN = TodoApplication.getTOKEN();

    private final DataCluster cluster;

    private final List<AbstractCommand> commands;

    public Bot(DataCluster cluster) {
        this.cluster = cluster;
        this.commands = List.of(
                new NewCommand(cluster),
                new ListCommand(cluster),
                new RemoveCommand(cluster),
                new CompleteCommand(cluster)
        );
    }

    @Bean
    private void build() {
        JDABuilder
                .createDefault(TOKEN)
                .setStatus(OnlineStatus.IDLE)
                .addEventListeners(new TaskListener(commands))
                .setActivity(Activity.listening("bla bla bla..."))
                .build();
    }

    @Bean
    private void loadCommand() throws InterruptedException {
        ArrayList<CommandData> commandData = new ArrayList<>();

        commands.forEach(command -> {
            commandData.add(command.getCommand());
        });

        getClient().updateCommands().addCommands(commandData).queue();
    }

    @Bean
    private void loadServer() throws InterruptedException {
        getClient().getGuilds().forEach(guild -> {
            Server.builder()
                    .id(guild.getId())
                    .allTask(0)
                    .members(0)
                    .complete(0)
                    .build().save(cluster.getServerRepository());
        });
    }

    public static JDA getClient() throws InterruptedException {
        return JDABuilder
                .createDefault(TOKEN)
                .build().awaitReady();
    }
}
