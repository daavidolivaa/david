package es.etg.dam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.etg.dam.DAO.Alumno;
import es.etg.dam.DAO.Curso;
import es.etg.dam.DAO.InstitutoDAO;
import es.etg.dam.DAO.InstitutoDAOFactory;

public class Main {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            // MENÚ 1 -> SELECCIÓN DE CONEXIÓN
            Modo modo = seleccionarModo(sc);

            if (modo == null) {
                System.out.println("Opción inválida. Saliendo...");
                return;
            }

            // Obtiene el DAO según el modo
            InstitutoDAO dao = InstitutoDAOFactory.obtenerDAO(modo);

            // Ejecuta menú correspondiente
            if (modo == Modo.SQLITE) {
                menuSQLite(sc, dao);
            } else if (modo == Modo.ORACLE) {
                menuOracle(sc, dao);
            } else {
                System.out.println("Modo MOCK aún no implementado.");
            }

        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------
    // MENÚ PRINCIPAL - ELECCIÓN DE BD
    // ---------------------------------------------------
    private static Modo seleccionarModo(Scanner sc) {

        System.out.println("\n===== SELECCIONA TIPO DE CONEXIÓN =====");
        System.out.println("1. Mock");
        System.out.println("2. SQLite");
        System.out.println("3. Oracle");
        System.out.print("Opción: ");

        int opcion = Integer.parseInt(sc.nextLine());

        return switch (opcion) {
            case 1 -> Modo.MOCK;
            case 2 -> Modo.SQLITE;
            case 3 -> Modo.ORACLE;
            default -> null;
        };
    }

    // ---------------------------------------------------
    // MENÚ SQLITE
    // ---------------------------------------------------
    private static void menuSQLite(Scanner sc, InstitutoDAO dao) {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n=========== MENÚ SQLITE ===========");
            System.out.println("1. Crear tablas");
            System.out.println("2. Listar alumnos");
            System.out.println("3. Insertar alumno");
            System.out.println("4. Actualizar alumno");
            System.out.println("5. Borrar alumno");
            System.out.println("6. Insertar lista de alumnos");

            System.out.println("7. Insertar curso");
            System.out.println("8. Listar cursos");

            System.out.println("9. Listar alumnos con cursos");
            System.out.println("10. Buscar alumno por nombre");

            System.out.println("11. Eliminar tabla alumno");
            System.out.println("12. Eliminar tabla curso");

            System.out.println("0. Volver");
            System.out.print("Elige opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            try {

                switch (opcion) {

                    // -------- ALUMNOS --------

                    case 1 -> {
                        dao.crearTabla();
                        System.out.println("Tablas creadas.");
                    }

                    case 2 -> {
                        System.out.println("\n--- LISTADO DE ALUMNOS ---");
                        for (Alumno a : dao.listarAlumnos()) {
                            System.out.println(a.getNombre() +
                                    " " + a.getApellido() +
                                    " " + a.getEdad());
                        }
                    }

                    case 3 -> {
                        System.out.println("\n--- INSERTAR ALUMNO ---");

                        System.out.print("Nombre: ");
                        String n = sc.nextLine();

                        System.out.print("Apellido: ");
                        String ap = sc.nextLine();

                        System.out.print("Edad: ");
                        int e = Integer.parseInt(sc.nextLine());

                        dao.insertar(new Alumno(n, ap, e));
                        System.out.println("Alumno insertado.");
                    }

                    case 4 -> {
                        System.out.println("\n--- ACTUALIZAR ALUMNO ---");

                        System.out.print("Nombre: ");
                        String n = sc.nextLine();

                        System.out.print("Apellido: ");
                        String ap = sc.nextLine();

                        System.out.print("Nueva edad: ");
                        int e = Integer.parseInt(sc.nextLine());

                        dao.actualizar(new Alumno(n, ap, e));
                        System.out.println("Alumno actualizado.");
                    }

                    case 5 -> {
                        System.out.println("\n--- BORRAR ALUMNO ---");

                        System.out.print("Nombre: ");
                        String n = sc.nextLine();

                        System.out.print("Apellido: ");
                        String ap = sc.nextLine();

                        dao.borrar(new Alumno(n, ap, 0));
                        System.out.println("Alumno borrado.");
                    }

                    case 6 -> {

                        System.out.print("¿Cuántos alumnos?: ");
                        int cantidad = Integer.parseInt(sc.nextLine());

                        List<Alumno> lista = new ArrayList<>();

                        for (int i = 1; i <= cantidad; i++) {

                            System.out.println("\nAlumno " + i);

                            System.out.print("Nombre: ");
                            String n = sc.nextLine();

                            System.out.print("Apellido: ");
                            String ap = sc.nextLine();

                            System.out.print("Edad: ");
                            int e = Integer.parseInt(sc.nextLine());

                            lista.add(new Alumno(n, ap, e));
                        }

                        dao.insertar(lista);
                        System.out.println("Lista insertada.");
                    }

                    // -------- CURSOS --------

                    case 7 -> {

                        System.out.println("\n--- INSERTAR CURSO ---");

                        System.out.print("Nombre del curso: ");
                        String c = sc.nextLine();

                        System.out.print("Descripción: ");
                        String d = sc.nextLine();

                        System.out.print("Nombre alumno: ");
                        String na = sc.nextLine();

                        System.out.print("Apellido alumno: ");
                        String aa = sc.nextLine();

                        dao.insertarCurso(new Curso(c, d, na, aa));

                        System.out.println("Curso insertado.");
                    }

                    case 8 -> {
                        System.out.println("\n--- LISTADO DE CURSOS ---");

                        for (Curso c : dao.listarCursos()) {
                            System.out.println(
                                    "ID: " + c.getId() +
                                            " | Curso: " + c.getNombre() +
                                            " | Alumno: " +
                                            c.getAlumnoNombre() + " " +
                                            c.getAlumnoApellido());
                        }
                    }

                    // -------- CONSULTAS --------

                    case 9 -> {
                        System.out.println("\n--- ALUMNOS CON CURSOS ---");

                        for (String s : dao.listarAlumnoConCursos()) {
                            System.out.println(s);
                        }
                    }

                    case 10 -> {
                        System.out.println("\n--- BUSCAR ALUMNO ---");

                        System.out.print("Nombre a buscar: ");
                        String n = sc.nextLine();

                        for (Alumno a : dao.buscarAlumnoPorNombre(n)) {
                            System.out.println(
                                    a.getNombre() + " "
                                            + a.getApellido()
                                            + " " + a.getEdad());
                        }
                    }

                    // -------- BORRAR TABLAS --------

                    case 11 -> {
                        dao.eliminarTablaAlumno();
                        System.out.println("Tabla alumno eliminada.");
                    }

                    case 12 -> {
                        dao.eliminarTablaCurso();
                        System.out.println("Tabla curso eliminada.");
                    }

                    case 0 ->
                        System.out.println(
                                "Volviendo al menú principal...");

                    default ->
                        System.out.println("Opción incorrecta.");
                }

            } catch (Exception e) {
                System.err.println("Error SQLite: " + e.getMessage());
            }
        }
    }

    // ---------------------------------------------------
    // MENÚ ORACLE
    // ---------------------------------------------------
    private static void menuOracle(Scanner sc, InstitutoDAO dao) {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n===== MENÚ ORACLE =====");
            System.out.println("1. Crear tabla");
            System.out.println("2. Insertar alumno");
            System.out.println("3. Actualizar alumno");
            System.out.println("4. Listar alumnos");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            try {

                switch (opcion) {

                    case 1 -> {
                        dao.crearTabla();
                        System.out.println("Tabla creada en Oracle");
                    }

                    case 2 -> System.out.println("Inserción Oracle - No implementado");

                    case 3 -> System.out.println("Actualización Oracle - No implementado");

                    case 4 -> System.out.println("Listado Oracle - No implementado");

                    case 0 -> System.out.println("Volviendo al menú principal...");

                    default -> System.out.println("Opción inválida.");
                }

            } catch (Exception e) {
                System.err.println("Error Oracle: " + e.getMessage());
            }
        }
    }
}
