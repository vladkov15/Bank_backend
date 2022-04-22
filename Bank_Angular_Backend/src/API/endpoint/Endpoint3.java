package API.endpoint;

import API.data.User;
import com.sun.net.httpserver.*;
// import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import API.endpoint.BaseEndpoint;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Endpoint3 extends BaseEndpoint implements HttpHandler {
    static Connection connection;
    static Statement statement;
    static ResultSet resultSet;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/Bank",
                    "postgres",
                    "14082001");
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String requestParamValue=null;
        String requestURI = httpExchange.getRequestURI().toString();
        System.out.println(requestURI);
        System.out.println(httpExchange.getRequestMethod());
        if("GET".equals(httpExchange.getRequestMethod())) {
            System.out.println("Endpoint3: GET handled");
        } else{
            System.out.println("Endpoint3: Nothing handled");
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        //String htmlResponse = "{\"key\": \"" + requestParamValue + "\"}";
        User newUser = null;
        List<String> mass = new ArrayList<>();
        try {
            ResultSet resultSet1 = statement.executeQuery("SELECT * FROM clients");
            while(resultSet1.next()) {
                System.out.println(resultSet1.getInt(1) + "  " +
                        resultSet1.getString(2) + "  " +
                        resultSet1.getString(3) + "  " +
                        resultSet1.getString(4) + "  " +
                        resultSet1.getString(5) + "  " +
                        resultSet1.getString(6) + "  " +
                        resultSet1.getDouble(7) + "  ");
                newUser = new User(resultSet1.getInt(1), resultSet1.getString(2), resultSet1.getString(3),
                        resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6),
                        resultSet1.getDouble(7));
                mass.add(newUser.convert());
            }
            String htmlResponse = mass.toString() ;
            super.setHttpExchangeResponseHeaders(httpExchange);
            httpExchange.sendResponseHeaders(200, htmlResponse.length());
            outputStream.write(htmlResponse.getBytes());
            outputStream.flush();
            outputStream.close();
            }catch (Exception e){
            e.printStackTrace();
        }
    }


}