package API.endpoint;

import com.sun.net.httpserver.*;
//import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class BaseEndpoint{
      public void setHttpExchangeResponseHeaders(HttpExchange httpExchange) {
          // Set common response headers
          httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
          httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
          httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
          httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
          httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");
          }
  }