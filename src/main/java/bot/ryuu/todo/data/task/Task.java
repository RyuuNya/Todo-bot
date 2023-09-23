package bot.ryuu.todo.data.task;

import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.theme.Theme;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String description;

    private String serverId;

    private Color color;

    public void save(DataCluster cluster) {
        cluster.getTaskRepository().save(this);
    }

    public MessageEmbed info() {
        return Theme.main()
                .setColor(color)
                .setTitle(name)
                .setDescription(description)
                .build();
    }
}