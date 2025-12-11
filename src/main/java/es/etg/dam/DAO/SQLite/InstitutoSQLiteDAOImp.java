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

    // ================= TABLAS ===================
    // CREAR TABLA
    @Override
    public void crearTabla() throws SQLException {

        String tablaAlumno = """
                CREATE TABLE IF NOT EXISTS alumno(
                    nombre TEXT NOT NULL,
                    apellido TEXT NOT NULL,
                    edad INTEGER,
                    PRIMARY KEY(nombre, apellido)
                );
                """;

        String tablaCurso = """
                CREATE TABLE IF NOT EXISTS curso(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    descripcion TEXT,
                    alumno_nombre TEXT NOT NULL,
                    alumno_apellido TEXT NOT NULL,
                    FOREIGN KEY(alumno_nombre, alumno_apellido)
                        REFERENCES alumno(nombre, apellido)
                );
                """;

        PreparedStatement ps1 = conn.prepareStatement(tablaAlumno);
        PreparedStatement ps2 = conn.prepareStatement(tablaCurso);
        ps1.executeUpdate();
        ps2.executeUpdate();
        ps1.close();
        ps2.close();
    }

    // ELIMINAR TABLA ALUMNOS
    @Override
    public void eliminarTablaAlumno() throws Exception {
        String query = "DROP TABLE alumno";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.execute();
        ps.close();
    }

    // ELIMINAR TABLA CURSO
    @Override
    public void eliminarTablaCurso() throws Exception {
        String query = "DROP TABLE curso";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.execute();
        ps.close();
    }

    // ================== ALUMNOS ===================
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

            Alumno alumno = new Alumno(nombre, apellido, edad);
            alumnos.add(alumno);
        }
        rs.close();
        ps.close();

        return alumnos;

    }

    // INSERTAR
    @Override
    public int insertar(Alumno alumno) throws SQLException {
        int numRegistrosActualizados = 0;
        final String query = "INSERT INTO alumno VALUES (?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, alumno.getNombre());
        ps.setString(2, alumno.getApellido());
        ps.setInt(3, alumno.getEdad());
        numRegistrosActualizados = ps.executeUpdate();

        ps.close();
        return numRegistrosActualizados;
    }

    // INSERTAR LISTA DE ALUMNOS
    @Override
    public int insertar(List<Alumno> alumnos) throws SQLException {
        final String query = "INSERT INTO alumno VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        for (Alumno alumno : alumnos) {
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setInt(3, alumno.getEdad());

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
    public int actualizar(Alumno alumno) throws SQLException {
        int numRegistrosActualizados = 0;
        final String query = "UPDATE alumno SET edad = ? WHERE nombre = ? AND apellido = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, alumno.getEdad());
        ps.setString(2, alumno.getNombre());
        ps.setString(3, alumno.getApellido());
        numRegistrosActualizados = ps.executeUpdate();

        ps.close();
        return numRegistrosActualizados;
    }

    // BORRAR ALUMNO
    @Override
    public int borrar(Alumno alumno) throws SQLException {
        int numRegistrosActualizados = 0;
        final String query = "DELETE FROM alumno WHERE nombre = ? AND apellido = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, alumno.getNombre());
        ps.setString(2, alumno.getApellido());
        numRegistrosActualizados = ps.executeUpdate();

        ps.close();
        return numRegistrosActualizados;
    }

    // =================== CURSOS ====================
    // INSERTAR CURSO
    @Override
    public int insertarCurso(Curso curso) throws SQLException {

        PreparedStatement ps = conn.prepareStatement("""
                    INSERT INTO curso(nombre, descripcion,
                    alumno_nombre, alumno_apellido)
                    VALUES (?,?,?,?)
                """);

        ps.setString(1, curso.getNombre());
        ps.setString(3, curso.getAlumnoNombre());
        ps.setString(4, curso.getAlumnoApellido());

        return ps.executeUpdate();
    }

    // LISTAR CURSOS
    @Override
    public List<Curso> listarCursos() throws Exception {

        final String query = "SELECT * FROM curso";

        List<Curso> cursos = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String nombreAlumno = rs.getString("alumno_nombre");
            String apellidoAlumno = rs.getString("alumno_apellido");

            Curso a = new Curso(id, nombre, nombreAlumno, apellidoAlumno);
            cursos.add(a);
        }
        rs.close();
        ps.close();

        return cursos;

    }

    // LISTAR ALUMNOS CON CURSOS
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

    // BUSCAR ALUMNO POR NOMBRE
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

            Alumno alumno = new Alumno(nom, ape, edad);
            lista.add(alumno);
        }

        rs.close();
        ps.close();

        return lista;
    }

    // BORRAR CURSO
    @Override
    public int borrarCurso(int id) throws Exception {

        final String query = "DELETE FROM curso WHERE id = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, id);

        int cursos = ps.executeUpdate();

        ps.close();
        return cursos;
    }
}
