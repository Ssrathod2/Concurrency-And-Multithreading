class DiningPhilosophers {
 
    private Semaphore[] leftForks, rightForks;
    
    public DiningPhilosophers() {
        leftForks = new Semaphore[5];
        rightForks = new Semaphore[5];
        
        for (int i = 0; i < 5; i++) {
            leftForks[i] = new Semaphore(1, true);
            rightForks[(i + 1) % 5] = leftForks[i]; // left fork of a philosopher is the right fork of the next philosopher, hence use the "same" semaphore (reference of the semaphore)
        }
        
        
    }
 
    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        
        leftForks[philosopher].acquire();
        pickLeftFork.run();
        rightForks[philosopher].acquire();
        pickRightFork.run();
        eat.run();
        putLeftFork.run();
        leftForks[philosopher].release();
        putRightFork.run();
        rightForks[philosopher].release();
        
    }
}
