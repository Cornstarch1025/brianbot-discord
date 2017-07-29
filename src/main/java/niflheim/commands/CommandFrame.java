package niflheim.commands;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandFrame {
    String[] aliases() default {};

    String help() default "No help provided";

    String usage() default "";

    long cooldown() default 0L;

    int level() default 0;

    boolean guildOwner() default false;

    boolean admin() default false;

    boolean toggleable() default true;

    Category category() default Category.GENERAL;

    Scope scope() default Scope.GUILD;

    Permission[] permissions() default {};
}
