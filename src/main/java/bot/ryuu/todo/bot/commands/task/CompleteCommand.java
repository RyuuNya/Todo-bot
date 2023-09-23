package bot.ryuu.todo.bot.commands.task;

import bot.ryuu.todo.bot.commands.AbstractCommand;
import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.data.server.Server;
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

public class CompleteCommand extends AbstractCommand {
    public CompleteCommand(DataCluster cluster) {
        super(cluster);

        setCode("_complete_command");
        setCommand(
                Commands.slash("complete", "perform tasks")
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
                            .setDescription(
                                    Messages.message("COMPLETE", lang(slash))
                            ).build()
            ).addActionRow(
                    StringSelectMenu.create(code + "_complete")
                            .addOptions(options)
                            .build()
            ).queue();
        } else
            slash.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription(
                                    Messages.message("LIST_EMPTY", lang(slash))
                            ).build()
            ).queue();
    }

    @Override
    public void buttonInteraction(ButtonInteractionEvent button) {
        super.buttonInteraction(button);

        switch (button.getButton().getId()) {
            case "_complete_command_okay" -> buttonOkay(button);
        }
    }

    private void buttonOkay(ButtonInteractionEvent button) {
        button.deferReply(true).setEmbeds(
                Theme.main()
                        .setDescription(
                                Messages.message("NOTHING", lang(button))
                        ).build()
        ).queue();
    }

    @Override
    public void stringSelectInteraction(StringSelectInteractionEvent select) {
        super.stringSelectInteraction(select);

        switch (select.getSelectMenu().getId()) {
            case "_complete_command_complete" -> completeTask(select);
        }
    }

    private void completeTask(StringSelectInteractionEvent select) {
        Optional<Server> server = cluster.getServer(select);

        if (server.isPresent()) {
            select.getSelectedOptions().forEach(option -> {
                Optional<Task> task = cluster.getTask(option.getValue());

                task.ifPresent(value -> {
                    cluster.getTaskRepository().delete(value);
                    server.get().incComplete(1);
                });
            });

            server.get().save(cluster.getServerRepository());

            select.deferEdit().setEmbeds(
                    Theme.main()
                            .setDescription(
                                    Messages.message("COMPLETED", lang(select))
                            ).build()
            ).setActionRow(
                    Button.secondary(code + "_okay", "okay")
            ).queue();
        } else
            select.deferReply(true).setEmbeds(
                    Theme.main()
                            .setDescription(
                                    Messages.message("LIST_EMPTY", lang(select))
                            ).build()
            ).queue();
    }
}
