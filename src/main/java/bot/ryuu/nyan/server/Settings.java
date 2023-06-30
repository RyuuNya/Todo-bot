package bot.ryuu.nyan.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Settings {
    OPEN(100, 10),
    HOT(200, 100);

    private final int maxTask;
    private final int maxTag;
}
