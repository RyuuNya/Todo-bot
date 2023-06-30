package bot.ryuu.nyan.utility;

import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.tag.Tag;
import bot.ryuu.nyan.tag.TagRepository;
import bot.ryuu.nyan.task.Task;
import bot.ryuu.nyan.task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Combine {
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagRepository tagRepository;

    public void saveServers(Server... servers) {
        serverRepository.saveAll(List.of(servers));
    }

    public void saveServers(ArrayList<Server> servers) {
        serverRepository.saveAll(servers);
    }

    public List<Server> getServers() {
        return serverRepository.findAll();
    }

    public Server findServer(String id) {
        return serverRepository.findById(id).orElse(null);
    }

    public List<Server> findServerByOwner(String owner) {
        return serverRepository.findAllByOwner(owner);
    }

    public void saveTasks(Task... tasks) {
        taskRepository.saveAll(List.of(tasks));
    }

    public void saveTasks(ArrayList<Task> tasks) {
        taskRepository.saveAll(tasks);
    }

    public Task findTask(String id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> findTaskName(String name) {
        return taskRepository.findAllByName(name);
    }

    public List<Task> getTaskByServer(String server) {
        return taskRepository.findAllByServerId(server);
    }

    public void saveTag(Tag... tags) {
        tagRepository.saveAll(List.of(tags));
    }

    public void saveTag(ArrayList<Tag> tags) {
        tagRepository.saveAll(tags);
    }

    public Tag findTag(String id) {
        return tagRepository.findById(id).orElse(null);
    }

    public List<Tag> findTagById(String server) {
        return tagRepository.findAllByServerId(server);
    }
}
