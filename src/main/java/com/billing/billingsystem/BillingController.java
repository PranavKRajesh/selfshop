package com.billing.billingsystem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
public class BillingController {

    private final ProductRepository productRepo;

    public BillingController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "index";
    }

    @GetMapping("/products")
    public String productsPage(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productRepo.save(product);
        return "redirect:/products";
    }

    @PostMapping("/products/update")
    public String updateProduct(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam double price) {
        Product p = productRepo.findById(id).orElseThrow();
        p.setName(name);
        p.setPrice(price);
        productRepo.save(p);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam int qty,
                            HttpSession session) {
        Product p = productRepo.findById(productId).orElseThrow();
        List<CartItem> cart = getCart(session);
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProductName().equals(p.getName())) {
                item.setQty(item.getQty() + qty);
                found = true;
                break;
            }
        }
        if (!found) cart.add(new CartItem(p.getName(), p.getPrice(), qty));
        session.setAttribute("cart", cart);
        return "redirect:/";
    }

    // Delete a single item from cart by index
    @GetMapping("/cart/remove/{index}")
    public String removeFromCart(@PathVariable int index, HttpSession session) {
        List<CartItem> cart = getCart(session);
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        session.setAttribute("cart", cart);
        return "redirect:/";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);
        double subtotal = cart.stream().mapToDouble(CartItem::getTotal).sum();
        double tax = Math.round(subtotal * 0.18 * 100.0) / 100.0;
        double total = Math.round((subtotal + tax) * 100.0) / 100.0;
        model.addAttribute("cart", cart);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        model.addAttribute("invoiceNumber", "INV-" + System.currentTimeMillis());
        return "invoice";
    }

    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/";
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        return cart;
    }
}