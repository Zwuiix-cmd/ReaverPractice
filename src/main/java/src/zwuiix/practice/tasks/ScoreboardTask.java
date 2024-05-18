package src.zwuiix.practice.tasks;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import src.zwuiix.practice.Loader;
import src.zwuiix.practice.session.ReaverSession;

public class ScoreboardTask extends Task {
    public ScoreboardTask()
    {
        Loader.getInstance().getServer().getScheduler().scheduleRepeatingTask(this, 1);
    }
    @Override
    public void onRun(int currentTick) {
        Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
            ReaverSession session = (ReaverSession) player;
            //session.getScoreboard().send();
        });
    }
}
