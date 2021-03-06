package com.juzix.wallet.event;

import android.content.Context;
import android.util.Log;

import com.juzhen.framework.network.NetState;
import com.juzix.wallet.entity.IndividualTransactionEntity;
import com.juzix.wallet.entity.NodeEntity;
import com.juzix.wallet.entity.RegionEntity;
import com.juzix.wallet.entity.SharedTransactionEntity;
import com.juzix.wallet.entity.SharedWalletEntity;
import com.juzix.wallet.entity.VoteTransactionEntity;
import com.juzix.wallet.entity.WalletEntity;

public class EventPublisher {

    private static final String TAG = "Portal.EventPublisher";

    public static final String ACTION_LOGIN = "com.juzix.wallet.ACTION_LOGIN";

    private Context context;

    private static EventPublisher instance = new EventPublisher();

    private EventPublisher() {
    }

    public static EventPublisher getInstance() {
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public static String getTag() {
        return TAG;
    }

    public void register(Object obj) {
        BusProvider.register(obj);
    }

    public void unRegister(Object obj) {
        BusProvider.unRegister(obj);
    }

    public void sendNetWorkStateChangedEvent(NetState netState) {
        BusProvider.post(new Event.NetWorkStateChangedEvent(netState));
    }

    public void sendUpdateSharedWalletTransactionEvent(SharedTransactionEntity sharedTransactionEntity) {
        Log.e(TAG, "sendUpdateSharedWalletTransactionEvent" + sharedTransactionEntity.toString());
        BusProvider.post(new Event.UpdateSharedWalletTransactionEvent(sharedTransactionEntity));
    }

    public void sendUpdateIndividualWalletTransactionEvent(IndividualTransactionEntity individualTransactionEntity) {
        BusProvider.post(new Event.UpdateIndividualWalletTransactionEvent(individualTransactionEntity));
    }

    public void sendUpdateCreateJointWalletProgressEvent(SharedWalletEntity sharedWalletEntity) {
        BusProvider.post(new Event.UpdateCreateJointWalletProgressEvent(sharedWalletEntity));
    }

    public void sendUpdateCandidateRegionInfoEvent(RegionEntity regionEntity) {
        BusProvider.post(new Event.UpdateCandidateRegionInfoEvent(regionEntity));
    }

    public void sendUpdateSelectedWalletEvent(WalletEntity entity) {
        BusProvider.post(new Event.UpdateSelectedWalletEvent(entity));
    }

    public void sendUpdateSharedWalletUnreadMessageEvent(String contractAddress, boolean hasUnreadMessage) {
        Log.e(TAG, contractAddress + ":" + hasUnreadMessage);
        BusProvider.post(new Event.UpdateSharedWalletUnreadMessageEvent(contractAddress, hasUnreadMessage));
    }

    public void sendUpdateTransactionUnreadMessageEvent(String uuid, boolean hasUnread) {
        BusProvider.post(new Event.UpdateTransactionUnreadMessageEvent(uuid, hasUnread));
    }

    public void sendUpdateWalletListEvent() {
        BusProvider.post(new Event.UpdateWalletListEvent());
    }

    public void sendUpdateAssetsTabEvent(int tabIndex) {
        BusProvider.post(new Event.UpdateAssetsTabEvent(tabIndex));
    }

    public void sendUpdateVoteTransactionListEvent(VoteTransactionEntity voteTransactionEntity) {
        BusProvider.post(new Event.UpdateVoteTransactionListEvent(voteTransactionEntity));
    }

    public void sendRemoveSharedWalletEvent(SharedWalletEntity sharedWalletEntity) {
        BusProvider.post(new Event.RemoveSharedWalletEvent(sharedWalletEntity));
    }

    public void sendNodeChangedEvent(NodeEntity nodeEntity) {
        BusProvider.post(new Event.NodeChangedEvent(nodeEntity));
    }
}
