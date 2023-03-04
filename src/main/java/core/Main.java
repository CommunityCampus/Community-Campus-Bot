package core;

import listeners.discord.DiscordEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {

    private static JDA jda;

    public static void main(String[] args) {
        jda = JDABuilder
                .createDefault("DISCORD_TOKEN")
                .addEventListeners(new DiscordEventListener())
                .build();
        Console.start();
    }

    public static JDA getJda() {
        return jda;
    }

}
