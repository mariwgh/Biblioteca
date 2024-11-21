package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BancoDeDados {
    public static Connection conexao;
    public static Connection getConnection(String senha , String user , String db , String server) throws SQLException {
        String URL = "jdbc:sqlserver://" + server + ":1433;databaseName=" + db +";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        try{
            conexao = DriverManager.getConnection(URL, user, senha);
            return conexao;
        }
        catch (SQLException erro){
            System.out.println(erro.getMessage());
            return null;
        }
    }
}