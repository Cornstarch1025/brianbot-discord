package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.core.Core;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@CommandFrame(
        name = "Help",
        example = ".help Chess",
        aliases = {"halp"},
        help = "Displays help information on modules as well as commands.",
        usage = ".help, .help <Module>, .help <Command>",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Help extends Command {
    private ArrayList<String> modules = new ArrayList<>(Arrays.asList("admin", "general", "information", "moderation", "fun", "music", "chess", "utility"));
    public void execute(Context context, String[] args) {
        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        switch (args.length){
            default:
                context.invalid(this);
                return;
            case 0:
                embed.setAuthor("Okita Help", null, context.jda.getSelfUser().getEffectiveAvatarUrl())
                        .setDescription("Ubiquitous Discord bot developed by Niflheim and Kirbyquerby. Please consider donating as server hosting is not free.")
                        .addField("General - 7", parseModule(Category.GENERAL), false)
                        .addField("Information - 4", parseModule(Category.INFO), false)
                        .addField("Moderation - 9", parseModule(Category.MOD), false)
                        .addField("Fun - 6", parseModule(Category.FUN), false)
                        .addField("Music - 19", parseModule(Category.MUSIC), false)
                        .addField("Chess - 4", parseModule(Category.CHESS), false)
                        .addField("Utility - 1", parseModule(Category.UTILITY), false)
                        .setFooter(".Help <Command> will pull up more information.", null);

                context.channel.sendMessage(embed.build()).queue();
                break;
            case 1:
                if(Core.getCommands().containsKey(args[0].toLowerCase())){
                    Command cmd = Core.getCommands().get(args[0].toLowerCase());
                    embed.setTitle(cmd.getInfo().name() + " Command")
                            .setDescription(cmd.getInfo().help())
                            .addField("Usage", cmd.getInfo().usage(), false)
                            .addField("Example Usage", cmd.getInfo().example(), false)
                            .addField("Alias", cmd.getInfo().aliases().length > 0 ? Arrays.toString(cmd.getInfo().aliases()).substring(1, Arrays.toString(cmd.getInfo().aliases()).length() - 1) : "No aliases!", false)
                            .setFooter("Donator Level " + cmd.getInfo().level() + " command", null);

                    context.channel.sendMessage(embed.build()).queue();
                } else if (modules.contains(args[0].toLowerCase())) {
                    embed.setTitle(args[0].toUpperCase() + args[0].toLowerCase().substring(1) + " Module")
                            .setDescription(parseModule(args[0].toLowerCase()))
                            .setFooter(".Help <Command> will pull up more information.", null);
                } else
                    context.channel.sendMessage("Command or Module note found!").queue();
                break;
        }
    }

    private String parseModule(Category category){
        StringBuilder commands = new StringBuilder();

        for (Map.Entry<String, Command> x: Core.getHelp().entrySet())
            if (x.getValue().getInfo().category().equals(category))
                commands.append("`").append(x.getKey()).append("`, ");

        if (commands.length() != 0)
            commands.deleteCharAt(commands.lastIndexOf(","));

        return commands.toString();
    }

    private String parseModule(String module) {
        Category category;

        switch (module){
            default:
                category = null;
                break;
            case "admin":
                category = Category.ADMIN;
                break;
            case "general":
                category = Category.GENERAL;
                break;
            case "information":
                category = Category.INFO;
                break;
            case "moderation":
                category = Category.MOD;
                break;
            case "fun":
                category = Category.FUN;
                break;
            case "music":
                category = Category.MUSIC;
                break;
            case "chess":
                category = Category.CHESS;
                break;
            case "utility":
                category = Category.UTILITY;
                break;
        }

        if (category == null)
            return null;
        else
            return parseModule(category);
    }
}