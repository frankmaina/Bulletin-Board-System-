
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class server {

    public static void main(String argv[]) throws Exception {
        String header;  //this is the request recieved from the client
        String body;
        String response; //this is the response to the client
        String header_type = "submit";
        String file_reader;
        int port=0; //port number
        //ServerSocket welcomeSocket = new ServerSocket(6789); 
        ServerSocket welcomeSocket = null;

        while (welcomeSocket == null) {
            try (BufferedReader br = new BufferedReader(new FileReader("servers.config"))) {
                while ((file_reader = br.readLine()) != null) {
                    port = Integer.parseInt(file_reader);
                    System.out.println("Looking for a free port to start a server..." + '\n');
                    try {
                        welcomeSocket = new ServerSocket(port);
                        System.out.println('\n' + "Server started on port: " + port + '\n');
                        break;
                    } catch (BindException e) {
                        System.out.println('\n'+ "Sorry the port address in already in use on port: " + port + '\n');
                        welcomeSocket = null;
                    }
                }
            }

        }

        //work on connnections now
        while (true) {
            //while true, we can constantly take in request, the server  does not shutdown after one request
            Socket connectionSocket = welcomeSocket.accept();
            //read stream from client
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //read the request header in that case its the type of request
            header = inFromClient.readLine();
            //inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            String title = inFromClient.readLine();
            body = inFromClient.readLine();
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            //read stream
            //print to command line or logs.txt
            System.out.println("Work done .The server received a : " + header + " request header from -  " + connectionSocket);
            if (header.equals(header_type)) {
                File file = new File("announcement.dat");
                if (!file.exists()) {
                    file.createNewFile(); //create the file if it does not exist otherwise contine with executing code.
                }
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter writer = new PrintWriter(bw);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String time_stamp = dateFormat.format(date);
                writer.println( '\n'+ time_stamp + "     " + title + "                   " + body + '\n');
                writer.flush();
                writer.close();
                response = "Announcement was submitted to the server . " + '\n';
                outToClient.writeBytes(response);
                /*
                //start attempt to replicte the submitted data
                String reader;
                //get details and arguements from user

               

                //try to get a live server connection for reoplication of data
                Socket clientSocket = null;
                int new_port;
                String[] replication_request = new String[3];
                
                while (clientSocket == null) {
                    try (BufferedReader br = new BufferedReader(new FileReader("servers.config"))) {
                        while ((reader = br.readLine()) != null) {
                            new_port = Integer.parseInt(reader);
                            System.out.println("Searching for a Server to replicate data, ");
                            if (new_port==port){
                            System.out.println("The Server cannot replicate data to its own port,");
                            }else{
                                try {
                                    clientSocket = new Socket("localhost", new_port);
                                    System.out.println("replication connection established to a server on : " + new_port + '\n');
                                    break;
                                } catch (ConnectException e) {
                                    System.out.println("Error connection to port: " + new_port + ", no replication.");
                                    clientSocket = null;
                                }
                            }
                        }
                    }

                }/*
                //prepa*re request header
                replication_request[0] = "replicate";
                replication_request[1] = title;
                replication_request[2] = body;
                
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                outToServer.writeBytes(replication_request[0] + '\n');
                outToServer.writeBytes(replication_request[1] + '\n');
                outToServer.writeBytes(replication_request[2] + '\n');*/
                
            } else if (header.equals("read"))  {
               

                String reader;
                String read_response = "";

                try (BufferedReader br = new BufferedReader(new FileReader("announcement.dat"))) {
                    while ((reader = br.readLine()) != null) {
                        read_response = read_response + reader + '\n';
                    }
                } catch (FileNotFoundException e) {
                    //the file was not found hence create one and return blank
                    File file = new File("announcement.dat");
                    if (!file.exists()) {
                        file.createNewFile(); //create the file if it does not exist otherwise contine with executing code.
                        read_response = "Nothing has been posted on the board yet!";
                    }
                }
                //respond back to client
                outToClient.writeBytes(read_response);
            }else if (header.equals("replication")){
                System.out.println("recieved");
            }


        }
    }
}
