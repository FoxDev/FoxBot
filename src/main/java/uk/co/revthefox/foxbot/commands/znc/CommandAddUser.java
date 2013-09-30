package uk.co.revthefox.foxbot.commands.znc;

import org.apache.commons.lang3.RandomStringUtils;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.commands.Command;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddUser extends Command
{
    public FoxBot foxbot;

    public CommandAddUser(FoxBot foxbot)
    {
        super("zncadduser", "command.znc.adduser");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 3)
        {
            String user = args[0];
            String password = RandomStringUtils.randomAlphanumeric(6);
            String bindhost = args[1];
            String network = args[2];

            // ---------------
            // Basic user info
            // ---------------

            foxbot.sendMessage("*controlpanel", String.format("adduser %s %s", user, password));
            foxbot.sendMessage("*controlpanel", String.format("set nick %s %s", user, user + ""));
            foxbot.sendMessage("*controlpanel", String.format("set altnick %s %s", user, user + "|bnc"));
            foxbot.sendMessage("*controlpanel", String.format("set ident %s %s", user, user.toLowerCase()));
            foxbot.sendMessage("*controlpanel", String.format("set bindhost %s %s", user, bindhost));
            foxbot.sendMessage("*controlpanel", String.format("set quitmsg %s Leaving", user));
            foxbot.sendMessage("*controlpanel", String.format("set buffercount %s 1000", user));
            foxbot.sendMessage("*controlpanel", String.format("set denysetbindhost %s true", user));
            foxbot.sendMessage("*controlpanel", String.format("set prependtimestamp %s true", user));

            // -----------
            // Add servers
            // -----------
            if (args[2].equalsIgnoreCase("Esper"))
            {
                foxbot.sendMessage("*controlpanel", String.format("addnetwork %s Esper", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper irc.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper availo.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper portlane.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper chaos.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper nova.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper optical.esper.net +6697", user));
            }
            else if (args[2].equalsIgnoreCase("Seion"))
            {
                foxbot.sendMessage("*controlpanel", String.format("addnetwork %s Seion", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Seion irc.ipv6.seion.us +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Seion malice.seion.us +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Seion fox.seion.us +6697", user));
            }
            // ------------
            // Load modules
            // ------------

            foxbot.sendMessage("*controlpanel", String.format("loadmodule %s clientnotify", user));
            foxbot.sendMessage("*controlpanel", String.format("loadmodule %s chansaver", user));
            foxbot.sendMessage("*controlpanel", String.format("loadmodule %s controlpanel", user));

            // ---------------------------------------
            // Send ZNC information to the adding user
            // ---------------------------------------

            foxbot.sendNotice(sender, String.format("User added! Send this info to the user - Username: %s - Password: %s",user, password));

            // -----------
            // Add channel
            // -----------

            // Give the account chance to connect
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
            foxbot.sendMessage("*send_raw", String.format("server %s %s JOIN %s", user, network, event.getChannel().getName()));
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %szncadduser <name> <bindhost> <network>", foxbot.getConfig().getCommandPrefix()));
    }
}
