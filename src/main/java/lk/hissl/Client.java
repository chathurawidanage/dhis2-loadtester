package lk.hissl;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chathura Widanage
 */
public class Client {

    private int client;
    private int count = 0;
    private AtomicInteger done = new AtomicInteger(0);

    public Client(int clinet) {
        this.client = clinet;
    }

    public void start(List<String> urls) {
        this.count = urls.size();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (final String url : urls) {
            executorService.execute(new Runnable() {
                public void run() {
                    HttpClient httpClint = HttpClientBuilder.create().build();
                    HttpGet httpGet = new HttpGet(url);
                    httpGet.setHeader("Authorization", Statics.authorization);
                    try {
                        HttpResponse httpResponse = httpClint.execute(httpGet);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            //System.out.println();
                            System.out.println(String.format("[INFO] Client [%d] : Request successful [%d of %d]", client, done.incrementAndGet(), count));
                            //System.out.println(readInputStream(httpResponse.getEntity().getContent()));
                            //System.out.println("---------------");
                            //System.out.println();
                        } else {
                            System.out.println(String.format("[ERROR] Client [%d] : Request Failed [%d of %d]", client, done.incrementAndGet(), count));
                        }
                    } catch (IOException e) {
                        System.out.println(String.format("[ERROR] Client [%d] : Request failed [%d of %d]", client, done.incrementAndGet(), count));
                    }
                }
            });
        }
        executorService.shutdown();
    }

    public static String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String body = "";
        String line = null;
        while ((line = br.readLine()) != null) {
            body += line;
        }
        return body;
    }
}
