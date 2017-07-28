package niflheim.rethink;

import com.sun.istack.internal.NotNull;
import niflheim.Okita;
import niflheim.utils.Settings;

import javax.annotation.Nullable;
import java.beans.ConstructorProperties;

public class GuildOptions implements ManagedObject {
    private final String id;
    private String prefix;
    private String autorole;
    private String welcome;
    private String goodbye;
    private String messageChannel;

    private boolean welcomeEnable = false;
    private boolean goodbyeEnable = false;

    @ConstructorProperties("id")
    public GuildOptions(String id) {
        this.id = id;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getPrefix() {
        if (prefix == null)
            return Settings.PREFIX;

        return prefix;
    }

    @Nullable
    public String getAutorole() {
        return autorole;
    }

    @Nullable
    public String getWelcome() {
        return welcome;
    }

    @Nullable
    public String getGoodbye() {
        return goodbye;
    }

    @Nullable
    public String getMessageChannel() {
        return messageChannel;
    }

    public boolean isWelcomeEnable() {
        return welcomeEnable;
    }

    public boolean isGoodbyeEnable() {
        return goodbyeEnable;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setAutorole(String autorole) {
        this.autorole = autorole;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public void setGoodbye(String goodbye) {
        this.goodbye = goodbye;
    }

    public void setMessageChannel(String messageChannel) {
        this.messageChannel = messageChannel;
    }

    public void setWelcomeEnable(boolean welcomeEnable) {
        this.welcomeEnable = welcomeEnable;
    }

    public void setGoodbyeEnable(boolean goodbyeEnable) {
        this.goodbyeEnable = goodbyeEnable;
    }

    public void save() {
        Okita.DATABASE.saveGuildOptions(this);
    }

    public void delete() {
        Okita.DATABASE.deleteGuildOptions(id);
    }
}
