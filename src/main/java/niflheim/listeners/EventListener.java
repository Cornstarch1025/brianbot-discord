package niflheim.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import niflheim.Okita;
import niflheim.rethink.GuildOptions;
import niflheim.utils.Settings;

public class EventListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        event.getChannel().sendMessage("Please use Okita in a guild, check `.help` for documentation.").queue();

        TextChannel pm = Okita.getTextChannelById(Settings.PM_LOG);

        if (pm != null)
            pm.sendMessage("`" + event.getAuthor().getName() + "`: " + event.getMessage().getContent()).queue();
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        GuildOptions options = Okita.registry.ofGuild(event.getGuild().getId());

        if (options.isWelcomeEnable() && options.getWelcome() != null && options.getWelcome().length() > 0) {
            TextChannel channel = Okita.getTextChannelById(options.getMessageChannel());

            if (channel != null && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE))
                channel.sendMessage(event.getUser().getAsMention() + " " + options.getWelcome()).queue();
            else {
                options.setMessageChannel(null);

                event.getGuild().getPublicChannel().sendMessage(event.getUser().getAsMention() + " " + options.getWelcome()).queue();
            }
        }

        if (options.getAutorole() != null) {
            Role role = event.getGuild().getRoleById(options.getAutorole());

            if (role == null || !event.getGuild().getSelfMember().canInteract(role) || !event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
                options.setAutorole(null);
                return;
            }

            event.getGuild().getController().addSingleRoleToMember(event.getMember(), role).queue();
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        GuildOptions options = Okita.registry.ofGuild(event.getGuild().getId());

        if (options.isGoodbyeEnable() && options.getGoodbye() != null && options.getGoodbye().length() > 0) {
            TextChannel channel = Okita.getTextChannelById(options.getMessageChannel());

            if (channel != null && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE))
                channel.sendMessage(event.getUser().getAsMention() + " " + options.getGoodbye()).queue();
            else {
                options.setMessageChannel(null);

                event.getGuild().getPublicChannel().sendMessage(event.getUser().getAsMention() + " " + options.getGoodbye()).queue();
            }
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        TextChannel logs = Okita.getTextChannelById(Settings.LOGS);

        if (logs != null)
            logs.sendMessage("Guild: `" + event.getGuild() + "` ID: `" + event.getGuild().getId() + "` was joined!").queue();
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        TextChannel logs = Okita.getTextChannelById(Settings.LOGS);

        if (logs != null)
            logs.sendMessage("Guild: `" + event.getGuild() + "` ID: `" + event.getGuild().getId() + "` was left!").queue();
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        if (event.isClosedByServer())
            Okita.LOG.info("Shard " + event.getJDA().getShardInfo().getShardId() + " has disconnected (closed by server) with code: " + event.getServiceCloseFrame().getCloseCode() + " " + event.getCloseCode());
        else
            Okita.LOG.info("Shard " + event.getJDA().getShardInfo().getShardId() + " has disconnected with code: " + event.getServiceCloseFrame().getCloseCode() + " " + event.getCloseCode());
    }

    @Override
    public void onReconnect(ReconnectedEvent event) {
        Okita.LOG.info("Shard " + event.getJDA().getShardInfo().getShardId() + " has reconnected.");
    }

    @Override
    public void onException(ExceptionEvent event) {
        if (!event.isLogged())
            Okita.LOG.error("Error thrown by JDA: ", event.getCause());
    }
}
