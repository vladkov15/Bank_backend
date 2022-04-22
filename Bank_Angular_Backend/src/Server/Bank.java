package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Bank <T> {
    static Connection connection  ;
    static Statement statement;
    static ResultSet resultSet;
    public static void main(String [] args) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Bank",
                "postgres",
                "14082001");
        statement = connection.createStatement();
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        int choose;
        while(true) {
            System.out.println("choose the number for the action");
            System.out.println("1---- Print all data in DB");
           // System.out.println("2---- C/O bank account");
            System.out.println("3---- Create account");
            System.out.println("4---- Top up a bank account");
            System.out.println("5---- Withdraw money from A bank account");
            System.out.println("0---- EXIT");
            choose=scanner.nextInt();
            switch(choose)
            {
                case 0:
                    System.out.println("GOODBYE");
                    return;
                case 1:
                    PrintTables();
                    break;
               // case 2:
                 //   UpdateOpenOrCloser();
                        // break;
                case 3:
                    CreateAccount();
                    break;
                case 4:
                    Up();
                    break;
                case 5:
                    Down();
                    break;
                default:
                    System.out.println("fail");
                    break;
            }
        }


    }

    public static void Down() throws SQLException {
        Scanner sc = new Scanner(System.in);
        PrintClients();
        System.out.println("Which account do you want to remove money?");
        String accId = CheckValuses(String.class,6);
        System.out.println("for what sum?");
        double minus = CheckValuses(Double.class,0);
        PreparedStatement ps = connection.prepareStatement("UPDATE CLIENTS SET account_balance = account_balance -"+Double.toString(minus)+
                " WHERE account_number ='"+accId+"'");
        int	affectedRows1=ps.executeUpdate();
        String sql = "INSERT INTO TRANSACTIONS(account_number, transaction) VALUES(?,?)";
        ps = connection.prepareStatement(sql,statement.RETURN_GENERATED_KEYS);
        ps.setString(1, accId);
        ps.setString(2,"-"+minus);
        int affectedRows2=ps.executeUpdate();
    }

    public static void Up() throws SQLException {
        Scanner sc = new Scanner(System.in);
        PrintClients();
        System.out.println("Which account do you want to top up?");
        String accId = sc.next();
        System.out.println("for what sum?");
        double plus = sc.nextDouble();
        PreparedStatement ps = connection.prepareStatement("UPDATE CLIENTS SET account_balance =clients.account_balance +"+Double.toString(plus)+
                " WHERE account_number ='"+accId+"'");
        int	affectedRows1=ps.executeUpdate();
        String sql = "INSERT INTO TRANSACTIONS(account_number, transaction) VALUES(?,?)";
        ps = connection.prepareStatement(sql,statement.RETURN_GENERATED_KEYS);
        ps.setString(1, accId);
        ps.setString(2,"+"+plus);
        int affectedRows2=ps.executeUpdate();

    }
    public static void CreateAccount() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input first name");
        String fName = CheckValuses(String.class,20);
        System.out.println("Input second name");
        String sName = CheckValuses(String.class,30);
        System.out.println("Input sex");
        String sex = CheckValuses(String.class,6);
        System.out.println("Input birthday");
        String birthday = CheckValuses(String.class,10);
        System.out.println("Input id for bank account");
        String accId = CheckValuses(String.class,6);
        Double balance =0.0;
        String active ="YES";
        String sql = "INSERT INTO clients(first_name,last_name,birthday,sex,account_number,account_balance)"
                +" VALUES(?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql,statement.RETURN_GENERATED_KEYS);
        ps.setString(1, fName);
        ps.setString(2, sName);
        ps.setString(3, birthday);
        ps.setString(4, sex);
        ps.setString(5, accId);
        ps.setDouble(6, balance);
        int affectedRows = ps.executeUpdate();
    }
    public static  void PrintTables() {
        try {
            ResultSet resultSet1 = statement.executeQuery("SELECT * FROM clients");
            if(resultSet1!=null) {
                System.out.println("ID  FIRST_NAME                  SECOND_NAME                          BIRTHDAY  SEX     ACCOUNT_N  ACCOUNT_B    ");
                System.out.println("-----------------------------------------------------------------------------------------");
                while(resultSet1.next()) {
                    System.out.println(resultSet1.getInt(1)+"  "+
                            resultSet1.getString(2)+"  "+
                            resultSet1.getString(3)+"  "+
                            resultSet1.getString(4)+"  "+
                            resultSet1.getString(5)+"  "+
                            resultSet1.getString(6)+"  "+
                            resultSet1.getDouble(7)+"  ");
                }
                System.out.println();
            }
            ResultSet resultSet2 = statement.executeQuery("SELECT * FROM TRANSACTIONS");
            if(resultSet2!=null) {
                System.out.println("TRNC_NUM        BANK_ACC_ID       TRANSACTION");
                System.out.println("------------------------------------------------");
                while(resultSet2.next()) {
                    System.out.println(resultSet2.getInt(1)+"  "+
                            resultSet2.getString(2)+"  "+
                            resultSet2.getString(3)+"  ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*public static void UpdateOpenOrCloser(){

        try {
            PrintClients();
            System.out.println("Which bank account, you would like to Close Or Open?");
            System.out.println("Enter the bank acount id(BANK_ACC_DI)");
            String str = new Scanner(System.in).nextLine();
            resultSet = statement.executeQuery("SELECT * FROM CLIENTS WHERE BANK_ACC_ID ='"+str+"'");
            String active = null;
            if(resultSet!=null) {
                System.out.println("ACC_NUM        FIRST_NAME      SECOND_NAME     BANK_ACC_ID    ACC_BALANCE       ACTIVE");
                System.out.println("-----------------------------------------------------------------------------------------");
                while(resultSet.next()) {
                    System.out.println(resultSet.getInt(1)+"  "+
                            resultSet.getString(2)+"  "+
                            resultSet.getString(3)+"  "+
                            resultSet.getString(4)+"  "+
                            resultSet.getDouble(5)+"  "+
                            resultSet.getString(6)+"  ");
                    active=resultSet.getString(6);
                }
                System.out.println();
                int affectedRows=0;
                System.out.println("Corect account?  YES/NO");
                int a = new Scanner(System.in).nextInt();
                if(a==1) {
                    if(active.equals("YES")) {
                        PreparedStatement ps = connection.prepareStatement("UPDATE CLIENTS SET ACTIVE = 'NO' WHERE BANK_ACC_ID ='"+str+"'");
                        affectedRows=ps.executeUpdate();
                        PrintClients();
                    }else {
                        PreparedStatement ps = connection.prepareStatement("UPDATE CLIENTS SET ACTIVE = 'YES' WHERE BANK_ACC_ID ='"+str+"'");
                        affectedRows=ps.executeUpdate();
                        PrintClients();
                    }
                }else {
                    System.out.println("Sorry, let start again");
                }
            }else {System.out.println("Table is empty");}


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }*/
    public static void PrintClients() throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM clients");
        if(resultSet!=null) {
            System.out.println("ID       FIRST_NAME      SECOND_NAME        BIRTHDAY        SEX     ACCOUNT_N       ACCOUNT_B");
            System.out.println("-----------------------------------------------------------------------------------------");
            while(resultSet.next()) {
                System.out.println(resultSet.getInt(1)+"  "+
                        resultSet.getString(2)+"  "+
                        resultSet.getString(3)+"  "+
                        resultSet.getString(4)+"  "+
                        resultSet.getString(5)+"  "+
                        resultSet.getString(6)+"  "+
                        resultSet.getDouble(7)+"   ");
            }
            System.out.println();
        }
    }
    @SuppressWarnings("unchecked")
    public static  <T> T CheckValuses(Class<T> t,int lenght) {
        Scanner sc = new Scanner(System.in);
        if(t.equals(String.class)) {
            String str;
            while(true) {
                if(sc.hasNext()) {
                    str=sc.next();
                    if(str.length()<=lenght && str.length()>0) {
                        break;
                    }else {System.out.println("Enter characters for 1 to "+lenght);}
                }
            }
            return (T) str;
        }else {
            Double num;
            while(true) {
                if(sc.hasNextDouble()) {
                    num=sc.nextDouble();
                    break;
                }
            }

            return (T) num;
        }
    }
}
