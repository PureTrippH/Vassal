package org.puretripp.vassal.utils.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface Invitable {
    public void addInvite(InviteDeliverer inviter);
    public void acceptInvite(InviteDeliverer inviter);
    public void rejectInvite(InviteDeliverer inviter);
    public InviteDeliverer getInvite(int index);
    public List<InviteDeliverer> getAllInvites();

}
