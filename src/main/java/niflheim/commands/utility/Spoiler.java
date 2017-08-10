package niflheim.commands.utility;

import net.dv8tion.jda.core.Permission;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.utils.AnimatedGifEncoder;
import niflheim.utils.WrapUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@CommandFrame(
        help = "Creates a spoiler for others to view at their own risk!",
        usage = ".spoiler <Text>",
        cooldown = 3000L,
        category = Category.UTILITY,
        scope = Scope.TEXT
)
public class Spoiler extends Command {
    private final Color DARKTHEME = new Color(54, 57, 62);
    private final Color HOVERCOLOR = new Color(155, 155, 155);
    private final Color TEXTCOLOR = Color.WHITE;
    private final Font FONT = new Font("Arial", Font.PLAIN, 15);
    private final int BUFFER = 3;

    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.invalid(this);
            return;
        }

        if (!context.guild.getSelfMember().hasPermission(context.channel, Permission.MESSAGE_MANAGE, Permission.MESSAGE_ATTACH_FILES)) {
            context.channel.sendMessage("Okita needs Message Manage, and Attach File permissions for this command.").queue();
            return;
        }

        StringBuilder spoiler = new StringBuilder();

        for (String x : args)
            spoiler.append(x).append(" ");

        try {
            Canvas c = new Canvas();
            List<String> lines = WrapUtil.wrap(spoiler.toString().replace("\n", " "), c.getFontMetrics(FONT), 400 - (2 * BUFFER) - 2);
            if (lines.size() > 8) {
                replace("Too many lines (>8)", context);
                return;
            }

            BufferedImage text = new BufferedImage(400, lines.size() * (FONT.getSize() + BUFFER) + 2 * BUFFER + 2, BufferedImage.TYPE_INT_RGB);
            BufferedImage hover = new BufferedImage(400, lines.size() * (FONT.getSize() + BUFFER) + 2 * BUFFER + 2, BufferedImage.TYPE_INT_RGB);
            Graphics2D textG = text.createGraphics();
            Graphics2D hoverG = hover.createGraphics();
            textG.setFont(FONT);
            hoverG.setFont(FONT);
            textG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            hoverG.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            textG.setColor(DARKTHEME);
            hoverG.setColor(DARKTHEME);
            textG.fillRect(1, 1, text.getWidth() - 2, text.getHeight() - 2);
            hoverG.fillRect(1, 1, text.getWidth() - 2, text.getHeight() - 2);
            textG.setColor(TEXTCOLOR);
            hoverG.setColor(HOVERCOLOR);

            for (int i = 0; i < lines.size(); i++)
                textG.drawString(lines.get(i), BUFFER + 1, (i + 1) * (FONT.getSize() + BUFFER) - 1);
            hoverG.drawString("Hover to view Spoiler Text", BUFFER + 1, FONT.getSize() + BUFFER - 1);

            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start("spoiler.gif");
            e.setDelay(1);
            e.addFrame(hover);
            e.setDelay(60000);
            e.addFrame(text);
            e.setDelay(60000);
            e.finish();

            context.message.delete().queue(s -> context.channel.sendFile(new File("spoiler.gif"), null).queue());
            textG.dispose();
            hoverG.dispose();
        } catch (Exception e) {
            replace("Spoiler generation failed!", context);
        }
    }

    private void replace(String input, Context context) {
        context.message.delete().queue(s -> context.channel.sendMessage(input).queue());
    }
}