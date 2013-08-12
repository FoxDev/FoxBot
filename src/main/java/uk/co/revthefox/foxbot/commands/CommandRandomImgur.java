package uk.co.revthefox.foxbot.commands;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.util.Random;
import java.util.concurrent.Future;
import java.util.regex.Matcher;

public class CommandRandomImgur extends Command
{
    private FoxBot foxbot;

    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public CommandRandomImgur(FoxBot foxbot)
    {
        super("imgur", "command.imgur");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {
        if (args.length == 0)
        {
            Future<Response> future;
            Response response;

            Matcher matcher;

            channel.sendMessage(String.format("(%s) Generating a working Imgur link...", foxbot.getUtils().munge(sender.getNick())));

            String imgurLink = new Random().nextBoolean() ? String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(5)) : String.format("http://imgur.com/gallery/%s", RandomStringUtils.randomAlphanumeric(7));

            try
            {
                future = asyncHttpClient.prepareGet(imgurLink).execute();
                response = future.get();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                foxbot.getBot().sendMessage(channel, "Something went wrong...");
                return;
            }

            if (!String.valueOf(response.getStatusCode()).contains("404"))
            {
                channel.sendMessage(new Random().nextBoolean() ? String.format("(%s) %sRandom Imgur: %s%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, imgurLink) : String.format("(%s) %sRandom Imgur: %s%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, imgurLink));
                return;
            }

            this.execute(sender, channel, args);

            //channel.sendMessage(new Random().nextBoolean() ? String.format("(%s) %sRandom Imgur: %shttp://imgur.com/gallery/%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, RandomStringUtils.randomAlphanumeric(5)) : String.format("(%s) %sRandom Imgur: %shttp://imgur.com/gallery/%s", foxbot.getUtils().munge(sender.getNick()), Colors.GREEN, Colors.NORMAL, RandomStringUtils.randomAlphanumeric(7)));
        }
    }
}
