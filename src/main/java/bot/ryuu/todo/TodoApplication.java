package bot.ryuu.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApplication {
	private static String TOKEN;

	public static void main(String[] args) {
		if (args.length > 0)
			TOKEN = args[0];

		SpringApplication.run(TodoApplication.class, args);
	}

	public static String getTOKEN() {
		return TOKEN;
	}
}
