package bot.ryuu.nyan.task.stage;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StageRepository extends JpaRepository<Stage, String> {
    @NotNull
    List<Stage> findAll();
    @NotNull
    Optional<Stage> findById(@NotNull String id);
}
