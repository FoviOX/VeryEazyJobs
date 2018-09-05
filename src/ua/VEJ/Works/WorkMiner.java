package ua.VEJ.Works;

import ua.VEJ.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import com.sk89q.worldguard.protection.flags.*;
import java.util.logging.*;
import org.bukkit.*;
import com.sk89q.worldguard.protection.*;
import org.bukkit.event.block.*;

public class WorkMiner
{
    Core c;
    Location loc;
    
    public WorkMiner() {
        this.c = Core.getInstance();
    }
    
    @SuppressWarnings("deprecation")
	public void checkBreakedBlock(final Player p, final Block b, final int exp) {
        boolean on = true;
        boolean global = true;
        if (b.getType() != Material.AIR) {
            if (this.c.db.containsBlock(b)) {
                on = false;
            }
            final ApplicableRegionSet set = this.c.getWG().getRegionManager(p.getWorld()).getApplicableRegions(b.getLocation());
            if (!set.getRegions().isEmpty()) {
                global = false;
                if (set.allows(DefaultFlag.BLOCK_BREAK)) {
                    global = true;
                }
            }
            if (on && global && this.c.setupEconomy() && this.c.db.constainsMiner(p.getName()) && p.getGameMode() != GameMode.CREATIVE) {
                this.loc = b.getLocation();
                if (this.c.materials.get(b.getType().name()) != null) {
                    this.c.econ.depositPlayer((OfflinePlayer)p, (double)this.c.materials.get(b.getType().name()));
                    p.sendTitle("", this.c.mearn.replace("%price%", new StringBuilder().append((double)this.c.materials.get(b.getType().name())).toString()).replaceAll("%block%", b.getType().name()).replace("%exp%", new StringBuilder().append(exp).toString()));
                    if (this.c.breakparticle != null) {
                        try {
                            p.spawnParticle(Particle.valueOf(this.c.breakparticle), this.loc.getX() + 0.5, this.loc.getY(), this.loc.getZ() + 0.5, 15);
                        }
                        catch (Exception e) {
                            this.c.getLogger().log(Level.INFO, " Particle '" + this.c.breakparticle + "' not found");
                        }
                    }
                    if (this.c.breaksound != null) {
                        try {
                            p.playSound(p.getLocation(), Sound.valueOf(this.c.breaksound), 20.0f, 20.0f);
                        }
                        catch (Exception e) {
                            this.c.getLogger().log(Level.INFO, " Sound '" + this.c.breaksound + "' not found");
                        }
                    }
                }
            }
        }
    }
    
    public void addBlockToStorage(final BlockPlaceEvent e, final Block b, final Player p) {
        if (this.c.toggle.get(p.getName()) == 1) {
            e.setCancelled(true);
        }
        Material[] values;
        for (int length = (values = Material.values()).length, i = 0; i < length; ++i) {
            final Material m = values[i];
            if (this.c.materials.get(m.name()) != null && b.getType() == m) {
                this.c.db.addBlock(this.c.db.getLast(), b);
                this.c.db.save();
            }
        }
    }
}
