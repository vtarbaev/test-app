package test.app.cft;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class MainProcessor implements Runnable {

    private ItemQueue mainQueue;
    private int processorCount;
    private int groupCount;

    private ItemQueue[] queues;

    private ReentrantLock lock = new ReentrantLock();

    private ConcurrentLinkedQueue<String> LOG = new ConcurrentLinkedQueue();

    MainProcessor(ItemQueue mainQueue, int groupCount, int processorCount) {
        this.mainQueue = mainQueue;
        this.processorCount = processorCount;
        this.groupCount = groupCount;
    }

    @Override
    public void run() {
        queues = new ItemQueue[groupCount];
        for (int i = 0; i < groupCount; i++) {
            queues[i] = new ItemQueue();
        }

        ExecutorService mainService = Executors.newFixedThreadPool(processorCount);



        int a = 0;

        while (true) {
            try {
                Thread.sleep(1);
                Item item = mainQueue.take();
                if (item.getGroupId() == -1) {

                    for (int i = 0; i < groupCount; i++) {
                        queues[i].add(new StopItem(item.getId(), i));
                    }

                    break;
                }
                int groupIndex = item.getGroupId();
                queues[groupIndex].put(item);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        for (int i = 0; i < processorCount; i++) {
            mainService.submit(new ItemProcessor(i));
        }

        for (String s : LOG) {
            System.out.println(s);
        }

        mainService.shutdown();
    }



    private int getIndex(final int queueIndex) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = groupCount - 1;
            if (queues[queueIndex].getProcessed() > 10) {
                ItemQueue temp = queues[queueIndex];
                queues[queueIndex] = queues[processorCount];
                queues[queueIndex].resetProcessed();
                for (int i = processorCount; i < n; i++) {
                    queues[i] = queues[i + 1];
                }
                queues[n] = temp;
            }
            return queueIndex;
        } finally {
            lock.unlock();
        }
    }

    private class ItemProcessor implements Runnable {

        private int queueIndex;

        ItemProcessor(int queueIndex) {
            this.queueIndex = queueIndex;
        }

        @Override
        public void run() {
            System.out.println("Start " + Thread.currentThread().getName());
            while (true) {
                try {
                    queueIndex = getIndex(queueIndex);
                    Item item = queues[queueIndex].poll(5, TimeUnit.MILLISECONDS);
                    if (item != null) {
                        if (item.isEnd()) {
                            System.out.println("stop command");
                            break;
                        } else {
                            LOG.add(String.format("%d %d %d", queueIndex, item.getId(), item.getGroupId()));
                        }
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("Stop " + Thread.currentThread().getName());
        }

    }

}
