package src.zwuiix.practice.commands.global;

import cn.nukkit.Server;
import src.zwuiix.practice.network.commands.BaseCommand;
import src.zwuiix.practice.network.commands.arguments.TargetArgument;
import src.zwuiix.practice.session.ReaverSession;
import src.zwuiix.practice.utils.TextUtil;

public class LatencyCommand extends BaseCommand {
    public LatencyCommand() {
        super("latency", "", "", new String[]{"ping", "ms"});
    }

    @Override
    public void prepare() {
        setOnlyPlayer(true);
        registerArgument(0, new TargetArgument("target", true));
    }

    @Override
    public void onSessionRun(ReaverSession session, String[] args) {
        if(args.length >= 1) {
            ReaverSession target = (ReaverSession) Server.getInstance().getPlayerExact(args[0]);
            if(target == null) {
                session.sendMessage(TextUtil.PREFIX + "§cThe player isn't connected!");
                return;
            }

            session.sendMessage(TextUtil.PREFIX + "§c" + target.getName() + "§f currently has §c" + target.getPing() + "ms");
            return;
        }

        session.sendMessage(TextUtil.PREFIX + "§fYou currently have §c" + session.getPing() + "ms");
    }
}
