package edu.eci.arep.parcial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveServer {
    public static void main(String[] args) throws IOException, Exception {
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
                 String clientPath= getPath(command);
                 List<String> clientParams= getParams(command);    
                 String clientOperation= getOperation(clientPath, clientParams);
                out.println("HTTP/1.1 200 OK\r\n"+"Content-Type: application/json\r\n"+"\r\n"+ clientOperation);
                        
            }
            out.close();
            in.close();
            clientSocket.close();
        }
    }

    public static String getPath(String input){
        return input.substring(0,input.indexOf("("));
    }


    public static List<String> getParams(String input){
        List<String> params= new ArrayList<>();
        String paramString= input.substring(input.indexOf("(")+1,input.indexOf(")"));
        if(!paramString.isEmpty()){
            String[] arrayparam= paramString.split(",");
            for(String param: arrayparam){
                params.add(param.trim());
            }
        }
        return params;
    }

    public static String getOperation(String path, List<String> params) throws Exception{
        Class<?> c= Class.forName(params.get(3));
        String response=null;
        if(params.size()==1){
            if("Class".equals(path)){
                return String.valueOf(Class(params));       
            }
        }
        else if(params.size()==2){
            Method method= c.getMethod(path, String.class, String.class);
            response= (String) method.invoke(params.get(0), params.get(1));
            return response.toString();
        }

        else if(params.size()==4){
            Method method= c.getMethod(path,String.class,String.class, Object.class,Object.class);
            response= (String) method.invoke(params.get(0), params.get(1),params.get(2),params.get(3));
            return response.toString();
        }else if(params.size()==6){
            Method method= c.getMethod(path, String.class,String.class, Object.class,Object.class, Object.class, Object.class);
            response= (String) method.invoke(params.get(0), params.get(1),params.get(2),params.get(3),params.get(4),params.get(5));
            return response.toString();
        }else{
            throw new IllegalArgumentException("Numero de parametros diferente al aceptado");
        }
        return response;
    }

    public static java.lang.Class<?> Class(List<String> className) throws ClassNotFoundException{        
        return Class.forName(className.get(0));
    }









}
