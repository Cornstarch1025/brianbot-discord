package niflheim.core;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import niflheim.commands.Command;
import niflheim.commands.Scope;
import niflheim.utils.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Core {
    private static ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Long> cooldowns = new ConcurrentHashMap<>();
    private static ArrayList<String> disabled = new ArrayList<>();

    public static void onGuild(String command, Context context) {
        if (!commands.containsKey(command))
            return;

        if (!context.guild.getSelfMember().hasPermission(context.channel, Permission.MESSAGE_WRITE))
            return;

        if (!context.guild.getSelfMember().hasPermission(context.channel, Permission.MESSAGE_EMBED_LINKS)) {
            context.channel.sendMessage("Okita needs `" + Permission.MESSAGE_EMBED_LINKS.getName() + "` to work properly.").queue(s -> selfDelete(context, s));
            return;
        }

        if (disabled.contains(command)) {
            context.channel.sendMessage("This command has been temporarily disabled.").queue(s -> selfDelete(context, s));
            return;
        }

        Command cmd = commands.get(command);

        if (levelCheck(cmd, context) && rateCheck(command, context) && voiceCheck(cmd, context) && permissionCheck(cmd, context)) {
            String[] args = context.message.getContent().split("\\s+");

            if (args.length > 1)
                args = Arrays.copyOfRange(args, 1, args.length);
            else
                args = new String[0];

            try {
                commands.get(command).execute(context, args);
            } catch (PermissionException e) {
                context.channel.sendMessage("Okita is missing required permission `" + e.getPermission().getName() + "` for this command.").queue(s -> selfDelete(context, s));
            }

            rateLimit(context.user, command);
        }
    }

    public static ConcurrentHashMap<String, Command> getCommands() {
        return commands;
    }

    public static ConcurrentHashMap<String, Long> getCooldowns() {
        return cooldowns;
    }

    public static ArrayList<String> getDisabled() {
        return disabled;
    }

    private static boolean rateCheck(String command, Context context) {
        if (isRateLimited(context.user, command)) {
            long newCd = cooldowns.get(context.user.getId() + " " + command) - System.currentTimeMillis();
            rateLimit(context.user, command);

            context.channel.sendMessage("**Slow Down!** Please wait " + newCd / 1000 + " seconds.").queue(s -> selfDelete(context, s));
            return false;
        }

        return true;
    }

    private static boolean levelCheck(Command cmd, Context context) {
        if (cmd.getInfo().admin() && !Settings.ADMIN.contains(context.user.getId())) {
            context.channel.sendMessage("This command is Administrator only!").queue(s -> selfDelete(context, s));
            return false;
        }

        if (cmd.getInfo().guildOwner() && context.guild.getOwner() != context.member) {
            context.channel.sendMessage("This command is Guild Owner only!").queue(s -> selfDelete(context, s));
            return false;
        }

        if (cmd.getInfo().level() > 0 && context.userOptions.getLevel() < cmd.getInfo().level()) {
            context.channel.sendMessage("This command is restricted to Level " + cmd.getInfo().level() + " Donators!").queue(s -> selfDelete(context, s));
            return false;
        }

        return true;
    }

    private static boolean permissionCheck(Command cmd, Context context) {
        if (cmd.getInfo().permissions().length > 0 && !cmd.getInfo().scope().checkPermission(context, cmd.getInfo().permissions())) {
            StringBuilder msg = new StringBuilder("You have insufficient permissions in the ");
            switch (cmd.getInfo().scope()) {
                default:
                    break;
                case GUILD:
                    msg.append("guild.");
                    break;
                case TEXT:
                    msg.append("channel.");
                    break;
                case VOICE:
                    msg.append("voice channel.");
                    break;
            }

            context.channel.sendMessage(msg.toString()).queue(s -> selfDelete(context, s));
            return false;
        }

        return true;
    }

    private static boolean voiceCheck(Command cmd, Context context) {
        if (cmd.getInfo().scope() == Scope.VOICE && !context.member.getVoiceState().inVoiceChannel()) {
            context.channel.sendMessage("This command requires you to be in a Voice Channel.").queue(s -> selfDelete(context, s));
            return false;
        }

        return true;
    }

    private static void selfDelete(Context context, Message message) {
        if (context.guild.getSelfMember().hasPermission(context.channel, Permission.MESSAGE_MANAGE))
            message.delete().queueAfter(2, TimeUnit.SECONDS);
    }

    private static boolean isRateLimited(User user, String command) {
        return cooldowns.containsKey(user.getId() + " " + command) && System.currentTimeMillis() < cooldowns.get(user.getId() + " " + command);
    }

    private static void rateLimit(User user, String command) {
        if (commands.get(command).getInfo().cooldown() != 0L) {
            cooldowns.put(user.getId() + " " + command, System.currentTimeMillis() + commands.get(command).getInfo().cooldown());

            for (String x : commands.get(command).getInfo().aliases())
                cooldowns.put(user.getId() + " " + x, System.currentTimeMillis() + commands.get(command).getInfo().cooldown());
        }
    }
}
