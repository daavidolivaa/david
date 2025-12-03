package es.etg.dam;

public class InstitutoDAOFactory {

    public static InstitutoDAO obtenerDAO(Modo modo) throws Exception {

        if (modo == Modo.SQLITE) {
            return new InstitutoSQLiteDAOImp();
        } else if (modo == Modo.ORACLE) {
            return new InstitutoOracleXeDAOImp();
        } else {
            return new InstitutoMockDAOImp();
        }
    }
}
