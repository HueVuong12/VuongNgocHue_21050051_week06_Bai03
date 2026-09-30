package vn.fit.vuongngochue_bai3_tuan06.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vn.fit.vuongngochue_bai3_tuan06.dto.RepriceReportDTO;
import vn.fit.vuongngochue_bai3_tuan06.dto.RevenueShareDTO;
import vn.fit.vuongngochue_bai3_tuan06.model.Product;
import vn.fit.vuongngochue_bai3_tuan06.model.ShoppingCart;
import vn.fit.vuongngochue_bai3_tuan06.repository.ProductRepository;
import vn.fit.vuongngochue_bai3_tuan06.repository.ShoppingCartRepository;

import java.util.*;

@ApplicationScoped
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ShoppingCartRepository cartRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    public boolean createProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean updateProduct(Product product) {
        return productRepository.update(product);
    }

    public boolean deleteProduct(int id) {
        return productRepository.delete(id);
    }

    /**
     * Yêu cầu 3: Điều chỉnh giá sản phẩm hàng loạt theo doanh số giỏ hàng (Dynamic Repricing by Popularity)
     * - Tính tổng số lượng đặt (SUM(quantity)) của từng sản phẩm trong toàn bộ ShoppingCart
     * - Nếu tổng đặt >= 5: tăng giá 10% (newPrice = oldPrice * 1.10)
     * - Nếu chưa có lượt đặt nào (quantity = 0): giảm giá 5% (newPrice = oldPrice * 0.95)
     * - Cập nhật lại giá mới vào bảng Product và trả về danh sách biến động giá
     */
    public List<RepriceReportDTO> applyDynamicRepricing() {
        List<Product> products = productRepository.findAll();
        List<ShoppingCart> allCarts = cartRepository.findAll();

        // Gom nhóm và tính tổng số lượng đặt của từng productId
        Map<Integer, Integer> productQtyMap = new HashMap<>();
        for (ShoppingCart cart : allCarts) {
            productQtyMap.put(cart.getProductId(),
                    productQtyMap.getOrDefault(cart.getProductId(), 0) + cart.getQuantity());
        }

        List<RepriceReportDTO> report = new ArrayList<>();

        for (Product p : products) {
            int totalQty = productQtyMap.getOrDefault(p.getId(), 0);
            double oldPrice = p.getPrice();
            double newPrice = oldPrice;
            String adjustment = null;

            if (totalQty >= 5) {
                newPrice = Math.round((oldPrice * 1.10) * 100.0) / 100.0;
                adjustment = "+10%";
            } else if (totalQty == 0) {
                newPrice = Math.round((oldPrice * 0.95) * 100.0) / 100.0;
                adjustment = "-5%";
            }

            if (adjustment != null) {
                p.setPrice(newPrice);
                productRepository.update(p);
                report.add(new RepriceReportDTO(
                        p.getId(),
                        p.getName(),
                        totalQty,
                        oldPrice,
                        newPrice,
                        adjustment
                ));
            }
        }

        return report;
    }

    /**
     * Yêu cầu 5: Thống kê Doanh thu dự kiến & Đóng góp % từng sản phẩm (Revenue Share Analytics)
     * - Tính doanh thu của từng sản phẩm trong giỏ hàng: productRevenue = price * SUM(quantity)
     * - Tính tổng doanh thu toàn bộ giỏ hàng: totalRevenue = SUM(productRevenue)
     * - Tính tỷ lệ % đóng góp: (productRevenue / totalRevenue) * 100 (làm tròn 2 chữ số thập phân)
     * - Sắp xếp giảm dần theo doanh thu
     */
    public List<RevenueShareDTO> getRevenueShareReport() {
        List<Product> products = productRepository.findAll();
        List<ShoppingCart> allCarts = cartRepository.findAll();

        Map<Integer, Product> productMap = new HashMap<>();
        for (Product p : products) {
            productMap.put(p.getId(), p);
        }

        // Tính tổng số lượng trong giỏ hàng của từng sản phẩm
        Map<Integer, Integer> cartUnitsMap = new HashMap<>();
        for (ShoppingCart cart : allCarts) {
            cartUnitsMap.put(cart.getProductId(),
                    cartUnitsMap.getOrDefault(cart.getProductId(), 0) + cart.getQuantity());
        }

        double totalRevenue = 0.0;
        List<RevenueShareDTO> statsList = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : cartUnitsMap.entrySet()) {
            int productId = entry.getKey();
            int units = entry.getValue();
            Product product = productMap.get(productId);
            if (product != null) {
                double revenue = Math.round((product.getPrice() * units) * 100.0) / 100.0;
                totalRevenue += revenue;
                statsList.add(new RevenueShareDTO(
                        productId,
                        product.getName(),
                        units,
                        revenue,
                        "" // Sẽ gán sau khi có totalRevenue
                ));
            }
        }

        final double finalTotalRevenue = totalRevenue;
        for (RevenueShareDTO dto : statsList) {
            double percent = (finalTotalRevenue > 0) ? (dto.getRevenueContribution() / finalTotalRevenue) * 100.0 : 0.0;
            dto.setPercentage(String.format(Locale.US, "%.2f%%", percent));
        }

        // Sắp xếp giảm dần theo doanh thu đóng góp
        statsList.sort((a, b) -> Double.compare(b.getRevenueContribution(), a.getRevenueContribution()));

        return statsList;
    }
}
