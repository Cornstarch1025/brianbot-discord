package niflheim.commands.fun;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        aliases = {"8ball"},
        help = "Do you dare test your fate with the Almighty 8Ball?",
        usage = ".8ball <Question>",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.GUILD
)
public class EightBall extends Command {
    private final String[] responses = {"As I see it, yes.", "Ask again later.", "Better not tell you now.", "Cannot predict now.", "Concentrate and ask again.", "Don't count on it.", "It is certain.", "It is decidedly so.", "Most likely.", "My reply is no.", "My sources say no.", "Outlook good.", "Outlook not so good.", "Reply hazy, try again.", "Signs point to yes.", "Very doubtful.", "Without a doubt.", "Yes.", "Yes, definitely.", "You may rely on it."};

    public void execute(Context context, String[] args) {
        if(args.length == 0){
            context.invalid(this);
            return;
        }

        context.channel.sendMessage(responses[(int)(Math.random()*20)]).queue();
    }
}