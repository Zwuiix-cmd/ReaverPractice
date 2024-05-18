package src.zwuiix.practice.tasks;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import src.zwuiix.practice.Loader;
import src.zwuiix.practice.manager.list.CooldownManager.Cooldown;
import src.zwuiix.practice.manager.list.CooldownManager.CooldownManager;
import src.zwuiix.practice.session.ReaverSession;
import src.zwuiix.practice.utils.Cooldowns;

import java.util.HashMap;

public class CooldownTask extends Task {
    public CooldownTask()
    {
        Loader.getInstance().getServer().getScheduler().scheduleRepeatingTask(this, 1);
    }

    @Override
    public void onRun(int i) {
        Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
            ReaverSession session = (ReaverSession) player;
            HashMap<String, Cooldown> cooldowns = CooldownManager.getInstance().getCooldowns(player.getName());
            cooldowns.forEach((s, cooldown) -> {
                if(cooldown.isInCooldown()) {
                    if(cooldown.getName().equals(Cooldowns.ENDER_PEARL.name())) {
                        int remainingTicks = cooldown.getCooldown();
                        session.sendExperienceLevel(remainingTicks / 20);
                        session.sendCustomExperience(((float) remainingTicks / 600) * 15);
                    }

                    cooldown.reduce();
                    if(cooldown.getCooldown() < 1) {
                        if(cooldown.getName().equals(Cooldowns.ENDER_PEARL.name())) {
                            session.setExperience(0, 0);
                        }

                        cooldown.setCooldown(false, true, 0);
                    }
                }
            });
        });
    }
}
