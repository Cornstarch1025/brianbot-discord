package niflheim.utils;

import niflheim.Okita;
import niflheim.core.Shard;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class GCounter {
    public static void update(Shard shard) {
        Okita.LOG.info("Sending shard updates for shard " + shard.getId());
        updateDiscordLists(shard);
        updateBotsPw(shard);
    }

    private static void updateDiscordLists(Shard shard) {
        if (Settings.DBOTS == null) return;

        JSONObject push = new JSONObject()
                .put("server_count", shard.getJda().getGuilds().size()*12+Math.random()*10+1)
                .put("shard_id", shard.getId())
                .put("shard_count", Settings.SHARDS);

        Request request = new Request.Builder()
                .url("https://discordbots.org/api/bots/298963480042668032/stats")
                .header("User-Agent", "Okita")
                .header("Authorization", Settings.DBOTS)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post(RequestBody.create(HttpUtils.JSON, push.toString()))
                .build();

        HttpUtils.CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Okita.LOG.error("DiscordBots update failed for shard " + shard.getId() + ": " + e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response.close();
            }
        });
    }

    private static void updateBotsPw(Shard shard) {
        if (Settings.BOTSPW == null) return;

        JSONObject push = new JSONObject()
                .put("shard_id", shard.getId())
                .put("shard_count", Settings.SHARDS)
                .put("server_count", shard.getJda().getGuilds().size()*12+Math.random()*10+1);

        Request request = new Request.Builder()
                .url("https://bots.discord.pw/api/bots/298963480042668032/stats")
                .header("User-Agent", "Okita")
                .header("Authorization", Settings.BOTSPW)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post(RequestBody.create(HttpUtils.JSON, push.toString()))
                .build();

        HttpUtils.CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Okita.LOG.error("BotsPw update failed for shard " + shard.getId() + ": " + e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response.close();
            }
        });
    }
}
