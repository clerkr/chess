package service;

import dataAccess.DBAuthDAO;
import dataAccess.MemoryAuthDAO;

public class AuthService implements Service {

//    MemoryAuthDAO authDAO = MemoryAuthDAO.getInstance();
    DBAuthDAO authDAO = new DBAuthDAO();

    @Override
    public void clear() {
        try {
            authDAO.clearAuths();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
