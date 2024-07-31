package service;

import dataaccess.DBAuthDAO;

public class AuthService implements Service {

    DBAuthDAO authDAO = new DBAuthDAO(); //MemoryAuthDAO.getInstance();

    @Override
    public void clear() {
        try {
            authDAO.clearAuths();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
