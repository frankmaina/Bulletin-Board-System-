
import java.net.*;
import java.io.*;

/**
 *
 * @author frank
 */
public class client {

    public static void main(String argv[]) throws Exception {

        String[] request = new String[3];
        int port;
        String reader;
        //get details and arguements from user

        Socket clientSocket = null;

        //try to get a live server connection
        while (clientSocket == null) {
            try (BufferedReader br = new BufferedReader(new FileReader("servers.config"))) {
                while ((reader = br.readLine()) != null) {
                    port = Integer.parseInt(reader);
                    System.out.println("Searching for a Server to connect to on port, " + port);
                    try {
                        clientSocket = new Socket("localhost", port);
                        System.out.println("Connection to Server accepted on port: " + port + '\n');
                        break;
                    } catch (ConnectException e) {
                        System.out.println("Error connection to port: " + port + ", moving on.");
                        clientSocket = null;
                    }
                }
            }

        }
        //initialise buffers
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //holds the type of request to be made either read or write
         try {
         if (argv[0].equals("l")) {
         request[0] = "read";
         request[1] = "";
         } else if (argv[0].equals("s")) {
         request[0] = "submit";
         request[1] = argv[1];
         request[2] = argv[2];
         }
         } catch (Exception e) {
         System.out.println(e);
         }
         
        
        /*request[0] = "read";
        request[1] = "title";
        request[2] = "no class today";*/
        //define requests
        //request[1] = inFromUser.readLine(); //holds the actual request.
        //send requests to server
        outToServer.writeBytes(request[0] + '\n');
        outToServer.writeBytes(request[1] + '\n');
        outToServer.writeBytes(request[2] + '\n');

        //start on response
        String response;
        if (request[0].equals("submit")) {

            //just print out reponse from server as this is not a read request]
            response = inFromServer.readLine();
            System.out.println(response);
        } else {

            //do a while loop to read the response from the server
            while ((response = inFromServer.readLine()) != null) {
                System.out.println(response);
            }
        }

    }

}
