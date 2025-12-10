package es.etg.dam.DAO;

import es.etg.dam.DAO.Mock.InstitutoMockDAOImp;
import es.etg.dam.DAO.Oracle.InstitutoOracleXeDAOImp;
import es.etg.dam.DAO.SQLite.InstitutoSQLiteDAOImp;
import es.etg.dam.Modo;

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
