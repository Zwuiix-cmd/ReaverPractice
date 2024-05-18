package src.zwuiix.practice.manager.list.CooldownManager;

import src.zwuiix.practice.session.ReaverSession;

public class Cooldown {
    protected String name;
    protected ReaverSession session;
    protected int cooldown = 0;

    public Cooldown(String name, ReaverSession session)
    {
        this.name = name;
        this.session = session;
    }

    public Cooldown(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCooldown(boolean status, boolean notify, String message, int time)
    {
        if(status) {
            this.cooldown = time;
            if(notify && session != null) session.sendActionBar(message);
            return;
        }

        this.cooldown = 0;
        if(notify && session != null) session.sendActionBar(message);
    }

    public void setCooldown(boolean status, boolean notify, int time)
    {
        setCooldown(status, notify, status ? "§aCooldown started" : "§aCooldown Ended", time);
    }

    public boolean isInCooldown()
    {
        return this.cooldown >= 1;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void reduce() {
        this.cooldown--;
    }
}
