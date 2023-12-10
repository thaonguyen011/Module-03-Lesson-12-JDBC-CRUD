package com.example.user_manager.model;


import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO{
    private String jdbcURL = "jdbc:mysql://localhost:3306/users";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Modicung2486!";

    private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    private static final String SELECT_USER_BY_COUNTRY = "select * from users where country = ?";
    private static final String SELECT_ALL_USER_BY_NAME = "select * from users order by name";
    private static final String CREATE_TABLE_EMPLOYEE = "create table employee (" +
            "id int(3) not null auto_increment," +
            "name varchar(120) not null," +
            "salary int(120) not null," +
            "created_Date datetime," +
            "primary key(id)" +
            ");";
    private static final String DROP_TABLE_EMPLOYEE = "drop table if exists employee;";
    private static final String INSERT_EMPLOYEE = "insert into employee(name, salary, created_Date) values (?,?,?);";
    private static final String UPDATE_EMPLOYEE = "update employee set salary = ? where name = ?;";


    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void insertUser(User user) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public User selectUser(int id) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public List<User> selectUserByCountry(String country) throws SQLException {
        List<User> users = new ArrayList<>();
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_COUNTRY);
            statement.setString(1, country);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                User user = new User(id, name, email, country);
                users.add(user);
            }
        return users;

    }

    @Override
    public List<User> sortUserByName() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USER_BY_NAME);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String country = rs.getString("country");
            User user = new User(id, name, email, country);
            users.add(user);
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "{call get_user_by_id(?)}";

        try {
            Connection connection = getConnection();
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, id);
            ResultSet rs = callableStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public void insertUserStore(User user) throws SQLException {
        String sql = "{CALL insert_user(?,?,?)}";
        Connection connection = null;
        try {
            connection = getConnection();
            CallableStatement statement = connection.prepareCall(sql);
            connection.setAutoCommit(false);
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            printSQLException(e);
            try {
                connection.rollback();
            } catch (SQLException ex) {
                printSQLException(ex);
            }
        }

    }

    @Override
    public void addUserTransaction(User user, List<Integer> permission) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtAssignment = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            pstmt = connection.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getCountry());
            int rowAffected = pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            int userId = 0;

            if (rs.next()) {
                userId = rs.getInt(1);
            }

            if (rowAffected == 1) {
                String sqlPivot = "insert into user_permision(user_id, permision_id) values (?,?)";
                pstmtAssignment = connection.prepareStatement(sqlPivot);
                for (int permissionId : permission) {
                    pstmtAssignment.setInt(1, userId);
                    pstmtAssignment.setInt(2, permissionId);
                    pstmtAssignment.executeUpdate();
                }
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.out.println(e.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (pstmtAssignment != null) pstmtAssignment.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void insertUpdateWithoutTransaction() {
       try {
           Connection connection = getConnection();
           PreparedStatement psDrop = connection.prepareStatement(DROP_TABLE_EMPLOYEE);
           psDrop.execute();

           PreparedStatement psCreate = connection.prepareStatement(CREATE_TABLE_EMPLOYEE);
           psCreate.execute();

           PreparedStatement psInsert = connection.prepareStatement(INSERT_EMPLOYEE);
           psInsert.setString(1, "Quynh");
           psInsert.setInt(2, 100000000);
           psInsert.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
           psInsert.executeUpdate();

           psInsert.setString(1, "Ngan");
           psInsert.setInt(2, 10000000);
           psInsert.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
           psInsert.executeUpdate();

           PreparedStatement psUpdate = connection.prepareStatement(UPDATE_EMPLOYEE);
           psUpdate.setString(1, "hello");
           psUpdate.setString(2,"Quynh");
           psUpdate.executeUpdate();

       } catch (Exception ex) {
           ex.printStackTrace();
       }
    }

    @Override
    public void insertUpdateUseTransaction() {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            PreparedStatement psInsert = connection.prepareStatement(INSERT_EMPLOYEE);
            PreparedStatement psUpdate = connection.prepareStatement(UPDATE_EMPLOYEE);

            statement.execute(DROP_TABLE_EMPLOYEE);
            statement.execute(CREATE_TABLE_EMPLOYEE);

            connection.setAutoCommit(false);

            psInsert.setString(1, "Quynh");
            psInsert.setInt(2, 1000000);
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psInsert.setString(1, "Ngan");
            psInsert.setInt(2, 1000000);
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            psUpdate.setInt(1, 200000);
            psUpdate.setString(2,"Quynh");
            psUpdate.execute();

            connection.commit();
            connection.setAutoCommit(true);


        } catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public List<Employee> selectAllEmployeeUseCallableStatement() {
        List<Employee> employees = new ArrayList<>();
        String query = "{call select_all_employee();}";

        try {
            Connection connection = getConnection();
            CallableStatement cS = connection.prepareCall(query);
            ResultSet rs = cS.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int salary = rs.getInt("salary");
                Timestamp createdDate = rs.getTimestamp("created_Date");
                Employee employee = new Employee(id, name, salary, createdDate);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<User> selectAllUserUseCallableStatement() {
        List<User> employees = new ArrayList<>();
        String query = "{call select_all_user();}";

        try {
            Connection connection = getConnection();
            CallableStatement cS = connection.prepareCall(query);
            ResultSet rs = cS.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                User employee = new User(id, name, email, country);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
}