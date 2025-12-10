package es.etg.dam.DAO.SQLite;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.etg.dam.DAO.Alumno;
import es.etg.dam.DAO.Curso;
import es.etg.dam.DAO.InstitutoDAO;

public class InstitutoSQLiteDAOImp implements InstitutoDAO {

    private static final String DATABASE_NAME = "/es/etg/dam/Instituto.db";
    private static final String JDBC_URL = "jdbc:sqlite:%s";

    private final Connection conn;

    public InstitutoSQLiteDAOImp() throws Exception {
        URL resource = InstitutoSQLiteDAOImp.class.getResource(DATABASE_NAME);
        String path = new File(resource.toURI()).getAbsolutePath();
        String url = String.format(JDBC_URL, path);
        conn = DriverManager.getConnection(url);
    }

    // ---------------- TABLAS -----------------------

    // CREAR TABLA
    @Override
    public void crearTabla() throws SQLException {

        final String sqlAlumno = "CREATE TABLE IF NOT EXISTS curso (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT," +
                "alumno_nombre TEXT NOT NULL," +
                "FOREIGN KEY (alumno_nombre) REFERENCES alumno(nombre))";

        String sqlCurso = """
                CREATE TABLE IF NOT EXISTS curso(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT,
                    descripcion TEXT,
                    alumno_nombre TEXT,
                    alumno_apellido TEXT,
                    FOREIGN KEY(alumno_nombre, alumno_apellido)
                    REFERENCES alumno(nombre,apellido))
                """;

        PreparedStatement ps1 = conn.prepareStatement(sqlAlumno);
        PreparedStatement ps2 = conn.prepareStatement(sqlCurso);
        ps1.executeUpdate();
        ps2.executeUpdate();
        ps1.close();
        ps2.close();
    }

    // ELIMINAR TABLA ALUMNOS
    @Override
    public void eliminarTablaAlumno() throws Exception {
        String sql = "DROP TABLE alumno";
        PreparedStatement st = conn.prepareStatement(sql);
        st.execute();
        st.close();
    }

    // ELIMINAR TABLA CURSO
    @Override
    public void eliminarTablaCurso() throws Exception {
        String sql = "DROP TABLE curso";
        PreparedStatement st = conn.prepareStatement(sql);
        st.execute();
        st.close();
    }

    // ---------------- ALUMNOS -----------------------

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

    // INSERTAR
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

    // INSERTAR LISTA DE ALUMNOS
    @Override
    public int insertar(List<Alumno> alumnos) throws SQLException {
        final String sql = "INSERT INTO alumno VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        for (Alumno a : alumnos) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getApellido());
            ps.setInt(3, a.getEdad());
            // Añade a la lista de ejecución ese insert
            ps.addBatch();
        }
        conn.setAutoCommit(false);
        ps.executeBatch();
        conn.setAutoCommit(true);
        ps.close();
        return alumnos.size();
    }

    // ACTUALIZAR
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

    // BORRAR ALUMNO
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

    // ---------------- CURSOS -----------------------

    @Override
    public int insertarCurso(Curso c) throws SQLException {

        PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO curso(nombre, descripcion,
                    alumno_nombre, alumno_apellido)
                    VALUES (?,?,?,?)
                """);

        ps.setString(1, c.getNombre());
        ps.setString(2, c.getDescripcion());
        ps.setString(3, c.getAlumnoNombre());
        ps.setString(4, c.getAlumnoApellido());

        return ps.executeUpdate();
    }

    @Override
    public List<Curso> listarCursos() throws Exception {

        final String query = "SELECT * FROM curso";

        List<Curso> cursos = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String descripcion = rs.getString("descripcion");
            String nombreAlumno = rs.getString("nombre del alumno");
            String apellidoAlumno = rs.getString("apellido del alumno");

            Curso a = new Curso(id, nombre, descripcion, nombreAlumno, apellidoAlumno);
            cursos.add(a);
        }
        rs.close();
        ps.close();

        return cursos;

    }

    public List<String> listarAlumnoConCursos() throws Exception {

        final String query = """
                SELECT alumno.nombre,
                       alumno.apellido,
                       curso.nombre AS curso
                FROM alumno
                JOIN curso
                    ON alumno.nombre = curso.alumno_nombre
                    AND alumno.apellido = curso.alumno_apellido
                """;

        List<String> lista = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String curso = rs.getString("curso");

            String registro = (nombre + " " + apellido + " " + curso);

            lista.add(registro);
        }

        rs.close();
        ps.close();

        return lista;
    }

    @Override
    public List<Alumno> buscarAlumnoPorNombre(String nombre) throws Exception {

        final String query = "SELECT * FROM alumno WHERE nombre LIKE ?";

        List<Alumno> lista = new ArrayList<>();

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, "%" + nombre + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String nom = rs.getString("nombre");
            String ape = rs.getString("apellido");
            int edad = rs.getInt("edad");

            Alumno a = new Alumno(nom, ape, edad);
            lista.add(a);
        }

        rs.close();
        ps.close();

        return lista;
    }
}
