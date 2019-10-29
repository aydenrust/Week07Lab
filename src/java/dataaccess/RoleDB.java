/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Role;
import models.User;

/**
 *
 * @author awarsyle
 */
public class RoleDB {
    public Role getRole(int roleID) throws SQLException {

        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            Role role = null;
            String preparedQuery = "SELECT RoleID, RoleName FROM role_table WHERE RoleID=?";
            PreparedStatement ps = connection.prepareStatement(preparedQuery);
            ps.setInt(1, roleID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String roleName = rs.getString(2);
                role = new Role(roleID, roleName);
            }

            return role;
        } finally {
            connectionPool.freeConnection(connection);
        }
    }
    
       /**
     * This method inserts role elements and return the number of rows affected.
     *
     * @param role role
     * @return rows rows
     * @throws java.sql.SQLException
     */
    public int insert(Role role) throws SQLException {

        ConnectionPool connectionPool = null;
        Connection connection = null;

        int rows = 0;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            String preparedQuery
                    = "INSERT INTO role_table "
                    + "(RoleID, RoleName) "
                    + "VALUES "
                    + "(?, ?)";

            PreparedStatement ps = connection.prepareStatement(preparedQuery);

            String id = Integer.toString(role.getRoleID());
            ps.setString(1, id);
            ps.setString(2, role.getRoleName());

            rows = ps.executeUpdate();
            ps.close();
            return rows;
        } finally {
            connectionPool.freeConnection(connection);
        }
    }

    /**
     * This method update the role record.
     *
     * @param role role to be updated
     * @return successCount Number of records updated
     * @throws java.sql.SQLException
     */
    public int update(Role role) throws SQLException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            String preparedQuery = "UPDATE Role_table set RoleID=?, RoleName=?";
            int successCount = 0;

            PreparedStatement ps = connection.prepareStatement(preparedQuery);
            
            String id = Integer.toString(role.getRoleID());
            ps.setString(1, id);
            ps.setString(2, role.getRoleName());
            

            successCount = ps.executeUpdate();
            ps.close();
            return successCount;
        } finally {
            connectionPool.freeConnection(connection);
        }

    }

    /**
     * This method queries the database for all roles. Every role is put into an
     * ArrayList of roles
     *
     * @return ArrayList roles - the list of roles retrieved from the database.
     * @throws SQLException
     */
    public List<Role> getAll() throws SQLException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();
            Role role;
            ArrayList<Role> roles = new ArrayList<>();

            String preparedQuery = "SELECT * FROM role_table";
            PreparedStatement ps = connection.prepareStatement(preparedQuery);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int roleId = rs.getInt(1);
                String roleName = rs.getString(2);


                role = new Role(roleId, roleName);
                        
                roles.add(role);

            }

            return roles;
        } finally {
            connectionPool.freeConnection(connection);
        }
    }

    /**
     * This method physically deletes a role from the role_
     *
     * @param role
     * @return false returns false if there's nothing to delete
     * @throws java.sql.SQLException
     */
    public boolean delete(Role role) throws SQLException {
        ConnectionPool connectionPool = null;
        Connection connection = null;
        try {
            connectionPool = ConnectionPool.getInstance();
            connection = connectionPool.getConnection();

            String DELETE_STMT = "DELETE FROM Role_Table where RoleID = ?";
            PreparedStatement prepare = connection.prepareStatement(DELETE_STMT);
            prepare.setString(1, Integer.toString(role.getRoleID()));

            int rowCount = prepare.executeUpdate();
            prepare.close();
            return rowCount == 1;

        } finally {
            connectionPool.freeConnection(connection);
        }
    }
}
