package bot.ryuu.todo.bot.commands.task;

import bot.ryuu.todo.bot.commands.AbstractCommand;
import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.data.task.Task;
import bot.ryuu.todo.theme.Theme;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.Optional;

public class ListCommand extends AbstractCommand {
    public ListCommand(DataCluster cluster) {
        super(cluster);

        setCode("_list_command");
        setCommand(
                Commands.slash("list", "list of all tasks")
                        .setGuildOnly(true)
        );
    }

    @Override
    public void slashInteraction(SlashCommandInteractionEvent slash) {
        super.slashInteraction(slash);

        ArrayList<SelectOption> options = new ArrayList<>();

        cluster.getTaskServer(slash).forEach(task -> {
            options.add(SelectOption.of(task.getName(), task.getId()));
        });

        if (options.size() > 0) {

            slash.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription("Select the task you are interested in").build()
            ).addActionRow(
                    StringSelectMenu.create(code + "_list")
                            .addOptions(options)
                            .build()
            ).queue();
        } else
            slash.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription("The task list is empty").build()
            ).queue();
    }

    @Override
    public void buttonInteraction(ButtonInteractionEvent button) {
        super.buttonInteraction(button);

        switch (button.getButton().getId()) {
            case "_list_command_back" -> buttonBack(button);
        }
    }

    private void buttonBack(ButtonInteractionEvent button) {
        ArrayList<SelectOption> options = new ArrayList<>();

        cluster.getTaskServer(button).forEach(task -> {
            options.add(SelectOption.of(task.getName(), task.getId()));
        });

        if (options.size() > 0) {

            button.deferEdit().setEmbeds(
                    Theme.main()
                            .setDescription("Select the task you are interested in").build()
            ).setActionRow(
                    StringSelectMenu.create(code + "_list")
                            .addOptions(options)
                            .build()
            ).queue();
        } else
            button.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription("The task list is empty").build()
            ).queue();
    }

    @Override
    public void stringSelectInteraction(StringSelectInteractionEvent select) {
        super.stringSelectInteraction(select);

        Optional<Task> task = cluster.getTask(select.getSelectedOptions().get(0).getValue());

        if (task.isPresent()) {
            select.deferEdit().setEmbeds(
                    task.get().info()
            ).setActionRow(
                    Button.secondary(code + "_back", "back")
            ).queue();
        } else
            select.deferReply(true).setEmbeds(
                    Theme.error()
                            .setDescription("error occurred").build()
            ).queue();
    }
}
