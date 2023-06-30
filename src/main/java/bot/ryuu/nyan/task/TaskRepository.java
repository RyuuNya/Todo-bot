package bot.ryuu.nyan.task;

import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    @NotNull
    List<Task> findAll();
    @NotNull
    Optional<Task> findById(@NotNull String id);
    @NotNull
    List<Task> findAllByServerId(@NotNull String id);
    @NotNull
    List<Task> findAllByName(@NotNull String name);
    @NotNull
    List<Task> findAllByTags(@NotNull String... id);
}
