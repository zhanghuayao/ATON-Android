package com.juzix.wallet.component.adapter;

import android.content.Context;
import android.widget.TextView;

import com.juzhen.framework.util.NumberParserUtils;
import com.juzix.wallet.R;
import com.juzix.wallet.component.adapter.base.ViewHolder;
import com.juzix.wallet.component.widget.AutoTextView;
import com.juzix.wallet.entity.VoteDetailItemEntity;
import com.juzix.wallet.utils.BigDecimalUtil;
import com.juzix.wallet.utils.DateUtil;

import java.util.List;

/**
 * @author matrixelement
 */
public class VoteDetailListAdapter extends CommonAdapter<VoteDetailItemEntity> {

    public VoteDetailListAdapter(int layoutId, List<VoteDetailItemEntity> datas) {
        super(layoutId, datas);
    }

    @Override
    protected void convert(Context context, ViewHolder viewHolder, VoteDetailItemEntity item, int position) {
        viewHolder.setText(R.id.tv_create_time, item.getCreateTime());
        viewHolder.setText(R.id.tv_valid_and_invalid_ticket, String.format("%s/%s", NumberParserUtils.getPrettyNumber(item.getValidVoteNum(), 0), NumberParserUtils.getPrettyNumber(item.getInvalidVoteNum(), 0)));
        viewHolder.setText(R.id.tv_ticket_price, context.getString(R.string.amount_with_unit, NumberParserUtils.getPrettyNumber(item.getTicketPrice(), 0)));
        viewHolder.setText(R.id.tv_vote_staked_and_unstaked, String.format("%s/%s", NumberParserUtils.getPrettyNumber(item.getVoteStaked(), 0), NumberParserUtils.getPrettyNumber(item.getVoteUnStaked(), 0)));
        viewHolder.setText(R.id.tv_vote_profit, context.getString(R.string.amount_with_unit, NumberParserUtils.getPrettyNumber(BigDecimalUtil.div(item.getProfit(), "1E18"), 4)));
        TextView tv = (AutoTextView) viewHolder.getView(R.id.tv_wallet_address_and_name);
        tv.setText(String.format("%s(%s)", item.getWalletAddress(), item.getWalletName()));
        boolean exceedExpireTime = DateUtil.parse(item.getExpireTime(), DateUtil.DATETIME_FORMAT_PATTERN_WITH_SECOND) > System.currentTimeMillis();
        viewHolder.setText(R.id.tv_time_desc, exceedExpireTime ? context.getString(R.string.estimatedTime) : context.getString(R.string.actualExpirationTime));
        viewHolder.setText(R.id.tv_expire_time, item.getExpireTime());
    }

}
