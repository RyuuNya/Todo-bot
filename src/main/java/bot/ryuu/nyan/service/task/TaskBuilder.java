package bot.ryuu.nyan.service.task;

import bot.ryuu.nyan.language.Message;
import bot.ryuu.nyan.server.NotificationServer;
import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.tag.Tag;
import bot.ryuu.nyan.tag.TagRepository;
import bot.ryuu.nyan.task.Task;
import bot.ryuu.nyan.task.TaskRank;
import bot.ryuu.nyan.task.TaskRepository;
import bot.ryuu.nyan.task.TaskType;
import bot.ryuu.nyan.theme.PackEmoji;
import bot.ryuu.nyan.theme.Theme;
import bot.ryuu.nyan.worker.Worker;
import bot.ryuu.nyan.worker.WorkerRepository;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Task creation processing*/
@Component
public class TaskBuilder {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private WorkerRepository workerRepository;

    private final Map<String, Task> time = new HashMap<>();

    public static final String CODE = "_task_builder";

    public void execute(SlashCommandInteractionEvent event) {
        if (event.getFullCommandName().equals("task new")) {
            newTask(event);
        }
    }

    public void execute(ButtonInteractionEvent event) {
        if (event.getButton().getId() != null)
            switch (event.getButton().getId()) {
                case CODE+"_new_task_full" -> newTask(event, true);
                case CODE+"_new_task" -> newTask(event, false);
                case CODE+"_set_tag" -> setTagTask(event);
                case CODE+"_set_rank" -> setRankTask(event);
                case CODE+"_set_type" -> setTypeTask(event);
                case CODE+"_create", CODE+"_success" -> createTask(event);
                case CODE+"_cancel" -> cancelTask(event);
                case CODE+"_assign" -> assignTask(event);
                case CODE+"_back" -> back(event);
            }
        else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    public void execute(StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId() != null)
            switch (event.getSelectMenu().getId()) {
                case CODE+"_set_tag" -> setTagTask(event);
                case CODE+"_set_rank" -> setRankTask(event);
                case CODE+"_set_type" -> setTypeTask(event);
            }
        else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    public void execute(EntitySelectInteractionEvent event) {
        if (event.getSelectMenu().getId() != null)
            switch (event.getSelectMenu().getId()) {
                case CODE + "_assign_task" -> setAssignTask(event);
            }
        else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    public void execute(ModalInteractionEvent event) {
        switch (event.getModalId()) {
            case CODE+"_new_task_full" -> newTask(event, true);
            case CODE+"_new_task" -> newTask(event, false);
        }
    }

    /**Slash Command Interaction*/
    private void newTask(SlashCommandInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent())
            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " " + Message.NEW_TASK.content(server.get())).build()
            ).addActionRow(
                    Button.secondary(CODE+"_new_task", "new task").withEmoji(PackEmoji.BASETASK.getEmoji()),
                    Button.secondary(CODE+"_new_task_full", "new full task").withEmoji(PackEmoji.FULLTASK.getEmoji())
            ).setEphemeral(true).queue();
        else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**Button Interaction*/
    private void newTask(ButtonInteractionEvent event, boolean full) {
        TextInput name = TextInput.create("name", "Name", TextInputStyle.SHORT)
                .setPlaceholder("Name task")
                .setMinLength(0)
                .setMaxLength(255)
                .build();

        TextInput description = TextInput.create("description", "description", TextInputStyle.PARAGRAPH)
                .setPlaceholder("description task")
                .setMinLength(0)
                .setMaxLength(4000)
                .build();

        String id = CODE+"_new_task";
        if (full)
            id += "_full";
        Modal modal = Modal.create(id, "New task")
                .addComponents(ActionRow.of(name), ActionRow.of(description))
                .build();

        event.replyModal(modal).queue();
    }

    private void back(ButtonInteractionEvent event) {
        Task task = time.get(event.getGuild().getId());

        event.deferEdit()
                .setEmbeds(task.getContent())
                .setComponents(ActionRow.of(editActionRow()),
                        ActionRow.of(editDoubleActionRow()),
                        ActionRow.of(finalActionRow()))
                .queue();
    }

    // task parameter
    private void setTagTask(ButtonInteractionEvent event) {
        ArrayList<SelectOption> options = new ArrayList<>();
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        for (Tag tag : tagRepository.findAllByServerId(event.getGuild().getId()))
            options.add(SelectOption.of(tag.getName(), tag.getId()));

        if (options.size() > 0 && server.isPresent()) {
            event.deferEdit().setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TAG.getEmoji().getAsMention() + " " + Message.SET_TAG.content(server.get())).build()
            ).setComponents(
                    ActionRow.of(
                            StringSelectMenu.create(CODE+"_set_tag")
                                    .addOptions(options)
                                    .setMaxValues(10)
                                    .build()
                    ),
                    ActionRow.of(Button.secondary(CODE  + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji()))
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TAG.getEmoji().getAsMention() + " The tag list is empty").build()
            ).queue();
    }

    private void setRankTask(ButtonInteractionEvent event) {
        List<SelectOption> options = TaskRank.getOptionList();
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent())
            event.deferEdit().setEmbeds(
                    Theme.embed().setDescription(PackEmoji.RANK.getEmoji().getAsMention() + " " + Message.SET_RANK.content(server.get())).build()
            ).setComponents(
                    ActionRow.of(
                            StringSelectMenu.create(CODE+"_set_rank")
                                    .addOptions(options)
                                    .build()
                    ),
                    ActionRow.of(Button.secondary(CODE  + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji()))
            ).queue();
        else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void setTypeTask(ButtonInteractionEvent event) {
        Task task = time.get(event.getGuild().getId());
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (task != null && server.isPresent()) {
            List<SelectOption> options = List.of(
                    SelectOption.of("short", "type_short").withDescription("does not require any feedback from the performer"),
                    SelectOption.of("full", "type_full").withDescription("requires the performer to provide feedback"),
                    SelectOption.of("stage", "type_stage").withDescription("allows the user to divide the task into stages and perform them gradually")
            );

            event.deferEdit().setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TYPE.getEmoji().getAsMention() + " " + Message.SET_TYPE.content(server.get())).build()
            ).setComponents(
                    ActionRow.of(
                            StringSelectMenu.create(CODE+"_set_type")
                                    .addOptions(options)
                                    .build()
                    ),
                    ActionRow.of(Button.secondary(CODE  + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji()))
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void assignTask(ButtonInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent())
            event.deferEdit().setEmbeds(
                    Theme.embed().setDescription(PackEmoji.ROLES.getEmoji().getAsMention() + " " + Message.ASSIGN.content(server.get())).build()
            ).setComponents(
                    ActionRow.of(
                            EntitySelectMenu.create(CODE + "_assign_task", EntitySelectMenu.SelectTarget.USER)
                                    .setMaxValues(1)
                                    .build()
                    ),
                    ActionRow.of(Button.secondary(CODE  + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji()))
            ).queue();
    }

    // task edit
    private void createTask(ButtonInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());
        Task task = time.get(event.getGuild().getId());

        if (server.isPresent()) {
            time.remove(event.getGuild().getId());

            server.get().incrementTask();
            serverRepository.save(server.get());
            taskRepository.save(task);

            if (task.getDoing() != null) {
                Optional<Worker> worker = workerRepository.findById(task.getDoing());

                if (worker.isPresent()) {
                    worker.get().addTask(task);
                    workerRepository.save(worker.get());

                    if (server.get().getNotification() != null) {
                        NotificationServer.NEW_TASK.action(server.get().getNotification(), event.getGuild(), worker.get().getId());
                    }
                }
            }

            event.deferReply(true).setEmbeds(
                    Theme.taskCreate().build()
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void cancelTask(ButtonInteractionEvent event) {
        Task task = time.get(event.getGuild().getId());
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (task != null && server.isPresent()) {
            time.remove(event.getGuild().getId());

            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " " + Message.CANCEL_TASK.content(server.get())).build()
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**String Select Interaction*/
    private void setTagTask(StringSelectInteractionEvent event) {
        Task task = time.get(event.getGuild().getId());

        if (task != null) {
            task.setTags(new HashSet<>());
            for (SelectOption selectOption : event.getSelectedOptions()) {
                Optional<Tag> tag = tagRepository.findById(selectOption.getValue());

                tag.ifPresent(task::addTag);
            }

            time.put(event.getGuild().getId(), task);

            event.deferEdit()
                    .setEmbeds(task.getContent())
                    .setComponents(ActionRow.of(editActionRow()),
                            ActionRow.of(editDoubleActionRow()),
                            ActionRow.of(finalActionRow()))
                    .queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void setRankTask(StringSelectInteractionEvent event) {
        Task task = time.get(event.getGuild().getId());

        if (task != null) {
            switch (event.getSelectedOptions().get(0).getValue()) {
                case "rank_s" -> task.setTaskRank(TaskRank.S);
                case "rank_a" -> task.setTaskRank(TaskRank.A);
                case "rank_b" -> task.setTaskRank(TaskRank.B);
            }

            time.put(event.getGuild().getId(), task);

            event.deferEdit()
                    .setEmbeds(task.getContent())
                    .setComponents(ActionRow.of(editActionRow()),
                            ActionRow.of(editDoubleActionRow()),
                            ActionRow.of(finalActionRow()))
                    .queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void setTypeTask(StringSelectInteractionEvent event) {
        Task task = time.get(event.getGuild().getId());
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (task != null && server.isPresent()) {
            String option = event.getSelectedOptions().get(0).getValue();
            boolean func = true;

            if (option.equals("type_short")) {
                task.setTaskType(TaskType.SHORT);
                time.put(event.getGuild().getId(), task);
            } else if (server.get().getResult() != null) {
                if (option.equals("type_full"))
                    task.setTaskType(TaskType.FULL);
                else
                    task.setTaskType(TaskType.STAGE);

                time.put(event.getGuild().getId(), task);
            } else
                func = false;


            if (func)
                event.deferEdit()
                        .setEmbeds(task.getContent())
                        .setComponents(ActionRow.of(editActionRow()),
                                ActionRow.of(editDoubleActionRow()),
                                ActionRow.of(finalActionRow()))
                        .queue();
            else
                event.deferEdit()
                        .setEmbeds(
                                task.getContent(),
                                Theme.embed().setDescription(PackEmoji.ERROR.getEmoji().getAsMention() + "to assign task type **`stage`** or **`full`** you need to assign a channel to the results").build()
                        )
                        .setComponents(ActionRow.of(editActionRow()),
                                ActionRow.of(editDoubleActionRow()),
                                ActionRow.of(finalActionRow()))
                        .queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**Entity Select Interaction*/
    private void setAssignTask(EntitySelectInteractionEvent event) {
        Task task = time.get(event.getGuild().getId());
        User user = event.getMentions().getUsers().get(0);

        if (task != null) {
            task.setDoing(user.getId());

            time.put(event.getGuild().getId(), task);

            event.deferEdit()
                    .setEmbeds(task.getContent())
                    .setComponents(ActionRow.of(editActionRow()),
                            ActionRow.of(editDoubleActionRow()),
                            ActionRow.of(finalActionRow()))
                    .queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**Modal Interaction Event*/
    private void newTask(ModalInteractionEvent event, boolean full) {
        String name = Objects.requireNonNull(event.getValue("name")).getAsString();
        String description = Objects.requireNonNull(event.getValue("description")).getAsString();

        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            var task = Task.builder()
                    .name(name)
                    .description(description)
                    .server(server.get())
                    .active(false)
                    .taskType(TaskType.SHORT)
                    .taskRank(TaskRank.C)
                    .tags(new HashSet<>())
                    .build();

            if (full) {
                time.put(server.get().getId(), task);

                event.deferEdit()
                        .setEmbeds(task.getContent())
                        .setComponents(ActionRow.of(editActionRow()),
                                ActionRow.of(editDoubleActionRow()),
                                ActionRow.of(finalActionRow()))
                        .queue();
            } else {
                server.get().incrementTask();
                serverRepository.save(server.get());
                taskRepository.save(task);

                event.deferReply(true).setEmbeds(
                        Theme.taskCreate().build()
                ).queue();
            }
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**Action row*/
    public List<ItemComponent> editActionRow() {
        return List.of(
                Button.secondary(CODE+"_set_tag", "set tag").withEmoji(PackEmoji.TAG.getEmoji()),
                Button.secondary(CODE+"_set_rank", "set rank").withEmoji(PackEmoji.RANK.getEmoji()),
                Button.secondary(CODE+"_set_type", "set type").withEmoji(PackEmoji.TYPE.getEmoji())
        );
    }

    public List<ItemComponent> editDoubleActionRow() {
        return List.of(
                Button.secondary(CODE+"_new_task_full", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                Button.secondary(CODE+"_assign", "assign").withEmoji(PackEmoji.ROLES.getEmoji())
        );
    }

    public List<ItemComponent> finalActionRow() {
        return List.of(
                Button.success(CODE+"_success", "create"),
                Button.danger(CODE+"_cancel", "cancel")
        );
    }
}
