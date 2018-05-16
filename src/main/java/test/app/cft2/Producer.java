package test.app.cft2;

import java.util.Random;

public class Producer implements Runnable {

    private int groupCount;
    private Queue sourceQueue;

    public Producer(Queue sourceQueue, int groupCount) {
        this.sourceQueue = sourceQueue;
        this.groupCount = groupCount;
    }

    @Override
    public void run() {
        //System.out.println("producerService started");

        Random r = new Random(System.currentTimeMillis());
        int id = 0;
        for (int i = 0; i < 1000; i++) {
            int groupId = r.nextInt(groupCount);
            sourceQueue.add(new Item(id, groupId));
            id++;

            if (i % 5 == 0) {
                sourceQueue.add(new Item(id, 2));
                id++;
            }
            if (i % 2 == 0) {
                sourceQueue.add(new Item(id, groupCount - 1));
                id++;
            }

        }

        sourceQueue.add(new StopItem(Integer.MAX_VALUE, -1));

        //System.out.println("producerService stopped");
    }
}
