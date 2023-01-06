package org.puretripp.vassal.types;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.puretripp.vassal.types.governments.GovStyles;
import org.puretripp.vassal.types.townships.Township;
import org.puretripp.vassal.utils.VassalsPlayer;

import java.util.ArrayList;

public class Nation {

    private String name;
    private Material bannerType = Material.WHITE_BANNER;
    private BannerMeta banner;
    private int cityCount;
    private GovStyles style;

    private int nationBal;
    private ArrayList<VassalsPlayer> leaders;
    private ArrayList<VassalsPlayer> council;
    private int kill;

    public Nation(String name, BannerMeta banner, GovStyles style,
        ArrayList<VassalsPlayer> leaders, ArrayList<VassalsPlayer> council,
        ArrayList<Township> towns) {
        this.name = name;
        if(banner == null ) {
            this.banner = (BannerMeta) (new ItemStack(Material.WHITE_BANNER)).getItemMeta();
        } else {
            this.banner = banner;
        }
        this.cityCount = cityCount;
        this.style = style;
        this.leaders = leaders;
        this.council = council;
    }

    public String getName() { return name; }

    public void setBanner(BannerMeta banner) { this.banner = banner; }

    public void setBannerType(Material m) { this.bannerType = m; }
    public Material getBannerType() { return bannerType; }
    public BannerMeta getBanner() { return banner; }

}
