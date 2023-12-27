package org.puretripp.vassal.menus.submenus;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.MenuIcon;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.events.OnVertexSelectionOpen;
import org.puretripp.vassal.types.Vassal;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.types.Residence;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.Arrays;

public class SubclaimMenu extends Menu {
    private Residence subclaim;
    private VassalsPlayer vp;
    private Township town;
    private StateHandler<VassalsPlayer> state;


    public SubclaimMenu(Residence subclaim, Township town, VassalsPlayer vp, StateHandler state) {
        super("Subclaim Manager", vp, state.getPlugin());
        this.subclaim = subclaim;
        this.state = state;
        this.vp = vp;
        this.town = town;
        populateItems();
    }

    public void populateItems() {
        super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.SPRUCE_SIGN, ChatColor.GREEN + subclaim.getName(),
                "setName"), () -> {
            vp.getPlayer().sendTitle(ChatColor.GREEN + "Rename Mode Enabled",
                    ChatColor.GRAY +"Type New Name in Chat", 30, 30, 30);
        }));
        super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.TRIPWIRE_HOOK, ChatColor.GREEN + "Set Border",
                "setBorder"), () -> {
            vp.setSelectionMode(subclaim);
            OnVertexSelectionOpen playerSelect = new OnVertexSelectionOpen(subclaim, vp);
            Bukkit.getPluginManager().callEvent(playerSelect);
            Player player = vp.getPlayer();
            player.closeInventory();
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
            player.sendTitle(ChatColor.GREEN + "Selection Mode Enabled",
                    ChatColor.GRAY +"Right Click To Set a Vertex", 30, 30, 30);
            player.sendMessage(ChatColor.GREEN + "Selection Mode Enabled");
            player.sendMessage(ChatColor.GRAY + "Right Click To Set a Vertex");
            player.sendMessage(ChatColor.GRAY + "Left Click to Exit");
        }));
        super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.OBSERVER, ChatColor.GREEN + "Show Border",
                "showBorder"), () -> {
            subclaim.display(vp, vp.getPlayer());
        }));
        super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.WRITABLE_BOOK, ChatColor.GREEN + "Set Owner",
                "setOwner")));
        super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.PHANTOM_MEMBRANE, ChatColor.GREEN + "Current Height: " + subclaim.getMaxHeight(),
                "setHeight", Arrays.asList(new String[]{ChatColor.GREEN + "Left Click to Increase",
                        ChatColor.RED + "Right Click to Decrease"})), () -> {
            subclaim.setMaxHeight(subclaim.getMaxHeight() + 1);
        }, () -> {
            subclaim.setMaxHeight(subclaim.getMaxHeight() - 1);
        }));
        super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.GREEN_BANNER, ChatColor.GREEN + "Save Claim",
                "save"), () -> {
            try {
                if (subclaim.polygonSize() <= 2 || subclaim.getMaxHeight() == 0) {
                    throw new IllegalArgumentException("Please Fill Out All Fields");
                }
                vp.getSelected().addResidence(subclaim);
                Player player = vp.getPlayer();
                player.sendMessage(ChatColor.RED + vp.getSelected().getName());
                player.closeInventory();
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.5f);
                player.sendTitle(ChatColor.GREEN + "Residence Saved!",
                        ChatColor.GRAY +"You May Now See it In the Town Menu", 30, 30, 30);
                vp.clearTasks();
            } catch (Exception err) {
                vp.getPlayer().sendMessage(ChatColor.RED + err.getMessage());
            }
        }));

        super.refreshContents();
    }
}
