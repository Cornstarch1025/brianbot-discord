package niflheim.commands.admin;

import net.dv8tion.jda.core.EmbedBuilder;
import niflheim.Okita;
import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.core.Context;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.concurrent.*;

@CommandFrame(
        help = "Evaluates given Nashorn code and returns output or debug output.",
        usage = ".eval <code>",
        admin = true,
        toggleable = false,
        category = Category.ADMIN
)
public class Eval extends Command {
    private ScriptEngine engine;

    public Eval() {
        engine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util)");
        } catch (ScriptException e) {
            Okita.LOG.error(e.getMessage());
        }
    }

    @Override
    public void execute(Context context, String[] args) {
        if (args.length == 0) {
            context.invalid(this);
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setColor(Color.CYAN);

        final String script;
        StringBuilder code = new StringBuilder();

        for (String x : args)
            code.append(x).append(" ");

        script = code.toString().replaceAll("```java", "").replaceAll("```", "");

        context.channel.sendTyping().queue();

        engine.put("jda", context.jda);
        engine.put("channel", context.channel);
        engine.put("user", context.user);
        engine.put("bot", context.jda.getSelfUser());
        engine.put("member", context.member);
        engine.put("message", context.message);
        engine.put("guild", context.guild);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> future = service.schedule(() -> {
            Object out;

            try {
                out = engine.eval(
                        "(function() {"
                                + "with (imports) {\n" + script + "\n}"
                                + "})();");
            } catch (Exception e) {
                embed.setAuthor("Debug Output", null, context.user.getEffectiveAvatarUrl())
                        .setDescription("`" + e.getMessage() + "`")
                        .setFooter(context.time(), null);

                context.channel.sendMessage(embed.build()).queue();
                return;
            }

            if (out == null)
                embed.setAuthor("Evaluation Output", null, context.user.getEffectiveAvatarUrl())
                        .setDescription("Task completed successfully.")
                        .setFooter(context.time(), null);
            else
                embed.setAuthor("Evaluation Output", null, context.user.getEffectiveAvatarUrl())
                        .setDescription(out.toString())
                        .setFooter(context.time(), null);

            context.channel.sendMessage(embed.build()).queue();
        }, 0, TimeUnit.SECONDS);

        Thread eval = new Thread("Eval") {
            @Override
            public void run() {
                try {
                    future.get(10, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    future.cancel(true);
                    context.channel.sendMessage("Task has timed out.").queue();
                } catch (Exception ex) {
                    embed.setAuthor("Eval Requested By " + context.user.getName(), null, context.user.getEffectiveAvatarUrl())
                            .setDescription("`" + ex.getMessage() + "`");

                    context.channel.sendMessage(embed.build()).queue();
                }
            }
        };

        eval.start();
    }
}
