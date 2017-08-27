package niflheim.commands.general;

import com.jagrosh.jdautilities.menu.buttonmenu.ButtonMenuBuilder;
import com.jagrosh.jdautilities.menu.pagination.PaginatorBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.exceptions.PermissionException;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@CommandFrame(
        aliases = {"halp"},
        help = "Displays help information on modules as well as commands.",
        usage = ".help, .help <Module>, .help <Command>",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Help extends Command {
    private final String MUSIC = "\uD83C\uDFB5";
    private final String INFO = "\uD83D\uDCC4";

    private ButtonMenuBuilder helpMenu = new ButtonMenuBuilder()
            .setEventWaiter(Okita.waiter)
            .setDescription("React to see commands!")
            .setColor(Color.CYAN)
            .setChoices(INFO, MUSIC)
            .setTimeout(1, TimeUnit.MINUTES);

    public void execute(Context context, String[] args) {
        EmbedBuilder builder;
        helpMenu.setAction(e -> {
            switch (e.getName()) {
                case MUSIC:
                    helpMenu.setDescription("Music Commands").build().display(context.channel);
                    break;
                case INFO:

                    break;
            }
        }).setUsers(context.user)
        .build()
        .display(context.channel);
    }

    private String pageLoader(Emote react) {
        return null;
    }
}