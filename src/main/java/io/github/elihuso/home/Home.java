package io.github.elihuso.home;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

public final class Home extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(this.getDataFolder() + "/" + player.getUniqueId().toString());
        } catch (Exception ex) {
            try {
                config.save(this.getDataFolder() + "/" + player.getUniqueId().toString());
            } catch (Exception exception) {
                return false;
            }
        }
        String path = "";
        if (args.length == 0)
            path = "home";
        else
            path = args[0];
        if (command.getName().equalsIgnoreCase("home")) {
            Location target = config.getLocation(path);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "No such home!");
                return false;
            }
            player.teleport(target);
            player.sendMessage(ChatColor.GREEN + "Teleport to home " + (path.equalsIgnoreCase("home") ? "" : ChatColor.WHITE + path) + ChatColor.GREEN + ".");
            return true;
        }
        if (command.getName().equalsIgnoreCase("sethome")) {
            config.set(path, player.getLocation());
            try {
                config.save(this.getDataFolder() + "/" + player.getUniqueId().toString());
            } catch (IOException e) {
                return false;
            }
            player.sendMessage(ChatColor.GREEN + "Set your location as home " + (path.equalsIgnoreCase("home") ? "" : ChatColor.WHITE + path) + ChatColor.GREEN + ".");
            return true;
        }
        if (command.getName().equalsIgnoreCase("delhome")) {
            config.set(path, null);
            try {
                config.save(this.getDataFolder() + "/" + player.getUniqueId().toString());
            } catch (IOException e) {
                return false;
            }
            player.sendMessage(ChatColor.YELLOW + "Deleted.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("listhome")) {
            Set<String> homeSet = config.getKeys(false);
            String[] homes = homeSet.toArray(String[]::new);
            if (homes.length == 0) {
                player.sendMessage(ChatColor.RED + "You have never set a home!");
                return false;
            }
            player.sendMessage("Homes you set:\n----------------------------------------------------------------");
            for (String v : homes) {
                Location location = config.getLocation(v);
                player.sendMessage(ChatColor.WHITE + v +
                        ChatColor.AQUA + "  world: " + ChatColor.WHITE + location.getWorld().getName() +
                        ChatColor.GREEN + "  x: " + ChatColor.WHITE + location.getBlockX() +
                        ChatColor.GREEN + "  y: " + ChatColor.WHITE + location.getBlockY() +
                        ChatColor.GREEN + "  z: " + ChatColor.WHITE + location.getBlockZ());
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
