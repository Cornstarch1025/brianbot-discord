package niflheim.commands.mod;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CommandFrame(
        name = "Prune",
        example = ".prune 100",
        aliases = {"nuke"},
        help = "Prunes a channel of 2-100 messages, but cannot delete messages older than 2 weeks.",
        usage = ".prune <Number>",
        cooldown = 3000L,
        category = Category.MOD,
        scope = Scope.TEXT,
        permissions = {Permission.MESSAGE_MANAGE}
)
public class Prune extends Command {
    public void execute(Context context, String[] args) {
        switch (args.length) {
            default:
                context.invalid(this);
                break;
            case 1:
                try {
                    int num = Integer.parseInt(args[0]);

                    if (num < 2 || num > 100) {
                        context.channel.sendMessage("Please enter a number inclusive to 2-100!").queue();
                        return;
                    }

                    List<Message> messages = context.channel.getIterableHistory().stream().limit(num).filter(e -> ChronoUnit.WEEKS.between(e.getCreationTime(), OffsetDateTime.now()) < 2).collect(Collectors.toList());
                    TextChannel target = context.guild.getTextChannelById(context.channel.getId());

                    if (messages.size() > 1 && messages.size() < 101)
                        target.deleteMessages(messages).queue(success -> target.sendMessage("Was able to prune " + messages.size() + " message(s)").queue(success2 -> success2.delete().queueAfter(2, TimeUnit.SECONDS)));
                    else {
                        context.channel.sendMessage("Prune could not be completed.").queue();
                        return;
                    }
                } catch (Exception e) {
                    context.invalid(this);
                    return;
                }
                break;
        }
    }
}