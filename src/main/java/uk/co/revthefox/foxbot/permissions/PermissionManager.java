package uk.co.revthefox.foxbot.permissions;

import com.typesafe.config.ConfigException;
import org.pircbotx.User;
import uk.co.revthefox.foxbot.FoxBot;

public class PermissionManager
{
    private FoxBot foxbot;

    public PermissionManager(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public Boolean userHasPermission(User user, String permission)
    {
        String userName = user.getNick();

        if (foxbot.getConfig().getUsersMustBeVerified() && (!user.isVerified()))
        {
            foxbot.getBot().sendNotice(user, "You must be logged into nickserv to use bot commands.");
            return false;
        }
        try
        {
            if (foxbot.getPermissionsFile().getStringList("permissions." + userName).contains("-" + permission))
            {
                return false;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission)|| foxbot.getPermissionsFile().getStringList("permissions.default").contains("permissions.*"))
            {
                return true;
            }
            if (foxbot.getPermissionsFile().getStringList("permissions." + userName).contains(permission) || foxbot.getPermissionsFile().getStringList("permissions." + userName).contains("permissions.*"))
            {
                return true;
            }
        }
        catch (ConfigException ex)
        {
            if (foxbot.getPermissionsFile().getStringList("permissions.default").contains(permission))
            {
                return true;
            }
        }
        return false;
    }
}
