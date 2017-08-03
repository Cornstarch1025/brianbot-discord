package niflheim.core;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import niflheim.Okita;
import niflheim.commands.Command;
import niflheim.rethink.GuildOptions;
import niflheim.rethink.UserOptions;

import java.text.DateFormat;
import java.util.Date;

public class Context {
    public final Message message;
    public final TextChannel channel;
    public final Guild guild;
    public final GuildOptions guildOptions;
    public final UserOptions userOptions;
    public final JDA jda;
    public final Member member;
    public final User user;

    public Context(GuildMessageReceivedEvent event) {
        this(event, Okita.registry.ofGuild(event.getGuild().getId()), Okita.registry.ofUser(event.getAuthor().getId()));
    }

    public Context(GuildMessageReceivedEvent event, GuildOptions guildOptions, UserOptions userOptions) {
        this.guildOptions = guildOptions;
        this.userOptions = userOptions;
        message = event.getMessage();
        channel = event.getChannel();
        guild = event.getGuild();
        jda = event.getJDA();
        member = event.getMember();
        user = event.getAuthor();
    }

    public String time() {
        Date now = new Date();

        return DateFormat.getDateInstance(DateFormat.FULL).format(now).replaceFirst(",", " ") + " at " + DateFormat.getTimeInstance(DateFormat.LONG).format(now);
    }

    public void invalid(Command command) {
        channel.sendMessage("Invalid usage! The correct usage is: `" + command.getInfo().usage() + "`!").queue();
    }
}
