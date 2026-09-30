package fit.se.democart.servlet;

import fit.se.democart.beans.Product;
import fit.se.democart.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", urlPatterns = { "/products", "/product-detail" })
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/product-detail".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    Product product = productDAO.getProductById(id);
                    req.setAttribute("product", product);
                    req.getRequestDispatcher("/product-detail.jsp").forward(req, resp);
                    return;
                } catch (NumberFormatException ignored) {
                }
            }
            resp.sendRedirect(req.getContextPath() + "/products");
        } else {
            List<Product> products = productDAO.getAllProducts();
            req.setAttribute("products", products);
            req.getRequestDispatcher("/product-list.jsp").forward(req, resp);
        }
    }
}
