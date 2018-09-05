package ua.VEJ.Works;

import ua.VEJ.*;
import org.bukkit.scheduler.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.potion.*;
import org.bukkit.entity.*;
import org.bukkit.event.block.*;
import org.bukkit.block.*;
import org.bukkit.*;

public class WorkLoader
{
    Core c;
    HashMap<UUID, String> lastloc;
    
    public WorkLoader() {
        this.c = Core.getInstance();
        this.lastloc = new HashMap<UUID, String>();
    }
    
    @SuppressWarnings("deprecation")
	public void checkCargo(final Player p, final Block b, final Action a) {
        final PlayerInventory pi = p.getInventory();
        if ((a == Action.LEFT_CLICK_BLOCK || a == Action.RIGHT_CLICK_BLOCK) && this.c.toggle.get(p.getName()) != null && this.c.toggle.get(p.getName()) == 0 && b.getLocation().toVector().isInAABB(this.c.selloc1.toVector(), this.c.selloc2.toVector()) && b.getType() == Material.valueOf(this.c.cargomaterial)) {
            this.c.toggle.put(p.getName(), 1);
            if (this.c.offhand) {
                if (pi.getItemInOffHand() != null && pi.getItemInOffHand().getType() != Material.AIR) {
                    p.getWorld().dropItem(p.getLocation(), pi.getItemInOffHand());
                }
                pi.setItemInOffHand(this.c.cargo);
            }
            else if (!this.c.offhand) {
                if (pi.getItem(0) != null && pi.getItem(0).getType() != Material.AIR) {
                    p.getWorld().dropItem(p.getLocation(), pi.getItem(0));
                }
                pi.setItem(0, this.c.cargo);
            }
            this.removeAbillites(p);
            p.sendMessage(String.valueOf(this.c.prf) + this.c.giveCargo);
            final Location l = b.getLocation();
            this.getLastLocations().put(p.getUniqueId(), String.valueOf(l.getBlockX()) + " " + l.getBlockY() + " " + l.getBlockZ() + " " + l.getWorld());
            b.setType(Material.AIR);
            p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.valueOf(this.c.cargomaterial).getId());
            new BukkitRunnable() {
                int sec = WorkLoader.this.c.cargosec;
                
                public void run() {
                    if (p.isOnline() && !p.isDead()) {
                        for (final int i : WorkLoader.this.c.warnings) {
                            if (this.sec == i) {
                                p.sendMessage(WorkLoader.this.c.deleteCargo.replace("%sec%", new StringBuilder(String.valueOf(this.sec)).toString()));
                                p.sendTitle("", WorkLoader.this.c.deleteCargo.replace("%sec%", new StringBuilder(String.valueOf(this.sec)).toString()));
                            }
                        }
                        if (this.sec == 0) {
                            WorkLoader.this.c.toggle.put(p.getName(), 0);
                            this.cancel();
                            if (WorkLoader.this.c.offhand) {
                                pi.setItemInOffHand((ItemStack)null);
                            }
                            else if (!WorkLoader.this.c.offhand) {
                                pi.setItem(0, (ItemStack)null);
                            }
                            WorkLoader.this.returnBlock(p);
                        }
                    }
                    else {
                        WorkLoader.this.c.toggle.put(p.getName(), 0);
                        this.cancel();
                        WorkLoader.this.returnBlock(p);
                    }
                    if (WorkLoader.this.c.toggle.get(p.getName()) == 0) {
                        if (WorkLoader.this.c.offhand) {
                            pi.setItemInOffHand((ItemStack)null);
                        }
                        else if (!WorkLoader.this.c.offhand) {
                            pi.setItem(0, (ItemStack)null);
                        }
                        WorkLoader.this.returnBlock(p);
                        this.cancel();
                    }
                    --this.sec;
                }
            }.runTaskTimer((Plugin)this.c, 0L, 20L);
        }
    }
    
    public HashMap<UUID, String> getLastLocations() {
        return this.lastloc;
    }
    
    public void returnBlock(final Player p) {
        p.getWorld().getBlockAt(this.c.getApi().stringToLocation(this.getLastLocations().get(p.getUniqueId()))).setType(Material.valueOf(this.c.cargomaterial));
    }
    
    public void plQuit(final Player p) {
        if (this.c.toggle.get(p.getName()) == 1) {
            final PlayerInventory pi = p.getInventory();
            if (this.c.offhand) {
                pi.setItemInOffHand((ItemStack)null);
            }
            else if (!this.c.offhand) {
                pi.setItem(0, (ItemStack)null);
            }
            if (this.lastloc != null) {
                p.getWorld().getBlockAt(this.c.getApi().stringToLocation(this.getLastLocations().get(p.getUniqueId()))).setType(Material.valueOf(this.c.cargomaterial));
            }
        }
    }
    
    public void removeAbillites(final Player p) {
        if (p.getGameMode() != GameMode.SURVIVAL) {
            p.setGameMode(GameMode.SURVIVAL);
        }
        if (p.getFlySpeed() != 0.2f || p.getWalkSpeed() != 0.2f) {
            p.setFlySpeed(0.2f);
            p.setWalkSpeed(0.2f);
        }
        if (p.getActivePotionEffects().size() > 0) {
            for (final PotionEffect pe : p.getActivePotionEffects()) {
                if (pe != null) {
                    p.removePotionEffect(pe.getType());
                }
            }
        }
        if (p.isFlying() || p.getAllowFlight()) {
            p.setFlying(false);
            p.setAllowFlight(false);
        }
        p.setPassenger((Entity)null);
        if (p.getPlayerTime() != p.getWorld().getTime()) {
            p.setPlayerTime(p.getWorld().getTime(), true);
        }
    }
    
    public void createSign(final SignChangeEvent e) {
        final Player p = e.getPlayer();
        if (e.getLine(0).equals("[vej]") && p.hasPermission("vej.commands") && e.getLine(1).equals("cargo")) {
            e.setLine(0, this.c.prf);
            e.setLine(1, this.c.line1);
            e.setLine(2, this.c.line2);
            e.setLine(3, this.c.line3);
            this.c.getConfig().set("Sign.Location", (Object)e.getBlock().getLocation());
            p.sendMessage(String.valueOf(this.c.prf) + this.c.succSetlocationSign);
            this.c.getConfig().options().copyDefaults(true);
            this.c.saveConfig();
            this.c.reloadConfig();
            this.c.LoadConfig();
        }
    }
    
    public void checkSignClick(final Action a, final Block b, final Player p) {
        final PlayerInventory pi = p.getInventory();
        if ((a == Action.RIGHT_CLICK_BLOCK || a == Action.LEFT_CLICK_BLOCK) && b.getState() instanceof Sign) {
            final Sign s = (Sign)b.getState();
            if (s.getBlock().getLocation().equals((Object)this.c.locsign)) {
                if (this.c.toggle.get(p.getName()) == 1) {
                    this.c.toggle.put(p.getName(), 0);
                    this.returnBlock(p);
                    p.sendMessage(String.valueOf(this.c.prf) + this.c.moneySellCargo);
                    this.c.econ.depositPlayer((OfflinePlayer)p, this.c.PriceCargo);
                    if (this.c.offhand) {
                        pi.setItemInOffHand((ItemStack)null);
                    }
                    else if (!this.c.offhand) {
                        pi.setItem(0, (ItemStack)null);
                    }
                }
                else {
                    p.sendMessage(String.valueOf(this.c.prf) + this.c.noHaveCargo);
                }
            }
        }
    }
}
