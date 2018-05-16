package test.app.cft2;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Router implements Runnable {

    private ReentrantLock lock;
    private Queue sourceQueue;
    private int groupCount;
    private int processCount;

    private Queue[] groupQueues;
    private int[] links;

    public Router(Queue sourceQueue, int groupCount, int processCount) {
        this.sourceQueue = sourceQueue;
        this.groupCount = groupCount;
        this.processCount = processCount;
    }

    @Override
    public void run() {

        lock = new ReentrantLock();
        ExecutorService processService = Executors.newFixedThreadPool(processCount);

        groupQueues = new Queue[groupCount];
        links = new int[groupCount];
        for (int i = 0; i < groupCount; i++) {
            groupQueues[i] = new Queue(i + 1);
            links[i] = i;
        }

        for (int i = 0; i < processCount; i++) {
            processService.submit(new Processor(i));
        }

        while (true) {
            try {
                Thread.sleep(5);
                Item item = sourceQueue.take();

                if (item.isEnd()) {

                    for (int i = 0; i < groupQueues.length; i++) {
                        groupQueues[i].add(new StopItem(Integer.MAX_VALUE, i));
                    }

                    //System.out.println("stop");
                    break;
                } else {
                    int group = item.getGroupId();
                    groupQueues[group].add(item);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        processService.shutdown();

    }

    void nextQueue(int index) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {



            int current = links[index];
            links[index] = links[processCount];

            for (int i = processCount; i < groupCount - 1; i++) {
                links[i] = links[i+1];
            }

            links[groupCount-1] = current;

        } finally {
            lock.unlock();
        }
    }

    class Processor implements Runnable {

        private int index;
        private int processed = 0;

        public Processor(int index) {
            this.index = index;
        }

        @Override
        public void run() {

            while (true) {

                try {
                    Thread.sleep(10);

                    int qIndex = links[index];

                    Item item = groupQueues[qIndex].take();
                    if (item.isEnd()) {
                        break;
                    }
                    processed++;
                    System.out.println(item.getId() + " " + (item.getGroupId() + 1) + " " + Thread.currentThread().getName());

                    if (processed >= 10) {
                        processed = 0;
                        nextQueue(index);
                    }

                } catch (InterruptedException e) {

                }

            }

        }

    }
}
