package com.example.user_manager.controller;

import com.example.user_manager.model.Employee;
import com.example.user_manager.model.IUserDAO;
import com.example.user_manager.model.User;
import com.example.user_manager.model.UserDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="UserServlet", urlPatterns = "/users")

public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IUserDAO userDao;

    public void init() {
        userDao = new UserDAO();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            action ="";
        }
        try {
            switch (action) {
                case "create":
                    insertUser(req, resp);
                    break;
                case "edit":
                    updateUser(req, resp);
                    break;
                case "search":
                    searchUser(req, resp);
                    break;
            }
        } catch (SQLException e) {
                throw new ServletException();
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            action ="";
        }

        try {
            switch (action) {
                case "create":
                   showNewForm(req, resp);
                   break;
                case "edit":
                    showEditForm(req, resp);
                    break;
                case "delete":
                    deleteUser(req, resp);
                    break;
                case "search":
                    showSearchForm(req, resp);
                    break;
                case "sort":
                    sortUser(req, resp);
                    break;
                case "test-without-tran":
                    testWithoutTran(req, resp);
                    break;
                case "test-with-tran":
                    testWithTran(req, resp);
                    break;
                default:
                    listUser(req, resp);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException();
        }
    }

    private void testWithTran(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException{
        PrintWriter writer = resp.getWriter();
        writer.println("<html>");
        writer.println("<p>Hihi</p>");
        writer.println("</html>");
        userDao.insertUpdateUseTransaction();
    }

    private void testWithoutTran(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException{
        PrintWriter writer = resp.getWriter();
        writer.println("<html>");
        writer.println("<p>Hello</p>");
        writer.println("</html>");
        userDao.insertUpdateWithoutTransaction();
    }

    protected void insertUser(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String country = req.getParameter("country");
        User newUser = new User(name, email, country);

        String add = req.getParameter("add");
        String edit = req.getParameter("edit");
        String delete = req.getParameter("delete");
        String view = req.getParameter("view");

        List<Integer> permissions = new ArrayList<>();

        if (add != null) permissions.add(1);
        if (edit != null) permissions.add(2);
        if (delete != null) permissions.add(3);
        if (view != null) permissions.add(4);

        userDao.addUserTransaction(newUser, permissions);

        RequestDispatcher dispatcher = req.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(req, resp);
    }

    protected void updateUser(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, ServletException, IOException{
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String address = req.getParameter("address");

        User user = new User(id, name, email, address);
        userDao.updateUser(user);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(req, resp);
    }

    protected void deleteUser(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        userDao.deleteUser(id);

        List<User> userList = userDao.selectAllUsers();
        req.setAttribute("userList", userList);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req, resp);
    }

    protected void showNewForm(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/create.jsp");
        dispatcher.forward(req, resp);
    }

    protected void showEditForm(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("id"));
        User existingUser = userDao.getUserById(id);
        req.setAttribute("user", existingUser);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/edit.jsp");
        dispatcher.forward(req, resp);
    }

    protected void listUser(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException, ServletException {
        List<User> userList = userDao.selectAllUsers();
        req.setAttribute("userList", userList);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req, resp);
    }

    private void sortUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        List<User> userList = userDao.sortUserByName();
        req.setAttribute("userList", userList);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req, resp);
    }

    private void searchUser(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String country = req.getParameter("country");
        List<User> userList = userDao.selectUserByCountry(country);
        req.setAttribute("userList", userList);
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/list.jsp");
        dispatcher.forward(req, resp);
    }

    private void showSearchForm(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("user/search.jsp");
        dispatcher.forward(req, resp);
    }
}
