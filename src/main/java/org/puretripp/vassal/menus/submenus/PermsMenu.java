package org.puretripp.vassal.menus.submenus;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.lustrouslib.menu.Menu;
import org.lustrouslib.menu.MenuIcon;
import org.lustrouslib.menu.interfaces.GUIMenu;
import org.lustrouslib.wrapper.StateHandler;
import org.puretripp.vassal.main.Main;
import org.puretripp.vassal.types.Nation;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.general.VassalWorld;
import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.interfaces.PermissionHolder;

import java.util.UUID;

public class PermsMenu extends Menu implements GUIMenu {
    private Township town;
    private StateHandler<VassalsPlayer> state;
    private Nation nation;
    private VassalsPlayer target;

    public PermsMenu(boolean isNation, Nation nation, Township town, VassalsPlayer target, StateHandler state) {
        super("Permissions Menu", target, state.getPlugin());
        this.nation = nation;
        this.state = state;
        this.town = town;
        this.target = target;
        populateItems();
    }

    public void populateItems() {
        ItemStack add = MenuIcon.generateCustomSkull(ChatColor.GREEN + "Create Rank", "createMenu",
                null, "790f62ec5fa2e93e67cf1e00db8af4b47ac7ac769aa09a203a1f575a12710b10");
        ItemStack list = MenuIcon.generateCustomSkull(ChatColor.GREEN + "Create Rank", "createMenu",
                null, "8a16038bc8e6518afa91498dab7675c01cb31a125d21c49b861294d39e1c560c");
        ItemStack promote = MenuIcon.generateCustomSkull(ChatColor.GREEN + "Promote Players", "createMenu",
                null, "1c8e0cfebc7f9c7e16fbaaae025d1b1d19d5ee633666bcf25fa0b40d5bd21bcd");
        super.contents.add(new MenuIcon(add, () -> {
            target.pushMenu(new PermClassEditor(target, town, new PermClass("Test"), state));
        }));
        super.contents.add(new MenuIcon(list));
        super.contents.add(new MenuIcon(promote));
        super.refreshContents();
    }

    public void open(VassalsPlayer p) {
        Bukkit.getPlayer(p.getUUID()).openInventory(this.inv);
    }


    private class PlayerList extends Menu {
        private Township town;
        private Nation nation;
        private VassalsPlayer target;
        private NamespacedKey key = new NamespacedKey(Main.getPlugin(Main.class), "iconClickFunction");

        public PlayerList(boolean isNation, VassalsPlayer user, Nation n, Township t, StateHandler state) {
            super("Permission Menu", user, state.getPlugin());
            this.nation = n;
            this.town = t;
            populateItems();
        }

        public void populateItems() {
            for(UUID uuid : town.getPlayers()) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
                super.contents.add(new MenuIcon(MenuIcon.generateSkull(ChatColor.RED + p.getName(), ("Perms_" + p.getUniqueId()),
                        null, uuid)));
            }
            super.refreshContents();
        }

        @EventHandler
        public void invClick(final InventoryClickEvent e) {
            if (!e.getInventory().equals(inv)) return;
            if (e.getCurrentItem() == null) return;
            if (e.getCurrentItem().getType() != Material.PLAYER_HEAD) return;
            SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if((data.get(key, PersistentDataType.STRING).substring(0, 6)).equals("Perms_")) {
                e.getWhoClicked().sendMessage(data.get(key, PersistentDataType.STRING));
                OfflinePlayer p = Bukkit.getOfflinePlayer(
                        UUID.fromString(data.get(key, PersistentDataType.STRING).substring(6)));
                target = VassalWorld.getWorldInstance().onlinePlayers.get(e.getWhoClicked().getUniqueId());
            }
        }
    }

    private class PermClassEditor extends Menu {
        private PermissionHolder holder;
        private VassalsPlayer user;
        private PermClass perm;

        public PermClassEditor(VassalsPlayer user, PermissionHolder holder, PermClass perm, StateHandler state) {
            super("Permission Editor", user, state.getPlugin());
            this.holder = holder;
            this.perm = perm;
            this.user = user;
            this.populateItems();
        }

        public void populateItems() {
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.DIAMOND_PICKAXE,
                    "Can Break Blocks: " + ChatColor.RED + perm.isCanBreakBlocks(), null), () -> {
                perm.setCanBreakBlocks(!perm.isCanBreakBlocks());
                user.getPlayer().playSound(user.getPlayer(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 1.0f);
                super.refreshContents();
            }));
            super.contents.add(new MenuIcon(MenuIcon.generateItem(Material.GRASS_BLOCK,
                    "Can Place Blocks: " + ChatColor.RED + perm.isCanPlaceBlocks(), null), () -> {
                perm.setCanBreakBlocks(!perm.isCanPlaceBlocks());
                user.getPlayer().playSound(user.getPlayer(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 1.0f);
                super.refreshContents();
            }));
            super.refreshContents();
        }
    }
}