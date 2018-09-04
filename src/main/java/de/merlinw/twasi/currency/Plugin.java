package de.merlinw.twasi.currency;

import de.merlinw.twasi.currency.database.CurrencyService;
import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.services.ServiceRegistry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Plugin extends TwasiPlugin {
    @Override
    public void onActivate() {
        ServiceRegistry.register(new CurrencyService());
    }

    @Override
    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return UserPlugin.class;
    }

    public static String getApiContent(String url) throws IOException {
        return getApiContent(new HttpGet(url));
    }

    public static String getApiContent(HttpRequestBase request) throws IOException {
        int timeout = 3;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
