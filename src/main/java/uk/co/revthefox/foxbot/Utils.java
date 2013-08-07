package uk.co.revthefox.foxbot;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import org.pircbotx.Colors;
import org.pircbotx.User;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{

    private FoxBot foxbot;

    public Utils(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public String parseChatUrl(String stringToParse, User sender)
    {
        try
        {
            AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setUserAgent("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36").build();
            AsyncHttpClient client = new AsyncHttpClient(cf);
            Future<Response> future = client.prepareGet(stringToParse).setFollowRedirects(true).execute();
            Response response = future.get();
            String output = response.getResponseBody("UTF-8");
            URLConnection conn = new URL(stringToParse).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36");
            String size = (conn.getContentLengthLong() / 1024) + "kb";
            String content_type = conn.getContentType().contains(";") ? conn.getContentType().split(";")[0] : conn.getContentType();
            
            if (response.getStatusCode() != 200 && response.getStatusCode() != 302 && response.getStatusCode() != 301)
            {
                return String.format("(%s's URL) %sError: %s%s %s ", sender.getNick(), Colors.RED, Colors.NORMAL, response.getStatusCode(), response.getStatusText());
            }
            if (!content_type.contains("html"))
            {
                return "(" + sender.getNick() + "'s URL)" + Colors.GREEN + "Content Type: " + Colors.NORMAL + content_type + Colors.GREEN + " Size:" + Colors.NORMAL + (conn.getContentLengthLong() / 1024) + "kb";
            }
            
            Pattern p = Pattern.compile("<title>.+</title>");
            Matcher m;
            String title = "Error?";
            for (String line : output.split("\n"))
            {
                m = p.matcher(line);
                if (m.find())
                {
                    title = line.split("<title>")[1].split("</title>")[0];
                }
            }
            return String.format("(%s's URL) " + Colors.GREEN + "Title: " + Colors.NORMAL + "%s " + Colors.GREEN + "Content type: " + Colors.NORMAL + "%s " + Colors.GREEN + "Size: " + Colors.NORMAL + "%s", sender.getNick(), title, content_type, size);
        } catch (Exception ex)
        {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "null";
    }
}
