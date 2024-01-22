package org.puretripp.vassal.utils.interfaces;

public interface Invitable {
    public void addInvite(Inviter inviter);
    public void acceptInvite(Inviter inviter);
    public void rejectInvite(Inviter inviter);
    public Inviter getInvite(int index);
    public Inviter[] getAllInvites();

}
