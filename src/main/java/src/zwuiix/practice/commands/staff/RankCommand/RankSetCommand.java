package src.zwuiix.practice.commands.staff.RankCommand;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import src.zwuiix.practice.manager.list.RankManager.Rank;
import src.zwuiix.practice.manager.list.RankManager.RankManager;
import src.zwuiix.practice.network.commands.SubCommand;
import src.zwuiix.practice.network.commands.arguments.EnumArgument;
import src.zwuiix.practice.network.commands.arguments.TargetArgument;
import src.zwuiix.practice.session.ReaverSession;
import src.zwuiix.practice.utils.TextUtil;

import java.util.ArrayList;

public class RankSetCommand extends SubCommand {
    public RankSetCommand(String name) {
        super(name);
    }

    @Override
    public void prepare() {
        ArrayList<String> rankNamesList = new ArrayList<>();
        RankManager.getInstance().getRanks().forEach((s, rank) -> rankNamesList.add(rank.getName()));

        registerArgument(0, new TargetArgument("target", false));
        registerArgument(1, new EnumArgument("rank", false, rankNamesList.toArray(new String[]{})));
    }

    @Override
    public void onRun(CommandSender commandSender, String[] args) {
        String target = args[0];
        Rank rank = RankManager.getInstance().getRankByName(args[1]);
        RankManager.getInstance().setPlayersRank(target, rank);

        ReaverSession session = (ReaverSession) Server.getInstance().getPlayerExact(target);
        if(session != null) session.sendMessage(TextUtil.PREFIX + "§aYou have received the rank §c" + rank.getName() + "§a.");
        commandSender.sendMessage(TextUtil.PREFIX + "§aYou have defined §c" + target + "§a's rank on §c" + rank.getName() + "§a.");
    }
}
