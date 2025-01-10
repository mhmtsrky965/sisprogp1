import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Server1 {
    private final int SERVER_ID = 1;
    private final int CLIENT_PORT = 6001;
    private final int ADMIN_PORT = 7001;
    private final int INTER_SERVER_PORT = 5001;
    
    private final ConcurrentHashMap<Integer, Subscriber> subscribers;
    private final ReentrantLock subscriberLock;
    private final ExecutorService threadPool;
    private volatile boolean isRunning;
    
    private Socket server2Connection;
    private Socket server3Connection;
    
    public Server1() {
        this.subscribers = new ConcurrentHashMap<>();
        this.subscriberLock = new ReentrantLock();
        this.threadPool = Executors.newFixedThreadPool(10);
        this.isRunning = true;
    }
    
    public void start() {
        connectToOtherServers();
        startListeners();
    }
    
    private void connectToOtherServers() {
        try {
            server2Connection = new Socket("localhost", 5002);
            server3Connection = new Socket("localhost", 5003);
            
            // Start replication handlers
            threadPool.submit(new ReplicationHandler(server2Connection));
            threadPool.submit(new ReplicationHandler(server3Connection));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private class ReplicationHandler implements Runnable {
        private final Socket connection;
        
        public ReplicationHandler(Socket connection) {
            this.connection = connection;
        }
        
        @Override
        public void run() {
            try {
                while (isRunning) {
                    // Handle replication messages
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        new Server1().start();
    }
}
