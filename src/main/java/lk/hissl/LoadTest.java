package lk.hissl;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chathura Widanage
 */
public class LoadTest {
    public static void main(String[] args) {
        int clients = 1;
        if (args.length > 0) {
            clients = Integer.parseInt(args[0]);
        }

        System.out.println("[INFO] Starting test with " + clients + " clients.");
        JSONArray jsonArray = new JSONArray(Statics.json);
        List<String> urlList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String u = jsonArray.get(i).toString();
            if (u.contains("/api/") && !u.contains("/api/apps") && u.contains(Statics.targetDomain)) {
                urlList.add(u);
            }
        }
        for (int i = 0; i < clients; i++) {
            Client client = new Client(i);
            client.start(urlList);
        }
    }
}
