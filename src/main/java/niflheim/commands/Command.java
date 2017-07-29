package niflheim.commands;

import niflheim.core.Context;

public abstract class Command {
    private final CommandFrame commandInfo = this.getClass().getAnnotation(CommandFrame.class);

    public CommandFrame getInfo() {
        return commandInfo;
    }

    public abstract void execute(Context context, String[] args);
}
