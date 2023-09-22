package bot.ryuu.todo.data.task;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    @NotNull
    Optional<Task> findById(@NotNull String id);

    @NotNull
    List<Task> findAllByServerId(@NotNull String serverId);
}
