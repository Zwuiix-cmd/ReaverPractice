package src.zwuiix.practice.manager.list.CooldownManager;

import src.zwuiix.practice.manager.Manager;
import src.zwuiix.practice.session.ReaverSession;

import java.util.HashMap;

public class CooldownManager extends Manager {
    protected static CooldownManager instance;
    protected HashMap<String, HashMap<String, Cooldown>> cooldowns = new HashMap<>();

    public static CooldownManager getInstance() {
        return instance;
    }

    public CooldownManager() {
        super("CooldownManager");
        instance = this;
    }

    public Cooldown getCooldown(String target, String name, ReaverSession session)
    {
        HashMap<String, Cooldown> cooldowns = this.cooldowns.getOrDefault(target.toLowerCase(), new HashMap<>());
        Cooldown cooldown = cooldowns.get(name.toLowerCase());
        if(!cooldowns.containsKey(name.toLowerCase())) {
            cooldown = new Cooldown(name, session);
            setCooldown(target, cooldown);
        }

        return cooldown;
    }

    public HashMap<String, Cooldown> getCooldowns(String target)
    {
        return this.cooldowns.getOrDefault(target.toLowerCase(), new HashMap<>());
    }

    public void setCooldown(String target, Cooldown cooldown)
    {
        HashMap<String, Cooldown> cooldowns = this.cooldowns.getOrDefault(target.toLowerCase(), new HashMap<>());
        cooldowns.put(cooldown.getName().toLowerCase(), cooldown);
        this.cooldowns.put(target.toLowerCase(), cooldowns);
    }

    public HashMap<String, HashMap<String, Cooldown>> getCooldowns() {
        return cooldowns;
    }
}
