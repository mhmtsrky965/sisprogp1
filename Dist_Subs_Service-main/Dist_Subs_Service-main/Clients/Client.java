import java.net.*;
import java.io.*;

public class Client {
    private Socket serverConnection;
    private String name;
    private int ID;
    
    public Client(String name) {
        this.name = name;
    }
    
    public void subscribe(String[] interests) {
        try {
            serverConnection = new Socket("localhost", 6001);
            
            Subscriber subscriber = Subscriber.newBuilder()
                .setNameSurname(name)
                .setDemand(DemandType.SUBS)
                .addAllInterests(Arrays.asList(interests))
                .build();
                
            subscriber.writeTo(serverConnection.getOutputStream());
            
            // Read response
            Subscriber response = Subscriber.parseFrom(serverConnection.getInputStream());
            this.ID = response.getID();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Client client = new Client("Test User");
        client.subscribe(new String[]{"sports", "technology"});
    }
}
