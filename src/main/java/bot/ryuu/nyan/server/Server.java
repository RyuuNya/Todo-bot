package bot.ryuu.nyan.server;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    @Id
    private String id;

    private String owner;

    private int CurrentTask;
    private int CurrentTag;

    private String chanelEnd;

    private String result;
    private String notification;

    private Language language;

    private Set<String> role;
    private Set<String> editor;

    @Enumerated
    private Settings settings;

    public void incrementTask() {
        setCurrentTask(getCurrentTask() + 1);
    }

    public boolean taskPermission() {
        return getCurrentTask() <= settings.getMaxTask();
    }
}
