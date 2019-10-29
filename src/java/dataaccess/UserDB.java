package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import models.User;
import java.util.List;
import models.Role;

public class UserDB {

    /**
     * This method inserts user elements and return the number of rows affected.
     *
     * @author Euna Cho
     * @param user user
     * @return rows rows
     * @throws java.sql.SQLException
     */
    public int insert(User user) throws SQLException {

        ConnectionPool connectionPool = null;
        Connection connection = null;

        int rows = 0;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            String preparedQuery
                    = "INSERT INTO User_Table "
                    + "(email, fname, lname, password) "
                    + "VALUES "
                    + "(?, ?, ?, ?)";

            PreparedStatement ps = connection.prepareStatement(preparedQuery);

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFname());
            ps.setString(3, user.getLname());
            ps.setString(4, user.getPassword());

            rows = ps.executeUpdate();
            ps.close();
            return rows;
        } finally {
            connectionPool.freeConnection(connection);
        }
    }

    /**
     * This method update the User record.
     *
     * @param user User to be updated
     * @return successCount Number of records updated
     * @throws java.sql.SQLException
     */
    public int update(User user) throws SQLException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            String preparedQuery = "UPDATE User_Table set active=?, fname=?, lname=?, password=? where email=?";
            int successCount = 0;

            PreparedStatement statement = connection.prepareStatement(preparedQuery);
            statement.setBoolean(1, user.isActive());
            statement.setString(2, user.getFname());
            statement.setString(3, user.getLname());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());

            successCount = statement.executeUpdate();
            statement.close();
            return successCount;
        } finally {
            connectionPool.freeConnection(connection);
        }

    }

    /**
     * This method queries the database for all users. Every user is put into an
     * ArrayList of users
     *
     * @return ArrayList users - the list of users retrieved from the database.
     * @throws SQLException
     */
    public List<User> getAll() throws SQLException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            User user;
            ArrayList<User> users = new ArrayList<>();

            String preparedQuery = "SELECT active, email, fname, lname, password, role FROM user_table";
            PreparedStatement ps = connection.prepareStatement(preparedQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                boolean active = rs.getBoolean(1);
                String userEmail = rs.getString(2);
                String fname = rs.getString(3);
                String lname = rs.getString(4);
                String password = rs.getString(5);
                
                int roleID = rs.getInt(6);
                RoleDB roleDB = new RoleDB();
                Role role = roleDB.getRole(roleID);
                        
                user = new User(userEmail, fname, lname, password, role);
                user.setActive(active);
                users.add(user);
            }

            return users;
        } finally {
            connectionPool.freeConnection(connection);
        }
    }

    /**
     * This method queries the database for a particular user (dude) that has a
     * matching email.
     *
     * @param email - the user's email to be searched for.
     * @return User dude - the user retrieved from the database.
     * @throws SQLException
     */
    public User getUser(String email) throws SQLException {

        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            User user = new User();
            String preparedQuery = "SELECT active, email, fname, lname, password, role FROM user_table WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(preparedQuery);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                boolean active = rs.getBoolean(1);
                String userEmail = rs.getString(2);
                String fname = rs.getString(3);
                String lname = rs.getString(4);
                String password = rs.getString(5);
                int roleID = rs.getInt(6);
                
                RoleDB roleDB = new RoleDB();
                Role role = roleDB.getRole(roleID);
                
                
                user = new User(userEmail, fname, lname, password, role);
                user.setActive(active);
            }

            return user;
        } finally {
            connectionPool.freeConnection(connection);
        }
    }

    /**
     * This method physically deletes a user from the user_table
     *
     * @param user
     * @return false returns false if there's nothing to de
     * @throws java.sql.SQLException
     */
    public boolean delete(User user) throws SQLException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            String DELETE_STMT = "DELETE FROM User_Table where email = ?";
            PreparedStatement prepare = connection.prepareStatement(DELETE_STMT);
            prepare.setString(1, user.getEmail());

            int rowCount = prepare.executeUpdate();
            prepare.close();
            return rowCount == 1;

        } finally {
            connectionPool.freeConnection(connection);
        }
    }
        /**
     * This method queries the database for all users. Every user is put into an
     * ArrayList of users
     *
     * @return ArrayList users - the list of users retrieved from the database.
     * @throws SQLException
     */
    public List<User> getAllActive() throws SQLException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            User user;
            ArrayList<User> users = new ArrayList<>();

            String preparedQuery = "SELECT active, email, fname, lname, password, role FROM user_table WHERE active = 1";
            PreparedStatement ps = connection.prepareStatement(preparedQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                boolean active = rs.getBoolean(1);
                String userEmail = rs.getString(2);
                String fname = rs.getString(3);
                String lname = rs.getString(4);
                String password = rs.getString(5);
                
                int roleID = rs.getInt(6);
                RoleDB roleDB = new RoleDB();
                Role role = roleDB.getRole(roleID);
                        
                user = new User(userEmail, fname, lname, password, role);
                user.setActive(active);
                users.add(user);
            }

            return users;
        } finally {
            connectionPool.freeConnection(connection);
        }
    }
}
