package me.seabarrel.unlockbending;

import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.event.AbilityStartEvent;
import com.projectkorra.projectkorra.event.PlayerBindChangeEvent;
import com.projectkorra.projectkorra.util.ActionBar;
import com.sun.tools.javac.Main;
import me.seabarrel.unlockbending.configuration.ConfigManager;
import me.seabarrel.unlockbending.data.LearnData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.HashMap;

public class MainListener implements Listener {

    @EventHandler
    public void onBend(AbilityStartEvent event) {
        if (ConfigManager.getConfig().getBoolean("Debug.SendConsoleEvent")) {
            System.out.println(event.getAbility().getName() + " - To turn this off set debug/SendConsoleEvent in the config to false");
        }
        if (!UnlockBending.canBend(event.getAbility())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBind(PlayerBindChangeEvent event) {
        System.out.println(event.getAbility());
        if (!UnlockBending.canBend(event.getAbility(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getType() != Material.BOOK) {
            return;
        }

        ItemMeta meta = event.getItem().getItemMeta();
        String abil = ChatColor.stripColor(meta.getDisplayName());
        if (CoreAbility.getAbility(abil) == null) {
            return;
        }

        if (meta.getLore().size() < 2) {
            return;
        }

        Duration duration;
        try {
            duration = Duration.ofHours(Long.parseLong(ChatColor.stripColor(meta.getLore().get(1))));
        } catch (Exception e) {
            return;
        }

        LearnData data = UnlockBending.getPlugin().getLearnData();
        HashMap<String, Long> learnt = data.get(event.getPlayer());
        if (learnt.keySet().contains(abil)) {
            event.getPlayer().sendMessage(ChatColor.RED + "You already know this move or are learning it right now. You can not relearn it!");
            return;
        }

        long learn = System.currentTimeMillis() + duration.toMillis();
        data.setLearn(event.getPlayer(), abil, learn);
        event.getPlayer().sendMessage(ChatColor.GREEN + "You are now learning the move " + abil + "! It will take " + String.valueOf(duration.toDays()) + " Days and " + String.valueOf(duration.toHoursPart()) + " hours");

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(UnlockBending.getPlugin(), new Runnable() {
            public void run(){
                event.getPlayer().getInventory().removeItem(event.getItem());
            }
        }, 1L);

    }

}
