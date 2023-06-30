package bot.ryuu.nyan.theme;

import lombok.Getter;

import java.util.List;

@Getter
public enum SystemMessage {
    NOT_ENOUGH_RIGHTS("У вас недостаточно прав", "You don't have enough rights");

    public String getRu() {
        return content.get(0);
    }

    public String getEn() {
        return content.get(1);
    }

    private final List<String> content;

    private SystemMessage(String... content) {
        this.content = List.of(content);
    }
}
