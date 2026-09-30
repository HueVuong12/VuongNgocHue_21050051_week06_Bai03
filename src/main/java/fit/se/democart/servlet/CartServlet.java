package fit.se.democart.servlet;

import fit.se.democart.beans.CartBean;
import fit.se.democart.beans.Product;
import fit.se.democart.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "CartServlet", urlPatterns = { "/cart" })
public class CartServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getCart(req);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        CartBean cart = getCart(req);

        if ("add".equalsIgnoreCase(action)) {
            String idStr = req.getParameter("id");
            String qtyStr = req.getParameter("quantity");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                int qty = 1;
                try {
                    if (qtyStr != null) qty = Integer.parseInt(qtyStr);
                } catch (NumberFormatException ignored) {
                }
                Product p = productDAO.getProductById(id);
                if (p != null) {
                    cart.addProduct(p, qty);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/cart");
        } else if ("update".equalsIgnoreCase(action)) {
            String idStr = req.getParameter("id");
            String qtyStr = req.getParameter("quantity");
            if (idStr != null && qtyStr != null) {
                int id = Integer.parseInt(idStr);
                int qty = Integer.parseInt(qtyStr);
                cart.updateQuantity(id, qty);
            }
            resp.sendRedirect(req.getContextPath() + "/cart");
        } else if ("remove".equalsIgnoreCase(action)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                cart.removeProduct(id);
            }
            resp.sendRedirect(req.getContextPath() + "/cart");
        } else if ("clear".equalsIgnoreCase(action)) {
            cart.clear();
            resp.sendRedirect(req.getContextPath() + "/cart");
        } else {
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }

    private CartBean getCart(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        CartBean cart = (CartBean) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
