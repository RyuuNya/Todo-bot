package bot.ryuu.todo.data.task;

import bot.ryuu.todo.data.DataCluster;
import bot.ryuu.todo.theme.Theme;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import net.dv8tion.jda.api.entities.MessageEmbed;

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

    public Task save(DataCluster cluster) {
        cluster.getTaskRepository().save(this);
        return this;
    }

    public MessageEmbed info() {
        return Theme.main()
                .setTitle(name)
                .setDescription(description)
                .build();
    }
}