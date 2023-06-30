package bot.ryuu.nyan.task;

import bot.ryuu.nyan.server.Server;
import bot.ryuu.nyan.tag.Tag;
import bot.ryuu.nyan.task.stage.Stage;
import bot.ryuu.nyan.theme.PackEmoji;
import bot.ryuu.nyan.theme.Theme;
import jakarta.persistence.*;
import lombok.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    @Lob
    private String description;

    private TaskRank taskRank;
    private TaskType taskType;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Stage> stages = new HashSet<>();

    private String doing;
    private boolean active;

    private boolean bored;

    private Date timeline;

    @ManyToOne
    private Server server;

    public String isBored() {
        if (bored) {
            return " [bored]";
        } else {
            return "";
        }
    }

    public boolean isDoing() {
        if (doing != null && !doing.equals(""))
            return true;
        else
            return false;
    }

    public MessageEmbed getContent() {
        return Theme.embed()
                .setTitle(getName())
                .setDescription(getDescription())
                .addField(new MessageEmbed.Field("Type", "`" + getTaskType().name().toLowerCase() + "`", true))
                .addField(new MessageEmbed.Field("Rank", getTaskRank().getEmoji().getFormatted()+"", true))
                .build();
    }


    public void addTag(ArrayList<Tag> tags) {
        this.tags.addAll(tags);
    }

    public void addTag(Tag tags) {
        this.tags.add(tags);
    }

    public boolean isTag(Tag tag) {
        return tags.contains(tag);
    }

    public String getTagsContent() {
        String content = "\n\n";

        for (Tag tag : tags) {
            content += "`" + tag.getName() + "` ";
        }

        return content;
    }

    public void setTimeline(String timeline) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy");
        this.timeline = formatter.parse(timeline);
    }

    public boolean isStage() {
        return !getTaskType().equals(TaskType.STAGE);
    }

    public String getStagesContent() {
        String content = "";

        List<Stage> stages = new ArrayList<>(getStages());
        Collections.sort(stages);

        int idx = 1;
        for (Stage stage : stages) {
            content += "❖ " + stage.getName() + " ";
            if (stage.isExecuted()) content += PackEmoji.COMPLETE_STAGE.getEmoji().getAsMention();
            content += "\n";
            idx++;
        }

        return content;
    }
}