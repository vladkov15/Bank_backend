package API.endpoint;

import com.sun.net.httpserver.*;
// import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.*;
import API.endpoint.BaseEndpoint;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Endpoint2 extends BaseEndpoint implements HttpHandler {    

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      String requestParamValue=null; 
      String requestURI = httpExchange.getRequestURI().toString();
      System.out.println(requestURI);
      System.out.println(httpExchange.getRequestMethod());
      if("GET".equals(httpExchange.getRequestMethod())) { 
          System.out.println("Endpoint2: GET handled");
          requestParamValue = "bgvjnfjcnv";
       }
       else if("POST".equals(httpExchange.getRequestMethod())) { 
           System.out.println("Endpoint2: Post handled");
         requestParamValue = handlePostRequest(httpExchange);        
        }  
      else{
          System.out.println("Endpoint2: Nothing handled");
      }
      handleResponse(httpExchange,requestParamValue); 
    }
    
    
    private String handleGetRequest(HttpExchange httpExchange) {
             return httpExchange.
                     getRequestURI()
                     .toString()
                     .split("\\?")[1]
                     .split("=")[1];
    }
    
    private String handlePostRequest(HttpExchange httpExchange) throws IOException {   
      BufferedReader httpInput = new BufferedReader(new InputStreamReader(
			  httpExchange.getRequestBody(), "UTF-8"));
      StringBuilder in = new StringBuilder();
      String input;
      while ((input = httpInput.readLine()) != null) {
        in.append(input).append(" ");
      }
      httpInput.close();
      return in.toString().trim();
    }
    
     private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
              OutputStream outputStream = httpExchange.getResponseBody();
              // StringBuilder htmlBuilder = new StringBuilder();
              // htmlBuilder.append("<html>").
              //         append("<body>").
              //         append("<h1>").
              //         append("Hello ")
              //         .append(requestParamValue)
              //         .append("</h1>")
              //         .append("</body>")
              //         .append("</html>");
              // String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
              String htmlResponse = "{\"key\": \"" + requestParamValue + "\"}";
              super.setHttpExchangeResponseHeaders(httpExchange);
              httpExchange.sendResponseHeaders(200, htmlResponse.length());
              outputStream.write(htmlResponse.getBytes());
              outputStream.flush();
              outputStream.close();
          }
  }