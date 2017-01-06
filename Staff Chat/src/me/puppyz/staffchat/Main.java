package me.puppyz.staffchat;

import java.util.Iterator;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public Main() {
    }

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        config.addDefault("enabled", Boolean.valueOf(true));
        config.options().copyDefaults(true);
        this.saveConfig();
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("staffchat")) {
            FileConfiguration config = this.getConfig();
            if(args.length == 0) {
                sender.sendMessage("You\'re runnning StaffChat 1.0 by iPuppyz!");
                return true;
            }

            if(args.length == 1) {
                if(sender.hasPermission("staffchat.enable") && args[0].equals("on")) {
                    if(config.getBoolean("enabled")) {
                        sender.sendMessage(ChatColor.DARK_RED + "StaffChat is already on!");
                        return true;
                    }

                    config.set("enabled", Boolean.valueOf(true));
                    this.saveConfig();
                    sender.sendMessage(ChatColor.RED + "StaffChat is now on!");
                    return true;
                }

                if(sender.hasPermission("staffchat.disable") && args[0].equals("off")) {
                    if(!config.getBoolean("enabled")) {
                        sender.sendMessage(ChatColor.DARK_RED + "StaffChat is already off!");
                        return true;
                    }

                    config.set("enabled", Boolean.valueOf(false));
                    this.saveConfig();
                    sender.sendMessage(ChatColor.RED + "StaffChat is now off!");
                    return true;
                }

                if(sender.hasPermission("staffchat.reload") && args[0].equals("reload")) {
                    try {
                        this.reloadConfig();
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        sender.sendMessage(ChatColor.RED + "Error while reloading StaffChat!");
                        return true;
                    }
                    sender.sendMessage(ChatColor.GREEN + "StaffChat was reloaded!");
                    return true;
                }
               if(args[0].equals("help") && sender.hasPermission("staffchat.help")) {
            	   sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "StaffChat Commands:" + ChatColor.GREEN + "\n /staffchat on - Enables StaffChat. \n /staffchat off - Disables StaffChat. \n /staffchat reload - Reloads StaffChat.");
               }
            }
        }

        return false;
    }

    @EventHandler
    public void staffChat(AsyncPlayerChatEvent e) {
        if(e.getPlayer().hasPermission("staffchat.chat")) {
            FileConfiguration config = this.getConfig();
            if(config.getBoolean("enabled") && e.getMessage().startsWith(config.getString("chat.token"))) {
                e.setCancelled(true);
                String message = e.getMessage();
                String message2 = message.replaceFirst(Pattern.quote(config.getString("chat.token")), "");
                String toBe = ChatColor.translateAlternateColorCodes('&', message2);
                Iterator var6 = Bukkit.getOnlinePlayers().iterator();

                while(var6.hasNext()) {
                    Player player = (Player)var6.next();
                    if(player.hasPermission("staffchat.chat")) {
                        String messageGetter = config.getString("chat.format");
                        String messageGetter1 = ChatColor.translateAlternateColorCodes('&', messageGetter);
                        String messageGetter2 = messageGetter1.replaceAll("%name%", e.getPlayer().getDisplayName());
                        String messageGetter3 = messageGetter2.replaceAll("%chat%", toBe);
                        player.sendMessage(messageGetter3);
                    }
                }
            }
        }

    }
}
