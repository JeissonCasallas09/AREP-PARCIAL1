package edu.eci.arep.parcial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 *
 * @author jeisson.casallas-r
 */
public class HttpServerFachada {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://localhost:45000/compreflex?comando=";
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket= new ServerSocket(35000);
        System.out.println("Corriendo por puerto 35000");
        while (true) { 
            Socket clientSocket= serverSocket.accept();
            PrintWriter out= new PrintWriter(clientSocket.getOutputStream(),true);
            BufferedReader in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String command=null;
            while((inputLine=in.readLine())!=null){
                if(inputLine.startsWith("GET /consulta?comando=")){
                    command= inputLine.split("=")[1].split(" ")[0];
                    break;
                }

                else if(inputLine.startsWith("GET /cliente")){
                    command=null;
                    break;
                }

                if(!in.ready()){
                    break;
                }
            }

            if(command!=null){
                URL obj = new URL(GET_URL + command);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                BufferedReader calculateIn= new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response= calculateIn.readLine();
                calculateIn.close();               
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: application/json\r\n"+"\r\n"+response);
            }else{
                String clientHtml=getClientHtml();
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: text/html\r\n"+"\r\n"+clientHtml);
            }
            out.close();
            in.close();
            clientSocket.close();
        }

    }

    public static String getClientHtml(){
        return "<!DOCTYPE html>\r\n" + //
                        "<html>\r\n" + //
                        "    <head>\r\n" + //
                        "        <title>Form Example</title>\r\n" + //
                        "        <meta charset=\"UTF-8\">\r\n" + //
                        "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + //
                        "    </head>\r\n" + //
                        "    <body>\r\n" + //
                        "        <h1>Reflective ChatGPT</h1>\r\n" + //
                        "        <form>\r\n" + //
                        "            <label for=\"comando\">Comando:</label><br>\r\n" + //
                        "            <input type=\"text\" id=\"comando\" name=\"comando\" value=\"binaryInvoke(java.lang.Math, max, double, 4.5, double, -3.7)\"><br><br>\r\n" + //
                        "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\r\n" + //
                        "        </form> \r\n" + //
                        "        <div id=\"getrespmsg\"></div>\r\n" + //
                        "\r\n" + //
                        "        <script>\r\n" + //
                        "            function loadGetMsg() {\r\n" + //
                        "                let comando = document.getElementById(\"comando\").value;\r\n" + //
                        "                const xhttp = new XMLHttpRequest();\r\n" + //
                        "                xhttp.onload = function() {\r\n" + //
                        "                    document.getElementById(\"getrespmsg\").innerHTML =\r\n" + //
                        "                    this.responseText;\r\n" + //
                        "                }\r\n" + //
                        "                xhttp.open(\"GET\", \"/consulta?comando=\"+comando);\r\n" + //
                        "                xhttp.send();\r\n" + //
                        "            }\r\n" + //
                        "        </script>\r\n" + //
                        "    </body>\r\n" + //
                        "</html>";
    }
}
