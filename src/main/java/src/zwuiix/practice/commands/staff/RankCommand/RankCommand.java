package src.zwuiix.practice.commands.staff.RankCommand;

import src.zwuiix.practice.network.commands.BaseCommand;
import src.zwuiix.practice.utils.Permissions;

public class RankCommand extends BaseCommand {
    public RankCommand() {
        super("rank", "", "", new String[]{});
    }

    @Override
    public void prepare() {
        setPermission(Permissions.RANK.name());
        this.registerSubCommand(new RankSetCommand("set"));
    }
}
