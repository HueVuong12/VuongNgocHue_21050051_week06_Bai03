package vn.fit.vuongngochue_bai3_tuan06;

import org.junit.jupiter.api.*;
import vn.fit.vuongngochue_bai3_tuan06.dto.*;
import vn.fit.vuongngochue_bai3_tuan06.model.Product;
import vn.fit.vuongngochue_bai3_tuan06.model.ShoppingCart;
import vn.fit.vuongngochue_bai3_tuan06.repository.impl.ProductRepositoryImpl;
import vn.fit.vuongngochue_bai3_tuan06.repository.impl.ShoppingCartRepositoryImpl;
import vn.fit.vuongngochue_bai3_tuan06.service.ProductService;
import vn.fit.vuongngochue_bai3_tuan06.service.ShoppingCartService;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusinessLogicTest {

    private static ProductRepositoryImpl productRepo;
    private static ShoppingCartRepositoryImpl cartRepo;
    private static ProductService productService;
    private static ShoppingCartService cartService;

    @BeforeAll
    public static void setup() throws Exception {
        productRepo = new ProductRepositoryImpl();
        cartRepo = new ShoppingCartRepositoryImpl();

        productService = new ProductService();
        setField(productService, "productRepository", productRepo);
        setField(productService, "cartRepository", cartRepo);

        cartService = new ShoppingCartService();
        setField(cartService, "cartRepository", cartRepo);
        setField(cartService, "productRepository", productRepo);
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @Order(1)
    public void testDatabaseSeeded() {
        List<Product> products = productRepo.findAll();
        assertNotNull(products);
        assertTrue(products.size() >= 10, "Bảng Product phải có ít nhất 10 sản phẩm mẫu");

        List<ShoppingCart> carts = cartRepo.findAll();
        assertNotNull(carts);
        assertTrue(carts.size() >= 10, "Bảng ShoppingCart phải có các lượt mua mẫu");
    }

    @Test
    @Order(2)
    public void testRequirement1_CustomerBillBreakdown() {
        // Test tính hóa đơn cho Nguyen Van A
        CustomerBillDTO billA = cartService.calculateCustomerBill("Nguyen Van A");
        assertNotNull(billA);
        assertEquals("Nguyen Van A", billA.getCustomerName());
        assertFalse(billA.getItems().isEmpty(), "Phải có danh sách món hàng trong giỏ");
        assertTrue(billA.getSubTotal() > 0, "Subtotal phải lớn hơn 0");
        System.out.println("YC1 - Nguyen Van A: SubTotal=" + billA.getSubTotal() + ", Discount=" + billA.getDiscount() + ", FinalTotal=" + billA.getFinalTotal());
    }

    @Test
    @Order(3)
    public void testRequirement2_AddItemWithValueCap() {
        // 1. Thêm hợp lệ: Product 3 (MacBook Air $1199) * 2 = $2398 <= $5000
        ShoppingCart validCart = new ShoppingCart(3, "Sinh Vien Test", 2);
        AddCartResponseDTO res = cartService.addItemToCart(validCart);
        assertNotNull(res);
        assertEquals(2, res.getQuantity());
        assertEquals(2398.0, res.getEstimatedTotal(), 0.01);
        System.out.println("YC2 - Hợp lệ: " + res.getProductName() + " x" + res.getQuantity() + " = $" + res.getEstimatedTotal());

        // 2. Thêm vượt hạn mức: Product 3 * 10 = $11,990 > $5000 -> phải ném IllegalStateException
        ShoppingCart invalidCart = new ShoppingCart(3, "Sinh Vien Test", 10);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            cartService.addItemToCart(invalidCart);
        });
        assertTrue(ex.getMessage().contains("5,000"), "Ngoại lệ phải thông báo vượt hạn mức $5,000");
        System.out.println("YC2 - Bị chặn thành công: " + ex.getMessage());
    }

    @Test
    @Order(4)
    public void testRequirement3_DynamicRepricing() {
        List<RepriceReportDTO> report = productService.applyDynamicRepricing();
        assertNotNull(report);
        System.out.println("YC3 - Báo cáo cập nhật giá động (" + report.size() + " sản phẩm thay đổi):");
        for (RepriceReportDTO r : report) {
            System.out.println("  " + r.getProductName() + ": Lượt đặt=" + r.getTotalQuantityOrdered() +
                    ", Giá cũ=" + r.getOldPrice() + " -> Giá mới=" + r.getNewPrice() + " (" + r.getAdjustment() + ")");
        }
    }

    @Test
    @Order(5)
    public void testRequirement4_CheckoutAndClearCart() {
        // Khách hàng Le Van C checkout
        CheckoutSummaryDTO summary = cartService.checkout("Le Van C");
        assertNotNull(summary);
        assertEquals("Le Van C", summary.getCustomerName());
        assertEquals("CHECKOUT_COMPLETED_AND_CART_CLEARED", summary.getStatus());
        System.out.println("YC4 - Checkout Le Van C: Items=" + summary.getTotalItems() + ", Qty=" + summary.getTotalQuantity() + ", Paid=$" + summary.getTotalPaid());

        // Kiểm tra xác nhận bảng ShoppingCart không còn bản ghi của Le Van C
        List<ShoppingCart> remaining = cartRepo.findByCustomerName("Le Van C");
        assertTrue(remaining.isEmpty(), "Sau khi checkout giỏ hàng của Le Van C phải trống");
    }

    @Test
    @Order(6)
    public void testRequirement5_RevenueShareAnalytics() {
        List<RevenueShareDTO> stats = productService.getRevenueShareReport();
        assertNotNull(stats);
        assertFalse(stats.isEmpty(), "Báo cáo doanh thu không được rỗng");
        System.out.println("YC5 - Thống kê tỷ lệ đóng góp doanh thu:");
        for (RevenueShareDTO s : stats) {
            System.out.println("  " + s.getProductName() + ": " + s.getTotalUnitsInCart() + " món, Doanh thu=$" + s.getRevenueContribution() + " (" + s.getPercentage() + ")");
        }
        // Kiểm tra đã sắp xếp giảm dần theo doanh thu
        for (int i = 0; i < stats.size() - 1; i++) {
            assertTrue(stats.get(i).getRevenueContribution() >= stats.get(i + 1).getRevenueContribution(),
                    "Danh sách phải được sắp xếp giảm dần theo doanh thu");
        }
    }
}
