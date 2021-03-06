package com.juzix.wallet.component.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juzix.wallet.R;
import com.juzix.wallet.app.Constants;
import com.juzix.wallet.component.adapter.SelectAddressListAdapter;
import com.juzix.wallet.component.ui.base.MVPBaseActivity;
import com.juzix.wallet.component.ui.contract.SelectAddressContract;
import com.juzix.wallet.component.ui.presenter.SelectAddressPresenter;
import com.juzix.wallet.component.widget.CommonTitleBar;
import com.juzix.wallet.entity.AddressEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author matrixelement
 */
public class SelectAddressActivity extends MVPBaseActivity<SelectAddressPresenter> implements SelectAddressContract.View {

    @BindView(R.id.list_wallet_address)
    ListView listWalletAddress;

    @BindView(R.id.layout_no_data)
    View emptyView;
    @BindView(R.id.commonTitleBar)
    CommonTitleBar commonTitleBar;

    private Unbinder unbinder;
    private SelectAddressListAdapter addressBookListAdapter;

    @Override
    protected SelectAddressPresenter createPresenter() {
        return new SelectAddressPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        unbinder = ButterKnife.bind(this);
        initViews();
        mPresenter.fetchAddressList();
    }

    private void initViews() {

        addressBookListAdapter = new SelectAddressListAdapter(R.layout.item_wallet_address_list, null);

        listWalletAddress.setAdapter(addressBookListAdapter);

        listWalletAddress.setEmptyView(emptyView);

        listWalletAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.selectAddress(position);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case Constants.RequestCode.REQUEST_CODE_ADD_ADDRESS:
                case Constants.RequestCode.REQUEST_CODE_EDIT_ADDRESS:
                    mPresenter.fetchAddressList();
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void notifyAddressListChanged(List<AddressEntity> addressEntityList) {
        addressBookListAdapter.notifyDataChanged(addressEntityList);
    }

    @Override
    public void setResult(AddressEntity addressEntity) {
        Intent intent = new Intent(this, SelectAddressActivity.class);
        intent.putExtra(Constants.Extra.EXTRA_ADDRESS, addressEntity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public String getAction() {
        return getIntent().getAction();
    }

    public static void actionStartForResult(Context context, String action, int requestCode) {
        Intent intent = new Intent(context, SelectAddressActivity.class);
        intent.setAction(action);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}
