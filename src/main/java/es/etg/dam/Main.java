package es.etg.dam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            InstitutoSQLiteDAOImp dao = new InstitutoSQLiteDAOImp();
            Scanner sc = new Scanner(System.in);
            int opcion = -1;

            while (opcion != 0) {

                System.out.println("\n===== MENÚ INSTITUTO =====");
                System.out.println("1. Listar alumnos");
                System.out.println("2. Insertar alumno");
                System.out.println("3. Actualizar alumno");
                System.out.println("4. Borrar alumno");
                System.out.println("5. Crear tabla alumnos");
                System.out.println("6. Eliminar tabla alumnos");
                System.out.println("7. Insertar lista de alumnos");
                System.out.println("0. Salir");
                System.out.print("Elige una opción: ");

                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {

                    //LISTAR ALUMNOS
                    case 1:
                        System.out.println("\n--- LISTADO DE ALUMNOS ---");
                        for (Alumno a : dao.listarAlumnos()) {
                            System.out.println(a.getNombre() + " " + a.getApellido() + " " + a.getEdad());
                        }
                        break;

                    //INSERTAR ALUMNOS
                    case 2:
                        System.out.println("\n--- INSERTAR ALUMNO ---");
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();

                        System.out.print("Apellido: ");
                        String apellido = sc.nextLine();

                        System.out.print("Edad: ");
                        int edad = Integer.parseInt(sc.nextLine());

                        Alumno nuevo = new Alumno(nombre, apellido, edad);
                        dao.insertar(nuevo);
                        System.out.println("Alumno insertado correctamente.");
                        break;

                    //ACTUALIZAR ALUMNOS
                    case 3:
                        System.out.println("\n--- ACTUALIZAR ALUMNO ---");

                        System.out.print("Nombre del alumno a actualizar: ");
                        String nombreActualizar = sc.nextLine();

                        System.out.print("Apellido del alumno a actualizar: ");
                        String apellidoActualizar = sc.nextLine();

                        System.out.print("Nueva edad: ");
                        int nuevaEdad = Integer.parseInt(sc.nextLine());

                        Alumno actualizado = new Alumno(nombreActualizar, apellidoActualizar, nuevaEdad);
                        dao.actualizar(actualizado);
                        System.out.println("Alumno actualizado.");
                        break;

                    //BORRAR ALUMNOS
                    case 4:
                        System.out.println("\n--- BORRAR ALUMNO ---");

                        System.out.print("Nombre del alumno: ");
                        String nombreBorrar = sc.nextLine();

                        System.out.print("Apellido del alumno: ");
                        String apellidoBorrar = sc.nextLine();

                        Alumno borrar = new Alumno(nombreBorrar, apellidoBorrar, 0);
                        dao.borrar(borrar);
                        System.out.println("Alumno borrado.");
                        break;

                    // CREAR TABLA ALUMNOS
                    case 5:
                        System.out.println("\n--- CREAR TABLA ALUMNOS ---");
                        dao.crearTablaAlumno();
                        System.out.println("Tabla creada correctamente.");
                        break;

                    //BORRAR TABLA ALUMNOS
                    case 6:
                        System.out.println("\n--- ELIMINAR TABLA ALUMNOS ---");
                        dao.eliminarTablaAlumno();
                        System.out.println("Tabla eliminada correctamente.");
                        break;

                    //INSERTAR LISTA DE ALUMNOS
                    case 7:
                        System.out.println("\n--- INSERTAR LISTA DE ALUMNOS ---");
                        System.out.print("¿Cuántos alumnos quieres insertar?: ");
                        int cantidad = Integer.parseInt(sc.nextLine());

                        List<Alumno> lista = new ArrayList<>();

                        for (int i = 1; i <= cantidad; i++) {
                            System.out.println("\nAlumno " + i + ":");
                            System.out.print("Nombre: ");
                            String n = sc.nextLine();

                            System.out.print("Apellido: ");
                            String ap = sc.nextLine();

                            System.out.print("Edad: ");
                            int e = Integer.parseInt(sc.nextLine());

                            lista.add(new Alumno(n, ap, e));
                        }

                        int insertados = dao.insertar(lista);
                        System.out.println("\nSe han insertado " + insertados + " alumnos correctamente.");
                        break;

                    //SALIDA DE PROGRAMA
                    case 0:
                        System.out.println("Saliendo del programa");
                        break;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                }
            }

            sc.close();

        } catch (Exception e) {
            System.err.println("Error al acceder a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
