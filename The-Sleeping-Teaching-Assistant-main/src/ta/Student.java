/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta;

import java.util.concurrent.Semaphore;


public class Student implements Runnable {

    private int programTime;
    private int count = 0;
    private int studentNum;
    private Mutexlock wakeup;
    private Semaphore chairs;
    private Semaphore available;
    private int numberofchairs;
    private int numberofTA;
    private int helpTime=5000;
    private Thread t;

    public Student(int programTime, Mutexlock wakeup, Semaphore chairs, Semaphore available, int studentNum, int numberofchairs, int numberofTA) {
        this.programTime = programTime;
        this.wakeup = wakeup;
        wakeup = new Mutexlock(numberofTA);
        this.chairs = chairs;
        this.available = available;
        this.studentNum = studentNum;
        t = Thread.currentThread();
        this.numberofchairs = numberofchairs;
        this.numberofTA = numberofTA;
    }

    @Override
    public void run() {
        while (true) {
            try {
                t.sleep(programTime * 1000);
                if (available.tryAcquire()) {
                    try {
                        wakeup.take();

                        t.sleep(helpTime);
                    } catch (InterruptedException e) {
                        continue;
                    } finally {
                        available.release();
                    }
                } else {

                    if (chairs.tryAcquire()) {
                        try {

                            available.acquire();
                            t.sleep(helpTime);

                            available.release();
                        } catch (InterruptedException e) {
                            continue;
                        }
                    } else {
                        t.sleep(helpTime);
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
