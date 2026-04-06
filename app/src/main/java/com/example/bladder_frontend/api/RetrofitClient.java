package com.example.bladder_frontend.api;

import android.content.Context;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class RetrofitClient {
    private static final String DEFAULT_BASE_URL = "https://f935p3xr-8000.inc1.devtunnels.ms/";
    private static Retrofit retrofit = null;

    public static String getBaseUrl(Context context) {
        SessionManager sessionManager = new SessionManager(context);
        String savedUrl = sessionManager.getServerUrl();
        return (savedUrl != null) ? savedUrl : DEFAULT_BASE_URL;
    }

    public static void resetRetrofit() {
        retrofit = null;
    }

    public static BladSenseApi getApi(Context context) {
        String baseUrl = getBaseUrl(context);
        if (retrofit == null) {
            final SessionManager sessionManager = new SessionManager(context);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder requestBuilder = original.newBuilder();

                            String token = sessionManager.fetchAuthToken();
                            if (token != null) {
                                requestBuilder.header("Authorization", "Bearer " + token);
                            }

                            // Bypass Microsoft Dev Tunnel anti-phishing page
                            requestBuilder.header("X-Tunnel-Skip-AntiPhishing-Page", "true");
                            requestBuilder.header("X-Skip-Anti-Phishing-Page", "true");

                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    })
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(BladSenseApi.class);
    }
}
