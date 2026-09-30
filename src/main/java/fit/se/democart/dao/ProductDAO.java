package fit.se.democart.dao;

import fit.se.democart.beans.Product;
import fit.se.democart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public ProductDAO() {
        ensureTableAndData();
    }

    private void ensureTableAndData() {
        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS products (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "model VARCHAR(255) NOT NULL, " +
                    "description VARCHAR(500), " +
                    "quantity INT NOT NULL, " +
                    "price DOUBLE NOT NULL, " +
                    "imgurl LONGTEXT)");

            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate("INSERT INTO products (model, description, quantity, price, imgurl) VALUES " +
                            "('Samsung', 'Samsung Smart TV UHD 4K Display', 10, 20000.0, 'images/samsung.svg'), " +
                            "('Sony', 'Sony Bravia 4K HDR Google TV', 10, 23000.0, 'images/sony.svg'), " +
                            "('Iphone 15', 'Apple iPhone 15 Titanium 128GB', 10, 17000.0, 'images/iphone15.svg'), " +
                            "('Iphone 16', 'Apple iPhone 16 Pro Max 256GB', 10, 18000.0, 'images/iphone16.svg')");
                }
            }
        } catch (Exception e) {
            System.err.println("[ProductDAO] Warning in ensureTableAndData: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, model, description, quantity, price, imgurl FROM products";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("model"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getString("imgurl")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list.isEmpty()) {
            list.add(new Product(1, "Samsung", "Samsung Smart TV UHD 4K", 10, 20000.0, "images/samsung.svg"));
            list.add(new Product(2, "Sony", "Sony Bravia OLED 4K", 10, 23000.0, "images/sony.svg"));
            list.add(new Product(3, "Iphone 15", "Apple iPhone 15 Titanium", 10, 17000.0, "images/iphone15.svg"));
            list.add(new Product(4, "Iphone 16", "Apple iPhone 16 Pro Max", 10, 18000.0, "images/iphone16.svg"));
        }
        return list;
    }

    public Product getProductById(int id) {
        String sql = "SELECT id, model, description, quantity, price, imgurl FROM products WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("model"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getDouble("price"),
                            rs.getString("imgurl"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Product p : getAllProducts()) {
            if (p.getId() == id)
                return p;
        }
        return null;
    }
}
