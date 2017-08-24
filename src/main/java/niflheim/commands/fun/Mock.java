package niflheim.commands.fun;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;

@CommandFrame(
        help = "Mock your enemies!",
        usage = ".mock <Message>",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.GUILD
)
public class Mock extends Command {
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.channel.sendMessage(mock("Please specify text to mock")).queue();
            return;
        }

        StringBuilder list = new StringBuilder();

        for (String str : args)
            list.append(str).append(" ");

        String mocker = mock(list.toString());

        context.channel.sendMessage(mocker).queue();
    }

    private String mock(String input) {
        char[] arr = input.replace('0', 'o').toLowerCase().toCharArray();
        for (int x = 0; x < arr.length; x++) {
            char c = arr[x];
            if (Character.isLetter(c)) {

                switch (c) {
                    case 'l':
                        arr[x] = 'L';
                        break;
                    case 'i':
                        break;
                    case 'o':
                        arr[x] = Math.random() - .5 > 0 ? c : '0';
                        break;
                    default:
                        arr[x] = Math.random() - .5 > 0 ? c : Character.toUpperCase(c);
                        break;
                }

            }
        }
        return new String(arr);
    }
}