import java.util.concurrent.Semaphore;

class DiningPhilosophers {
    // One fork per philosopher
    private final Semaphore[] forks = new Semaphore[5];

    public DiningPhilosophers() {
        for (int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1); // 1 permit = 1 fork
        }
    }

    // philosopher = 0..4
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        int left = philosopher;
        int right = (philosopher + 1) % 5;

        // Prevent deadlock by always locking in the same order
        int first = Math.min(left, right);
        int second = Math.max(left, right);

        forks[first].acquire();
        if (first == left) pickLeftFork.run(); else pickRightFork.run();

        forks[second].acquire();
        if (second == right) pickRightFork.run(); else pickLeftFork.run();

        // Eating
        eat.run();

        // Put down forks
        if (second == right) putRightFork.run(); else putLeftFork.run();
        forks[second].release();

        if (first == left) putLeftFork.run(); else putRightFork.run();
        forks[first].release();
    }
}

public class Main {
    public static void main(String[] args) {
        DiningPhilosophers dp = new DiningPhilosophers();

        // Create 5 philosopher threads
        for (int i = 0; i < 5; i++) {
            final int id = i;
            new Thread(() -> {
                try {
                    for (int j = 0; j < 3; j++) { // each philosopher eats 3 times
                        dp.wantsToEat(id,
                                () -> System.out.println("Philosopher " + id + " picks left fork"),
                                () -> System.out.println("Philosopher " + id + " picks right fork"),
                                () -> {
                                    System.out.println("Philosopher " + id + " is eating 🍝");
                                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                                },
                                () -> System.out.println("Philosopher " + id + " puts left fork"),
                                () -> System.out.println("Philosopher " + id + " puts right fork")
                        );
                        // Thinking
                        System.out.println("Philosopher " + id + " is thinking 💭");
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
