package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;

import java.net.InetAddress;
import java.net.Socket;

public class CommandPing extends Command
{

    private FoxBot foxbot;

    public CommandPing(FoxBot foxbot)
    {
        super("ping", "command.ping");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        Channel channel = event.getChannel();
        User sender = event.getUser();
        String host;
        int port = 80;
        if (args.length == 1)
        {
            try
            {
                host = args[0];
                String returns;
                Long time;
                Long start = System.currentTimeMillis();
                Socket socket = new Socket(InetAddress.getByName(host), port);
                socket.close();
                channel.sendMessage(foxbot.getUtils().colourise(String.format("&aPing response time: &r%sms", System.currentTimeMillis() - start)));
            } catch (Exception ex)
            {
                foxbot.sendNotice(sender, String.format("&c%s", ex.toString()));
            }
            return;
        }
        if (args.length == 2)
        {
            try
            {
                host = args[0];
                port = Integer.valueOf(args[1]);
                String returns;
                Long time;
                Long start = System.currentTimeMillis();
                Socket socket = new Socket(InetAddress.getByName(host), port);
                socket.close();
                channel.sendMessage(foxbot.getUtils().colourise(String.format("&aPing response time: &r%sms", System.currentTimeMillis() - start)));
            } catch (Exception ex)
            {
                String error = ex.toString();
                if (error.contains("java.net.UnknownHostException"))
                {
                    foxbot.sendNotice(sender, String.format("%s is an unknown address!", args[0]));
                    return;
                }
                if (error.contains("java.lang.NumberFormatException"))
                {
                    foxbot.sendNotice(sender, String.format("%s isn't a number!", args[1]));
                    return;
                }
                if (error.contains("java.lang.IllegalArgumentException"))
                {
                    foxbot.sendNotice(sender, String.format("%s is too high a number for a port!", args[1]));
                    return;
                }
                foxbot.sendNotice(sender, foxbot.getUtils().colourise(String.format("&c%s", error)));
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sping <address> [port]", foxbot.getConfig().getCommandPrefix()));
    }
}