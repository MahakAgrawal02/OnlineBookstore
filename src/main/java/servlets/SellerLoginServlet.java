package servlets;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.constant.BookStoreConstants;
import com.project.constant.db.UsersDBConstants;
import com.project.model.User;
import com.project.model.UserRole;
import com.project.service.UserService;
import com.project.service.impl.UserServiceImpl;

public class SellerLoginServlet extends HttpServlet {

    UserService userService = new UserServiceImpl();

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);
        String uName = req.getParameter(UsersDBConstants.COLUMN_USERNAME);
        String pWord = req.getParameter(UsersDBConstants.COLUMN_PASSWORD);
        try {
            User user = userService.login(UserRole.SELLER, uName, pWord, req.getSession());
            if (user != null) {
                RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");

                rd.include(req, res);
                pw.println("    <div id=\"topmid\"><h1>Welcome to Online <br>Book Store</h1></div>\r\n"
                        + "    <br>\r\n"
                        + "    <table class=\"tab\">\r\n"
                        + "        <tr>\r\n"
                        + "            <td><p>Welcome "+user.getFirstName()+", Happy Learning !!</p></td>\r\n"
                        + "        </tr>\r\n"
                        + "    </table>");
            } else {

                RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
                rd.include(req, res);
                pw.println("<div class=\"tab\">Incorrect UserName or PassWord</div>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}