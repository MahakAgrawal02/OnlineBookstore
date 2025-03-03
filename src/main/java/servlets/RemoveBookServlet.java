package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.constant.ResponseCode;
import com.project.model.UserRole;
import com.project.service.BookService;
import com.project.service.impl.BookServiceImpl;
import com.project.util.StoreUtil;

public class RemoveBookServlet extends HttpServlet {

    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<div class='alert alert-warning text-center mt-3'>Please login first to continue!</div>");
            return;
        }

        try {
            String bookId = req.getParameter("bookId");
            RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
            rd.include(req, res);
            StoreUtil.setActiveTab(pw, "removebook");

            pw.println("<div class='container mt-4'>");

            if (bookId == null || bookId.isBlank()) {
                // Render the remove book form
                showRemoveBookForm(pw);
                return;
            } 

            String responseCode = bookService.deleteBookById(bookId.trim());

            if (ResponseCode.SUCCESS.name().equalsIgnoreCase(responseCode)) {
                pw.println("<div class='alert alert-success text-center'>Book removed successfully!</div>");
            } else {
                pw.println("<div class='alert alert-danger text-center'>Book not available in the store.</div>");
            }

            pw.println("<div class='text-center mt-3'>");
            pw.println("<a href='removebook' class='btn btn-outline-primary'>Remove More Books</a>");
            pw.println("</div>");
            pw.println("</div>");

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<div class='alert alert-danger text-center mt-3'>Failed to remove the book! Please try again.</div>");
        }
    }

    private static void showRemoveBookForm(PrintWriter pw) {
        String form = "<div class='container mt-5'>"
                + "    <div class='card shadow-sm mx-auto' style='max-width: 500px;'>"
                + "        <div class='card-header text-white text-center'>"
                + "            <h4>Remove a Book</h4>"
                + "        </div>"
                + "        <div class='card-body'>"
                + "            <form action='removebook' method='post'>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookCode' class='form-label'>Enter Book ID:</label>"
                + "                    <input type='text' name='bookId' id='bookCode' class='form-control' placeholder='Enter Book ID' required>"
                + "                </div>"
                + "                <button type='submit' class='btn btn-primary w-100'>Remove Book</button>"
                + "            </form>"
                + "        </div>"
                + "    </div>"
                + "</div>";

        pw.println(form);
    }

}
