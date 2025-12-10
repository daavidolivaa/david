package es.etg.dam.DAO.Oracle;

import java.sql.SQLException;
import java.util.List;

import es.etg.dam.DAO.Alumno;
import es.etg.dam.DAO.Curso;
import es.etg.dam.DAO.InstitutoDAO;

public class InstitutoOracleXeDAOImp implements InstitutoDAO {

    @Override
    public void crearTabla() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearTablaAlumno'");
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

}
