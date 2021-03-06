package com.juzix.wallet.engine;

import android.text.TextUtils;

import com.juzix.wallet.app.Constants;
import com.juzix.wallet.app.SchedulersTransformer;
import com.juzix.wallet.config.AppSettings;
import com.juzix.wallet.entity.NodeEntity;
import com.juzix.wallet.event.EventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author matrixelement
 */
public class NodeManager {

    private final static String TAG = NodeManager.class.getSimpleName();
    private final static String[] DEFAULT_NODE_URL_LIST = new String[]{Constants.URL.URL_TEST_A, Constants.URL.URL_TEST_B};

    private NodeEntity curNode;
    private NodeService nodeService;

    private NodeManager() {

    }

    private static class InstanceHolder {
        private static volatile NodeManager INSTANCE = new NodeManager();
    }

    public static NodeManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public NodeEntity getCurNode() {
        return curNode;
    }

    public String getCurNodeAddress() {
        return curNode == null || TextUtils.isEmpty(curNode.getNodeAddress()) ? AppSettings.getInstance().getCurrentNodeAddress() : curNode.getNodeAddress();
    }

    public void setCurNode(NodeEntity curNode) {
        this.curNode = curNode;
    }

    public void init() {

        nodeService = new NodeService();

        Flowable
                .fromIterable(buildDefaultNodeList())
                .map(new Function<NodeEntity, NodeEntity>() {
                    @Override
                    public NodeEntity apply(NodeEntity nodeEntity) throws Exception {
                        return getInsertNode(nodeEntity).blockingGet();
                    }
                })
                .filter(new Predicate<NodeEntity>() {
                    @Override
                    public boolean test(NodeEntity nodeEntity) throws Exception {
                        return !nodeEntity.isNull();
                    }
                })
                .toList()
                .map(new Function<List<NodeEntity>, Boolean>() {
                    @Override
                    public Boolean apply(List<NodeEntity> nodeEntities) throws Exception {
                        return nodeService.insertNode(nodeEntities).blockingGet();
                    }
                })
                .map(new Function<Boolean, NodeEntity>() {
                    @Override
                    public NodeEntity apply(Boolean aBoolean) throws Exception {
                        return getCheckedNode().blockingGet();
                    }
                })
                .filter(new Predicate<NodeEntity>() {
                    @Override
                    public boolean test(NodeEntity nodeEntity) throws Exception {
                        return !nodeEntity.isNull();
                    }
                })
                .toSingle()
                .compose(new SchedulersTransformer())
                .subscribe(new Consumer<NodeEntity>() {
                    @Override
                    public void accept(NodeEntity nodeEntity) throws Exception {
                        switchNode(nodeEntity);
                        EventPublisher.getInstance().sendNodeChangedEvent(nodeEntity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public void switchNode(NodeEntity nodeEntity) {
        setCurNode(nodeEntity);
        AppSettings.getInstance().setCurrentNodeAddress(nodeEntity.getNodeAddress());
        Web3jManager.getInstance().init(nodeEntity.getNodeAddress());
    }

    public Single<List<NodeEntity>> getNodeList() {
        return nodeService.getNodeList();
    }

    public Single<NodeEntity> getCheckedNode() {
        return nodeService.getNode(true);
    }

    public Single<Boolean> insertNodeList(List<NodeEntity> nodeEntityList) {
        return nodeService.insertNode(nodeEntityList);
    }

    public Single<Boolean> deleteNode(NodeEntity nodeEntity) {
        return nodeService.deleteNode(nodeEntity.getId());
    }

    public Single<Boolean> updateNode(NodeEntity nodeEntity, boolean isChecked) {
        return nodeService.updateNode(nodeEntity.getId(), isChecked);
    }

    private List<NodeEntity> buildDefaultNodeList() {

        List<NodeEntity> nodeInfoEntityList = new ArrayList<>();

        NodeEntity nodeEntity = null;

        for (int i = 0; i < DEFAULT_NODE_URL_LIST.length; i++) {
            nodeEntity = new NodeEntity.Builder()
                    .id(UUID.randomUUID().hashCode())
                    .nodeAddress(DEFAULT_NODE_URL_LIST[i])
                    .isDefaultNode(true)
                    .isChecked(i == 0)
                    .build();
            nodeInfoEntityList.add(nodeEntity);
        }
        return nodeInfoEntityList;
    }


    private Single<NodeEntity> getInsertNode(NodeEntity nodeEntity) {
        return Single.create(new SingleOnSubscribe<NodeEntity>() {
            @Override
            public void subscribe(SingleEmitter<NodeEntity> emitter) throws Exception {
                List<NodeEntity> nodeEntityList = nodeService.getNode(nodeEntity.getNodeAddress()).blockingGet();
                if (nodeEntityList == null || nodeEntityList.isEmpty()) {
                    emitter.onSuccess(nodeEntity);
                } else {
                    emitter.onSuccess(NodeEntity.createNullNode());
                }
            }
        });
    }

}
