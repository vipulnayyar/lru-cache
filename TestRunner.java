import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Scanner;
import java.util.concurrent.locks.*;

public class TestRunner {
    public static void main(String[] args) {
        lrucache testcache = new lrucache();
        testcache.cachemap = new ConcurrentHashMap();
        testcache.list = new linkedList();

        System.out.println(Thread.currentThread().getName());
        //int i,j;
        for(int i=0; i<50; i++){
          //for(int j=0; j < 10 ; ++j ) {
           // final int temp1 = i;
            //final int temp2 = j;

              new Thread("" + i){
                  public void run(){
                
                        
                      /* try {
                              int a = 1024*1024*1034;
                              Thread.sleep(2000);

                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }*/


                      for (int k = 0; k <10 ; ++k ) {
                          lrucache.get("test" + temp1 + k);
                      }
                        //System.out.println("test" + temp1 + k);   
                      
                  }

                  //System.out.println(testcache);
                



              }.start();
          //}
          
        }
    
        System.out.println("Success");

    } 



      
        
}
