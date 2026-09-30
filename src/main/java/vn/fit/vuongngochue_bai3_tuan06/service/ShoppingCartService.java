package vn.fit.vuongngochue_bai3_tuan06.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vn.fit.vuongngochue_bai3_tuan06.dto.AddCartResponseDTO;
import vn.fit.vuongngochue_bai3_tuan06.dto.CartItemDetailDTO;
import vn.fit.vuongngochue_bai3_tuan06.dto.CheckoutSummaryDTO;
import vn.fit.vuongngochue_bai3_tuan06.dto.CustomerBillDTO;
import vn.fit.vuongngochue_bai3_tuan06.model.Product;
import vn.fit.vuongngochue_bai3_tuan06.model.ShoppingCart;
import vn.fit.vuongngochue_bai3_tuan06.repository.ProductRepository;
import vn.fit.vuongngochue_bai3_tuan06.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ShoppingCartService {

    @Inject
    private ShoppingCartRepository cartRepository;

    @Inject
    private ProductRepository productRepository;

    public List<ShoppingCart> getAllCarts() {
        return cartRepository.findAll();
    }

    public ShoppingCart getCartById(int id) {
        return cartRepository.findById(id);
    }

    public boolean createCart(ShoppingCart cart) {
        return cartRepository.save(cart);
    }

    public boolean updateCart(ShoppingCart cart) {
        return cartRepository.update(cart);
    }

    public boolean deleteCart(int id) {
        return cartRepository.delete(id);
    }

    /**
     * Yêu cầu 1: Tính tổng hóa đơn giỏ hàng của một khách hàng (Customer Cart Bill Breakdown)
     * - Lấy tất cả items của customerName từ ShoppingCartRepository
     * - Với mỗi item, lấy thông tin giá từ ProductRepository.findById(productId)
     * - Tính: itemTotal = quantity * price
     * - Cộng dồn subTotal, nếu subTotal > 2000 thì giảm giá 10% (discount = subTotal * 0.1), finalTotal = subTotal - discount
     * - Trả về đối tượng CustomerBillDTO chứa chi tiết và tổng tiền
     */
    public CustomerBillDTO calculateCustomerBill(String customerName) {
        List<ShoppingCart> items = cartRepository.findByCustomerName(customerName);
        List<CartItemDetailDTO> itemDetails = new ArrayList<>();
        double subTotal = 0.0;

        for (ShoppingCart item : items) {
            Product p = productRepository.findById(item.getProductId());
            String prodName = (p != null) ? p.getName() : "Unknown Product";
            double price = (p != null) ? p.getPrice() : 0.0;
            double itemTotal = Math.round((item.getQuantity() * price) * 100.0) / 100.0;
            subTotal += itemTotal;

            itemDetails.add(new CartItemDetailDTO(
                    item.getId(),
                    prodName,
                    price,
                    item.getQuantity(),
                    itemTotal
            ));
        }

        subTotal = Math.round(subTotal * 100.0) / 100.0;
        double discount = 0.0;
        if (subTotal > 2000.0) {
            discount = Math.round((subTotal * 0.10) * 100.0) / 100.0;
        }
        double finalTotal = Math.round((subTotal - discount) * 100.0) / 100.0;

        return new CustomerBillDTO(customerName, itemDetails, subTotal, discount, finalTotal);
    }

    /**
     * Yêu cầu 2: Thêm sản phẩm vào giỏ hàng có kiểm tra ràng buộc giá trị tối đa (Add Item with Value Cap)
     * - Khi thêm 1 sản phẩm vào giỏ, kiểm tra sản phẩm có tồn tại hay không
     * - Tính giá trị lô hàng thêm vào (product.price * quantity)
     * - Nếu giá trị vượt quá $5,000, từ chối thêm để tránh rủi ro gian lận
     * - Nếu hợp lệ, lưu vào DB và trả về AddCartResponseDTO kèm thông tin và tổng giá trị tính toán
     */
    public AddCartResponseDTO addItemToCart(ShoppingCart cart) {
        Product product = productRepository.findById(cart.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại!");
        }

        double estimatedTotal = Math.round((product.getPrice() * cart.getQuantity()) * 100.0) / 100.0;
        if (estimatedTotal > 5000.0) {
            throw new IllegalStateException("Giá trị đơn hàng vượt quá hạn mức cho phép ($5,000)!");
        }

        cartRepository.save(cart);
        return new AddCartResponseDTO(cart.getId(), product.getName(), cart.getQuantity(), estimatedTotal);
    }

    /**
     * Yêu cầu 4: Chuyển đổi toàn bộ giỏ hàng của khách thành Đơn đặt mua và Xóa giỏ (Checkout & Clear Cart)
     * - Nhận customerName
     * - Tính tổng số mặt hàng (totalItems), tổng số lượng (totalQuantity), tổng tiền (totalPaid)
     * - Xóa sạch các bản ghi giỏ hàng của khách hàng này trong bảng ShoppingCart
     * - Trả về CheckoutSummaryDTO phiếu tóm tắt thanh toán
     */
    public CheckoutSummaryDTO checkout(String customerName) {
        List<ShoppingCart> items = cartRepository.findByCustomerName(customerName);
        int totalItems = items.size();
        int totalQuantity = 0;
        double totalPaid = 0.0;

        for (ShoppingCart item : items) {
            totalQuantity += item.getQuantity();
            Product p = productRepository.findById(item.getProductId());
            if (p != null) {
                totalPaid += p.getPrice() * item.getQuantity();
            }
        }

        totalPaid = Math.round(totalPaid * 100.0) / 100.0;

        // Xóa sạch các record của customerName trong ShoppingCart
        cartRepository.deleteByCustomerName(customerName);

        return new CheckoutSummaryDTO(
                customerName,
                totalItems,
                totalQuantity,
                totalPaid,
                "CHECKOUT_COMPLETED_AND_CART_CLEARED"
        );
    }
}
