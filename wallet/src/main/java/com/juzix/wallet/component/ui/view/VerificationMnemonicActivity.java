package com.juzix.wallet.component.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.juzhen.framework.util.AndroidUtil;
import com.juzix.wallet.R;
import com.juzix.wallet.app.Constants;
import com.juzix.wallet.component.ui.base.MVPBaseActivity;
import com.juzix.wallet.component.ui.contract.VerificationMnemonicContract;
import com.juzix.wallet.component.ui.dialog.CommonTipsDialogFragment;
import com.juzix.wallet.component.ui.dialog.OnDialogViewClickListener;
import com.juzix.wallet.component.ui.presenter.VerificationMnemonicPresenter;
import com.juzix.wallet.component.widget.CommonTitleBar;
import com.juzix.wallet.component.widget.ShadowButton;
import com.juzix.wallet.entity.IndividualWalletEntity;

import java.util.ArrayList;

public class VerificationMnemonicActivity extends MVPBaseActivity<VerificationMnemonicPresenter> implements VerificationMnemonicContract.View, View.OnClickListener {

    private final static String       TAG = VerificationMnemonicActivity.class.getSimpleName();
    private              TextView     mTvMnemonic1;
    private              TextView     mTvMnemonic2;
    private              TextView     mTvMnemonic3;
    private              TextView     mTvMnemonic4;
    private              TextView     mTvMnemonic5;
    private              TextView     mTvMnemonic6;
    private              TextView     mTvMnemonic7;
    private              TextView     mTvMnemonic8;
    private              TextView     mTvMnemonic9;
    private              TextView     mTvMnemonic10;
    private              TextView     mTvMnemonic11;
    private              TextView     mTvMnemonic12;
    private              ShadowButton mBtnSubmit;
    private              Button       mBtnEmpty;

    public static void actionStart(Context context, String password, IndividualWalletEntity walletEntity, int type) {
        Intent intent = new Intent(context, VerificationMnemonicActivity.class);
        intent.putExtra(Constants.Extra.EXTRA_PASSWORD, password);
        intent.putExtra(Constants.Extra.EXTRA_WALLET, walletEntity);
        intent.putExtra(Constants.Extra.EXTRA_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected VerificationMnemonicPresenter createPresenter() {
        return new VerificationMnemonicPresenter(this);
    }

    @Override
    public String getPasswordFromIntent() {
        return getIntent().getStringExtra(Constants.Extra.EXTRA_PASSWORD);
    }

    @Override
    public IndividualWalletEntity getWalletFromIntent() {
        return getIntent().getParcelableExtra(Constants.Extra.EXTRA_WALLET);
    }

    @Override
    public void setCompletedBtnEnable(boolean enabled) {
        mBtnSubmit.setEnabled(enabled);
    }

    @Override
    public void setClearBtnEnable(boolean enabled) {
        mBtnEmpty.setEnabled(enabled);
        mBtnEmpty.setTextColor(ContextCompat.getColor(getContext(), enabled ? R.color.color_105cfe : R.color.color_d8d8d8));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_mnemonic);
        initView();

        mPresenter.init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit(){
        CommonTipsDialogFragment.createDialogWithTwoButton(ContextCompat.getDrawable(getContext(), R.drawable.icon_dialog_tips),
                string(R.string.backup_exit_tips),
                string(R.string.confirm),
                new OnDialogViewClickListener() {
                    @Override
                    public void onDialogViewClick(DialogFragment fragment, View view, Bundle extra) {
                        if (fragment != null){
                            fragment.dismiss();
                        }
                        if (getIntent().getIntExtra(Constants.Extra.EXTRA_TYPE, 0) == 0) {
                            MainActivity.actionStart(VerificationMnemonicActivity.this);
                        }
                        VerificationMnemonicActivity.this.finish();
                    }
                },string(R.string.cancel),
                new OnDialogViewClickListener() {
                    @Override
                    public void onDialogViewClick(DialogFragment fragment, View view, Bundle extra) {
                        if (fragment != null) {
                            fragment.dismiss();
                        }
                    }
                }).show(getSupportFragmentManager(), "showTips");
    }


    private void initView() {
        ((CommonTitleBar)findViewById(R.id.commonTitleBar)).setLeftDrawableClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               exit();
            }
        });
        mBtnSubmit = findViewById(R.id.sbtn_submit);
        mBtnEmpty = findViewById(R.id.btn_empty);
        mBtnSubmit.setOnClickListener(this);
        mBtnEmpty.setOnClickListener(this);
        mTvMnemonic1 = findViewById(R.id.tv_mnemonic1);
        mTvMnemonic2 = findViewById(R.id.tv_mnemonic2);
        mTvMnemonic3 = findViewById(R.id.tv_mnemonic3);
        mTvMnemonic4 = findViewById(R.id.tv_mnemonic4);
        mTvMnemonic5 = findViewById(R.id.tv_mnemonic5);
        mTvMnemonic6 = findViewById(R.id.tv_mnemonic6);
        mTvMnemonic7 = findViewById(R.id.tv_mnemonic7);
        mTvMnemonic8 = findViewById(R.id.tv_mnemonic8);
        mTvMnemonic9 = findViewById(R.id.tv_mnemonic9);
        mTvMnemonic10 = findViewById(R.id.tv_mnemonic10);
        mTvMnemonic11 = findViewById(R.id.tv_mnemonic11);
        mTvMnemonic12 = findViewById(R.id.tv_mnemonic12);
        addMnemonicListener();
    }

    @Override
    public void showBottomList(ArrayList<VerificationMnemonicContract.DataEntity> list) {
        FlexboxLayout flAll = findViewById(R.id.fl_all);
        flAll.removeAllViews();
        for (int i = 0;i < list.size();i++){
            VerificationMnemonicContract.DataEntity dataEntity = list.get(i);
            flAll.addView(createAllItemView(i, dataEntity));
        }
    }

    @Override
    public void showTopList(VerificationMnemonicContract.DataEntity[] list) {
        for (int i  = 0; i < list.length; i++){
            setCheckedView(i, list[i]);
        }
    }

    private TextView createAllItemView(int position, VerificationMnemonicContract.DataEntity dataEntity){
        TextView textView = new TextView(this);
        textView.setText(dataEntity.getMnemonic());
        textView.setGravity(Gravity.CENTER);
        textView.setAllCaps(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        if (!dataEntity.isChecked()){
            textView.setBackgroundResource(R.drawable.bg_shape_verify_mnemonic_n);
            textView.setTextColor(ContextCompat.getColor(this, R.color.color_316def));
        }else {
            textView.setBackgroundResource(R.drawable.bg_shape_verify_mnemonic_h);
            textView.setTextColor(ContextCompat.getColor(this, R.color.color_b6bbd0));
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.checkBottomListItem(position);
            }
        });
        int paddingLeftAndRight = AndroidUtil.dip2px(this, 12f);
        int paddingTopAndBottom = 0;
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, paddingTopAndBottom, paddingLeftAndRight, paddingTopAndBottom);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                AndroidUtil.dip2px(this, 24));
        int marginRight = AndroidUtil.dip2px(this, 10);
        int marginTop = AndroidUtil.dip2px(this, 12);
        layoutParams.setMargins(0, marginTop, marginRight, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private void addMnemonicListener(){
        mTvMnemonic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(0);
            }
        });
        mTvMnemonic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(1);
            }
        });
        mTvMnemonic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(2);
            }
        });
        mTvMnemonic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(3);
            }
        });
        mTvMnemonic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(4);
            }
        });
        mTvMnemonic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(5);
            }
        });
        mTvMnemonic7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(6);
            }
        });
        mTvMnemonic8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(7);
            }
        });
        mTvMnemonic9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(8);
            }
        });
        mTvMnemonic10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(9);
            }
        });
        mTvMnemonic11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(10);
            }
        });
        mTvMnemonic12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkTopListItem(11);
            }
        });
    }

    private void setCheckedView(int position, VerificationMnemonicContract.DataEntity dataEntity){
        String mnemonic = dataEntity == null ? "" : dataEntity.getMnemonic();
        switch (position){
            case 0:
                mTvMnemonic1.setText(mnemonic);
                break;
            case 1:
                mTvMnemonic2.setText(mnemonic);
                break;
            case 2:
                mTvMnemonic3.setText(mnemonic);
                break;
            case 3:
                mTvMnemonic4.setText(mnemonic);
                break;
            case 4:
                mTvMnemonic5.setText(mnemonic);
                break;
            case 5:
                mTvMnemonic6.setText(mnemonic);
                break;
            case 6:
                mTvMnemonic7.setText(mnemonic);
                break;
            case 7:
                mTvMnemonic8.setText(mnemonic);
                break;
            case 8:
                mTvMnemonic9.setText(mnemonic);
                break;
            case 9:
                mTvMnemonic10.setText(mnemonic);
                break;
            case 10:
                mTvMnemonic11.setText(mnemonic);
                break;
            case 11:
                mTvMnemonic12.setText(mnemonic);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sbtn_submit:
                mPresenter.submit();
                break;
            case R.id.btn_empty:
                mPresenter.emptyChecked();
                break;
        }
    }

    @Override
    public void showDisclaimerDialog(){
        CommonTipsDialogFragment.createDialogWithTitleAndOneButton(ContextCompat.getDrawable(this, R.drawable.icon_dialog_tips), string(R.string.disclaimer), string(R.string.disclaimerResume), string(R.string.understood), new OnDialogViewClickListener() {
            @Override
            public void onDialogViewClick(DialogFragment fragment, View view, Bundle extra) {
                if (getIntent().getIntExtra(Constants.Extra.EXTRA_TYPE, 0) == 0) {
                    MainActivity.actionStart(VerificationMnemonicActivity.this);
                }
                VerificationMnemonicActivity.this.finish();
            }
        }).show(getSupportFragmentManager(), "showDisclaimer");
    }

    @Override
    public void showBackupFailedDialog(){
        CommonTipsDialogFragment.createDialogWithTitleAndOneButton(ContextCompat.getDrawable(this, R.drawable.icon_dialog_tips), string(R.string.backupFailed), string(R.string.backupMnemonicFailedResume), string(R.string.understood), new OnDialogViewClickListener() {
            @Override
            public void onDialogViewClick(DialogFragment fragment, View view, Bundle extra) {
                if (fragment != null){
                    fragment.dismiss();
                }
            }
        }).show(getSupportFragmentManager(), "showBackupFaile");
    }
}
