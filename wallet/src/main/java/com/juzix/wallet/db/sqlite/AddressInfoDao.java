package com.juzix.wallet.db.sqlite;

import com.juzix.wallet.db.entity.AddressInfoEntity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddressInfoDao {

    public static List<AddressInfoEntity> getAddressInfoList() {
        List<AddressInfoEntity> list = new ArrayList<>();
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<AddressInfoEntity> results = realm.where(AddressInfoEntity.class)
                    .findAll();
            if (results != null) {
                list = realm.copyFromRealm(results);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            if (realm != null) {
                realm.cancelTransaction();
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return list;
    }

    public static String getAddressNameByAddress(String address) {
        String addressName = null;
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            AddressInfoEntity addressInfoEntity = realm.where(AddressInfoEntity.class)
                    .equalTo("address", address)
                    .findFirst();
            if (addressInfoEntity != null) {
                addressName = realm.copyFromRealm(addressInfoEntity).getName();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            if (realm != null) {
                realm.cancelTransaction();
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return addressName;
    }

    public static boolean insertAddressInfo(AddressInfoEntity entity) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(entity);
            realm.commitTransaction();
            return true;
        } catch (Exception exp) {
            if (realm != null) {
                realm.cancelTransaction();
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return false;
    }

    public static boolean updateAddressInfo(AddressInfoEntity oldAddressInfo, AddressInfoEntity newAddressInfo) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            if (oldAddressInfo.getAddress().equals(newAddressInfo.getAddress())) {
                //update
                realm.where(AddressInfoEntity.class)
                        .equalTo("address", oldAddressInfo.getAddress())
                        .findFirst()
                        .setName(newAddressInfo.getName());
            } else {
                //delete
                realm.where(AddressInfoEntity.class)
                        .equalTo("address", oldAddressInfo.getAddress())
                        .findAll()
                        .deleteFirstFromRealm();
                //insert
                realm.copyToRealmOrUpdate(newAddressInfo);

            }
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return false;

    }

    public static boolean updateNameWithAddress(String address, String name) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.where(AddressInfoEntity.class)
                    .equalTo("address", address)
                    .findFirst()
                    .setName(name);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return false;
    }

    public static boolean isExist(String address) {
        return getEntityWithAddress(address) != null;
    }

    public static AddressInfoEntity getEntityWithAddress(String address) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            AddressInfoEntity entity = realm.where(AddressInfoEntity.class)
                    .equalTo("address", address)
                    .findFirst();
            return realm.copyFromRealm(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        return null;
    }

    public static boolean deleteAddressInfo(String address) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.where(AddressInfoEntity.class)
                    .equalTo("address", address)
                    .findAll()
                    .deleteFirstFromRealm();
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            if (realm != null) {
                realm.cancelTransaction();
            }
        }finally {
            if (realm != null){
                realm.close();
            }
        }

        return false;
    }

}
