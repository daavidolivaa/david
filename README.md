# Actividad Conectores

- **Nombre:** David Oliva Huelamo
- **Curso:** 2ºDAM

## Descripción del Proyecto

El usuario puede elegir trabajar con:

-**SQLite
-Oracle XE (Docker)
-Mock**

Una vez seleccionada la base de datos, el sistema ofrece un menu comun con todas las opciones disponibles.

## Funcionalidades Principales

### Gestión de Alumnos

- Crear la tabla de alumnos.
- Insertar un alumno.
- Insertar una lista de varios alumnos.
- Listar todos los alumnos.
- Buscar alumnos por nombre.
- Actualizar la edad de un alumno.
- Eliminar un alumno.
- Eliminar la tabla completa.

### Gestión de Cursos

- Crear la tabla de cursos.
- Insertar un curso asociado a un alumno.
- Listar todos los cursos.
- Listar alumnos junto con sus cursos.
- Eliminar un curso por su ID.
- Eliminar la tabla completa.

## Diagrama UML de las Tablas

```mermaid
classDiagram
class Alumno {
        <<entity>>
        +nombre : String <<PK>PK>
        +apellido : String <<PK>PK>
        +edad : int
        +getNombre() : String
        +getApellido() : String
        +getEdad() : int
    }

    class Curso {
        <<entity>>
        +id : int <<PK>PK>
        +nombre : String
        +alumnoNombre : String <<FK>FK>
        +alumnoApellido : String <<FK>FK>
        +getId() : int
        +getNombre() : String
        +getAlumnoNombre() : String
        +getAlumnoApellido() : String
    }

    Alumno "1" --> "0..*" Curso : pertenece >
```

## Diagrama UML del Proyecto

```mermaid
classDiagram
    class Main {
        +main(args : String[]) : void
    }

    class InstitutoDAO {
        <<interface>>
        +crearTablas() : void
        +insertarAlumno(Alumno) : void
        +insertarListaAlumnos(List~Alumno~) : void
        +insertarCurso(Curso) : void
        +listarAlumnos() : List~Alumno~
        +listarCursos() : List~Curso~
        +listarAlumnosConCursos() : List~Object~
        +actualizarAlumno(Alumno) : void
        +buscarAlumnoPorNombre(String) : Alumno
        +borrarAlumno(String,String) : void
        +borrarCurso(int) : void
        +eliminarTablaAlumno() : void
        +eliminarTablaCurso() : void
    }

    class InstitutoDAOFactory {
        +obtenerDAO(tipo : String) : InstitutoDAO
    }

    class InstitutoSQLiteDAOImp {
        -conn : Connection
    }

    class InstitutoOracleXeDAOImp {
        -conn : Connection
    }

    class InstitutoMockDAOImp {
        -conn : Connection
    }

    class Alumno {
        <<entity>>
        +nombre : String <<PK>PK>
        +apellido : String <<PK>PK>
        +edad : int
        +getNombre() : String
        +getApellido() : String
        +getEdad() : int
    }

    class Curso {
        <<entity>>
        +id : int <<PK>PK>
        +nombre : String
        +alumnoNombre : String <<FK>FK>
        +alumnoApellido : String <<FK>FK>
        +getId() : int
        +getNombre() : String
        +getAlumnoNombre() : String
        +getAlumnoApellido() : String
    }

    %% Relaciones
    Main --> InstitutoDAOFactory : solicita DAO

    InstitutoDAOFactory --> InstitutoDAO : devuelve implementación

    InstitutoSQLiteDAOImp ..|> InstitutoDAO
    InstitutoOracleXeDAOImp ..|> InstitutoDAO
    InstitutoMockDAOImp ..|> InstitutoDAO

    InstitutoDAO --> Alumno : usa >
    InstitutoDAO --> Curso : usa >

    Alumno "1" --> "0..*" Curso : pertenece >
```