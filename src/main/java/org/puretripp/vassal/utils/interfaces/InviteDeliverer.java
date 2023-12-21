package org.puretripp.vassal.utils.interfaces;

import org.puretripp.vassal.utils.general.VassalsPlayer;
import org.puretripp.vassal.utils.interfaces.Invitable;

import java.util.ArrayList;

public interface InviteDeliverer {
    void addToInviter(Invitable inv);
    void removeInvite(Invitable inv);
    void sendInvite(Invitable inv);

}
