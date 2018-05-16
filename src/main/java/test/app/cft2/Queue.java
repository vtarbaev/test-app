package test.app.cft2;

import java.util.concurrent.PriorityBlockingQueue;

public class Queue extends PriorityBlockingQueue<Item> {

    public Queue(int groupId) {
        this.groupId = groupId;
    }

    private int groupId;

    public int getGroupId() {
        return groupId;
    }

    @Override
    public boolean add(Item item) {
        return super.add(item);
    }
}
