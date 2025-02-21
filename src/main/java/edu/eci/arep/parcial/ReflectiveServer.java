package edu.eci.arep.parcial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class ReflectiveServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket= new ServerSocket(45000);
        System.out.println("Corriendo por puerto 45000");
        while (true) { 
            Socket clientSocket= serverSocket.accept();
            PrintWriter out= new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String command=null;
            while((inputLine=in.readLine())!=null){
                if(inputLine.startsWith("GET /compreflex?comando=")){
                    command= inputLine.split("=")[1].split(" ")[0];
                    break;
                }

                if(!in.ready()){
                    break;
                }
            }

            if(command!=null){      
                       
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: application/json\r\n"+"\r\n"+response);
            }
            out.close();
            in.close();
            clientSocket.close();
        }
    }





}
