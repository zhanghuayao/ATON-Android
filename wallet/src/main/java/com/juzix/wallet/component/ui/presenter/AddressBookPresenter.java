package com.juzix.wallet.component.ui.presenter;

import com.juzix.wallet.component.ui.base.BaseActivity;
import com.juzix.wallet.component.ui.base.BasePresenter;
import com.juzix.wallet.component.ui.contract.AddressBookContract;
import com.juzix.wallet.component.ui.view.AddNewAddressActivity;
import com.juzix.wallet.db.entity.AddressInfoEntity;
import com.juzix.wallet.db.sqlite.AddressInfoDao;
import com.juzix.wallet.entity.AddressEntity;
import com.juzix.wallet.utils.CommonUtil;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author matrixelement
 */
public class AddressBookPresenter extends BasePresenter<AddressBookContract.View> implements AddressBookContract.Presenter {

    private List<AddressEntity> addressEntityList;

    public AddressBookPresenter(AddressBookContract.View view) {
        super(view);
    }

    @Override
    public void fetchAddressList() {
        Flowable.fromIterable(AddressInfoDao.getAddressInfoList()).filter(new Predicate<AddressInfoEntity>() {
            @Override
            public boolean test(AddressInfoEntity addressInfoEntity) throws Exception {
                return addressInfoEntity != null;
            }
        }).compose(((BaseActivity) getView()).bindToLifecycle()).map(new Function<AddressInfoEntity, AddressEntity>() {
            @Override
            public AddressEntity apply(AddressInfoEntity addressInfoEntity) throws Exception {
                return new AddressEntity(addressInfoEntity.getUuid(), addressInfoEntity.getName(),addressInfoEntity.getAddress(),addressInfoEntity.getAvatar());
            }
        }).toList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BiConsumer<List<AddressEntity>, Throwable>() {
            @Override
            public void accept(List<AddressEntity> addressEntities, Throwable throwable) throws Exception {
                addressEntityList = addressEntities;
                if (isViewAttached()) {
                    getView().notifyAddressListChanged(addressEntities);
                }
            }
        });
    }

    @Override
    public void deleteAddress(int position) {

        Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (addressEntityList != null && addressEntityList.size() > position) {
                    return AddressInfoDao.deleteAddressInfo(addressEntityList.get(position).getAddress());
                } else {
                    return false;
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean.booleanValue()) {
                    fetchAddressList();
                }
            }
        });
    }

    @Override
    public void editAddress(int position) {
        if (addressEntityList != null && addressEntityList.size() > position) {
            AddNewAddressActivity.actionStartWithExtraForResult(currentActivity(), addressEntityList.get(position));
        }
    }

    @Override
    public void selectAddress(int position) {
        if (addressEntityList != null && addressEntityList.size() > position) {
            CommonUtil.copyTextToClipboard(currentActivity(), addressEntityList.get(position).getAddress());
        }
    }
}
