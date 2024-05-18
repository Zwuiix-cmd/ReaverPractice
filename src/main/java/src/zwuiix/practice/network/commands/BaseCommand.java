package src.zwuiix.practice.network.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import src.zwuiix.practice.network.commands.arguments.Argument;
import src.zwuiix.practice.network.commands.arguments.EnumArgument;
import src.zwuiix.practice.session.ReaverSession;
import src.zwuiix.practice.utils.TextUtil;

import java.util.*;

abstract public class BaseCommand extends Command {
    protected boolean onlyPlayer = false;
    protected LinkedHashMap<Integer, Argument> arguments = new LinkedHashMap<>();
    protected LinkedHashMap<String, SubCommand> subCommands = new LinkedHashMap<>();

    public BaseCommand(String name, String description, String usageMessage, String[] aliases) {
        super(name, description, usageMessage, aliases);
        this.prepare();
        this.buildCommandOverloads();
    }

    public void buildCommandOverloads() {
        for (Map.Entry<String, SubCommand> entry : getSubCommands().entrySet()) {
            String name = entry.getKey();
            SubCommand subCommand = entry.getValue();

            List<CommandParameter> paramDataList = new ArrayList<>();
            CommandParameter commandParam = new CommandParameter(subCommand.name, true, new String[]{subCommand.name});
            paramDataList.add(commandParam);

            for (Map.Entry<Integer, Argument> entry2 : subCommand.getArguments().entrySet()) {
                Argument argument = entry2.getValue();
                CommandParameter commandParameter = new CommandParameter(argument.getName(), argument.getCommandParamType(), argument.isOptional());
                if (argument instanceof EnumArgument) {
                    commandParameter.enumData = new CommandEnum(subCommand.name + "Enums", Arrays.asList(((EnumArgument) argument).getEnumValues()));
                }
                paramDataList.add(commandParameter);
            }

            this.commandParameters.put(subCommand.getName(), paramDataList.toArray(new CommandParameter[]{}));
        }

        for (Map.Entry<Integer, Argument> entry : getArguments().entrySet()) {
            Argument argument = entry.getValue();

            CommandParameter commandParameter = new CommandParameter(argument.getName(), argument.getCommandParamType(), argument.isOptional());
            if(argument instanceof EnumArgument) {
                commandParameter.enumData = new CommandEnum(argument.getName() + "Enums", Arrays.asList(((EnumArgument) argument).getEnumValues()));
            }
            this.commandParameters.put(argument.getName(), new CommandParameter[]{commandParameter});
        }
    }

    abstract public void prepare();

    public boolean isOnlyPlayer() {
        return onlyPlayer;
    }

    public LinkedHashMap<String, SubCommand> getSubCommands() {
        return subCommands;
    }

    public void registerSubCommand(SubCommand subCommand)
    {
        this.subCommands.put(subCommand.name, subCommand);
    }

    public void setOnlyPlayer(boolean onlyPlayer) {
        this.onlyPlayer = onlyPlayer;
    }

    public void registerArgument(int number, Argument argument)
    {
        this.arguments.put(number, argument);
    }

    public LinkedHashMap<Integer, Argument> getArguments() {
        return arguments;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(getPermission() != null) {
            if(!commandSender.hasPermission(getPermission())) {
                commandSender.sendMessage(TextUtil.PREFIX + "§cYou are not allowed to use this command.");
                return true;
            }
        }

        if(getSubCommands().size() > 0) {
            if(strings.length > 1) {
                for (Map.Entry<String, SubCommand> entry : getSubCommands().entrySet()) {
                    String name = entry.getKey();
                    SubCommand subCommand = entry.getValue();
                    if(strings[0] != null && strings[0].equalsIgnoreCase(name)) {
                        String[] args = new String[strings.length - 1];
                        System.arraycopy(strings, 1, args, 0, strings.length - 1);

                        if(subCommand.getPermission() != null) {
                            if(!commandSender.hasPermission(subCommand.getPermission())) {
                                commandSender.sendMessage(TextUtil.PREFIX + "§cYou are not allowed to use this command.");
                                return true;
                            }
                        }

                        if(subCommand.isOnlyPlayer()) {
                            if(!commandSender.isPlayer()) {
                                commandSender.sendMessage(TextUtil.PREFIX + "§cYou cannot use this in the console.");
                                return true;
                            }

                            subCommand.onSessionRun((ReaverSession) commandSender, args);
                            return true;
                        }

                        subCommand.onRun(commandSender, args);
                        return true;
                    }
                }
            }
        }

        if(getArguments().size() > 0) {
            for (Map.Entry<Integer, Argument> entry : getArguments().entrySet()) {
                int index = entry.getKey();
                Argument argument = entry.getValue();
                if (!argument.isOptional() && (strings.length <= index || strings[index] == null)) {
                    commandSender.sendMessage(TextUtil.PREFIX + "§cYou've forgotten a few arguments.");
                    return true;
                }
            }

            if(this.isOnlyPlayer()) {
                if(!commandSender.isPlayer()) {
                    commandSender.sendMessage(TextUtil.PREFIX + "§cYou cannot use this in the console.");
                    return  true;
                }

                this.onSessionRun((ReaverSession) commandSender, strings);
                return true;
            }

            this.onRun(commandSender, strings);
            return true;
        }

        if(this.isOnlyPlayer()) {
            if(!commandSender.isPlayer()) {
                commandSender.sendMessage(TextUtil.PREFIX + "§cYou cannot use this in the console.");
                return  true;
            }

            this.onSessionRun((ReaverSession) commandSender, strings);
            return true;
        }

        this.onRun(commandSender, strings);
        return false;
    }

    public void onRun(CommandSender commandSender, String[] args)
    {
    }

    public void onSessionRun(ReaverSession session, String[] args)
    {
    }
}
