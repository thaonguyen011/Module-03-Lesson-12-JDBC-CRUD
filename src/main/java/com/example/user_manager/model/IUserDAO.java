package com.example.user_manager.model;

import com.example.user_manager.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
     void insertUser(User user) throws SQLException;

     User selectUser(int id);

     List<User> selectAllUsers();

     boolean deleteUser(int id) throws SQLException;

     boolean updateUser(User user) throws SQLException;

     List<User> selectUserByCountry(String country) throws SQLException;

     List<User> sortUserByName() throws SQLException;

    User getUserById(int id);

    void insertUserStore(User user) throws SQLException;

    void addUserTransaction(User user, List<Integer> permission);
    void insertUpdateWithoutTransaction();

    void insertUpdateUseTransaction();

    List<Employee> selectAllEmployeeUseCallableStatement();
    List<User> selectAllUserUseCallableStatement();
}
