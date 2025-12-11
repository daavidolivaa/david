package es.etg.dam.DAO.Oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import es.etg.dam.DAO.Alumno;
import es.etg.dam.DAO.Curso;
import es.etg.dam.DAO.InstitutoDAO;

public class InstitutoOracleXeDAOImp implements InstitutoDAO {

    private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    String DATABASE_USER = "usuario";
    String DATABASE_PASS = "usuario";
    private final Connection conn;

    public InstitutoOracleXeDAOImp() throws Exception {

        conn = DriverManager.getConnection(URL, DATABASE_USER, DATABASE_PASS);
    }

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

        PreparedStatement ps1 = conn.prepareStatement(tablaAlumno);
        PreparedStatement ps2 = conn.prepareStatement(tablaCurso);

        try {
            ps1.executeUpdate(tablaAlumno);
        } catch (Exception e) {
        }
        try {
            ps2.executeUpdate(tablaCurso);
        } catch (Exception e) {
        }

        ps1.close();
        ps2.close();
    }

    @Override
    public void eliminarTablaAlumno() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarTablaAlumno'");
    }

    @Override
    public List<Alumno> listarAlumnos() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarAlumnos'");
    }

    @Override
    public int insertar(Alumno a) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertar'");
    }

    @Override
    public int insertar(List<Alumno> alumnos) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertar'");
    }

    @Override
    public int actualizar(Alumno a) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

    @Override
    public int borrar(Alumno a) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrar'");
    }

    @Override
    public void eliminarTablaCurso() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarTablaCurso'");
    }

    @Override
    public int insertarCurso(Curso c) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertarCurso'");
    }

    @Override
    public List<Curso> listarCursos() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarCursos'");
    }

    @Override
    public List<String> listarAlumnoConCursos() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarAlumnoConCursos'");
    }

    @Override
    public List<Alumno> buscarAlumnoPorNombre(String nombre) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarAlumnoPorNombre'");
    }

    @Override
    public int borrarCurso(int id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrarCurso'");
    }

}
