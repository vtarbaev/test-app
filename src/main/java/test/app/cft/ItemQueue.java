package test.app.cft;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemQueue extends PriorityBlockingQueue<Item> {

    private AtomicInteger count;
    private AtomicInteger processed;

    public ItemQueue() {
        this.count = new AtomicInteger(0);
        this.processed = new AtomicInteger(0);
    }

    @Override
    public void put(Item item) {
        super.put(item);
        this.count.incrementAndGet();
    }

    /*
    @Override
    public Item take() throws InterruptedException {
        Item item = super.take();
        this.count.decrementAndGet();
        return item;
    }
    */

    @Override
    public Item poll(long timeout, TimeUnit unit) throws InterruptedException {
        try {
            Item result = super.poll(timeout, unit);
            this.count.decrementAndGet();
            this.processed.incrementAndGet();
            return result;
        } catch (InterruptedException e) {
            return null;
        }
    }

    public int getProcessed() {
        return processed.get();
    }

    public void resetProcessed() {
        this.processed.set(0);
    }

}
