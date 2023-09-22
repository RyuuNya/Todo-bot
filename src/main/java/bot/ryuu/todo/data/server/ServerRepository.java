package bot.ryuu.todo.data.server;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, String> {
    @NotNull
    Optional<Server> findById(@NotNull String id);
}
