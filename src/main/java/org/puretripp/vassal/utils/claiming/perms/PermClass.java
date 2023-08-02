package org.puretripp.vassal.utils.claiming.perms;

public class PermClass {
    private String name;
    private boolean canCreateResidence = false;
    private boolean canBreakBlocks = false;
    private boolean canPlaceBlocks = false;
    private boolean canAccessEconomyPanel = false;
    private boolean canRename = false;
    private boolean canManageClaimArea = false;
    private boolean canManagePolitics = false;
    private boolean canInvite = false;

    public PermClass(String name) {
        this.name = name;
    }

    public PermClass() {
        this.name = "Leader";
        this.canCreateResidence = true;
        this.canBreakBlocks = true;
        this.canPlaceBlocks = true;
        this.canAccessEconomyPanel = true;
        this.canRename = true;
        this.canManageClaimArea = true;
        this.canManagePolitics = true;
        this.canInvite = true;
    }

    public boolean isCanManagePolitics() {
        return canManagePolitics;
    }

    public void setCanManagePolitics(boolean canManagePolitics) {
        this.canManagePolitics = canManagePolitics;
    }

    public boolean isCanManageClaimArea() {
        return canManageClaimArea;
    }

    public void setCanManageClaimArea(boolean canManageClaimArea) {
        this.canManageClaimArea = canManageClaimArea;
    }

    public boolean isCanRename() {
        return canRename;
    }

    public void setCanRename(boolean canRename) {
        this.canRename = canRename;
    }

    public boolean isCanAccessEconomyPanel() {
        return canAccessEconomyPanel;
    }

    public void setCanAccessEconomyPanel(boolean canAccessEconomyPanel) {
        this.canAccessEconomyPanel = canAccessEconomyPanel;
    }

    public boolean isCanPlaceBlocks() {
        return canPlaceBlocks;
    }

    public void setCanPlaceBlocks(boolean canPlaceBlocks) {
        this.canPlaceBlocks = canPlaceBlocks;
    }

    public boolean isCanBreakBlocks() {
        return canBreakBlocks;
    }

    public void setCanBreakBlocks(boolean canBreakBlocks) {
        this.canBreakBlocks = canBreakBlocks;
    }

    public boolean isCanCreateResidence() {
        return canCreateResidence;
    }

    public void setCanCreateResidence(boolean canCreateResidence) {
        this.canCreateResidence = canCreateResidence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCanInvite() {
        return canInvite;
    }

    public void setCanInvite(boolean canInvite) {
        this.canInvite = canInvite;
    }
}
