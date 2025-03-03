package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.constant.BookStoreConstants;
import com.project.constant.db.BooksDBConstants;
import com.project.model.Book;
import com.project.model.UserRole;
import com.project.service.BookService;
import com.project.service.impl.BookServiceImpl;
import com.project.util.StoreUtil;

public class AddBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final BookService bookService = new BookServiceImpl();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<div class='alert alert-warning text-center mt-3'>Please Login First to Continue!</div>");
            return;
        }

        String bName = req.getParameter(BooksDBConstants.COLUMN_NAME);
        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);
        StoreUtil.setActiveTab(pw, "addbook");

        pw.println("<div class='container my-4'>");

        if (bName == null || bName.isBlank()) {
            showAddBookForm(pw);
            return;
        }

        // Process the form submission
        try {
            String uniqueID = UUID.randomUUID().toString();
            String bCode = uniqueID;
            String bAuthor = req.getParameter(BooksDBConstants.COLUMN_AUTHOR);
            double bPrice = Double.parseDouble(req.getParameter(BooksDBConstants.COLUMN_PRICE));
            int bQty = Integer.parseInt(req.getParameter(BooksDBConstants.COLUMN_QUANTITY));

            Book book = new Book(bCode, bName, bAuthor, bPrice, bQty);
            String message = bookService.addBook(book);

            if ("SUCCESS".equalsIgnoreCase(message)) {
                pw.println("<div class='alert alert-success text-center'>Book Added Successfully! <br> Add More Books.</div>");
            } else {
                pw.println("<div class='alert alert-danger text-center'>Failed to Add Book! Please Fill the Form Carefully.</div>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<div class='alert alert-danger text-center'>Failed to Add Book! Please Check Your Inputs.</div>");
        }

        pw.println("</div>");
    }

    private static void showAddBookForm(PrintWriter pw) {
        String form = "<div class='container'>"
                + "    <div class='card shadow-sm mx-auto' style='max-width: 500px;'>"
                + "        <div class='card-header bg-primary text-white text-center'>"
                + "            <h4>Add a New Book</h4>"
                + "        </div>"
                + "        <div class='card-body'>"
                + "            <form action='addbook' method='post'>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookName' class='form-label'>Book Name:</label>"
                + "                    <input type='text' name='name' id='bookName' class='form-control' placeholder='Enter Book Name' required>"
                + "                </div>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookAuthor' class='form-label'>Author:</label>"
                + "                    <input type='text' name='author' id='bookAuthor' class='form-control' placeholder='Enter Author Name' required>"
                + "                </div>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookPrice' class='form-label'>Price:</label>"
                + "                    <input type='number' name='price' id='bookPrice' class='form-control' placeholder='Enter Price' required>"
                + "                </div>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookQuantity' class='form-label'>Quantity:</label>"
                + "                    <input type='number' name='quantity' id='bookQuantity' class='form-control' placeholder='Enter Quantity' required>"
                + "                </div>"
                + "                <button type='submit' class='btn text-white w-100' style='background-color: #0d6efd;'>Add Book</button>"
                + "            </form>"
                + "        </div>"
                + "    </div>"
                + "</div>";

        pw.println(form);
    }

}
