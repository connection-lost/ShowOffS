package me.crafter.mc.showoffs;

import java.util.logging.Logger;

import mkremins.fanciful.FancyMessage;
import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ShowOffS extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Mincraft");
	public Chat chat;

    public void onEnable(){
        if (setupChat()){
        	logger.info("[ShowOff] Using Vault!");
        } else {
        	chat = null;
        }
        
    }
 

    public void onDisable() {
    }

    
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args){
    	if (!(sender instanceof Player)){
    		sender.sendMessage("Console cannot showoff!");
    		return true;
    	}
    	
    	if (cmd.getName().equals("showoff") || cmd.getName().equals("showto")){
    		
    		Player p = (Player)sender;
    		if (p.getItemInHand().getType().equals(Material.AIR)){
    			p.sendMessage(ChatColor.RED+"你的手上没有物品!");
    			return true;
    		}
    		
    		if (cmd.getName().equals("showto")){
    			if (args.length < 1){
    				sender.sendMessage(ChatColor.RED + "/showto <Player>");
    			}
    			if (Bukkit.getPlayer(args[0]) == null){
    				sender.sendMessage(ChatColor.RED + "找不到" + ChatColor.STRIKETHROUGH + "对象" + ChatColor.RESET + ChatColor.RED + "目标玩家。");
    			}
    		}
    		

    		ItemStack item = p.getItemInHand();
    		ItemMeta itemmeta = item.getItemMeta();
    		
    		String itemname = item.getType().name();
    		String itemdisplayname = itemmeta.hasDisplayName() ? itemmeta.getDisplayName() : itemname;
    		String multiplier = p.getItemInHand().getAmount() > 1 ? "*" + p.getItemInHand().getAmount() : "";
    		String playername = getPlayerName(p);
    		
    		if (cmd.getName().equals("showoff")) {
        		FancyMessage fm = new FancyMessage(playername + " 展示物品: ").color(ChatColor.AQUA);
        		if (!itemname.equals(itemdisplayname)){
            		fm.then(itemdisplayname + "(" + itemname + ")" + multiplier);
        		} else {
        			fm.then(itemdisplayname).color(ChatColor.AQUA);
        		}
        		fm.itemTooltip(item);
    			for (Player player : Bukkit.getOnlinePlayers()){
    				fm.send(player);
    			}
    		} else { //cmd is showto
        		FancyMessage fm = new FancyMessage(playername + " 向你悄悄展示物品: ");
        		if (!itemname.equals(itemdisplayname)){
            		fm.then(itemdisplayname + "(" + itemname + ")" + multiplier);
        		} else {
        			fm.then(itemdisplayname).color(ChatColor.AQUA);
        		}
        		fm.itemTooltip(item);
        		fm.send(Bukkit.getPlayer(args[0]));
    		}

    		return true;
    	}
    	
    	if (cmd.getName().equals("i")){
    		Player p = (Player)sender;
    		String playername = getPlayerName(p);
    		FancyMessage fm = new FancyMessage(playername);
    		fm.suggest("/tell " + p.getName() + " ");
    		fm.tooltip(ChatColor.GRAY + "点击私聊");
    		fm.then(": ");
    		fm.then("我在" + p.getWorld().getName() + "世界，坐标 x" + p.getLocation().getBlockX() + " y" + p.getLocation().getBlockY() + " z" + p.getLocation().getBlockZ());
			for (Player player : Bukkit.getOnlinePlayers()){
				fm.send(player);
			}			
    	}
    	
    	return true;
    }
    
    
    private boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
                return false;
        }
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
                return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }
    
    public String getPlayerName(Player p){
    	if (chat == null) return p.getName();
    	return ChatColor.translateAlternateColorCodes("&".charAt(0), (chat.getPlayerPrefix(p)+p.getName()+chat.getPlayerSuffix(p)));
    }
	
}
