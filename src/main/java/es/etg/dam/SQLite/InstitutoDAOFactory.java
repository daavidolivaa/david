package es.etg.dam.SQLite;

import es.etg.dam.Mock.InstitutoMockDAOImp;
import es.etg.dam.Modo;
import es.etg.dam.Oracle.InstitutoOracleXeDAOImp;

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
