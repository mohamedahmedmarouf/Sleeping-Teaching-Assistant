/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ta;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;



class Mutexlock {

    private final ReentrantLock entlock;
    private final Condition self[];
    private int num;
    private boolean signal = false;

    public Mutexlock(int taNum) {
       num=taNum;
       entlock=new ReentrantLock();
       self=new Condition[taNum];
     for(int i=0;i<num;i++){
       self[i]=entlock.newCondition();
     }
    }
    
    public void take() {
      
        entlock.lock();
        try{
            this.signal = true;
            try {
               
               
                self[num-1].signal();
            
            }catch(NullPointerException ex){}
             
        }finally {
            entlock.unlock();
        }       
}

   
    public  void release(){
        try{
        entlock.lock();
        while(!this.signal){
            try {
                 self[num-1].await();
            }
            catch(NullPointerException ex){}
            catch (InterruptedException ex) {
            Logger.getLogger(Mutexlock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.signal = false;
        } finally {
            entlock.unlock();
        }
    }

}
