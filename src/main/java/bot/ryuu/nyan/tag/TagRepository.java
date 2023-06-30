package bot.ryuu.nyan.tag;

import bot.ryuu.nyan.task.Task;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {
    @NotNull
    List<Tag> findAll();
    @NotNull
    Optional<Tag> findById(@NotNull String id);
    @NotNull
    List<Tag> findAllByServerId(@NotNull String id);

    Object findAllByName(String name);
}
