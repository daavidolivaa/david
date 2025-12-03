package es.etg.dam;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstitutoSQLiteDAOImp implements InstitutoDAO {

    private static final String DATABASE_NAME = "Instituto.db";
    private static final String JDBC_URL = "jdbc:sqlite:%s";

    private final Connection conn;

    public InstitutoSQLiteDAOImp() throws Exception {
        URL resource = InstitutoSQLiteDAOImp.class.getResource(DATABASE_NAME);
        String path = new File(resource.toURI()).getAbsolutePath();
        String url = String.format(JDBC_URL, path);
        conn = DriverManager.getConnection(url);
    }

    // LISTAR ALUMNOS
    @Override
    public List<Alumno> listarAlumnos() throws SQLException {
        final String query = "SELECT * FROM alumno";

        List<Alumno> alumnos = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            int edad = rs.getInt("edad");

            Alumno a = new Alumno(nombre, apellido, edad);
            alumnos.add(a);
        }
        rs.close();
        ps.close();

        return alumnos;

    }

    // CREAR TABLA ALUMNOS
    @Override
    public void crearTablaAlumno() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS alumno (" + " nombre text PRIMARY KEY);";
        PreparedStatement st = conn.prepareStatement(sql);
        st.execute();
        st.close();
    }

    // ELIMINAR TABLA ALUMNOS
    @Override
    public void eliminarTablaAlumno() throws Exception {
        String sql = "DROP TABLE alumno";
        PreparedStatement st = conn.prepareStatement(sql);
        st.execute();
        st.close();
    }

    //INSERTAR
    @Override
    public int insertar(Alumno a) throws SQLException {
        int numRegistrosActualizados = 0;
        final String sql = "INSERT INTO alumno VALUES (?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());
        ps.setInt(3, a.getEdad());
        numRegistrosActualizados = ps.executeUpdate();

        ps.close();
        return numRegistrosActualizados;
    }

    //INSERTAR LISTA DE ALUMNOS
    @Override
    public int insertar(List<Alumno> alumnos) throws SQLException {
        final String sql = "INSERT INTO alumno VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for (Alumno a : alumnos) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setInt(3, a.getEdad());
            //Añade a la lista de ejecución ese insert
            ps.addBatch();
        }
        conn.setAutoCommit(false);
        ps.executeBatch();
        conn.setAutoCommit(true);
        ps.close();
        return alumnos.size();
    }

    //ACTUALIZAR
    @Override
    public int actualizar(Alumno a) throws SQLException {
        int numRegistrosActualizados = 0;
        final String sql = "UPDATE alumno SET edad = ? WHERE nombre = ? AND apellido = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, a.getEdad());
        ps.setString(2, a.getNombre());
        ps.setString(3, a.getApellido());
        numRegistrosActualizados = ps.executeUpdate();

        ps.close();
        return numRegistrosActualizados;
    }

    //BORRAR ALUMNO
    @Override
    public int borrar(Alumno a) throws SQLException {
        int numRegistrosActualizados = 0;
        final String sql = "DELETE FROM alumno WHERE nombre = ? AND apellido = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, a.getNombre());
        ps.setString(2, a.getApellido());
        numRegistrosActualizados = ps.executeUpdate();

        ps.close();
        return numRegistrosActualizados;
    }
}
