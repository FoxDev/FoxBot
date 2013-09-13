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

        if (args.length == 1 || args.length == 2)
        {
            try
            {
                host = args[0];
                port = args.length == 2 ? Integer.parseInt(args[1]) : port;
                String returns;
                long start = System.currentTimeMillis();
                Socket socket = new Socket(InetAddress.getByName(host), port);
                long end = System.currentTimeMillis();
                
                socket.close();
                channel.sendMessage(foxbot.getUtils().colourise(String.format("&aPing response time: &r%sms", end - start)));
            } 
            catch (Exception ex)
            {
                if (ex instanceof UnknownHostException)
                {
                    foxbot.sendNotice(sender, String.format("%s is an unknown address!", args[0]));
                }
                else if (ex instanceof NumberFormatException)
                {
                    foxbot.sendNotice(sender, String.format("%s isn't a number!", args[1]));
                }
                else if (ex instanceof IllegalArgumentException)
                {
                    foxbot.sendNotice(sender, String.format("%s is too high a number for a port!", args[1]));
                }
                else
                {
                    foxbot.sendNotice(sender, foxbot.getUtils().colourise(String.format("&c%s", error)));
                }
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %sping <address> [port]", foxbot.getConfig().getCommandPrefix()));
    }
}
//
