package niflheim.commands;

import net.dv8tion.jda.core.Permission;
import niflheim.core.Context;

public enum Scope {
    GUILD {
        @Override
        public boolean checkPermission(Context context, Permission... permissions) {
            return context.member.hasPermission(permissions);
        }
    },
    TEXT {
        @Override
        public boolean checkPermission(Context context, Permission... permissions) {
            return context.member.hasPermission(context.channel, permissions);
        }
    },
    VOICE {
        @Override
        public boolean checkPermission(Context context, Permission... permissions) {
            return context.member.hasPermission(context.member.getVoiceState().getChannel(), permissions);
        }
    };

    public abstract boolean checkPermission(Context context, Permission... permissions);
}
