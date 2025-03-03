package servlets;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.constant.BookStoreConstants;
import com.project.service.UserService;
import com.project.service.impl.UserServiceImpl;

public class LogoutServlet extends HttpServlet {

    UserService authService = new UserServiceImpl();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        try {

            boolean logout = authService.logout(req.getSession());

            RequestDispatcher rd = req.getRequestDispatcher("CustomerLogin.html");
            rd.include(req, res);
//            StoreUtil.setActiveTab(pw, "logout");
            if (logout) {
                pw.println("<div class=\"container d-flex justify-content-center mt-3\">\n"
                        + "    <div class=\"alert alert-success text-center p-2 small\" style=\"max-width: 300px;\">\n"
                        + "        Successfully logged out!\n"
                        + "    </div>\n"
                        + "</div>\n"
                        + "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}