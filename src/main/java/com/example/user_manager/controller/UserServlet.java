package com.example.user_manager.controller;

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
import java.sql.SQLException;
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
                default:
                    listUser(req, resp);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException();
        }

    }

    protected void insertUser(HttpServletRequest req, HttpServletResponse resp)
        throws SQLException, IOException, ServletException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String address = req.getParameter("address");
        User newUser = new User(name, email, address);

        userDao.insertUser(newUser);

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
        User existingUser = userDao.selectUser(id);
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
