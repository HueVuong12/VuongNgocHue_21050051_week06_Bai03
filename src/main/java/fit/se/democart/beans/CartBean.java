package fit.se.democart.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartBean implements Serializable {
    private List<CartItemBean> items = new ArrayList<>();

    public CartBean() {
    }

    public List<CartItemBean> getItems() {
        return items;
    }

    public void setItems(List<CartItemBean> items) {
        this.items = items;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) return;
        for (CartItemBean item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItemBean(product, quantity));
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void updateQuantity(int id, int qty) {
        if (qty <= 0) {
            removeProduct(id);
            return;
        }
        for (CartItemBean item : items) {
            if (item.getProduct().getId() == id) {
                item.setQuantity(qty);
                return;
            }
        }
    }

    public void removeProduct(int id) {
        items.removeIf(item -> item.getProduct().getId() == id);
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        double total = 0.0;
        for (CartItemBean item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
}
