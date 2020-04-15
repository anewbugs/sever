package data.enity;

public class Account {
    public String iduser;
    public String password;

    @Override
    public String toString() {
        return "Account{" +
                "iduser='" + iduser + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
