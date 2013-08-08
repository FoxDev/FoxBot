package uk.co.revthefox.foxbot.commands;

import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CommandUptime extends Command
{
    private FoxBot foxbot;

    public CommandUptime(FoxBot foxbot)
    {
        super("uptime", "command.uptime");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(User sender, Channel channel, String[] args)
    {       
            if(System.getProperty("os.name").toLowerCase().contains("win")){
                foxbot.getBot().sendNotice(sender, "This command is only supported on linux based systems.");
                return;
            }
            try
            {
                String uptime = new Scanner(new FileInputStream("/proc/uptime")).next().replaceAll("\\.[0-9]+", "");

                int unixTime = Integer.valueOf(uptime);
                int day = (int) TimeUnit.SECONDS.toDays(unixTime);
                long hours = TimeUnit.SECONDS.toHours(unixTime) - (day * 24);
                long minute = TimeUnit.SECONDS.toMinutes(unixTime) - (TimeUnit.SECONDS.toHours(unixTime) * 60);
                long seconds = TimeUnit.SECONDS.toSeconds(unixTime) - (TimeUnit.SECONDS.toMinutes(unixTime) * 60);
                channel.sendMessage(String.format("%sSystem uptime: %s%s days %s hours %s minutes %s seconds",
                        Colors.GREEN, Colors.NORMAL, day, hours, minute, seconds));
                return;
            }
            catch (FileNotFoundException ex)
            {
                foxbot.getBot().sendNotice(sender, "File \"/proc/uptime\" not found. Are you sure you're using Linux?");
                return;
            }
    }
}
