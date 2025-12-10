package es.etg.dam.DAO;

public class Curso {

    private int id;
    private String nombre;
    private String descripcion;
    private String alumnoNombre;
    private String alumnoApellido;

    public Curso(String nombre, String descripcion,
            String alumnoNombre, String alumnoApellido) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.alumnoNombre = alumnoNombre;
        this.alumnoApellido = alumnoApellido;
    }

    public Curso(int id, String nombre, String descripcion,
            String alumnoNombre, String alumnoApellido) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.alumnoNombre = alumnoNombre;
        this.alumnoApellido = alumnoApellido;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getAlumnoNombre() {
        return alumnoNombre;
    }

    public String getAlumnoApellido() {
        return alumnoApellido;
    }
}
