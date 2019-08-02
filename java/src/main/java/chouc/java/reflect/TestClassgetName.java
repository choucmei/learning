package chouc.java.reflect;

import java.sql.SQLException;

public class TestClassgetName {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String driveName = "com.mysql.jdbc.Driver";

        Class.forName("chouc.java.reflect.TestClassgetName");
        System.out.println();
    }
}
