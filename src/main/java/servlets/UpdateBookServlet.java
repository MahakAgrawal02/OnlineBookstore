package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.constant.BookStoreConstants;
import com.project.constant.ResponseCode;
import com.project.constant.db.BooksDBConstants;
import com.project.model.Book;
import com.project.model.UserRole;
import com.project.service.BookService;
import com.project.service.impl.BookServiceImpl;
import com.project.util.StoreUtil;

public class UpdateBookServlet extends HttpServlet {
    BookService bookService = new BookServiceImpl();

    public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter pw = res.getWriter();
        res.setContentType(BookStoreConstants.CONTENT_TYPE_TEXT_HTML);

        if (!StoreUtil.isLoggedIn(UserRole.SELLER, req.getSession())) {
            RequestDispatcher rd = req.getRequestDispatcher("SellerLogin.html");
            rd.include(req, res);
            pw.println("<table class=\"tab\"><tr><td>Please Login First to Continue!!</td></tr></table>");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("SellerHome.html");
        rd.include(req, res);
        StoreUtil.setActiveTab(pw, "storebooks");
        pw.println("<div class='container my-2'>");

        try {
            if (req.getParameter("updateFormSubmitted") != null) {
                String bName = req.getParameter(BooksDBConstants.COLUMN_NAME);
                String bCode = req.getParameter(BooksDBConstants.COLUMN_BARCODE);
                String bAuthor = req.getParameter(BooksDBConstants.COLUMN_AUTHOR);
                double bPrice = Double.parseDouble(req.getParameter(BooksDBConstants.COLUMN_PRICE));
                int bQty = Integer.parseInt(req.getParameter(BooksDBConstants.COLUMN_QUANTITY));

                Book book = new Book(bCode, bName, bAuthor, bPrice, bQty);
                String message = bookService.updateBook(book);
                if (ResponseCode.SUCCESS.name().equalsIgnoreCase(message)) {
                    pw.println(
                            "<div class=\"alert alert-success text-center mt-3\">Book Detail Updated Successfully!</div>\n"
                            + "");
                } else {
                    pw.println("<div class=\"alert alert-danger text-center mt-3\">Failed to Update Book!!</div>\n"
                            + "");
                    // rd.include(req, res);
                }

                return;
            }

            String bookId = req.getParameter("bookId");

            if (bookId != null) {
                Book book = bookService.getBookById(bookId);
                showUpdateBookForm(pw, book);
            }

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<table class=\"tab\"><tr><td>Failed to Load Book data!!</td></tr></table>");
        }
    }

    private static void showUpdateBookForm(PrintWriter pw, Book book) {
        String form = "<div class='container'>"
                + "    <div class='card shadow-sm mx-auto' style='max-width: 500px;'>"
                + "        <div class='card-header bg-primary text-white text-center'>"
                + "            <h4>Update Book Details</h4>"
                + "        </div>"
                + "        <div class='card-body'>"
                + "            <form action='updatebook' method='post'>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookCode' class='form-label'>Book Code:</label>"
                + "                    <input type='text' name='barcode' id='bookCode' class='form-control' value='" 
                + book.getBarcode() + "' readonly>"
                + "                </div>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookName' class='form-label'>Book Name:</label>"
                + "                    <input type='text' name='name' id='bookName' class='form-control' value='" 
                + book.getName() + "' required>"
                + "                </div>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookAuthor' class='form-label'>Author:</label>"
                + "                    <input type='text' name='author' id='bookAuthor' class='form-control' value='" 
                + book.getAuthor() + "' required>"
                + "                </div>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookPrice' class='form-label'>Price:</label>"
                + "                    <input type='number' name='price' id='bookPrice' class='form-control' value='" 
                + book.getPrice() + "' required>"
                + "                </div>"
                + "                <div class='mb-3'>"
                + "                    <label for='bookQuantity' class='form-label'>Quantity:</label>"
                + "                    <input type='number' name='quantity' id='bookQuantity' class='form-control' value='" 
                + book.getQuantity() + "' required>"
                + "                </div>"
                + "                <button type='submit' name='updateFormSubmitted' class='btn text-white w-100' style='background-color: #0d6efd;'>Update Book</button>"
                + "            </form>"
                + "        </div>"
                + "    </div>"
                + "</div>";

        pw.println(form);
    }

}
