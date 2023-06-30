package bot.ryuu.nyan.service;

import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.server.ServerRepository;
import bot.ryuu.nyan.tag.Tag;
import bot.ryuu.nyan.tag.TagRepository;
import bot.ryuu.nyan.theme.SystemEmoji;
import bot.ryuu.nyan.theme.Theme;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TagManager {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private SettingsManager settingsManager;

    public void execute(SlashCommandInteractionEvent event) {
        boolean security = settingsManager.getPermission(event.getMember());

        if (security)
            switch (event.getFullCommandName()) {
                case "tag new" -> newTag(event);
                case "tag remove" -> removeTag(event);
            }
        else
            event.replyEmbeds(
                    Theme.embed().setDescription(SystemEmoji.SECURITY.getCode() + " you don't have enough rights").build()
            ).setEphemeral(true).queue();
    }

    public void execute(StringSelectInteractionEvent event) {
        boolean security = settingsManager.getPermission(event.getMember());

        if (security)
            switch (event.getSelectMenu().getId()) {
                case "_tag_manager_remove_tag" -> removeTag(event);
            }
        else
            event.replyEmbeds(
                    Theme.embed().setDescription(SystemEmoji.SECURITY.getCode() + " you don't have enough rights").build()
            ).setEphemeral(true).queue();
    }

    /**Slash Command Interaction*/
    public void newTag(SlashCommandInteractionEvent event) {
        Optional<Server> server = serverRepository.findById(event.getGuild().getId());

        if (server.isPresent()) {
            String name = event.getOption("name").getAsString();

            Tag tag = Tag.builder()
                    .name(name)
                    .server(server.get())
                    .build();

            tagRepository.save(tag);

            event.replyEmbeds(
                    Theme.embed().setDescription("The tag was successfully created").build()
            ).setEphemeral(true).queue();
        } else {
            event.replyEmbeds(
                    Theme.embed().setDescription("Something didn't go according to plan").build()
            ).setEphemeral(true).queue();
        }
    }

    public void removeTag(SlashCommandInteractionEvent event) {
        List<Tag> tags = tagRepository.findAllByServerId(event.getGuild().getId());

        if (tags.size() > 0) {
            ArrayList<SelectOption> options = new ArrayList<>();

            for (Tag tag : tags) {
                options.add(SelectOption.of(tag.getName(), tag.getId()));
            }

            event.replyEmbeds(
                    Theme.embed().setDescription("Select the tags you want to delete").build()
            ).addActionRow(
                    StringSelectMenu.create("_tag_manager_remove_tag")
                            .addOptions(options)
                            .setMaxValues(20)
                            .build()
            ).setEphemeral(true).queue();
        } else {
            event.replyEmbeds(
                    Theme.embed().setDescription("Unfortunately the list of tags is still empty, to create a tag use the command `/tag new`.").build()
            ).setEphemeral(true).queue();
        }
    }

    /**String Select Interaction*/
    public void removeTag(StringSelectInteractionEvent event) {
        ArrayList<Tag> tags = new ArrayList<>();

        for (SelectOption selectOption : event.getSelectedOptions()) {
            Optional<Tag> tag = tagRepository.findById(selectOption.getValue());

            tag.ifPresent(tags::add);
        }

        tagRepository.deleteAll(tags);

        event.deferEdit().setEmbeds(
                Theme.embed().setDescription(SystemEmoji.TRASH.getCode() + " the selected tags have been deleted").build()
        ).queue();
    }
}
