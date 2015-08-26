import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Scanner;


class Node
{
    protected String data;
    protected Node next, prev;
 
    /* Constructor */
    public Node()
    {
        next = null;
        prev = null;
        data = "";
    }
    /* Constructor */
    public Node(String d, Node n, Node p)
    {
        data = d;
        next = n;
        prev = p;
    }
    /* Function to set link to next node */
    public void setLinkNext(Node n)
    {
        next = n;
    }
    /* Function to set link to previous node */
    public void setLinkPrev(Node p)
    {
        prev = p;
    }    
    /* Funtion to get link to next node */
    public Node getLinkNext()
    {
        return next;
    }
    /* Function to get link to previous node */
    public Node getLinkPrev()
    {
        return prev;
    }
    /* Function to set data to node */
    public void setData(String d)
    {
        data = d;
    }
    /* Function to get data from node */
    public String getData()
    {
        return data;
    }
}
 
/* Class linkedList */
class linkedList
{
    protected Node start;
    protected Node end ;
    public int size;
 
    /* Constructor */
    public linkedList()
    {
        start = null;
        end = null;
        size = 0;
    }
    /* Function to check if list is empty */
    public boolean isEmpty()
    {
        return start == null;
    }
    /* Function to get size of list */
    public int getSize()
    {
        return size;
    }
    /* Function to insert element at begining */
    public void insertAtStart(String val)
    {
        Node nptr = new Node(val, null, null);        
        if(start == null)
        {
            start = nptr;
            end = start;
        }
        else
        {
            start.setLinkPrev(nptr);
            nptr.setLinkNext(start);
            start = nptr;
        }
        size++;
    }
    /* Function to insert element at end */
    public Node insertAtEnd(String val)
    {
        Node nptr = new Node(val, null, null);        
        if(start == null)
        {
            start = nptr;
            end = start;
        }
        else
        {
            nptr.setLinkPrev(end);
            end.setLinkNext(nptr);
            end = nptr;
        }
        size++;

        return nptr;
    }

    /* Function to delete node at position */
    public void deleteAtPos(Node n)
    {        
                
    }    
}









class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));

			String key = convertStreamToString(input);
			
			//lrucache.cachemap.put("abc","data");
		
			Node temp = (Node)lrucache.cachemap.get(key);

			if(temp == null){
				
				System.out.println("-ve");
				temp = (Node)lrucache.list.insertAtEnd(key + "-data");
				lrucache.cachemap.put(key,temp);

				try{
					Thread.sleep(500); // simulated data fetch latency period for negative cache lookup
				}catch (InterruptedException e) {
				    e.printStackTrace();
				}
			
            }
            
            String outp = (String)temp.getData();
			System.out.println(outp);
            output.write((key + " , " + outp).getBytes());
            output.close();
            input.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String convertStreamToString(java.io.InputStream input) {
    	InputStream in = input;
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb=new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		
		String read = new String();

		try {
                read = br.readLine();
                sb.append(read);
            } catch (IOException e) {
		
		}

		return sb.toString();
	}
}


class MultiThreadedServer implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    public MultiThreadedServer(int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            new Thread(
                new WorkerRunnable(
                    clientSocket, "Multithreaded Server")
            ).start();
        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

}


public class lrucache{

	public static ConcurrentHashMap cachemap;
	public static linkedList list;

	public static void main(String[] args) {
		
		MultiThreadedServer server = new MultiThreadedServer(9000);
		cachemap = new ConcurrentHashMap();		
		list = new linkedList();
		new Thread(server).start();

		try {
		    Thread.sleep(20 * 10000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();

	}
}