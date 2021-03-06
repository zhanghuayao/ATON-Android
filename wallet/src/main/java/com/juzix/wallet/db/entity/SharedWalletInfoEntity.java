package com.juzix.wallet.db.entity;

import com.juzix.wallet.entity.OwnerEntity;
import com.juzix.wallet.entity.SharedWalletEntity;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SharedWalletInfoEntity extends RealmObject {
    //唯一识别码
    @PrimaryKey
    private String uuid;
    /**
     * 钱包名称
     */
    private String name;
    /**
     * 创建者地址
     */
    private String creatorAddress;
    /**
     * 合约地址
     */
    private String contractAddress;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 共享钱包所有人
     */
    private RealmList<OwnerInfoEntity> owner;
    /**
     * 更新时间
     */
    private long updateTime;
    /**
     * 所需签名数
     */
    private int requiredSignNumber;
    /**
     * 钱包头图
     */
    private String avatar;
    /**
     * 节点地址
     */
    private String nodeAddress;

    public SharedWalletInfoEntity() {
    }

    private SharedWalletInfoEntity(Builder builder) {
        setUuid(builder.uuid);
        setName(builder.name);
        setCreatorAddress(builder.creatorAddress);
        setContractAddress(builder.address);
        setCreateTime(builder.createTime);
        setUpdateTime(builder.updateTime);
        setOwnerArrayList(builder.owner);
        setRequiredSignNumber(builder.requiredSignNumber);
        setAvatar(builder.avatar);
        setNodeAddress(builder.nodeAddress);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorAddress() {
        return creatorAddress;
    }

    public void setCreatorAddress(String creatorAddress) {
        this.creatorAddress = creatorAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public RealmList<OwnerInfoEntity> getOwner() {
        return owner;
    }

    public void setOwner(RealmList<OwnerInfoEntity> owner) {
        this.owner = owner;
    }

    public ArrayList<OwnerInfoEntity> getOwnerArrayList() {
        ArrayList<OwnerInfoEntity> addressInfoEntities = new ArrayList<>();
        if (this.owner == null) {
            return addressInfoEntities;
        }
        for (OwnerInfoEntity infoEntity : this.owner) {
            addressInfoEntities.add(infoEntity);
        }
        return addressInfoEntities;
    }

    public void setOwnerArrayList(ArrayList<OwnerInfoEntity> owner) {
        if (owner == null) {
            return;
        }
        this.owner = new RealmList<>();
        for (OwnerInfoEntity addressInfoEntity : owner) {
            this.owner.add(addressInfoEntity);
        }
    }

    public int getRequiredSignNumber() {
        return requiredSignNumber;
    }

    public void setRequiredSignNumber(int requiredSignNumber) {
        this.requiredSignNumber = requiredSignNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNodeAddress() {
        return nodeAddress;
    }

    public void setNodeAddress(String nodeAddress) {
        this.nodeAddress = nodeAddress;
    }

    public SharedWalletEntity buildWalletEntity() {
        SharedWalletEntity.Builder builder = new SharedWalletEntity.Builder();
        builder.uuid(uuid);
        builder.name(name);
        builder.contractAddress(contractAddress);
        builder.creatorAddress(creatorAddress);
        builder.createTime(createTime);
        builder.updateTime(updateTime);
        builder.owner(buildAddressEntityList());
        builder.requiredSignNumber(requiredSignNumber);
        builder.avatar(avatar);
        builder.finished(true);
        builder.nodeAddress(nodeAddress);
        return builder.build();
    }

    public ArrayList<OwnerEntity> buildAddressEntityList() {
        ArrayList<OwnerEntity> addressEntityArrayList = new ArrayList<>();
        for (OwnerInfoEntity entity : owner) {
            addressEntityArrayList.add(new OwnerEntity(entity.getUuid(), entity.getName(), entity.getAddress(),entity.getNodeAddress()));
        }
        return addressEntityArrayList;
    }

    @Override
    public String toString() {
        return "SharedWalletInfoEntity{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", creatorAddress='" + creatorAddress + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", createTime=" + createTime +
                ", owner=" + owner +
                ", updateTime=" + updateTime +
                ", requiredSignNumber=" + requiredSignNumber +
                ", avatar='" + avatar + '\'' +
                ", nodeAddress='" + nodeAddress + '\'' +
                '}';
    }

    public static final class Builder {
        private String uuid;
        private String name;
        private String creatorAddress;
        private String address;
        private long createTime;
        private long updateTime;
        private ArrayList<OwnerInfoEntity> owner;
        private int requiredSignNumber;
        private String avatar;
        private String nodeAddress;

        public Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder creatorAddress(String val) {
            creatorAddress = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder createTime(long val) {
            createTime = val;
            return this;
        }

        public Builder updateTime(long val) {
            updateTime = val;
            return this;
        }

        public Builder owner(ArrayList<OwnerInfoEntity> val) {
            owner = val;
            return this;
        }

        public Builder requiredSignNumber(int val) {
            requiredSignNumber = val;
            return this;
        }

        public Builder avatar(String val) {
            avatar = val;
            return this;
        }

        public Builder nodeAddress(String val) {
            nodeAddress = val;
            return this;
        }

        public SharedWalletInfoEntity build() {
            return new SharedWalletInfoEntity(this);
        }
    }
}
