package src.zwuiix.practice.utils;

import cn.nukkit.Server;

public class TextUtil {
    public static String PREFIX = "§8[§cReaver§8] §f";

    public static void broadcastTip(String message)
    {
        Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
            player.sendTip(message);
        });
    }

    public static void broadcastActionBar(String message)
    {
        Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
            player.sendActionBar(message);
        });
    }
}
