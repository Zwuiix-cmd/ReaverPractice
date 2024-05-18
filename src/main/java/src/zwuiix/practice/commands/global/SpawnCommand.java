package src.zwuiix.practice.commands.global;

import src.zwuiix.practice.manager.list.CooldownManager.Cooldown;
import src.zwuiix.practice.manager.list.CooldownManager.CooldownManager;
import src.zwuiix.practice.network.commands.BaseCommand;
import src.zwuiix.practice.session.ReaverSession;
import src.zwuiix.practice.utils.Cooldowns;
import src.zwuiix.practice.utils.TextUtil;

public class SpawnCommand extends BaseCommand {
    public SpawnCommand() {
        super("spawn", "", "", new String[]{"hub"});
    }

    @Override
    public void prepare() {
        setOnlyPlayer(true);
    }

    @Override
    public void onSessionRun(ReaverSession session, String[] args) {
        Cooldown combatTag = CooldownManager.getInstance().getCooldown(session.getName(), Cooldowns.COMBAT_LOGGER.name(), session);
        if(combatTag.isInCooldown()) {
            session.sendMessage(TextUtil.PREFIX + "§cYou can't do this in combat.");
            return;
        }

        session.spawn();
    }
}
