package org.puretripp.vassal.utils.general;

import java.util.ArrayList;

public interface InviteDeliverer {
    ArrayList<VassalsPlayer> members = null;
    ArrayList<VassalsPlayer> invites = null;


    void addPlayer(Invitable inv);
    void removeInvite(Invitable inv);
    void sendInvite(Invitable inv);

}
