package bot.ryuu.nyan.language;

import bot.ryuu.nyan.server.Language;
import bot.ryuu.nyan.server.Server;

public enum Message {
    // Task builder
    NEW_TASK {
        public String content(Server server) {
            if (server.getLanguage().equals(Language.RU))
                return "Создание нового задания";
            else
                return "Creating a new task";
        }
    },
    SET_TAG {
        public String content(Server server) {
            if (server.getLanguage().equals(Language.RU))
                return "Выберите нужные теги";
            else
                return "Select the desired tags";
        }
    },
    SET_RANK {
        public String content(Server server) {
            if (server.getLanguage().equals(Language.RU))
                return "Выберите нужный ранг";
            else
                return "Select the desired ranks";
        }
    },
    SET_TYPE {
        public String content(Server server) {
            if (server.getLanguage().equals(Language.RU))
                return "Выберите нужный тип";
            else
                return "Select the desired type";
        }
    },
    ASSIGN {
        public String content(Server server) {
            if (server.getLanguage().equals(Language.RU))
                return "Выберите пользователя, которому вы хотите дать задание";
            else
                return "Select the user you want to give the task to";
        }
    },
    CANCEL_TASK {
        public String content(Server server) {
            if (server.getLanguage().equals(Language.RU))
                return "Создание задачи было отменено";
            else
                return "The task creation was canceled";
        }
    };

    public abstract String content(Server server);
}
