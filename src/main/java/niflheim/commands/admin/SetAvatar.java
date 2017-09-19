package niflheim.commands.admin;

import net.dv8tion.jda.core.entities.Icon;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;

import java.io.InputStream;
import java.net.URL;

@CommandFrame(
        name = "SetAvatar",
        example = ".setavatar https://i.imgur.com/S0NkEwX.jpg",
        help = "Sets the bot's avatar via URL.",
        usage = ".setavatar <Url>",
        admin = true,
        toggleable = false,
        category = Category.ADMIN
)
public class SetAvatar extends Command {
    @Override
    public void execute(Context context, String[] args) {
        if (args.length != 1) {
            context.invalid(this);
            return;
        }

        try {
            InputStream input = new URL(args[0]).openStream();

            context.jda.getSelfUser().getManager().setAvatar(Icon.from(input)).queue(success -> context.channel.sendMessage("Okita's avatar successfully changed.").queue());
        } catch (Exception e) {
            Okita.LOG.error("<Ignore> Error while changing bot avatar <Ignore>");
        }
    }
}
