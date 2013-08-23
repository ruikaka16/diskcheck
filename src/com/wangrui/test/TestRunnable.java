package com.wangrui.test;

public class TestRunnable implements Runnable {
	 private int i ;
	 
	 public void run() {
	  for( ; i < 30; i++){
	   System.out.println(Thread.currentThread().getName()+ " " + i + "ok");
	  }
	  
	 }

	 public static void main(String[] args) {
	  int i = 0;
	  for(; i < 30; i++){
	   System.out.println(Thread.currentThread().getName() + " " + i);
	   if(i == 10){
	    TestRunnable tr = new TestRunnable();
	    new Thread(tr, "子线程0").start();
	    new Thread(tr, "子线程1").start();
	   }
	  }
	  
	 }

	}