package bot.ryuu.nyan.worker;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, String> {
    @NotNull
    List<Worker> findAll();
    @NotNull
    Optional<Worker> findById(@NotNull String id);
}
