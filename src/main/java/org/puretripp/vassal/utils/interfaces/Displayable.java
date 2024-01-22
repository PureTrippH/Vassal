package org.puretripp.vassal.utils.interfaces;

import org.bukkit.scheduler.BukkitRunnable;
import org.puretripp.vassal.utils.general.VassalsPlayer;

import java.util.ArrayList;
import java.util.List;

public interface Displayable<E> {
    List<E> display(VassalsPlayer vp);
    void destroyDisplay(List<E> list, VassalsPlayer vp);
}
