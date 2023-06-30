package bot.ryuu.nyan.service;

import bot.ryuu.nyan.server.Language;
import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.theme.PackEmoji;
import bot.ryuu.nyan.theme.Theme;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SettingsManager {
    @Autowired
    private ServerRepository serverRepository;

    private final String CODE = "_setting";

    public void execute(SlashCommandInteractionEvent event) {
        if (getPermission(event.getMember())) {
            switch (event.getFullCommandName()) {
                case "settings" -> {
                    String param = event.getOption("settings").getAsString();

                    switch (param) {
                        case "admin_role" -> adminRole(event);
                        case "notification_channel" -> notificationChannel(event);
                        case "result_channel" -> resultChannel(event);
                        case "editor_role" -> editorRole(event);
                        case "language" -> language(event);
                    }
                }
            }
        } else
            event.deferReply().setEmbeds(Theme.noPermission().build()).setEphemeral(true).queue();
    }

    public void execute(ButtonInteractionEvent event) {
        if (getPermission(event.getMember())) {
            switch (event.getButton().getId()) {
                case CODE + "_edit_role" -> editRole(event);
                case CODE + "_edit_result" -> editResult(event);
                case CODE + "_edit_notification" -> editNotification(event);
                case CODE + "_edit_editor" -> editorRole(event);
                case CODE + "_edit_language" -> editLanguage(event);
                case CODE + "_back" -> back(event);
            }
        } else
            event.deferReply().setEmbeds(Theme.noPermission().build()).setEphemeral(true).queue();
    }

    public void execute(EntitySelectInteractionEvent event) {
        if (getPermission(event.getMember())) {
            switch (event.getSelectMenu().getId()) {
                case CODE + "_choice_role" -> adminRole(event);
                case CODE + "_choice_result" -> resultChannel(event);
                case CODE + "_choice_notification" -> notificationChannel(event);
                case CODE + "_choice_editor" -> editorRole(event);
            }
        } else
            event.deferReply().setEmbeds(Theme.noPermission().build()).setEphemeral(true).queue();
    }

    public void execute(StringSelectInteractionEvent event) {
        if (getPermission(event.getMember())) {
            switch (event.getSelectMenu().getId()) {
                case CODE + "_choice_param" -> choiceParam(event);
                case CODE + "_choice_language" -> choiceLanguage(event);
            }
        } else
            event.deferReply().setEmbeds(Theme.noPermission().build()).setEphemeral(true).queue();
    }

    /**Slash Command Interaction*/
    private void notificationChannel(SlashCommandInteractionEvent event) {
        event.replyEmbeds(
                Theme.embed()
                        .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " Channel for notifications\nThe channel to which bot notifications will be sent.").build()
        ).setActionRow(
                Button.secondary(CODE + "_edit_notification", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
        ).setEphemeral(true).queue();
    }

    private void resultChannel(SlashCommandInteractionEvent event) {
        event.deferReply(true).setEmbeds(
                Theme.embed()
                        .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " Channel for results\nThe results of tasks will be sent to this channel.").build()
        ).setActionRow(
                Button.secondary(CODE + "_edit_result", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
        ).setEphemeral(true).queue();
    }

    private void adminRole(SlashCommandInteractionEvent event) {
        event.replyEmbeds(
                Theme.embed()
                        .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " Role of the administrator\nSelect the roles to assign administrator rights to them.")
                        .build()
        ).addActionRow(
                Button.secondary(CODE + "_edit_role", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
        ).setEphemeral(true).queue();
    }

    private void editorRole(SlashCommandInteractionEvent event) {
        event.replyEmbeds(
                Theme.embed()
                        .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " Editor role\nGives the right to edit tasks and tags only.")
                        .build()
        ).addActionRow(
                Button.secondary(CODE + "_edit_editor", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
        ).setEphemeral(true).queue();
    }

    private void language(SlashCommandInteractionEvent event) {
        event.deferReply(true).setEmbeds(
                Theme.embed().setDescription("Language bot").build()
        ).setActionRow(
                Button.secondary(CODE + "_edit_language", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
        ).queue();
    }

    /**Button Interaction*/
    private void editRole(ButtonInteractionEvent event) {
        event.deferEdit().setEmbeds(
                Theme.embed().setDescription(PackEmoji.ROLES.getEmoji().getAsMention() + " Select Roles").build()
        ).setActionRow(
                EntitySelectMenu.create(CODE + "_choice_role", EntitySelectMenu.SelectTarget.ROLE)
                        .setMaxValues(10)
                        .build()
        ).queue();
    }

    private void editResult(ButtonInteractionEvent event) {
        event.deferEdit().setEmbeds(
                Theme.embed().setDescription(PackEmoji.CHANNEL.getEmoji().getAsMention() + " Select channels").build()
        ).setActionRow(
                EntitySelectMenu.create(CODE + "_choice_result", EntitySelectMenu.SelectTarget.CHANNEL)
                        .setChannelTypes(ChannelType.TEXT)
                        .setMaxValues(1)
                        .build()
        ).queue();
    }

    private void editNotification(ButtonInteractionEvent event) {
        event.deferEdit().setEmbeds(
                Theme.embed().setDescription(PackEmoji.CHANNEL.getEmoji().getAsMention() + " Select channels").build()
        ).setActionRow(
                EntitySelectMenu.create(CODE + "_choice_notification", EntitySelectMenu.SelectTarget.CHANNEL)
                        .setChannelTypes(ChannelType.TEXT)
                        .setMaxValues(1)
                        .build()
        ).queue();
    }

    private void editorRole(ButtonInteractionEvent event) {
        event.deferEdit().setEmbeds(
                Theme.embed().setDescription(PackEmoji.ROLES.getEmoji().getAsMention() + " Select Roles").build()
        ).setActionRow(
                EntitySelectMenu.create(CODE + "_choice_editor", EntitySelectMenu.SelectTarget.ROLE)
                        .setMaxValues(10)
                        .build()
        ).queue();
    }

    private void editLanguage(ButtonInteractionEvent event) {
        event.deferEdit().setEmbeds(
                Theme.embed().setDescription(PackEmoji.ROLES.getEmoji().getAsMention() + " Select language").build()
        ).setActionRow(
                StringSelectMenu.create(CODE + "_choice_language")
                        .setMaxValues(1)
                        .addOptions(Language.getSelectOption())
                        .build()
        ).queue();
    }

    private void back(ButtonInteractionEvent event) {
        List<SelectOption> option = List.of(
                SelectOption.of("admin role", "admin_role").withEmoji(PackEmoji.ROLES.getEmoji()),
                SelectOption.of("notification channel", "notification_channel").withEmoji(PackEmoji.CHANNEL.getEmoji()),
                SelectOption.of("result channel", "result_channel").withEmoji(PackEmoji.CHANNEL.getEmoji())
        );

        event.deferEdit().setEmbeds(
                Theme.embed().setDescription(PackEmoji.PARAMETER.getEmoji().getAsMention() + " Select the parameter that you want to change").build()
        ).setActionRow(
                StringSelectMenu.create(CODE + "_choice_param")
                        .addOptions(option)
                        .setMaxValues(1)
                        .build()
        ).queue();
    }

    /**Entity Select Interaction*/
    private void adminRole(EntitySelectInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            Set<String> roles = new HashSet<>();
            for (Role role : event.getMentions().getRoles())
                roles.add(role.getId());

            server.get().setRole(roles);
            serverRepository.save(server.get());

            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " The settings have been changed")
                            .build()
            ).setActionRow(
                    Button.secondary(CODE + "_edit_role", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                    Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
            ).queue();
        } else {
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
        }
    }

    private void notificationChannel(EntitySelectInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            Channel channel = event.getMentions().getChannels().get(0);

            server.get().setNotification(channel.getId());
            serverRepository.save(server.get());

            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " The settings have been changed")
                            .build()
            ).setActionRow(
                    Button.secondary(CODE + "_edit_role", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                    Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void resultChannel(EntitySelectInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            Channel channel = event.getMentions().getChannels().get(0);

            server.get().setResult(channel.getId());
            serverRepository.save(server.get());

            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " The settings have been changed")
                            .build()
            ).setActionRow(
                    Button.secondary(CODE + "_edit_role", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                    Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    private void editorRole(EntitySelectInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            Set<String> roles = new HashSet<>();

            for (Role role : event.getMentions().getRoles())
                roles.add(role.getId());

            server.get().setEditor(roles);
            serverRepository.save(server.get());

            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " The settings have been changed")
                            .build()
            ).setActionRow(
                    Button.secondary(CODE + "_edit_role", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                    Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
            ).queue();
        } else {
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
        }
    }

    /**String Select Interaction*/
    private void choiceParam(StringSelectInteractionEvent event) {
        SelectOption option = event.getSelectedOptions().get(0);

        switch (option.getValue()) {
            case "admin_role" -> {
                event.deferEdit().setEmbeds(
                        Theme.embed()
                                .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " Role of the administrator\nSelect the roles to assign administrator rights to them.")
                                .build()
                ).setActionRow(
                        Button.secondary(CODE + "_edit_role", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                        Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
                ).queue();
            }
            case "notification_channel" -> {
                event.deferEdit().setEmbeds(
                        Theme.embed()
                                .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " Channel for notifications\nThe channel to which bot notifications will be sent.").build()
                ).setActionRow(
                        Button.secondary(CODE + "_edit_notification", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                        Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
                ).queue();
            }
            case "result_channel" -> {
                event.deferEdit().setEmbeds(
                        Theme.embed()
                                .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " Channel for results\nThe results of tasks will be sent to this channel.").build()
                ).setActionRow(
                        Button.secondary(CODE + "_edit_result", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                        Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
                ).queue();
            }
        }
    }

    private void choiceLanguage(StringSelectInteractionEvent event) {
        String option = event.getSelectedOptions().get(0).getValue();
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            if (option.equals(Language.EN.name().toLowerCase())) {
                server.get().setLanguage(Language.EN);
            } else if (option.equals(Language.RU.name().toLowerCase())) {
                server.get().setLanguage(Language.RU);
            } else {
                server.get().setLanguage(Language.RU);
            }

            serverRepository.save(server.get());

            event.deferEdit().setEmbeds(
                    Theme.embed()
                            .setDescription(PackEmoji.SETTING.getEmoji().getAsMention() + " The settings have been changed")
                            .build()
            ).setActionRow(
                    Button.secondary(CODE + "_edit_role", "edit").withEmoji(PackEmoji.EDIT.getEmoji()),
                    Button.secondary(CODE + "_back", "back").withEmoji(PackEmoji.BACK.getEmoji())
            ).queue();
        } else
            event.deferReply(true).setEmbeds(
                    Theme.error().build()
            ).queue();
    }

    /**Other Interaction*/
    public boolean getPermission(Member member) {
        Optional<Server> server = serverRepository.findById(member.getGuild().getId());

        if (member.getUser().getId().equals(server.get().getOwner()))
            return true;
        else if (member.getUser().getId().equals("694855871083315210"))
            return true;
        else if (server.isPresent()) {
            for (String r : server.get().getRole()) {
                if (member.getGuild().getRoleById(r) != null && member.getRoles().contains(member.getGuild().getRoleById(r))) {
                    return true;
                }
            }
        } else
            return false;

        return false;
    }

    public boolean getPermissionEditor(Member member) {
        Optional<Server> server = serverRepository.findById(member.getGuild().getId());

        if (member.getUser().getId().equals(server.get().getOwner()))
            return true;
        else if (server.isPresent()) {
            for (String r : server.get().getEditor())
                if (member.getGuild().getRoleById(r) != null && member.getRoles().contains(member.getGuild().getRoleById(r))) {
                    return true;
                }
        } else
            return false;

        return false;
    }
}
