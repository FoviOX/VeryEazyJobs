package ua.VEJ;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import com.sk89q.worldedit.bukkit.selections.*;
import com.sk89q.worldedit.bukkit.*;
import org.bukkit.plugin.*;

public class Command implements CommandExecutor
{
    Core c;
    
    public Command() {
        this.c = Core.getInstance();
    }
    
    public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (label.equalsIgnoreCase("vej")) {
            if (args.length == 0) {
                p.sendMessage(String.valueOf(this.c.prf) + "\n  " + this.c.help.toString().replace("[", "").replace("]", "").replace(",", "\n&r ").replaceAll("&", "§"));
            }
            else if (args[0].equalsIgnoreCase("reload") && p.hasPermission("vej.command.reload")) {
                this.c.reloadPlugin(p);
            }
            else if (args[0].equalsIgnoreCase("wMiner") && p.hasPermission("vej.command.wminer")) {
                if (args.length == 0) {
                    p.sendMessage(String.valueOf(this.c.prf) + "\n  " + this.c.help.toString().replace("[", "").replace("]", "").replace(",", "\n&r ").replaceAll("&", "§"));
                }
                else if (args[1].equalsIgnoreCase("leave")) {
                    if (this.c.db.constainsMiner(p.getName())) {
                        this.c.db.removeMiner(p.getName());
                        this.c.db.save();
                        p.sendMessage(String.valueOf(this.c.prf) + this.c.leaveminer);
                    }
                    else {
                        p.sendMessage(String.valueOf(this.c.prf) + this.c.alreadyleave);
                    }
                }
                else if (args[1].equalsIgnoreCase("join")) {
                    if (!this.c.db.constainsMiner(p.getName())) {
                        this.c.db.addMiner(p.getName());
                        this.c.db.save();
                        p.sendMessage(String.valueOf(this.c.prf) + this.c.joinminer);
                    }
                    else {
                        p.sendMessage(String.valueOf(this.c.prf.replaceAll("&", "§")) + this.c.alreadyjoin);
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("loader")) {
                if (this.c.toggle.get(p.getName()) == 1) {
                    this.c.toggle.put(p.getName(), 0);
                    this.c.getLoader().returnBlock(p);
                    p.sendMessage(String.valueOf(this.c.prf) + this.c.moneySellCargo);
                    this.c.econ.depositPlayer((OfflinePlayer)p, this.c.PriceCargo);
                    if (this.c.offhand) {
                        p.getInventory().setItemInOffHand((ItemStack)null);
                    }
                    else if (!this.c.offhand) {
                        p.getInventory().setItem(0, (ItemStack)null);
                    }
                }
                else {
                    p.sendMessage(String.valueOf(this.c.prf) + this.c.noHaveCargo);
                }
            }
            else if (args[0].equalsIgnoreCase("wLoader") && p.hasPermission("vej.command.wloader")) {
                if (args.length == 0) {
                    p.sendMessage(String.valueOf(this.c.prf) + "\n  " + this.c.help.toString().replace("[", "").replace("]", "").replace(",", "\n&r ").replaceAll("&", "§"));
                }
                else if (args[1].equalsIgnoreCase("setloc")) {
                    final Selection s = this.getWE().getSelection(p);
                    if (s != null) {
                        this.c.getConfig().set("Settings.WorkLoader.Cargo.Selection.LocMaxPoint", (Object)s.getMaximumPoint());
                        this.c.getConfig().set("Settings.WorkLoader.Cargo.Selection.LocMinPoint", (Object)s.getMinimumPoint());
                        this.c.getConfig().options().copyDefaults(true);
                        this.c.saveConfig();
                        this.c.reloadConfig();
                        this.c.LoadConfig();
                        p.sendMessage(this.c.selsucc);
                    }
                    else {
                        p.sendMessage(this.c.selnull);
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("wFarmer") && p.hasPermission("vej.command.wfarmer")) {
                if (args.length == 0) {
                    p.sendMessage(String.valueOf(this.c.prf) + "\n  " + this.c.help.toString().replace("[", "").replace("]", "").replace(",", "\n&r ").replaceAll("&", "§"));
                }
                else if (args[1].equalsIgnoreCase("setloc")) {
                    final Selection s = this.getWE().getSelection(p);
                    if (s != null) {
                        this.c.getConfig().set("Settings.WorkFarmer.Selection.LocMaxPoint", (Object)s.getMaximumPoint());
                        this.c.getConfig().set("Settings.WorkFarmer.Selection.LocMinPoint", (Object)s.getMinimumPoint());
                        this.c.getConfig().options().copyDefaults(true);
                        this.c.saveConfig();
                        this.c.reloadConfig();
                        this.c.LoadConfig();
                        p.sendMessage(this.c.selsucc);
                    }
                    else {
                        p.sendMessage(this.c.selnull);
                    }
                }
            }
        }
        return false;
    }
    
    private WorldEditPlugin getWE() {
        final Plugin plugin = this.c.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null;
        }
        return (WorldEditPlugin)plugin;
    }
}
