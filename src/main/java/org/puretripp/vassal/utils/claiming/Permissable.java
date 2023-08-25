package org.puretripp.vassal.utils.claiming;

import org.bukkit.entity.Player;
import org.puretripp.vassal.utils.claiming.perms.PermClass;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public interface Permissable {
    ArrayList<PermClass> permClasses = new ArrayList<PermClass>();
    HashMap<VassalsPlayer, PermClass> permMap = new HashMap<VassalsPlayer, PermClass>();

    void addPlayer(VassalsPlayer vp, PermClass pc);
    void setPlayerRank(VassalsPlayer vp, PermClass pc);
    PermClass getRank(VassalsPlayer vp);
    Set<Player> getPlayersObjs();
    Set<UUID> getPlayersUUID();
    void addPermClass(PermClass pc);
    PermClass removePermClass(PermClass pc);


}
