import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Scanner;
import java.util.concurrent.locks.*;

public class TestRunnerSimple {
    public static void main(String[] args) {
        lrucache testcache = new lrucache();
        testcache.cachemap = new ConcurrentHashMap();
        testcache.list = new linkedList();

        // Test 1

        for(int i=0; i<50; i++){

            final int temp = i;
              new Thread("" + i){
                  public void run(){
            
                      for (int j = 1; j <= 100000 ; ++j ) {
                          lrucache.put(j + (temp * 100000), j + (temp * 100000) + 10);
                      }
                      
                  }

              }.start();
          
        }

        System.out.println("\n Starting inserting 25 extra values \n");
        // Test 2

          for (int j = 1; j <= 25 ; ++j ) {
              lrucache.put(j, j + 10 );
          }
                      
        System.out.println(lrucache.list.size);
        int flag = 0;
        for (int j = 1; j <= 25 ; ++j ) {
            int ans = lrucache.get(j);
            //System.out.println(ans);
            if (ans != (j + 10)) flag = 1;
        }

        if (flag == 0) {
          System.out.println("Success");  
        }else {
          System.out.println("Failure. Values not entered properly");

        }
        
    } 



      
        
}
