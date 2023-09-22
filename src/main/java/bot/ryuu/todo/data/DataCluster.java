package bot.ryuu.todo.data;

import bot.ryuu.todo.data.server.Server;
import bot.ryuu.todo.data.server.ServerRepository;
import bot.ryuu.todo.data.task.Task;
import bot.ryuu.todo.data.task.TaskRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataCluster {
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private TaskRepository taskRepository;

    /**Server interface*/
    public Optional<Server> getServer(String id) {
        return serverRepository.findById(id);
    }

    public Optional<Server> getServer(SlashCommandInteractionEvent event) {
        if (event.getGuild() != null)
            return serverRepository.findById(event.getGuild().getId());
        else
            return Optional.empty();
    }

    public Optional<Server> getServer(ButtonInteractionEvent event) {
        if (event.getGuild() != null)
            return serverRepository.findById(event.getGuild().getId());
        else
            return Optional.empty();
    }

    public Optional<Server> getServer(StringSelectInteractionEvent event) {
        if (event.getGuild() != null)
            return serverRepository.findById(event.getGuild().getId());
        else
            return Optional.empty();
    }

    public Optional<Server> getServer(EntitySelectInteractionEvent event) {
        if (event.getGuild() != null)
            return serverRepository.findById(event.getGuild().getId());
        else
            return Optional.empty();
    }

    /**Task interface*/
    public Optional<Task> getTask(String id) {
        return taskRepository.findById(id);
    }

    public Optional<Task> getTask(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) != null) {
            return taskRepository.findById(event.getOption(name).getAsString());
        } else
            return Optional.empty();
    }

    public List<Task> getTaskServer(String server) {
        return taskRepository.findAllByServerId(server);
    }

    public List<Task> getTaskServer(SlashCommandInteractionEvent event) {
        if (event.getGuild() != null)
            return taskRepository.findAllByServerId(event.getGuild().getId());
        else
            return List.of();
    }

    public List<Task> getTaskServer(ButtonInteractionEvent event) {
        if (event.getGuild() != null)
            return taskRepository.findAllByServerId(event.getGuild().getId());
        else
            return List.of();
    }

    public List<Task> getTaskServer(StringSelectInteractionEvent event) {
        if (event.getGuild() != null)
            return taskRepository.findAllByServerId(event.getGuild().getId());
        else
            return List.of();
    }

    public ServerRepository getServerRepository() {
        return serverRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }
}