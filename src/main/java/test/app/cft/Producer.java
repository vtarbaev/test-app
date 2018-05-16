package test.app.cft;

import java.util.Random;

public class Producer implements Runnable {

    private ItemQueue queue;
    private int maxGroup;

    public Producer(ItemQueue queue, int maxGroup) {
        this.queue = queue;
        this.maxGroup = maxGroup;
    }

    @Override
    public void run() {

        final Random r = new Random();

        final int n = 1_00;

        for (int i = 0; i < n; i++ ) {

            int k = r.nextInt(maxGroup);
            if (k > 3) {
                queue.put(new Item(i, 5));
            } else {
                queue.put(new Item(i, r.nextInt(maxGroup)));
            }

            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        queue.put(new Item(Integer.MAX_VALUE,-1));

        System.out.println("producer stop");

    }
}
