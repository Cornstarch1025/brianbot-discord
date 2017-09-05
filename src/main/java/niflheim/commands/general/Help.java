package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.core.Core;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;

@CommandFrame(
        aliases = {"halp"},
        help = "Displays help information on modules as well as commands.",
        usage = ".help, .help <Module>, .help <Command>",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Help extends Command {
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
                    embed.setTitle(args[0].toUpperCase().substring(0,1) + args[0].toLowerCase().substring(1) + " Command")
                            .setDescription(cmd.getInfo().help())
                            .addField("Usage", cmd.getInfo().usage(), false)
                            .addField("Alias", Arrays.toString(cmd.getInfo().aliases()).substring(1, Arrays.toString(cmd.getInfo().aliases()).length() - 1), false)
                            .setFooter("Donator Level " + cmd.getInfo().level() + " command", null);

                    context.channel.sendMessage(embed.build()).queue();
                    break;
                }
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
}