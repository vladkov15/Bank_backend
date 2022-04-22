package API.endpoint;

import com.sun.net.httpserver.*;
// import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.*;
import API.endpoint.BaseEndpoint;
import API.data.ToJson;

public class Endpoint1 extends BaseEndpoint implements HttpHandler {    
         
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      String requestParamValue=null; 
      String requestURI = httpExchange.getRequestURI().toString();
      System.out.println(requestURI);
      System.out.println(httpExchange.getRequestMethod());
      if("GET".equals(httpExchange.getRequestMethod())) { 
          System.out.println("Endpoint1: GET handled");
          requestParamValue = handleGetRequest(httpExchange);
          // requestParamValue = "Val";
       }
       else if("POST".equals(httpExchange.getRequestMethod())) { 
           System.out.println("Endpoint1: Post handled");
         requestParamValue = handlePostRequest(httpExchange);        
        }  
      else{
          System.out.println("Endpoint1: Nothing handled");
      }
      handleResponse(httpExchange,requestParamValue); 
    }
    
    
    private String handleGetRequest(HttpExchange httpExchange) {
      String reqURI = httpExchange.getRequestURI().toString();
      System.out.println("Handling request URI: " + reqURI);
      String uriArgs = reqURI.split("\\?")[1];
      String[] keyVal = uriArgs.split("&");
      int sz = keyVal.length;
      ToJson item;
      if (sz == 1) {
        String[] one = keyVal[0].split("=");
        System.out.println("!" + one[0] + "!");

        if (one[0].equals("val1")){
          item = new ToJson(one[1], -1);
        }
        else {
          int i = Integer.parseInt(one[1]);
          item = new ToJson("Default val", i);
        }
      }
      else if( sz == 2){
        String[] one = keyVal[0].split("=");
        String[] two = keyVal[1].split("=");
        int i = Integer.parseInt(two[1]);
        item = new ToJson(one[1],  i);
      }
      else {
        item = new ToJson("Default val", -1);
      }
      return item.convert();
      
            //  return httpExchange.
            //          getRequestURI()
            //          .toString()
            //          .split("\\?")[1]
            //          .split("=")[1];
    }
    
    private String handlePostRequest(HttpExchange httpExchange) {   
             return httpExchange.getRequestBody().toString();
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
              String htmlResponse = "{\"key\": \"value\"}";
              htmlResponse = requestParamValue;
              super.setHttpExchangeResponseHeaders(httpExchange);
              httpExchange.sendResponseHeaders(200, htmlResponse.length());
              outputStream.write(htmlResponse.getBytes("UTF-8"));
              outputStream.flush();
              outputStream.close();
          }
  }