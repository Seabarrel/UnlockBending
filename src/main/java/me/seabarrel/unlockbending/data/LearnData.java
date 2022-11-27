package me.seabarrel.unlockbending.data;

import me.seabarrel.unlockbending.UnlockBending;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LearnData extends AbstractFile {

    public LearnData(UnlockBending plugin) {
        super(plugin, "ranks.yml");
    }

    public void setLearn(OfflinePlayer player, String ability, long willLearnAt) {
        config.set(player.getUniqueId()  + "." + ability, willLearnAt);
        save();
    }

    public HashMap<String, Long> get(Player player) {
        HashMap<String, Long> results = new HashMap<String, Long>();
        try {
            ConfigurationSection section = config.getConfigurationSection(player.getUniqueId() + "");
            String[] keys = section.getKeys(false).toArray(new String[0]);
            for (String key : keys) {
                results.put(key, section.getLong(key));
            }
        } catch (Exception e) { }
        return results;
    }

    public boolean learnAbility(Player player, String ability) {
        try {
            long learnAt = config.getLong(player.getUniqueId() + "." + ability);
            System.out.println(learnAt);
            return learnAt > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public long getAbility(Player player, String ability) {
        long learnAt = 0;
        try {
            learnAt = config.getLong(player.getUniqueId() + "." + ability);
        } catch (Exception e) {
        }
        return learnAt;
    }
}
