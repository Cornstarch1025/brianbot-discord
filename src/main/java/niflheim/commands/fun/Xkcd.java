package niflheim.commands.fun;

import niflheim.commands.Category;
import niflheim.commands.Command;
import niflheim.commands.CommandFrame;
import niflheim.commands.Scope;
import niflheim.core.Context;
import niflheim.utils.HttpUtils;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Random;

@CommandFrame(
        help = "Sends a random Xkcd comic.",
        usage = ".xkcd",
        cooldown = 3000L,
        category = Category.FUN,
        scope = Scope.TEXT
)
public class Xkcd extends Command {
    public void execute(Context context, String[] args) {
        if (args.length != 0) {
            context.invalid(this);
            return;
        }

        Request comic = new Request.Builder()
                .url("http://xkcd.com/info.0.json")
                .build();

        HttpUtils.CLIENT.newCall(comic).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.channel.sendMessage("Failed to grab xkcd comic!").queue();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();

                if (responseBody == null)
                    return;

                JSONObject latest = new JSONObject(new JSONTokener(responseBody.byteStream()));

                int max = latest.getInt("num");
                int rand = 500 + new Random().nextInt(max - 500);

                Request request = new Request.Builder()
                        .url("http://xkcd.com/" + rand + "/info.0.json")
                        .build();

                HttpUtils.CLIENT.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                        context.channel.sendMessage("Failed to grab xkcd comic!").queue();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody responseBody1 = response.body();

                        if (responseBody1 == null)
                            return;

                        JSONObject comic = new JSONObject(new JSONTokener(responseBody1.byteStream()));

                        context.channel.sendMessage(comic.getString("img".replaceAll("\\\\/", "/"))).queue();
                    }
                });
            }
        });
    }
}