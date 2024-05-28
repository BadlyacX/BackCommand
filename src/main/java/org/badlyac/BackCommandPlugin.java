package org.badlyac;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackCommandPlugin extends JavaPlugin implements Listener {

    private final Map<UUID, Location> deathLocations = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        deathLocations.clear();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        deathLocations.put(player.getUniqueId(), deathLocation);
        player.sendMessage(ChatColor.YELLOW + "你的死亡位置已經被記錄。使用 /back 回到死亡地點。");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("back")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                UUID playerUUID = player.getUniqueId();
                if (deathLocations.containsKey(playerUUID)) {
                    Location deathLocation = deathLocations.get(playerUUID);
                    player.teleport(deathLocation);
                    player.sendMessage(ChatColor.GREEN + "你已經被傳送到死亡地點。");
                } else {
                    player.sendMessage(ChatColor.RED + "沒有記錄到你的死亡位置。");
                }
                return true;
            } else if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(ChatColor.RED + "此指令只能由玩家使用。");
                return true;
            }
        }
        return false;
    }
}
