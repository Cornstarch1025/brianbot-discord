package niflheim.core;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import niflheim.Okita;
import niflheim.rethink.GuildOptions;

public class Context {
    public final Message message;
    public final TextChannel channel;
    public final Guild guild;
    public final GuildOptions guildOptions;
    public final JDA jda;
    public final Member member;
    public final User user;

    public Context(GuildMessageReceivedEvent event) {
        this(event, Okita.registry.ofGuild(event.getGuild().getId()));
    }

    public Context(GuildMessageReceivedEvent event, GuildOptions guildOptions){
        this.guildOptions = guildOptions;
        message = event.getMessage();
        channel = event.getChannel();
        guild = event.getGuild();
        jda = event.getJDA();
        member = event.getMember();
        user = event.getAuthor();
    }
}
