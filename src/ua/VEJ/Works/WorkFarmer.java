package ua.VEJ.Works;

import ua.VEJ.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.block.*;
import org.bukkit.*;
import org.bukkit.material.*;

public class WorkFarmer
{
    Core c;
    
    public WorkFarmer() {
        this.c = Core.getInstance();
    }
    
    @SuppressWarnings("deprecation")
	public void breakWheat(final PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            final Player p = e.getPlayer();
            final PlayerInventory pi = p.getInventory();
            final Block b = e.getClickedBlock();
            final ItemStack is = pi.getItemInMainHand();
            final Material item = is.getType();
            if (b.getLocation().toVector().isInAABB(this.c.fselloc1.toVector(), this.c.fselloc2.toVector())) {
                if (b.getType().getId() == 59) {
                    if (this.isFullyGrown(b)) {
                        if (item.getId() == 290) {
                            if (59 - is.getDurability() > 0) {
                                is.setDurability((short)(pi.getItemInMainHand().getDurability() + 5));
                            }
                            else {
                                is.setAmount(0);
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            }
                        }
                        else if (item.getId() == 291) {
                            if (131 - is.getDurability() >= 0) {
                                is.setDurability((short)(pi.getItemInMainHand().getDurability() + 5));
                            }
                            else {
                                is.setAmount(0);
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            }
                        }
                        else if (item.getId() == 292) {
                            if (250 - is.getDurability() >= 0) {
                                is.setDurability((short)(pi.getItemInMainHand().getDurability() + 5));
                            }
                            else {
                                is.setAmount(0);
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            }
                        }
                        else if (item.getId() == 294) {
                            if (32 - is.getDurability() >= 0) {
                                is.setDurability((short)(pi.getItemInMainHand().getDurability() + 5));
                            }
                            else {
                                is.setAmount(0);
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            }
                        }
                        else {
                            if (item.getId() != 293) {
                                e.setCancelled(true);
                                p.sendMessage(String.valueOf(this.c.prf) + this.c.fhoe);
                                return;
                            }
                            if (1561 - is.getDurability() >= 0) {
                                is.setDurability((short)(pi.getItemInMainHand().getDurability() + 5));
                            }
                            else {
                                is.setAmount(0);
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                            }
                        }
                        e.setCancelled(true);
                        p.sendMessage(String.valueOf(this.c.prf) + this.c.fearn.replace("%price%", new StringBuilder().append(this.c.fprice).toString()));
                        this.c.econ.depositPlayer((OfflinePlayer)p, this.c.fprice);
                        b.setType(b.getType());
                        final Item it = p.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.WHEAT));
                        it.setCustomName("customwheat");
                        it.setPickupDelay(99999999);
                        final Runnable runn = new Runnable() {
                            @Override
                            public void run() {
                                it.remove();
                            }
                        };
                        this.c.getServer().getScheduler().runTaskLater((Plugin)this.c, runn, 35L);
                    }
                    else {
                        e.setCancelled(true);
                        p.sendMessage(String.valueOf(this.c.prf) + this.c.fntgrow);
                    }
                }
                else {
                    e.setCancelled(true);
                    p.sendMessage(String.valueOf(this.c.prf) + this.c.fhoe);
                }
            }
        }
    }
    
    public boolean isFullyGrown(final Block b) {
        final MaterialData md = b.getState().getData();
        return ((Crops)md).getState() == CropState.RIPE;
    }
}
