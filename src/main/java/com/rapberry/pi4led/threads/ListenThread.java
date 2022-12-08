package com.rapberry.pi4led.threads;

public class ListenThread extends Thread{

    public ListenThread(String name) {
        super(name);
    }

    @Override
    public void run(){
        System.out.printf("%s started... \n", Thread.currentThread().getName());
        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e){
            System.out.println("Thread has been interrupted");
        }
        System.out.printf("%s fiished... \n", Thread.currentThread().getName());
    }

    public void receive() {

    }
}
