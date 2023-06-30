package bot.ryuu.nyan.server;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, String> {
    @NotNull
    List<Server> findAll();
    @NotNull
    Optional<Server> findById(@NotNull String id);
    @NotNull
    List<Server> findAllByOwner(@NotNull String owner);
}
