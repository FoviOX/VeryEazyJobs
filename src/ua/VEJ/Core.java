package ua.VEJ;

import org.bukkit.plugin.java.*;
import ua.VEJ.API.*;
import net.milkbowl.vault.economy.*;
import org.bukkit.inventory.*;
import ua.VEJ.Works.*;
import ua.VEJ.database.*;
import java.util.logging.*;
import org.bukkit.command.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import com.sk89q.worldguard.bukkit.*;

public class Core extends JavaPlugin implements Listener
{
    private static Core instance;
    private static FoXaPi api;
    public String prf;
    public String giveCargo;
    public String deleteCargo;
    public String cargomaterial;
    public String fearn;
    public String fntgrow;
    public String fhoe;
    public int cargosec;
    public Location selloc1;
    public Location selloc2;
    public Location fselloc1;
    public Location fselloc2;
    public List<Integer> warnings;
    public double fprice;
    public boolean offhand;
    public Economy econ;
    public ItemStack cargo;
    public HashMap<String, Integer> toggle;
    public String line1;
    public String line2;
    public String line3;
    public String noHaveCargo;
    public String succSetlocationSign;
    public String moneySellCargo;
    public double PriceCargo;
    public Location locsign;
    public String breaksound;
    public String breakparticle;
    public List<String> ignorerg;
    public String mearn;
    public HashMap<String, Double> materials;
    ItemStack note;
    ItemStack none;
    List<String> help;
    List<String> cargolores;
    String relcfg;
    String relingcfg;
    String noperm;
    String alreadyjoin;
    String alreadyleave;
    String joinminer;
    String leaveminer;
    String nameCargo;
    String refillCargo;
    String selnull;
    String selsucc;
    String fcancelblock;
    double mprice;
    PlayerInventory oldinv;
    WorkLoader wload;
    WorkMiner wmine;
    WorkFarmer wfarm;
    public Database db;
    ArrayList<Location> locs;
    
    public Core() {
        this.toggle = new HashMap<String, Integer>();
        this.materials = new HashMap<String, Double>();
        this.none = new ItemStack(Material.AIR, 1);
        this.locs = new ArrayList<Location>();
        Core.instance = this;
        Core.api = new FoXaPi();
    }
    
    public static Core getInstance() {
        return Core.instance;
    }
    
    public FoXaPi getApi() {
        return Core.api;
    }
    
    public WorkMiner getMiner() {
        return this.wmine;
    }
    
    public WorkLoader getLoader() {
        return this.wload;
    }
    
    public WorkFarmer getFarmer() {
        return this.wfarm;
    }
    
    @SuppressWarnings("deprecation")
	public void onEnable() {
        final int start = (int)System.currentTimeMillis();
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getLogger().log(Level.INFO, "Plugin loading... ");
        this.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        this.getLogger().log(Level.INFO, "_ _                 ___                 _       _          " + this.getDescription().getVersion());
        this.getLogger().log(Level.INFO, "| | | ___  _ _  _ _ | __> ___ .___ _ _  | | ___ | |_  ___");
        this.getLogger().log(Level.INFO, "| ' |/ ._>| '_>| | || _> <_> | / /| | |_| |/ . \\| . \\<_-<");
        this.getLogger().log(Level.INFO, "|__/ \\___.|_|  `_. ||___><___|/___`_. |\\__/\\___/|___//__/");
        this.getLogger().log(Level.INFO, "               <___'              <___'                  ");
        this.getLogger().log(Level.INFO, "  _                   ");
        this.getLogger().log(Level.INFO, " |_ ._   _. |_  |  _  ");
        this.getLogger().log(Level.INFO, " |_ | | (_| |_) | (/_ ");
        this.getLogger().log(Level.INFO, "");
        this.getLogger().log(Level.INFO, "Author " + this.getDescription().getAuthors());
        this.getLogger().log(Level.INFO, "Updates you can download here");
        this.getLogger().log(Level.INFO, "spigotmc.org/resources/49161/");
        this.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        this.materials.clear();
        this.reloadConfig();
        this.saveDefaultConfig();
        this.getCommand("vej").setExecutor((CommandExecutor)new Command());
        this.LoadConfig();
        this.createCargo();
        this.wmine = new WorkMiner();
        this.wload = new WorkLoader();
        this.wfarm = new WorkFarmer();
        this.setupEconomy();
        this.toggle.clear();
        for (final Player p : this.getServer().getOnlinePlayers()) {
            this.toggle.put(p.getName(), 0);
        }
        (this.db = new Database()).create();
        for (final World w : this.getServer().getWorlds()) {
            if (this.db.getYaml().getConfigurationSection("Blocks") != null) {
                for (final String s : this.db.getYaml().getConfigurationSection("Blocks").getKeys(false)) {
                    final Location loc = new Location(w, Double.parseDouble(this.db.getYaml().getString("Blocks." + s).split(" ")[0]), Double.parseDouble(this.db.getYaml().getString("Blocks." + s).split(" ")[1]), Double.parseDouble(this.db.getYaml().getString("Blocks." + s).split(" ")[2]));
                    if (w.getBlockAt(loc).getType().getId() == 0) {
                        this.db.removeBlock(Integer.parseInt(s), w);
                        this.db.save();
                    }
                }
            }
        }
        final int end = (int)System.currentTimeMillis() - start;
        this.getLogger().log(Level.INFO, "Plugin loaded! [" + end + "ms]");
    }
    
    public void onDisable() {
        final int start = (int)System.currentTimeMillis();
        this.getLogger().log(Level.INFO, "Plugin disabling...");
        for (final World w : this.getServer().getWorlds()) {
            for (final Entity en : w.getEntities()) {
                if (en instanceof Item) {
                    final Item it = (Item)en;
                    if (it.getCustomName() == null || !it.getCustomName().equals("customwheat")) {
                        continue;
                    }
                    en.remove();
                }
            }
        }
        this.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        this.getLogger().log(Level.INFO, "_ _                 ___                 _       _          " + this.getDescription().getVersion());
        this.getLogger().log(Level.INFO, "| | | ___  _ _  _ _ | __> ___ .___ _ _  | | ___ | |_  ___");
        this.getLogger().log(Level.INFO, "| ' |/ ._>| '_>| | || _> <_> | / /| | |_| |/ . \\| . \\<_-<");
        this.getLogger().log(Level.INFO, "|__/ \\___.|_|  `_. ||___><___|/___`_. |\\__/\\___/|___//__/");
        this.getLogger().log(Level.INFO, "               <___'              <___'                  ");
        this.getLogger().log(Level.INFO, "  _                     ");
        this.getLogger().log(Level.INFO, " | \\ o  _  _. |_  |  _  ");
        this.getLogger().log(Level.INFO, " |_/ | _> (_| |_) | (/_ ");
        this.getLogger().log(Level.INFO, "");
        this.getLogger().log(Level.INFO, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        final int end = (int)System.currentTimeMillis() - start;
        this.getLogger().log(Level.INFO, "Plugin disable. [" + end + "ms]");
    }
    
    public void createCargo() {
        this.cargo = new ItemStack(Material.valueOf(this.cargomaterial));
        final ItemMeta im = this.cargo.getItemMeta();
        im.setDisplayName(this.nameCargo);
        this.translateColorCodes(this.cargolores);
        im.setLore(this.cargolores);
        this.cargo.setItemMeta(im);
    }
    
    public boolean setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null || this.getServer().getPluginManager().getPlugin("Essentials") == null) {
            this.getLogger().log(Level.WARNING, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            if (this.getServer().getPluginManager().getPlugin("Essentials") == null) {
                this.getLogger().log(Level.WARNING, "Plugin Essentials not detected");
            }
            if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
                this.getLogger().log(Level.WARNING, "Plugin Vault not detected");
            }
            this.getLogger().log(Level.WARNING, "Plugin VEJ " + this.getDescription().getVersion() + " disable");
            this.getLogger().log(Level.WARNING, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            this.getPluginLoader().disablePlugin((Plugin)this);
            return false;
        }
        final RegisteredServiceProvider<Economy> rsp = (RegisteredServiceProvider<Economy>)this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.econ = (Economy)rsp.getProvider();
        return this.econ != null;
    }
    
    public void setcnf() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.reloadConfig();
        this.LoadConfig();
    }
    
    public List<String> translateColorCodes(final List<String> list) {
        final List<String> nlist = new ArrayList<String>();
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i).replaceAll("&", "§");
            nlist.add(s);
        }
        list.clear();
        for (final String s2 : nlist) {
            list.add(s2);
        }
        return list;
    }
    
    @SuppressWarnings("deprecation")
	public void reloadPlugin(final Player p) {
        final short start = (short)System.currentTimeMillis();
        this.LoadConfig();
        if (p != null) {
            p.sendMessage(String.valueOf(this.prf) + this.relingcfg);
        }
        for (final World w : this.getServer().getWorlds()) {
            if (this.db.getYaml().getConfigurationSection("Blocks") != null) {
                for (final String s : this.db.getYaml().getConfigurationSection("Blocks").getKeys(false)) {
                    final Location loc = new Location(w, Double.parseDouble(this.db.getYaml().getString("Blocks." + s).split(" ")[0]), Double.parseDouble(this.db.getYaml().getString("Blocks." + s).split(" ")[1]), Double.parseDouble(this.db.getYaml().getString("Blocks." + s).split(" ")[2]));
                    if (w.getBlockAt(loc).getType().getId() == 0) {
                        this.db.removeBlock(Integer.parseInt(s), w);
                        this.db.save();
                    }
                }
            }
        }
        this.db.refresh();
        final short end = (short)((short)System.currentTimeMillis() - start);
        if (p != null) {
            p.sendMessage(String.valueOf(this.prf) + this.relcfg + " [" + end + "ms]");
        }
    }
    
    public void LoadConfig() {
        this.reloadConfig();
        this.saveDefaultConfig();
        this.fprice = this.getConfig().getDouble("Settings.WorkFarmer.Price");
        this.fselloc1 = (Location)this.getConfig().get("Settings.WorkFarmer.Selection.LocMinPoint");
        this.fselloc2 = (Location)this.getConfig().get("Settings.WorkFarmer.Selection.LocMaxPoint");
        this.cargolores = (List<String>)this.getConfig().getStringList("Settings.WorkLoader.Cargo.CargoLores");
        this.offhand = this.getConfig().getBoolean("Settings.WorkLoader.Cargo.UseOffHand");
        this.line1 = this.getConfig().getString("Settings.WorkLoader.Cargo.Sign.Lines.Line2").replaceAll("&", "§");
        this.line2 = this.getConfig().getString("Settings.WorkLoader.Cargo.Sign.Lines.Line3").replaceAll("&", "§");
        this.line3 = this.getConfig().getString("Settings.WorkLoader.Cargo.Sign.Lines.Line4").replaceAll("&", "§");
        this.locsign = (Location)this.getConfig().get("Settings.WorkLoader.Cargo.Sign.Location");
        this.PriceCargo = this.getConfig().getInt("Settings.WorkLoader.Cargo.Price");
        this.cargomaterial = this.getConfig().getString("Settings.WorkLoader.Cargo.Material");
        this.cargosec = this.getConfig().getInt("Settings.WorkLoader.Cargo.SecondsBeforeRemove");
        this.warnings = (List<Integer>)this.getConfig().getIntegerList("Settings.WorkLoader.Cargo.Warnings");
        this.selloc1 = (Location)this.getConfig().get("Settings.WorkLoader.Cargo.Selection.LocMinPoint");
        this.selloc2 = (Location)this.getConfig().get("Settings.WorkLoader.Cargo.Selection.LocMaxPoint");
        for (final String i : this.getConfig().getConfigurationSection("Settings.WorkMiner.Ores").getKeys(false)) {
            this.materials.put(i, this.getConfig().getDouble("Settings.WorkMiner.Ores." + i));
        }
        this.breaksound = this.getConfig().getString("Settings.WorkMiner.OnBreakSound");
        this.breakparticle = this.getConfig().getString("Settings.WorkMiner.OnBreakParticle");
        this.ignorerg = (List<String>)this.getConfig().getStringList("Settings.WorkMiner.IgnoreRegions");
        this.selsucc = this.getConfig().getString("Messages.Plugin.SelectionSuccessCreate").replaceAll("&", "§");
        this.relcfg = this.getConfig().getString("Messages.Plugin.ReloadConfig").replaceAll("&", "§");
        this.relingcfg = this.getConfig().getString("Messages.Plugin.ReloadingConfig").replaceAll("&", "§");
        this.prf = this.getConfig().getString("Messages.Plugin.Prefix").replaceAll("&", "§");
        this.noperm = this.getConfig().getString("Messages.Plugin.NoPerms").replaceAll("&", "§");
        this.help = (List<String>)this.getConfig().getStringList("Messages.Plugin.Help");
        this.selnull = this.getConfig().getString("Messages.Plugin.SelectionError").replaceAll("&", "§");
        this.mearn = this.getConfig().getString("Messages.WorkMiner.Earn").replaceAll("&", "§");
        this.alreadyjoin = this.getConfig().getString("Messages.WorkMiner.AlreadyJoin").replaceAll("&", "§");
        this.alreadyleave = this.getConfig().getString("Messages.WorkMiner.AlreadyLeave").replaceAll("&", "§");
        this.joinminer = this.getConfig().getString("Messages.WorkMiner.JoinMiner").replaceAll("&", "§");
        this.leaveminer = this.getConfig().getString("Messages.WorkMiner.LeaveMiner").replaceAll("&", "§");
        this.refillCargo = this.getConfig().getString("Messages.WorkLoader.refillCargo").replaceAll("&", "§");
        this.succSetlocationSign = this.getConfig().getString("Messages.WorkLoader.succSetlocationSign").replaceAll("&", "§");
        this.moneySellCargo = this.getConfig().getString("Messages.WorkLoader.moneySellCargo").replaceAll("&", "§").replaceAll("%price%", new StringBuilder().append(this.PriceCargo).toString());
        this.nameCargo = this.getConfig().getString("Messages.WorkLoader.nameCargo").replaceAll("&", "§");
        this.noHaveCargo = this.getConfig().getString("Messages.WorkLoader.noHaveCargo").replaceAll("&", "§");
        this.giveCargo = this.getConfig().getString("Messages.WorkLoader.giveCargo").replaceAll("&", "§");
        this.deleteCargo = this.getConfig().getString("Messages.WorkLoader.deleteCargo").replaceAll("&", "§");
        this.fcancelblock = this.getConfig().getString("Messages.WorkFarmer.cancelBreakBlock").replaceAll("&", "§");
        this.fearn = this.getConfig().getString("Messages.WorkFarmer.earnMoney").replaceAll("&", "§");
        this.fntgrow = this.getConfig().getString("Messages.WorkFarmer.notFullyGrow").replaceAll("&", "§");
        this.fhoe = this.getConfig().getString("Messages.WorkFarmer.notHaveHoe").replaceAll("&", "§");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void workLoader(final PlayerInteractEvent e) {
        this.getLoader().checkCargo(e.getPlayer(), e.getClickedBlock(), e.getAction());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSign(final SignChangeEvent e) {
        if (!e.isCancelled()) {
            this.getLoader().createSign(e);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignClick(final PlayerInteractEvent e) {
        this.getLoader().checkSignClick(e.getAction(), e.getClickedBlock(), e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        this.toggle.put(p.getName(), 0);
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(final PlayerQuitEvent e) {
        this.getLoader().plQuit(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(final PlayerDeathEvent e) {
        if (e.getEntity().isOnline() && this.toggle.get(e.getEntity().getName()) == 1) {
            final Iterator<ItemStack> it = e.getDrops().iterator();
            while (it.hasNext()) {
                final ItemStack itm = it.next();
                if (itm.getType() == Material.valueOf(this.cargomaterial) && itm.getItemMeta().getLore() != null) {
                    it.remove();
                }
            }
            this.getLoader().returnBlock(e.getEntity());
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onCmd(final PlayerCommandPreprocessEvent e) {
        if (!e.isCancelled() && this.toggle.get(e.getPlayer().getName()) == 1 && !e.getMessage().equals("/vej loader")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(final PlayerDropItemEvent e) {
        if (!e.isCancelled() && this.toggle.get(e.getPlayer().getName()) == 1) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(final InventoryClickEvent e) {
        if (!e.isCancelled() && this.toggle.get(e.getWhoClicked().getName()) == 1) {
            e.setCancelled(true);
            if (e.getWhoClicked().getGameMode() == GameMode.CREATIVE) {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(final BlockPlaceEvent e) {
        if (!e.isCancelled()) {
            this.getMiner().addBlockToStorage(e, e.getBlockPlaced(), e.getPlayer());
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void workMiner(final BlockBreakEvent e) {
        if (!e.isCancelled()) {
            getMiner().checkBreakedBlock(e.getPlayer(), e.getBlock(), e.getExpToDrop());
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
    public void onBreakWheat(final PlayerInteractEvent e) {
        this.getFarmer().breakWheat(e);
        if (!e.getPlayer().hasPermission("vej.farmer.allowbreak") && (e.getAction() != Action.LEFT_CLICK_AIR || e.getAction() != Action.RIGHT_CLICK_AIR) && e.getClickedBlock() != null && e.getClickedBlock().getType() != Material.AIR && e.getClickedBlock().getType().getId() != 59 && e.getClickedBlock().getLocation().toVector().isInAABB(this.fselloc1.toVector(), this.fselloc2.toVector())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(String.valueOf(this.prf) + this.fcancelblock);
        }
    }
    
    public WorldGuardPlugin getWG() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }
}
