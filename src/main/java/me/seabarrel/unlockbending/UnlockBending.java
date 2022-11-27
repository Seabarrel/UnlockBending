package me.seabarrel.unlockbending;

import com.projectkorra.projectkorra.ability.Ability;
import me.seabarrel.unlockbending.command.ScrollCommand;
import me.seabarrel.unlockbending.configuration.ConfigManager;
import me.seabarrel.unlockbending.data.LearnData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class UnlockBending extends JavaPlugin {

    private static UnlockBending plugin;
    private LearnData learnData;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        new ConfigManager();
        new ScrollCommand();
        learnData = new LearnData(this);
        getServer().getPluginManager().registerEvents(new MainListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static UnlockBending getPlugin() {
        return plugin;
    }

    public LearnData getLearnData() {
        return learnData;
    }

    public static boolean canBend(Ability ability) {
       return canBend(ability.getName(), ability.getPlayer());
    }

    public static boolean canBend(String ability, Player player) {
        for (String abil : ConfigManager.getConfig().getStringList("Unlocked.Default")) {
            if (ability.equalsIgnoreCase(abil)) {
                return true;
            }
        }
        if (player.isOp() && ConfigManager.getConfig().getBoolean("Permissions.BypassUnlockWhenOP")) {
            return true;
        }
        System.out.println("DE");
        if (!UnlockBending.getPlugin().getLearnData().learnAbility(player, ability)) {
            System.out.println("BUG");
            player.sendMessage(ChatColor.RED + "You did not unlock this move yet!");
            return false;
        }
        System.out.println("GIN");
        long learnt = UnlockBending.getPlugin().getLearnData().getAbility(player, ability);
        if (System.currentTimeMillis() > learnt) {
            return true;
        } else {
            Duration durationLeft = Duration.ofMillis(learnt - System.currentTimeMillis());
            String days = String.valueOf(durationLeft.toDays());
            String hours = String.valueOf(durationLeft.toHoursPart());
            String minutes = String.valueOf(durationLeft.toMinutesPart());
            String seconds = String.valueOf(durationLeft.toSecondsPart());
            player.sendMessage(ChatColor.RED + "You are still learning this move! Time left: " + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds!");
            return false;
        }
    }
}
