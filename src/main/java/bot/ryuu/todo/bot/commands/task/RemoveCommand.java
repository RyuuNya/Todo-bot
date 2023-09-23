package bot.ryuu.todo.bot.commands.task;

import bot.ryuu.todo.bot.commands.AbstractCommand;
import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.data.task.Task;
import bot.ryuu.todo.language.Messages;
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

public class RemoveCommand extends AbstractCommand {
    public RemoveCommand(DataCluster cluster) {
        super(cluster);

        setCode("_remove_command");
        setCommand(
                Commands.slash("remove", "delete tasks")
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
                            .setDescription(Messages.message("REMOVE", lang(slash))).build()
            ).addActionRow(
                    StringSelectMenu.create(code + "_remove")
                            .setMaxValues(50)
                            .addOptions(options)
                            .build()
            ).queue();
        } else
            replyError(slash);
    }

    @Override
    public void buttonInteraction(ButtonInteractionEvent button) {
        super.buttonInteraction(button);

        switch (button.getButton().getId()) {
            case "_remove_command_okay" -> buttonOkay(button);
        }
    }

    private void buttonOkay(ButtonInteractionEvent button) {
        button.deferReply(true).setEmbeds(
                Theme.main()
                        .setDescription(Messages.message("NOTHING", lang(button))).build()
        ).queue();
    }

    @Override
    public void stringSelectInteraction(StringSelectInteractionEvent select) {
        super.stringSelectInteraction(select);

        switch (select.getSelectMenu().getId()) {
            case "_remove_command_remove" -> removeTasks(select);
        }
    }

    private void removeTasks(StringSelectInteractionEvent select) {
        select.getSelectMenu().getOptions().forEach(option -> {
            Optional<Task> task = cluster.getTask(option.getValue());

            task.ifPresent(value -> cluster.getTaskRepository().delete(value));
        });

        select.deferEdit().setEmbeds(
                Theme.main()
                        .setDescription(Messages.message("REMOVED", lang(select))).build()
        ).setActionRow(
                Button.secondary(code + "_okay", "okay")
        ).queue();
    }
}
