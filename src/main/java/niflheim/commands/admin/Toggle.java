package niflheim.commands.admin;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;
import niflheim.core.Core;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@CommandFrame(
        name = "Toggle",
        example = ".toggle ping",
        help = "Toggles whether a module or command can be used.",
        usage = ".toggle <Module>, .toggle <Command>",
        admin = true,
        toggleable = false,
        category = Category.ADMIN
)
public class Toggle extends Command {
    private EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

    @Override
    public void execute(Context context, String[] args) {
        if (args.length != 1) {
            context.invalid(this);
            return;
        }

        String command = args[0].toLowerCase();

        switch (command) {
            default:
                if (Core.getCommands().containsKey(command))
                    toggleCommand(context, command, Core.getCommands().get(command));
                else
                    context.channel.sendMessage("Command or module not found!").queue();
                break;
            case "admin":
                context.channel.sendMessage("Admin module can not be toggled off.").queue();
                break;
            case "fun":
                toggleModule(context, Category.FUN);
                break;
            case "general":
                toggleModule(context, Category.GENERAL);
                break;
            case "info":
                toggleModule(context, Category.INFO);
                break;
            case "mod":
                toggleModule(context, Category.MOD);
                break;
            case "music":
                toggleModule(context, Category.MUSIC);
                break;
            case "utility":
                toggleModule(context, Category.UTILITY);
                break;
        }
    }

    private String toggleCommand(Context context, String name, Command cmd) {
        if (!cmd.getInfo().toggleable()) {
            context.channel.sendMessage("This command can not be toggled").queue();
            return "";
        }

        StringBuilder toggle = new StringBuilder();
        String[] aliases = cmd.getInfo().aliases();
        embed.setTitle("Toggle Command");

        if (Core.setDisabled(name))
            toggle.append("`" + name + "` and its aliases has been toggled off.").append("\n");
        else
            toggle.append("`" + name + "` and its aliases has been toggled on.").append("\n");

        for (String x : aliases)
            Core.setDisabled(x);

        if (context != null) {
            context.channel.sendMessage(embed.setDescription(toggle.toString()).setFooter(context.time(), null).build()).queue();
            return "";
        }

        return toggle.toString();
    }

    private void toggleModule(Context context, Category category) {
        StringBuilder toggle = new StringBuilder();
        embed.setTitle("Toggle Module");

        for (Map.Entry<String, Command> x : Core.getCommands().entrySet()) {
            ArrayList<String> aliases = new ArrayList<>(Arrays.asList(x.getValue().getInfo().aliases()));
            if (x.getValue().getInfo().category().equals(category) && !aliases.contains(x.getKey()))
                toggle.append(toggleCommand(null, x.getKey(), x.getValue()));
        }

        context.channel.sendMessage(embed.setDescription(toggle.toString()).setFooter(context.time(), null).build()).queue();
    }
}
