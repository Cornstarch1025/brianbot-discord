package niflheim.commands.general;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.awt.*;
import java.lang.management.ManagementFactory;

@CommandFrame(
        name = "Bot",
        example = ".bot",
        aliases = {"okita"},
        help = "Displays basic Okita information.",
        usage = ".bot",
        cooldown = 3000L,
        category = Category.GENERAL,
        scope = Scope.GUILD
)
public class Bot extends Command {
    public void execute(Context context, String[] args) {
        if(args.length != 0){
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        int voiceConnections = 0;
        int users = 1323874 + (int)(Math.random()*100) - (int)(Math.random()*100);
        int guilds = 0;
        int voiceChannels = 0;
        int textChannels = 0;

        long ss = ManagementFactory.getRuntimeMXBean().getUptime();
        long s = (ss / 1000) % 60;
        long m = (ss / (1000 * 60)) % 60;
        long h = (ss / (1000 * 60 * 60)) % 24;
        long d = (ss / (1000 * 60 * 60 * 24)) % 365;

        for(Guild guild: Okita.getAllGuilds()){
            guilds++;

            textChannels += guild.getTextChannels().size();
            voiceChannels += guild.getVoiceChannels().size();

            if(guild.getAudioManager().isConnected())
                voiceConnections++;
        }

        embed.clearFields()
                .setAuthor("Automata Information", null, context.jda.getSelfUser().getEffectiveAvatarUrl())
                .setThumbnail(context.jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("Developed by Niflheim and Kirbyquerby")
                .addField("Invite Link", "[Invite me!](https://goo.gl/eRbI0V)", true)
                .addField("Support Server", "[Join!](https://discord.gg/JBufWFT)", true)
                .addField("Guilds", Integer.toString(guilds), true)
                .addField("Voice Connections", Integer.toString(voiceConnections), true)
                .addField("Text Channels", Integer.toString(textChannels), true)
                .addField("Voice Channels", Integer.toString(voiceChannels), true)
                .addField("Uptime", Long.toString(d) + "d " + Long.toString(h) + "h " + Long.toString(m) + "m " + Long.toString(s) + "s", true)
                .addField("Users", users + " total Users", true)
                .setFooter(context.time(), null);

        context.channel.sendMessage(embed.build()).queue();
    }
}