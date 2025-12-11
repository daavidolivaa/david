package es.etg.dam.DAO;

import java.sql.SQLException;
import java.util.List;

public interface InstitutoDAO {

    // Tablas

    public void crearTabla() throws Exception;

    public void eliminarTablaAlumno() throws Exception;

    public void eliminarTablaCurso() throws Exception;

    // Alumnos

    public List<Alumno> listarAlumnos() throws SQLException;

    public int insertar(Alumno a) throws SQLException;

    public int insertar(List<Alumno> alumnos) throws SQLException;

    public int actualizar(Alumno a) throws SQLException;

    public int borrar(Alumno a) throws SQLException;

    // Cursos

    int insertarCurso(Curso c) throws Exception;

    List<Curso> listarCursos() throws Exception;

    List<String> listarAlumnoConCursos() throws Exception;

    List<Alumno> buscarAlumnoPorNombre(String nombre) throws Exception;

    int borrarCurso(int id) throws Exception;
}
