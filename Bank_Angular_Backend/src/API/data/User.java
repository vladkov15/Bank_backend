package API.data;

public class User {
    int id;
    String first_name;
    String last_name;
    String birthday;
    String sex;
    String account_number;
    double account_balance;

    public User(int id, String first_name, String last_name, String birthday, String sex, String account_number, double account_balance) {
        this.id = id;
        this.first_name = first_name.strip();
        this.last_name = last_name.strip();
        this.birthday = birthday.strip();
        this.sex = sex.strip();
        this.account_number = account_number;
        this.account_balance = account_balance;
    }
    public String convert(){
        StringBuffer sBuffer = new StringBuffer("{");
        sBuffer.append("\"id\": " + String.valueOf(this.id) + ",");
        sBuffer.append("\"first_name\": \"" + this.first_name + "\",");
        sBuffer.append("\"last_name\": \"" + this.last_name + "\",");
        sBuffer.append("\"birthday\": \"" + this.birthday + "\",");
        sBuffer.append("\"sex\": \"" + this.sex + "\",");
        sBuffer.append("\"account_number\": \"" + this.account_number + "\",");
        sBuffer.append("\"account_balance\": " + String.valueOf(this.account_balance));
        sBuffer.append("}");
        return sBuffer.toString();
    }}
