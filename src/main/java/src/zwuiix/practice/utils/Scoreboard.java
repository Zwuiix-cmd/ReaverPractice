package src.zwuiix.practice.utils;

import cn.nukkit.network.protocol.RemoveObjectivePacket;
import cn.nukkit.network.protocol.SetDisplayObjectivePacket;
import cn.nukkit.network.protocol.SetScorePacket;
import cn.nukkit.network.protocol.types.DisplaySlot;
import cn.nukkit.network.protocol.types.SortOrder;
import src.zwuiix.practice.manager.list.CooldownManager.Cooldown;
import src.zwuiix.practice.manager.list.CooldownManager.CooldownManager;
import src.zwuiix.practice.session.ReaverSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scoreboard {
    protected ReaverSession session;
    protected HashMap<Integer, SetScorePacket.ScoreInfo> lines = new HashMap<>();

    public Scoreboard(ReaverSession session)
    {
        this.session = session;
    }

    protected void sendScoreboard(String objectiveName, String displayName)
    {
        SetDisplayObjectivePacket packet = new SetDisplayObjectivePacket();
        packet.objectiveName = objectiveName;
        packet.displayName = displayName;
        packet.sortOrder = SortOrder.ASCENDING;
        packet.criteriaName = "dummy";
        packet.displaySlot = DisplaySlot.SIDEBAR;
        this.session.getNetworkSession().sendPacket(packet);
    }

    protected void removeScoreboard(String objectiveName)
    {
        RemoveObjectivePacket packet = new RemoveObjectivePacket();
        packet.objectiveName = objectiveName;
        this.session.getNetworkSession().sendPacket(packet);
    }

    public void sendSetScore(String objectiveName, int id, String line)
    {
        if(lines.containsKey(id)) {
            //removeSetScore(id);
        }

        SetScorePacket.ScoreInfo scoreInfo = new SetScorePacket.ScoreInfo(id, objectiveName, id, line);
        scoreInfo.entityId = session.getId();
        lines.put(id, scoreInfo);

        SetScorePacket packet = new SetScorePacket();
        packet.action = SetScorePacket.Action.SET;

        List<SetScorePacket.ScoreInfo> scoreInfos = new ArrayList<>();
        scoreInfos.add(scoreInfo);
        packet.infos = scoreInfos;
        this.session.getNetworkSession().sendPacket(packet);
    }

    protected void removeSetScore(int id)
    {
        lines.remove(id);
        SetScorePacket packet = new SetScorePacket();
        packet.action = SetScorePacket.Action.REMOVE;
        packet.infos = (List<SetScorePacket.ScoreInfo>) lines.get(id);
        this.session.getNetworkSession().sendPacket(packet);
    }

    protected void send(String title, List<String> lines)
    {
        int i = 0;
        for (String line : lines) {
            sendSetScore(session.getName(), i, line);
            i++;
        }

        sendScoreboard(session.getName(), title);
    }

    public void send()
    {
        Cooldown combatTag =  CooldownManager.getInstance().getCooldown(session.getName(), Cooldowns.COMBAT_LOGGER.name(), session);
        Cooldown enderpearl =  CooldownManager.getInstance().getCooldown(session.getName(), Cooldowns.ENDER_PEARL.name(), session);

        List<String> lines = new ArrayList<>();
        lines.add("§r§f§a");

        if(combatTag.isInCooldown()) {
            lines.add("§r§c| §fCombatTag: §c" + combatTag.getCooldown() / 20);
        }
        if(enderpearl.isInCooldown()) {
            lines.add("§r§c| §fEnderPearl: §c" + enderpearl.getCooldown() / 20);
        }

        lines.add("§r§f§b");
        send("§c§lReaver", lines);
    }
}
