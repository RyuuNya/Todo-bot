package bot.ryuu.todo.language;

import bot.ryuu.todo.language.packing.Param;
import bot.ryuu.todo.language.packing.Wrap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class Messages {
    private static String path;

    @Value("${language.path}")
    public void setPath(String path) {
        Messages.path = path;
    }

    public static String message(String name, LanguageType language) {
        String message = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            Wrap wrap = mapper.readValue(new File(path), Wrap.class);

            for (Param param : wrap.params()) {
                if (param.name().equals(name)) {
                    message = (language == LanguageType.EN) ? param.en() : param.ru();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}