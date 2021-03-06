package com.juzix.wallet.component.ui.contract;

import com.juzix.wallet.component.ui.base.IPresenter;
import com.juzix.wallet.component.ui.base.IView;
import com.juzix.wallet.entity.CandidateEntity;

/**
 * @author matrixelement
 */
public class NodeDetailContract {

    public interface View extends IView {

        /**
         * @return
         */
        CandidateEntity getCandidateEntityFromIntent();

        /**
         * 展示节点详情信息
         *
         * @param candidateEntity
         */
        void showNodeDetailInfo(CandidateEntity candidateEntity);

        /**
         * 展示实时票龄
         *
         * @param epoch
         */
        void showEpoch(long epoch);
    }

    public interface Presenter extends IPresenter<View> {

        /**
         * 获取实时的票龄
         */
        void getNodeDetailInfo();

        /**
         * 投票
         */
        void voteTicket();
    }
}
