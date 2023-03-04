package listeners.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DiscordEventListener extends ListenerAdapter {

    private final String
            INFO = "info:",
            INFO_RULES = "rules",
            INFO_INTERN = "intern";

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String id = event.getComponentId();

        if (id.startsWith(INFO)) {
            id = id.replace(INFO, "");

            if (id.startsWith(INFO_RULES)) {
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setColor(new Color(68, 68, 224))
                                .setTitle("Regeln")
                                .setDescription("Bitte verhaltet euch gut und haltet euch an die ToS.")
                                .build()
                ).setEphemeral(true).queue();
            } else if (id.startsWith(INFO_INTERN)) {
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setColor(new Color(68, 68, 224))
                                .setTitle("Bereiche")
                                .setDescription("""
                                        Dieser Server ist unterteilt in einen öffentlichen Bereich (den jedes neue Mitglied sehen kann) und einen internen Bereich, für den eine Freischaltung erforderlich ist.
                                        
                                        Der öffentliche Bereich ist für Smalltalk und zum Begrüßen von Neuankömmlingen.
                                        Hier findest du außerdem Events, Gewinnspiele und immer die neuesten Discord-Ankündigungen.
                                        
                                        Der interne Bereich ist für alle, die aktiv eine Community betreuen, eine eigene gegründet haben oder dies vorhaben und mehr Informationen suchen.
                                        Hier findest du wertvolle Ressourcen, Blog-Artikel, kannst Fragen stellen, Vorschläge einbringen, dich mit Gleichgesinnten austauschen und vieles mehr.
                                        Um unseren wertvollen Mitgliedern einen Save-Space zu bieten, ist für diesen Bereich eine Freischaltung erforderlich (<#1071174848254070837>). Dort musst du ein paar Fragen über deine Erfahrung und deine bisherige Arbeit beantworten, woraufhin du von einem unserer Moderatoren freigeschaltet wirst.
                                        """)
                                .build()
                ).setEphemeral(true).queue();
            }

        }
    }

}
