package bot.ryuu.todo.data.server;

import bot.ryuu.todo.language.LanguageType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    @Id
    private String id;

    private int complete;
    private int allTask;
    private int members;

    private LanguageType language;

    public void incComplete(int increment) {
        this.complete += increment;
    }

    public void incAllTask(int increment) {
        this.allTask += increment;
    }

    public Server save(ServerRepository serverRepository) {
        serverRepository.save(this);
        return this;
    }
}