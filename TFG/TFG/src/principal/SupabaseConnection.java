package principal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SupabaseConnection {

    // Abre una conexión con la base de datos.
    public static Connection abrirConexion() throws SQLException {

        String url = "jdbc:postgresql://aws-1-eu-west-3.pooler.supabase.com:6543/postgres";

        String username = "postgres.neegrhfjslezlrqmvipk";
        String password = "Jyr**1997";

        return DriverManager.getConnection(url, username, password);
    }
}
