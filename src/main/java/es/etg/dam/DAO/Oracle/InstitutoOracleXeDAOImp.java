package es.etg.dam.DAO.Oracle;

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

public class InstitutoOracleXeDAOImp implements InstitutoDAO {

    private final String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    String DATABASE_USER = "usuario";
    String DATABASE_PASS = "usuario";
    private final Connection conn;

    public InstitutoOracleXeDAOImp() throws Exception {

        conn = DriverManager.getConnection(URL, DATABASE_USER, DATABASE_PASS);
    }

    // ================= TABLAS ===================
    @Override
    public void crearTabla() throws SQLException {

        String tablaAlumno = """
                CREATE TABLE alumno (
                    nombre VARCHAR2(50),
                    apellido VARCHAR2(50),
                    edad NUMBER,
                    CONSTRAINT pk_alumno PRIMARY KEY (nombre, apellido)
                )
                """;

        String tablaCurso = """
                CREATE TABLE curso (
                    id NUMBER GENERATED ALWAYS AS IDENTITY,
                    nombre VARCHAR2(50) NOT NULL,
                    descripcion VARCHAR2(100),
                    alumno_nombre VARCHAR2(50) NOT NULL,
                    alumno_apellido VARCHAR2(50) NOT NULL,
                    CONSTRAINT pk_curso PRIMARY KEY (id),
                    CONSTRAINT fk_curso_alumno FOREIGN KEY (alumno_nombre, alumno_apellido)
                    REFERENCES alumno(nombre, apellido)
                    ON DELETE CASCADE
                )
                """;

        try (PreparedStatement ps = conn.prepareStatement(tablaAlumno)) {
            ps.executeUpdate();
        } catch (SQLException e) {
        }

        try (PreparedStatement ps = conn.prepareStatement(tablaCurso)) {
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    // ================= ALUMNOS ===================
    // INSERTAR ALUMNOS
    @Override
    public int insertar(Alumno alumno) throws SQLException {

        try (PreparedStatement ps = conn
                .prepareStatement("INSERT INTO alumno(nombre, apellido, edad) VALUES (?,?,?)")) {
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());
            ps.setInt(3, alumno.getEdad());

            int filas = ps.executeUpdate();
            return filas;
        }
    }

    // INSERTAR LISTA DE ALUMNOS
    @Override
    public int insertar(List<Alumno> alumnos) throws SQLException {

        try (PreparedStatement ps = conn
                .prepareStatement("INSERT INTO alumno(nombre, apellido, edad) VALUES (?,?,?)")) {
            for (Alumno alumno : alumnos) {
                ps.setString(1, alumno.getNombre());
                ps.setString(2, alumno.getApellido());
                ps.setInt(3, alumno.getEdad());
                ps.addBatch();
            }

            ps.executeBatch();
        }

        return alumnos.size();
    }

    // ACTUALIZAR ALUMNO
    @Override
    public int actualizar(Alumno alumno) throws SQLException {

        try (PreparedStatement ps = conn
                .prepareStatement("UPDATE alumno SET edad = ? WHERE nombre = ? AND apellido = ?")) {
            ps.setInt(1, alumno.getEdad());
            ps.setString(2, alumno.getNombre());
            ps.setString(3, alumno.getApellido());

            int filas = ps.executeUpdate();
            return filas;
        }
    }

    // BORRAR ALUMNO
    @Override
    public int borrar(Alumno alumno) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(" DELETE FROM alumno WHERE nombre = ? AND apellido = ?")) {
            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getApellido());

            int filas = ps.executeUpdate();
            return filas;
        }
    }

    // LISTAR ALUMNO
    @Override
    public List<Alumno> listarAlumnos() throws SQLException {

        List<Alumno> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM alumno")) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    int edad = rs.getInt("edad");

                    Alumno alumno = new Alumno(nombre, apellido, edad);
                    lista.add(alumno);
                }
            }
        }
        return lista;
    }

    // BUSCAR ALUMNO POR SU NOMBRE
    @Override
    public List<Alumno> buscarAlumnoPorNombre(String nombre) throws Exception {

        List<Alumno> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM alumno WHERE nombre LIKE ?")) {
            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    String nom = rs.getString("nombre");
                    String ape = rs.getString("apellido");
                    int edad = rs.getInt("edad");

                    Alumno alumno = new Alumno(nom, ape, edad);
                    lista.add(alumno);
                }
            }
        }

        return lista;
    }

    // ============= CURSOS =============
    // INSERTAR CURSO
    @Override
    public int insertarCurso(Curso curso) throws SQLException {

        final String query = "INSERT INTO curso(nombre, alumno_nombre, alumno_apellido) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, curso.getNombre());
            ps.setString(2, curso.getAlumnoNombre());
            ps.setString(3, curso.getAlumnoApellido());

            return ps.executeUpdate();
        }
    }

    // LISTAR CURSO
    @Override
    public List<Curso> listarCursos() throws Exception {

        List<Curso> lista = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM curso"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String nombreAlumno = rs.getString("alumno_nombre");
                String apellidoAlumno = rs.getString("alumno_apellido");

                Curso curso = new Curso(id, nombre, nombreAlumno, apellidoAlumno);
                lista.add(curso);
            }
        }

        return lista;
    }

    // LISTAR ALUMNO CON CURSOS
    @Override
    public List<String> listarAlumnoConCursos() throws Exception {

        List<String> lista = new ArrayList<>();

        final String query = """
                SELECT a.nombre, a.apellido, c.nombre AS curso
                FROM alumno a
                JOIN curso c
                ON a.nombre = c.alumno_nombre
                AND a.apellido = c.alumno_apellido
                """;

        try (PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String curso = rs.getString("curso");

                lista.add(nombre + " " + apellido + " " + curso);
            }
        }

        return lista;
    }

    // BORRAR CURSO
    @Override
    public int borrarCurso(int id) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM curso WHERE id = ?")) {

            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    // ELIMINAR LA TABLA ALUMNO
    @Override
    public void eliminarTablaAlumno() throws Exception {

        final String query = "DROP TABLE alumno CASCADE CONSTRAINTS";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
        }
    }

    // ELIMINAR LA TABLA CURSO
    @Override
    public void eliminarTablaCurso() throws Exception {

        final String query = "DROP TABLE curso CASCADE CONSTRAINTS";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
        }
    }
}
