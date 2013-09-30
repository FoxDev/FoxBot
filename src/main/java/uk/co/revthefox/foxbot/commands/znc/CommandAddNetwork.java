/*
 * This file is part of Foxbot.
 *
 *     Foxbot is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foxbot is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foxbot. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.co.revthefox.foxbot.commands.znc;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import uk.co.revthefox.foxbot.FoxBot;
import uk.co.revthefox.foxbot.commands.Command;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAddNetwork extends Command
{
    public FoxBot foxbot;

    public CommandAddNetwork(FoxBot foxbot)
    {
        super("zncaddnetwork", "command.znc.addnetwork");
        this.foxbot = foxbot;
    }

    @Override
    public void execute(final MessageEvent event, final String[] args)
    {
        User sender = event.getUser();

        if (args.length == 2)
        {
            String user = args[0];
            String network = args[1];
            if (args[1].equalsIgnoreCase("Esper"))
            {
                foxbot.sendMessage("*controlpanel", String.format("addnetwork %s Esper", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper irc.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper availo.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper portlane.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper chaos.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper nova.esper.net +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Esper optical.esper.net +6697", user));
            }
            else if (args[1].equalsIgnoreCase("Seion"))
            {
                foxbot.sendMessage("*controlpanel", String.format("addnetwork %s Seion", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Seion irc.ipv6.seion.us +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Seion malice.seion.us +6697", user));
                foxbot.sendMessage("*controlpanel", String.format("addserver %s Seion fox.seion.us +6697", user));
            }
            foxbot.sendMessage(String.format("?%s", user), String.format("The network: %s has been added to your account! Thank you for using SeionBNC", network));

            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        foxbot.sendNotice(sender, String.format("Wrong number of args! Use %szncaddnetwork <name> <bindhost>", foxbot.getConfig().getCommandPrefix()));
    }
}
