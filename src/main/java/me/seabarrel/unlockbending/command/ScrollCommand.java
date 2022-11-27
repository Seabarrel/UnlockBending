package me.seabarrel.unlockbending.command;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.projectkorra.projectkorra.command.PKCommand;
import me.seabarrel.unlockbending.configuration.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.util.*;

public class ScrollCommand extends PKCommand {

    public ScrollCommand() {
        super("scroll", "/bending scroll <Ability> <Hours to learn>", "Generates a scroll to learn an ability taking a surtain amount of time!", new String[]{"scroll", "sc"});
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        if (!player.hasPermission("scroll.master")) {
            player.sendMessage(ChatColor.RED + "You are not a scroll master yet. You are unable to perform this command!");
            return;
        }
        if (CoreAbility.getAbility(args.get(0)) == null) {
            player.sendMessage(ChatColor.RED + "This is not a valid move!!!!");
            return;
        }

        if (args.size() < 2) {
            player.sendMessage(ChatColor.RED + "You forgot to fill in the hours it takes to learn the move!");
            return;
        }

        int hours;
        try {
            hours = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "This is not a correct number! Pls fill in a number (hours)!!!!");
            return;
        }

        Duration duration = Duration.ofHours(hours);

        ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(ConfigManager.getConfig().getString("Scroll.Item")))));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CoreAbility.getAbility(args.get(0)).getElement().getColor() + args.get(0));
        ArrayList<String> lores = new ArrayList<>();
        lores.add(ChatColor.GRAY + "Time to learn: " + String.valueOf(duration.toDays()) + " Days and " + String.valueOf(duration.toHoursPart()) + " hours");
        lores.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + String.valueOf(duration.toHours()));
        meta.setLore(new ArrayList<String>(lores));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
        player.sendMessage(ChatColor.GREEN + "Successfully gave you a scroll!");
    }

    @Override
    protected List<String> getTabCompletion(final CommandSender sender, final List<String> args) {
        if (args.size() >= 2 || !sender.hasPermission("bending.command.bind") || !(sender instanceof Player)) {
            return new ArrayList<String>();
        }

        List<String> abilities = new ArrayList<String>();
        final BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(sender.getName());
        if (args.size() == 0) {
            if (bPlayer != null) {
                for (final CoreAbility coreAbil : CoreAbility.getAbilities()) {
                    if (!coreAbil.isHiddenAbility() && bPlayer.canBind(coreAbil) && !abilities.contains(coreAbil.getName())) {
                        abilities.add(coreAbil.getName());
                    }
                }
            }
        } else {
            abilities = Arrays.asList("1-2-3-4-10-11-13-110-1100-2000".split("-"));
        }

        Collections.sort(abilities);
        return abilities;
    }
}
