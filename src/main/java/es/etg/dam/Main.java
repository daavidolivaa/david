package es.etg.dam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.etg.dam.SQLite.Alumno;
import es.etg.dam.SQLite.InstitutoDAO;
import es.etg.dam.SQLite.InstitutoDAOFactory;

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

            System.out.println("\n===== MENÚ SQLITE =====");
            System.out.println("1. Listar alumnos");
            System.out.println("2. Insertar alumno");
            System.out.println("3. Actualizar alumno");
            System.out.println("4. Borrar alumno");
            System.out.println("5. Crear tabla alumnos");
            System.out.println("6. Eliminar tabla alumnos");
            System.out.println("7. Insertar lista de alumnos");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            try {

                switch (opcion) {

                    case 1 -> {
                        System.out.println("\n--- LISTADO DE ALUMNOS ---");
                        for (Alumno a : dao.listarAlumnos()) {
                            System.out.println(a.getNombre() + " "
                                    + a.getApellido() + " "
                                    + a.getEdad());
                        }
                    }

                    case 2 -> {
                        System.out.println("\n--- INSERTAR ALUMNO ---");
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();

                        System.out.print("Apellido: ");
                        String apellido = sc.nextLine();

                        System.out.print("Edad: ");
                        int edad = Integer.parseInt(sc.nextLine());

                        dao.insertar(new Alumno(nombre, apellido, edad));
                        System.out.println("Alumno insertado correctamente.");
                    }

                    case 3 -> {
                        System.out.println("\n--- ACTUALIZAR ALUMNO ---");
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();

                        System.out.print("Apellido: ");
                        String apellido = sc.nextLine();

                        System.out.print("Nueva edad: ");
                        int edad = Integer.parseInt(sc.nextLine());

                        dao.actualizar(new Alumno(nombre, apellido, edad));
                        System.out.println("Alumno actualizado.");
                    }

                    case 4 -> {
                        System.out.println("\n--- BORRAR ALUMNO ---");

                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();

                        System.out.print("Apellido: ");
                        String apellido = sc.nextLine();

                        dao.borrar(new Alumno(nombre, apellido, 0));
                        System.out.println("Alumno eliminado.");
                    }

                    case 5 -> {
                        dao.crearTablaAlumno();
                        System.out.println("Tabla creada.");
                    }

                    case 6 -> {
                        dao.eliminarTablaAlumno();
                        System.out.println("Tabla eliminada.");
                    }

                    case 7 -> {

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

                    case 0 -> System.out.println("Volviendo al menú principal...");

                    default -> System.out.println("Opción inválida.");
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
                        dao.crearTablaAlumno();
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
