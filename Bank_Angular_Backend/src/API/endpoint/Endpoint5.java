package API.endpoint;

import com.sun.net.httpserver.*;
// import org.apache.commons.text.StringEscapeUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.concurrent.*;
import API.endpoint.BaseEndpoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Endpoint5 extends BaseEndpoint implements HttpHandler {
    static Connection connection;
    static Statement statement;
    static ResultSet resultSet;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
        String requestURI = httpExchange.getRequestURI().toString();
        System.out.println(requestURI);
        System.out.println(httpExchange.getRequestMethod());
        if("POST".equals(httpExchange.getRequestMethod())) {
            System.out.println("Endpoint5: Post handled");
            requestParamValue = handlePostRequest(httpExchange);
            boolean operation=true;
            Double amount= 0.0;
            String account_number="";
            JSONObject jsonObject = null;
            try {
                jsonObject= new JSONObject(requestParamValue);
                operation=jsonObject.getBoolean("operation");
                account_number=jsonObject.getString("account_number");
                amount=jsonObject.getDouble("amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/Bank",
                        "postgres",
                        "14082001");
                statement = connection.createStatement();
                if(operation==true){
                PreparedStatement ps = connection.prepareStatement("UPDATE CLIENTS SET account_balance =clients.account_balance +"+Double.toString(amount)+
                        " WHERE account_number ='"+account_number+"'");
                    int	affectedRows1=ps.executeUpdate();
                    String sql = "INSERT INTO TRANSACTIONS(account_number, transaction) VALUES(?,?)";
                    ps = connection.prepareStatement(sql,statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, account_number);
                    ps.setString(2,"+"+amount);
                    int affectedRows2=ps.executeUpdate();
                }else{
                    PreparedStatement ps = connection.prepareStatement("UPDATE CLIENTS SET account_balance =clients.account_balance -"+Double.toString(amount)+
                            " WHERE account_number ='"+account_number+"'");
                    int	affectedRows1=ps.executeUpdate();
                    String sql = "INSERT INTO TRANSACTIONS(account_number, transaction) VALUES(?,?)";
                    ps = connection.prepareStatement(sql,statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, account_number);
                    ps.setString(2,"-"+amount);
                    int affectedRows2=ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Endpoint4: Nothing handled");
        }
        handleResponse(httpExchange,requestParamValue);
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
        String htmlResponse = "{\"key\":\"ok\" }";
        super.setHttpExchangeResponseHeaders(httpExchange);
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}