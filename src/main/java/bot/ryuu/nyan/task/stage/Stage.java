package bot.ryuu.nyan.task.stage;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stage implements Comparable<Stage> {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    public String name;

    public int number;

    public boolean executed;

    @Override
    public int compareTo(@NotNull Stage o) {
        return (this.number > o.getNumber()) ? 1 : -1;
    }
}
