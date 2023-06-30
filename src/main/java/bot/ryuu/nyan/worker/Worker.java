package bot.ryuu.nyan.worker;

import bot.ryuu.nyan.service.find.FindFilter;
import bot.ryuu.nyan.task.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Worker {
    @Id
    private String id;

    public int lvl;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    private Set<Task> tasks = new HashSet<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        for (Task remove : getTasks()) {
            if (task.getId().equals(remove.getId())) {
                this.tasks.remove(remove);
                break;
            }
        }
    }

    public void upLvl(int point) {
        setLvl(getLvl() + 1);
    }
}
