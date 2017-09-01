package niflheim.utils;

import okhttp3.*;

import javax.annotation.Nonnull;
import java.io.IOException;

public class HttpUtils {
    public static final OkHttpClient CLIENT = new OkHttpClient.Builder().build();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //public static final MediaType TEXT = MediaType.parse("text/plain; charset=utf-8");

    public static final Callback EMPTY_CALLBACK = new Callback() {
        @Override
        public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
            call.cancel();
        }

        @Override
        public void onResponse(@Nonnull Call call, @Nonnull Response response) throws IOException {
            response.close();
        }
    };
}