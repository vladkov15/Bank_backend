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

public class Endpoint4 extends BaseEndpoint implements HttpHandler {
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
            System.out.println("Endpoint4: Post handled");
            requestParamValue = handlePostRequest(httpExchange);
            String first_name="";
            String last_name="";
            String birthday="";
            String sex="";
            String account_number="";
            JSONObject jsonObject = null;
            try {
                jsonObject= new JSONObject(requestParamValue);
                first_name=jsonObject.getString("first_name");
                last_name=jsonObject.getString("last_name");
                birthday=jsonObject.getString("birthday");
                sex=jsonObject.getString("sex");
                account_number=jsonObject.getString("account_number");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/Bank",
                        "postgres",
                        "14082001");
                statement = connection.createStatement();
                String sql = "INSERT INTO clients(first_name,last_name,birthday,sex,account_number,account_balance)"
                        +" VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql,statement.RETURN_GENERATED_KEYS);
                ps.setString(1, first_name);
                ps.setString(2, last_name);
                ps.setString(3, birthday);
                ps.setString(4, sex);
                ps.setString(5, account_number);
                ps.setDouble(6, 0);
                int affectedRows = ps.executeUpdate();
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