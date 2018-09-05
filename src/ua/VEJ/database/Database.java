package ua.VEJ.database;

import org.bukkit.configuration.file.*;
import ua.VEJ.*;
import org.bukkit.block.*;
import java.io.*;
import org.bukkit.configuration.*;
import java.util.*;
import org.bukkit.*;

public class Database {
    File db;
    YamlConfiguration yaml;
    int last;
    Core c;
    HashSet<Block> blocks;
    HashSet<String> miners;
    
    public Database() {
        this.yaml = new YamlConfiguration();
        this.last = 0;
        this.c = Core.getInstance();
        this.blocks = new HashSet<Block>();
        this.miners = new HashSet<String>();
    }
    
    public void create() {
        this.db = new File(this.c.getDataFolder(), "database.db");
        if (!this.db.exists()) {
            try {
                this.db.createNewFile();
                this.yaml.load(this.db);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            catch (InvalidConfigurationException e3) {
                e3.printStackTrace();
            }
        }
        else {
            this.refresh();
        }
    }
    
    public int getLast() {
        this.fix();
        return this.last;
    }
    
    public boolean constainsMiner(final String name) {
        this.fix();
        return this.miners.contains(name.toLowerCase());
    }
    
    public void addMiner(final String name) {
        this.fix();
        if (this.yaml != null) {
            this.miners.add(name.toLowerCase());
            this.yaml.set("Miners." + name.toLowerCase(), (Object)1);
        }
    }
    
    public void removeMiner(final String name) {
        this.fix();
        if (this.yaml != null) {
            this.miners.remove(name.toLowerCase());
            this.yaml.set("Miners." + name.toLowerCase(), (Object)null);
        }
    }
    
    @SuppressWarnings("null")
	public void refresh() {
        try {
            this.yaml.load(this.db);
            if (this.yaml.getConfigurationSection("Blocks") != null) {
                this.blocks.clear();
                for (final String s : this.yaml.getConfigurationSection("Blocks").getKeys(false)) {
                    final String[] args = this.yaml.getString("Blocks." + s).split(" ");
                    final World w = this.c.getServer().getWorld(args[4]);
                    final Location loc = new Location(w, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                    this.blocks.add(w.getBlockAt(loc));
                }
            }
            if (this.yaml.getConfigurationSection("Miners") != null) {
                this.miners.clear();
                for (final String s2 : this.yaml.getConfigurationSection("Miners").getKeys(false)) {
                    this.miners.add(s2);
                }
            }
        }
        catch (IOException | InvalidConfigurationException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public Block getBlock(final int i, final World w) {
        this.fix();
        if (this.yaml != null && this.yaml.getConfigurationSection("Blocks") != null) {
            for (final String s : this.yaml.getConfigurationSection("Blocks").getKeys(false)) {
                final String[] args = this.yaml.getString("Blocks." + s).split(" ");
                if (Integer.parseInt(s) == i && w.equals(this.c.getServer().getWorld(args[4]))) {
                    final Location loc = new Location(w, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                    return w.getBlockAt(loc);
                }
            }
        }
        return null;
    }
    
    public ArrayList<Block> getBlocks(final World w) {
        if (this.yaml != null) {
            final ArrayList<Block> list = new ArrayList<Block>();
            if (this.yaml.getConfigurationSection("Blocks") != null) {
                for (final String s : this.yaml.getConfigurationSection("Blocks").getKeys(false)) {
                    final Location loc = new Location(w, Double.parseDouble(this.yaml.getString("Blocks." + s).split(" ")[0]), Double.parseDouble(this.yaml.getString("Blocks." + s).split(" ")[1]), Double.parseDouble(this.yaml.getString("Blocks." + s).split(" ")[2]));
                    list.add(Bukkit.getServer().getWorld(w.getName()).getBlockAt(loc));
                }
            }
            return list;
        }
        return null;
    }
    
    public HashSet<Block> getHashBlocks(final World w) {
        return this.blocks;
    }
    
    public YamlConfiguration getYaml() {
        return this.yaml;
    }
    
    @SuppressWarnings("deprecation")
	public void addBlock(final int i, final Block b) {
        this.fix();
        if (this.yaml != null) {
            this.blocks.add(b);
            this.yaml.set("Blocks." + i, (Object)(String.valueOf(b.getLocation().getX()) + " " + b.getLocation().getY() + " " + b.getLocation().getZ() + " " + b.getType().getId() + " " + b.getLocation().getWorld().getName()));
        }
    }
    
    public boolean containsBlock(final Block b) {
        this.fix();
        return this.blocks.contains(b);
    }
    
    @Deprecated
    public boolean containsBlock(final int i, final World w, final int id) {
        this.fix();
        if (this.yaml != null) {
            if (this.yaml.getConfigurationSection("Blocks") != null) {
                for (final String s : this.yaml.getConfigurationSection("Blocks").getKeys(false)) {
                    if (Integer.parseInt(s) == i && id == Integer.parseInt(this.yaml.getString("Blocks." + s).split(" ")[3])) {
                        return true;
                    }
                }
            }
            else {
                this.yaml.createSection("Blocks");
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean containsBlock(final int i, final World w, final Block b) {
        this.fix();
        if (this.yaml != null && this.yaml.getConfigurationSection("Blocks") != null) {
            for (final String s : this.yaml.getConfigurationSection("Blocks").getKeys(false)) {
                if (Integer.parseInt(s) == i && b.getType().getId() == Integer.parseInt(this.yaml.getString("Blocks." + s).split(" ")[3])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void removeBlock(final int i, World w) {
        this.fix();
        if (this.yaml != null && this.yaml.getConfigurationSection("Blocks") != null) {
            for (final String s : this.yaml.getConfigurationSection("Blocks").getKeys(false)) {
                if (Integer.parseInt(s) == i) {
                    final String[] args = this.yaml.getString("Blocks." + s).split(" ");
                    w = this.c.getServer().getWorld(args[4]);
                    final Location loc = new Location(w, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                    this.blocks.remove(w.getBlockAt(loc));
                    this.yaml.set("Blocks." + s, (Object)null);
                }
            }
        }
    }
    
    public void save() {
        try {
            this.yaml.save(this.db);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void fix() {
        if (this.yaml.getConfigurationSection("Blocks") != null) {
            if (this.yaml.getConfigurationSection("Blocks") != null) {
                for (final String s : this.yaml.getConfigurationSection("Blocks").getKeys(false)) {
                    this.last = Integer.parseInt(s);
                }
            }
            ++this.last;
        }
    }
}
