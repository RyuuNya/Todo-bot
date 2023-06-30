package bot.ryuu.nyan.service.task;

import bot.ryuu.nyan.server.NotificationServer;
import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.service.SettingsManager;
import bot.ryuu.nyan.tag.TagRepository;
import bot.ryuu.nyan.task.stage.Stage;
import bot.ryuu.nyan.task.stage.StageRepository;
import bot.ryuu.nyan.task.Task;
import bot.ryuu.nyan.task.TaskRepository;
import bot.ryuu.nyan.theme.PackEmoji;
import bot.ryuu.nyan.theme.Theme;
import bot.ryuu.nyan.worker.Worker;
import bot.ryuu.nyan.worker.WorkerRepository;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TaskManager {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private SettingsManager settingsManager;
    @Autowired
    private TaskBuilder taskBuilder;

    private final String CODE = "_task";

    public void execute(SlashCommandInteractionEvent event) {
        if (event.getMember() != null) {
            boolean security = settingsManager.getPermissionEditor(event.getMember()) ||
                    settingsManager.getPermission(event.getMember());

            // admin or editor command
            if (event.getFullCommandName().equals("task new") && security)
                taskBuilder.execute(event);
            else if (event.getFullCommandName().equals("task remove") && security)
                removeTask(event);
            else if (event.getFullCommandName().equals("task give") && security)
                giveTask(event);

            // user command
            else if (event.getFullCommandName().equals("task all"))
                allTask(event);
            else if (event.getFullCommandName().equals("task active"))
                activeTask(event);
        } else
            event.replyEmbeds(
                    Theme.error().build()
            ).setEphemeral(true).queue();
    }

    public void execute(StringSelectInteractionEvent event) {
        if (event.getMember() != null) {
            boolean security = settingsManager.getPermissionEditor(event.getMember()) ||
                    settingsManager.getPermission(event.getMember());

            String menu = "";
            if (event.getSelectMenu().getId().contains("*"))
                menu = event.getSelectMenu().getId().substring(0, event.getSelectMenu().getId().indexOf("*"));
            else
                menu = event.getSelectMenu().getId();

            // admin or editor command
            if (menu.contains(TaskBuilder.CODE) && security)
                taskBuilder.execute(event);
            else if (menu.equals(CODE + "_remove_task") && security)
                removeTask(event);
            else if (menu.equals(CODE + "_give_task") && security)
                giveTask(event);

            // user command
            else if (menu.equals(CODE + "_list_task"))
                showTask(event);
            else if (menu.equals(CODE + "_list_task_active"))
                infoTask(event);
            else if (menu.equals(CODE + "_remove_stage"))
                removeStage(event);
            else if (menu.equals(CODE + "_complete_stage"))
                completeStage(event);
        } else
            event.replyEmbeds(
                    Theme.error().build()
            ).setEphemeral(true).queue();
    }

    public void execute(EntitySelectInteractionEvent event) {
        if (event.getMember() != null) {
            boolean security = settingsManager.getPermissionEditor(event.getMember()) ||
                    settingsManager.getPermission(event.getMember());

            String menu = "";
            if (event.getSelectMenu().getId().contains("*"))
                menu = event.getSelectMenu().getId().substring(0, event.getSelectMenu().getId().indexOf("*"));
            else
                menu = event.getSelectMenu().getId();

            // admin or editor command
            if (menu.contains(TaskBuilder.CODE) && security)
                taskBuilder.execute(event);

            // user command
        } else
            event.replyEmbeds(
                    Theme.error().build()
            ).setEphemeral(true).queue();
    }

    public void execute(ButtonInteractionEvent event) {
        if (event.getMember() != null) {
            boolean security = settingsManager.getPermissionEditor(event.getMember()) ||
                    settingsManager.getPermission(event.getMember());

            String button = event.getButton().getId();
            if (event.getButton().getId().contains("*"))
                button = event.getButton().getId().substring(0, event.getButton().getId().lastIndexOf('*'));

            // admin or editor command
            if (button.contains(TaskBuilder.CODE) && security)
                taskBuilder.execute(event);

            // user command
            else
                switch (button) {
                    case CODE + "_bored" -> boredTask(event);
                    case CODE + "_take" -> takeTask(event);
                    case CODE + "_complete" -> completeTask(event);

                    case CODE + "_new_stage" -> newStageTask(event);
                    case CODE + "_remove_stage" -> removeStageTask(event);
                    case CODE + "_complete_stage" -> completeStageTask(event);
                }
        }
    }

    public void execute(ModalInteractionEvent event) {
        if (event.getMember() != null) {
            boolean security = settingsManager.getPermissionEditor(event.getMember()) ||
                    settingsManager.getPermission(event.getMember());

            String modal = event.getModalId();
            if (event.getModalId().contains("*"))
                modal = event.getModalId().substring(0, event.getModalId().lastIndexOf('*'));

            System.out.println(modal);

            if (modal.contains(TaskBuilder.CODE) && security)
                taskBuilder.execute(event);
            else if (modal.equals(CODE + "_complete_task"))
                completeTask(event);
            else if (modal.equals(CODE + "_new_stage"))
                newStage(event);
        } else
            event.replyEmbeds(
                    Theme.error().build()
            ).setEphemeral(true).queue();
    }

    /**Slash Command Interaction*/
    private void removeTask(SlashCommandInteractionEvent event) {
        ArrayList<SelectOption> options = new ArrayList<>();

        for (Task task : taskRepository.findAllByServerId(event.getGuild().getId()))
            options.add(SelectOption.of(task.getName(), task.getId()));

        if (options.size() > 0) {
            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Select the tasks you want to delete").build()
            ).setActionRow(
                    StringSelectMenu.create(CODE + "_remove_task")
                            .setMaxValues(10)
                            .addOptions(options)
                            .build()
            ).setEphemeral(true).queue();
        } else
            event.replyEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Task list is empty").build()
            ).setEphemeral(true).queue();
    }

    private void giveTask(SlashCommandInteractionEvent event) {
        User user = event.getOption("user").getAsUser();
        ArrayList<SelectOption> options = new ArrayList<>();

        for (Task task : taskRepository.findAllByServerId(event.getGuild().getId()))
            options.add(SelectOption.of(task.getName(), task.getId()));

        if (options.size() > 0) {
            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Select the tasks you want to issue to the user").build()
            ).setActionRow(
                    StringSelectMenu.create(CODE + "_give_task*" + user.getId())
                            .addOptions(options)
                            .setMaxValues(10)
                            .build()
            ).setEphemeral(true).queue();
        } else
            event.replyEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Task list is empty").build()
            ).setEphemeral(true).queue();
    }

    private void allTask(SlashCommandInteractionEvent event) {
        ArrayList<SelectOption> options = new ArrayList<>();

        for (Task task : taskRepository.findAllByServerId(event.getGuild().getId())) {
            options.add(SelectOption.of(task.getName()+task.isBored(), task.getId()).withEmoji(task.getTaskRank().getEmoji()));
        }

        if (options.size() > 0) {
            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Select the task you are interested in").build()
            ).addActionRow(
                    StringSelectMenu
                            .create(CODE + "_list_task")
                            .addOptions(options)
                            .build()
            ).setEphemeral(true).queue();
        } else
            event.replyEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Task list is empty").build()
            ).setEphemeral(true).queue();
    }

    private void activeTask(SlashCommandInteractionEvent event) {
        ArrayList<SelectOption> options = new ArrayList<>();
        Optional<Worker> worker = workerRepository.findById(event.getUser().getId());

        if (worker.isPresent()) {
            for (Task task : worker.get().getTasks()) {
                options.add(SelectOption.of(task.getName()+task.isBored(), task.getId()));
            }

            if (options.size() != 0) {
                event.deferReply(true).setEmbeds(
                        Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Your list of active tasks").build()
                ).addActionRow(
                        StringSelectMenu
                            .create(CODE + "_list_task_active")
                            .addOptions(options)
                            .build()
                ).queue();
            } else
                event.replyEmbeds(
                        Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Task list is empty").build()
                ).setEphemeral(true).queue();
        } else
            event.replyEmbeds(
                    Theme.noPermission().build()
            ).setEphemeral(true).queue();
    }

    /**String Select Interaction*/
    private void removeTask(StringSelectInteractionEvent event) {
        for (SelectOption option : event.getSelectedOptions()) {
            Optional<Task> task = taskRepository.findById(option.getValue());

            if (task.isPresent()) {
                if (task.get().isDoing()) {
                    Optional<Worker> worker = workerRepository.findById(task.get().getDoing());

                    if (worker.isPresent()) {
                        worker.get().deleteTask(task.get());
                        workerRepository.save(worker.get());
                    }
                }

                task.get().setTags(new HashSet<>());
                task.get().setServer(null);

                taskRepository.save(task.get());
                taskRepository.delete(task.get());
            }
        }

        event.deferReply(true).setEmbeds(
                Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Selected tasks have been deleted").build()
        ).queue();
    }

    private void giveTask(StringSelectInteractionEvent event) {
        String user = event.getSelectMenu().getId().substring(event.getSelectMenu().getId().indexOf("*") + 1);

        Optional<Worker> worker = workerRepository.findById(user);
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (worker.isPresent() && server.isPresent()) {
            for (SelectOption selectOption : event.getSelectedOptions()) {
                Optional<Task> task = taskRepository.findById(selectOption.getValue());

                if (task.isPresent() && !task.get().isDoing()) {
                    task.get().setDoing(worker.get().getId());
                    taskRepository.save(task.get());

                    worker.get().addTask(task.get());
                    workerRepository.save(worker.get());

                }
            }

            if (server.get().getNotification() != null) {
                NotificationServer.NEW_TASK.action(server.get().getNotification(), event.getGuild(), worker.get().getId());
            }

            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " The task was given to").build()
            ).setEphemeral(true).queue();
        }
    }

    private void showTask(StringSelectInteractionEvent event) {
        Optional<Task> task = taskRepository.findById(event.getSelectedOptions().get(0).getValue());

        if (task.isPresent()) {
            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setTitle(task.get().getName())
                            .setDescription(task.get().getDescription())
                            .build()
            ).setActionRow(
                    Button.success(CODE + "_take*" + task.get().getId(), "take"),
                    Button.secondary(CODE + "_bored*" + task.get().getId(), "bored").withEmoji(PackEmoji.BORED.getEmoji())
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void infoTask(StringSelectInteractionEvent event) {
        Optional<Task> task = taskRepository.findById(event.getSelectedOptions().get(0).getValue());

        if (task.isPresent()) {
            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setTitle(task.get().getName())
                            .setDescription(task.get().getDescription())
                            .addField("Stages", task.get().getStagesContent(), false)
                            .build()
            ).setComponents(
                    ActionRow.of(StageActionRow(task.get())),
                    ActionRow.of(ControlActionRow(task.get()))
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    // stages
    private void removeStage(StringSelectInteractionEvent event) {
        String id = event.getSelectMenu().getId().substring(event.getSelectMenu().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        for (SelectOption option : event.getSelectedOptions()) {
            Optional<Stage> stage = stageRepository.findById(option.getValue());

            if (stage.isPresent()) {
                for (Stage stage1 : task.get().getStages())
                    if (stage1.getId().equals(stage.get().getId()))
                        task.get().getStages().remove(stage1);

                taskRepository.save(task.get());
                stageRepository.delete(stage.get());
            }
        }

        event.deferEdit().setEmbeds(
                Theme.embed()
                        .setTitle(task.get().getName())
                        .setDescription(task.get().getDescription())
                        .addField("Stages", task.get().getStagesContent(), false)
                        .build()
        ).setComponents(
                ActionRow.of(StageActionRow(task.get())),
                ActionRow.of(ControlActionRow(task.get()))
        ).queue();
    }

    private void completeStage(StringSelectInteractionEvent event) {
        for (SelectOption option : event.getSelectedOptions()) {
            Optional<Stage> stage = stageRepository.findById(option.getValue());

            if (stage.isPresent()) {
                stage.get().setExecuted(true);
                stageRepository.save(stage.get());
            }
        }

        String id = event.getSelectMenu().getId().substring(event.getSelectMenu().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        event.deferEdit().setEmbeds(
                Theme.embed()
                        .setTitle(task.get().getName())
                        .setDescription(task.get().getDescription())
                        .addField("Stages", task.get().getStagesContent(), false)
                        .build()
        ).setComponents(
                ActionRow.of(StageActionRow(task.get())),
                ActionRow.of(ControlActionRow(task.get()))
        ).queue();
    }

    /**Button Interaction*/
    public void takeTask(ButtonInteractionEvent event) {
        String id = event.getButton().getId().substring(event.getButton().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);
        Optional<Worker> worker = workerRepository.findById(event.getUser().getId());

        System.out.println(task.isPresent() + " " + worker.isPresent());

        if (task.isPresent() && worker.isPresent()) {
            task.get().setDoing(event.getUser().getId());
            taskRepository.save(task.get());

            worker.get().addTask(task.get());
            workerRepository.save(worker.get());

            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " The task has been activated").build()
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    public void boredTask(ButtonInteractionEvent event) {
        String id = event.getButton().getId().substring(event.getButton().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            task.get().setBored(true);
            taskRepository.save(task.get());

            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.BORED.getEmoji().getAsMention() + " the assignment was marked as boring").build()
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription("there is no such assignment").build()
            ).queue();
    }

    public void completeTask(ButtonInteractionEvent event) {
        String id = event.getButton().getId().substring(event.getButton().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            switch (task.get().getTaskType().name()) {
                case "SHORT" -> shortTask(event, task.get());
                case "STAGE", "FULL" -> stageOrFullTask(event, task.get());
            }
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    // stages
    private void newStageTask(ButtonInteractionEvent event) {
        String id = event.getButton().getId().substring(event.getButton().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        TextInput name = TextInput.create("name", "Name", TextInputStyle.SHORT)
                .setPlaceholder("Name task")
                .setMinLength(0)
                .setMaxLength(255)
                .build();

        if (task.isPresent()) {
            Modal modal = Modal.create(CODE + "_new_stage*" + task.get().getId(), "New stage")
                    .addComponents(ActionRow.of(name))
                    .build();

            event.replyModal(modal).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void removeStageTask(ButtonInteractionEvent event) {
        String id = event.getButton().getId().substring(event.getButton().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            ArrayList<SelectOption> options = new ArrayList<>();

            for (Stage stage : task.get().getStages())
                options.add(SelectOption.of(stage.getName(), stage.getId()));

            if (options.size() > 0)
                event.deferEdit().setEmbeds(
                        Theme.embed().setDescription(PackEmoji.STAGE.getEmoji().getAsMention() + " Select the stages you want to remove").build()
                ).setActionRow(
                        StringSelectMenu.create(CODE + "_remove_stage*" + task.get().getId())
                                .addOptions(options)
                                .build()
                ).queue();
            else
                event.deferReply(true).setEmbeds(
                        Theme.embed().setDescription(PackEmoji.STAGE.getEmoji().getAsMention() + " The list of stages is empty").build()
                ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void completeStageTask(ButtonInteractionEvent event) {
        String id = event.getButton().getId().substring(event.getButton().getId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            ArrayList<SelectOption> options = new ArrayList<>();

            for (Stage stage : task.get().getStages())
                options.add(SelectOption.of(stage.getName(), stage.getId()));

            if (options.size() > 0)
                event.deferEdit().setEmbeds(
                        Theme.embed().setDescription(PackEmoji.STAGE.getEmoji().getAsMention() + " Select the stages you want to perform").build()
                ).setActionRow(
                        StringSelectMenu.create(CODE + "_complete_stage*" + task.get().getId())
                                .setMaxValues(5)
                                .addOptions(options)
                                .build()
                ).queue();
            else
                event.deferReply(true).setEmbeds(
                        Theme.embed().setDescription(PackEmoji.STAGE.getEmoji().getAsMention() + " The list of stages is empty").build()
                ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**Modal Interaction Event*/
    public void completeTask(ModalInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());
        String id = event.getModalId().substring(event.getModalId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            if (task.get().isDoing()) {
                Optional<Worker> worker = workerRepository.findById(task.get().getDoing());

                if (worker.isPresent()) {
                    worker.get().deleteTask(task.get());
                    workerRepository.save(worker.get());
                }
            }

            task.get().setTags(new HashSet<>());
            task.get().setServer(null);

            taskRepository.save(task.get());
            taskRepository.delete(task.get());

            if (server.isPresent() && server.get().getNotification() != null) {
                NotificationServer.COMPLETE_TASK.action(server.get().getNotification(), event.getGuild(), event.getUser().getId() + "*" + task.get().getName());
            }

            if (server.isPresent() && server.get().getResult() != null) {
                TextChannel result = event.getGuild().getTextChannelById(server.get().getResult());

                if (result != null) {
                    result.sendMessage("Task name : `" + task.get().getName() + "` task id : `" + task.get().getId() + "` user complete : <@" + event.getUser().getId() + ">").setEmbeds(
                            Theme.embed().setTitle(task.get().getName()).setDescription(event.getValue("content").getAsString())
                                    .setAuthor(event.getUser().getName(), event.getUser().getAvatarUrl(), event.getUser().getAvatarUrl()).build(),
                            Theme.embed().setTitle("Links").setDescription(event.getValue("links").getAsString()).build()
                    ).queue();
                }
            }

            event.deferReply(true).setEmbeds(
                    Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " The task has been completed").build()
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void newStage(ModalInteractionEvent event) {
        String id = event.getModalId().substring(event.getModalId().lastIndexOf('*') + 1);
        Optional<Task> task = taskRepository.findById(id);

        String name = event.getValue("name").getAsString();

        if (task.isPresent()) {
            Stage stage = Stage.builder()
                    .name(name)
                    .number(task.get().getStages().size() + 1)
                    .executed(false)
                    .build();

            stageRepository.save(stage);
            task.get().getStages().add(stage);
            taskRepository.save(task.get());

            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setTitle(task.get().getName())
                            .setDescription(task.get().getDescription())
                            .addField("Stages", task.get().getStagesContent(), false)
                            .build()
            ).setComponents(
                    ActionRow.of(StageActionRow(task.get())),
                    ActionRow.of(ControlActionRow(task.get()))
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**Task complete other event*/
    private void shortTask(ButtonInteractionEvent event, Task task) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (task.isDoing()) {
            Optional<Worker> worker = workerRepository.findById(task.getDoing());

            if (worker.isPresent()) {
                worker.get().deleteTask(task);
                workerRepository.save(worker.get());
            }
        }

        task.setTags(new HashSet<>());
        task.setServer(null);

        taskRepository.save(task);
        taskRepository.delete(task);

        if (server.isPresent() && server.get().getNotification() != null) {
            NotificationServer.COMPLETE_TASK.action(server.get().getNotification(), event.getGuild(), event.getUser().getId() + "*" + task.getName());
        }

        event.deferReply(true).setEmbeds(
                Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " The task has been completed").build()
        ).queue();
    }

    private void stageOrFullTask(ButtonInteractionEvent event, Task task) {
        TextInput content = TextInput.create("content", "Content", TextInputStyle.PARAGRAPH)
                .setPlaceholder("content")
                .setMinLength(0)
                .setMaxLength(4000)
                .build();

        TextInput links = TextInput.create("links", "Links", TextInputStyle.SHORT)
                .setPlaceholder("resource link")
                .setMinLength(0)
                .setMaxLength(2000)
                .build();

        Modal modal = Modal.create(CODE + "_complete_task*" + task.getId(), "Complete task")
                .addComponents(ActionRow.of(content), ActionRow.of(links))
                .build();

        event.replyModal(modal).queue();
    }

    /**Other*/
    private List<ItemComponent> StageActionRow(Task task) {
        return List.of(
                Button.secondary(CODE + "_new_stage*" + task.getId(), "new stage").withEmoji(PackEmoji.ADD_STAGE.getEmoji()).withDisabled(task.isStage()),
                Button.secondary(CODE + "_remove_stage*" + task.getId(), "remove stage").withEmoji(PackEmoji.REMOVE_STAGE.getEmoji()).withDisabled(task.isStage()),
                Button.secondary(CODE + "_complete_stage*" + task.getId(), "complete stage").withEmoji(PackEmoji.COMPLETE_STAGE.getEmoji()).withDisabled(task.isStage())
        );
    }

    private List<ItemComponent> ControlActionRow(Task task) {
        return List.of(
                Button.success(CODE + "_complete*" + task.getId(), "complete"),
                Button.danger(CODE + "_close*" + task.getId(), "close")
        );
    }
}