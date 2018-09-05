package ua.VEJ.API;

import java.util.*;
import org.bukkit.*;

public class FoXaPi
{
    public List<String> replaceInList(final List<String> list, final String replacement, final String to) {
        final List<String> nlist = new ArrayList<String>();
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i).replaceAll(replacement, to);
            nlist.add(s);
        }
        list.clear();
        for (final String s2 : nlist) {
            list.add(s2);
        }
        return list;
    }
    
    public List<String> replaceInList(final List<String> list, final char replacement, final char to) {
        final List<String> nlist = new ArrayList<String>();
        for (int i = 0; i < list.size(); ++i) {
            final String s = list.get(i).replace(replacement, to);
            nlist.add(s);
        }
        list.clear();
        for (final String s2 : nlist) {
            list.add(s2);
        }
        return list;
    }
    
    public List<Integer> replaceInList(final List<Integer> list, final int replacement, final int to) {
        final List<String> nlist = new ArrayList<String>();
        for (int i = 0; i < list.size(); ++i) {
            final String s = Integer.toString(list.get(i)).replace(Integer.toString(replacement), Integer.toString(to));
            nlist.add(s);
        }
        list.clear();
        for (final String s2 : nlist) {
            list.add(Integer.parseInt(s2));
        }
        return list;
    }
    
    public List<Double> replaceInList(final List<Double> list, final double replacement, final double to) {
        final List<String> nlist = new ArrayList<String>();
        for (int i = 0; i < list.size(); ++i) {
            final String s = Double.toString(list.get(i)).replace(Double.toString(replacement), Double.toString(to));
            nlist.add(s);
        }
        list.clear();
        for (final String s2 : nlist) {
            list.add(Double.parseDouble(s2));
        }
        return list;
    }
    
    public boolean checkList(final ArrayList<Integer> list, final int needfound) {
        boolean end = false;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == needfound) {
                end = true;
                i = list.size();
            }
        }
        return end;
    }
    
    public boolean checkList(final ArrayList<Double> list, final double needfound) {
        boolean end = false;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == needfound) {
                end = true;
                i = list.size();
            }
        }
        return end;
    }
    
    public boolean checkList(final ArrayList<String> list, final String needfound) {
        boolean end = false;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals(needfound)) {
                end = true;
                i = list.size();
            }
        }
        return end;
    }
    
    public boolean checkList(final List<Integer> list, final int needfound) {
        boolean end = false;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == needfound) {
                end = true;
                i = list.size();
            }
        }
        return end;
    }
    
    public boolean checkList(final List<Double> list, final double needfound) {
        boolean end = false;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == needfound) {
                end = true;
                i = list.size();
            }
        }
        return end;
    }
    
    public boolean checkList(final List<String> list, final String needfound) {
        boolean end = false;
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals(needfound)) {
                end = true;
                i = list.size();
            }
        }
        return end;
    }
    
    public String locationToString(final Location loc) {
        final String s = String.valueOf(loc.getX()) + " " + loc.getY() + " " + loc.getZ() + " " + loc.getWorld().getName();
        return s;
    }
    
    public String locationToString(final double x, final double y, final double z, final String world) {
        final String s = String.valueOf(x) + " " + y + " " + z + " " + world;
        return s;
    }
    
    public String locationToString(final int x, final int y, final int z, final String world) {
        final String s = String.valueOf(x) + " " + y + " " + z + " " + world;
        return s;
    }
    
    public Location stringToLocation(final String s) {
        final String[] arg = s.split(" ");
        final Location loc = new Location(Bukkit.getServer().getWorld(arg[3]), (double)Integer.parseInt(arg[0]), (double)Integer.parseInt(arg[1]), (double)Integer.parseInt(arg[2]));
        return loc;
    }
    
    public Location stringToLocation(final String[] arg) {
        final Location loc = new Location(Bukkit.getServer().getWorld(arg[3]), (double)Integer.parseInt(arg[0]), (double)Integer.parseInt(arg[1]), (double)Integer.parseInt(arg[2]));
        return loc;
    }
}
