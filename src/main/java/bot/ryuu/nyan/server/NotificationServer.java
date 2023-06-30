package bot.ryuu.nyan.server;

import bot.ryuu.nyan.theme.PackEmoji;
import bot.ryuu.nyan.theme.Theme;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public enum NotificationServer {
    NEW_TASK {
        public void action(String channel, Guild guild, String content) {
            TextChannel notification = guild.getTextChannelById(channel);

            if (notification != null) {
                notification.sendMessage("<@" + content + ">").setEmbeds(
                        Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " Your task list has been updated").build()
                ).queue();
            }
        }
    },
    COMPLETE_TASK {
        public void action(String channel, Guild guild, String content) {
            TextChannel notification = guild.getTextChannelById(channel);

            String user = content.substring(0, content.indexOf('*'));
            String name = content.substring(content.indexOf('*') + 1);

            if (notification != null) {
                notification.sendMessage("").setEmbeds(
                        Theme.embed().setDescription(PackEmoji.TASK.getEmoji().getAsMention() + " The task `" + name + "` was completed by the <@" + user + ">").build()
                ).queue();
            }
        }
    };

    public abstract void action(String channel, Guild guild, String content);
}
