package com.tienda.controler;

import com.tienda.dto.EditProfileDTO;
import com.tienda.dto.RegisterDTO;
import com.tienda.model.Cart;
import com.tienda.model.Product;
import com.tienda.model.Sale;
import com.tienda.model.SaleDetail;
import com.tienda.model.User;
import com.tienda.model.User.Rol;
import com.tienda.repository.UserRepository;
import com.tienda.service.ProductService;
import com.tienda.service.SaleDetailService;
import com.tienda.service.SaleService;
import com.tienda.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ShopControler {

    private final UserRepository userRepository;
    private final ProductService productService;
    private final UserService userService;
    private final SaleService saleService;
    private final SaleDetailService saleDetailService;
    private final PasswordEncoder passwordEncoder;

    public ShopControler(ProductService productService, UserService userService,
                         SaleService saleService, SaleDetailService saleDetailService,
                         PasswordEncoder passwordEncoder, UserRepository userRepository) {

        this.productService = productService;
        this.userService = userService;
        this.saleService = saleService;
        this.saleDetailService = saleDetailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /* ============================================================
       HOME / CATÁLOGO
       ============================================================ */

    @GetMapping("/")
    public String index() {
        return "redirect:/catalog";
    }

    @GetMapping("/catalog")
    public String productList(Model model) {
        List<Product> listProducts = new ArrayList<>(productService.getAllProducts());
        model.addAttribute("products", listProducts);
        return "index";
    }



    /* ============================================================
       CARRITO
       ============================================================ */

    @GetMapping("/cart/add/{id}")
    public String addToCart(@PathVariable int id,
                            @RequestParam int quantity,
                            HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        Product product = productService.findProductById(id);
        int stock = product.getStock();

        int currentQty = cart.getProducts().getOrDefault(id, 0);
        int finalQty = Math.min(currentQty + quantity, stock);

        cart.addProduct(id, finalQty);

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        Map<Product, Integer> detailedItems = new HashMap<>();

        for (Map.Entry<Integer, Integer> entry : cart.getProducts().entrySet()) {
            Product p = productService.findProductById(entry.getKey());
            detailedItems.put(p, entry.getValue());
        }

        double total = detailedItems.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();

        model.addAttribute("items", detailedItems);
        model.addAttribute("total", total);

        return "cart/view";
    }
    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            return 0;
        }

        return cart.getProducts().values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam int productId,
                             @RequestParam int quantity,
                             HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) return "redirect:/cart";

        Product product = productService.findProductById(productId);
        int stock = product.getStock();

        quantity = Math.max(1, Math.min(quantity, stock));

        cart.addProduct(productId, quantity);

        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable int id, HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) cart.deleteProduct(id);

        return "redirect:/cart";
    }


    /* ============================================================
       CHECKOUT
       ============================================================ */

    @PostMapping("/checkout")
    public String checkout(HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getProducts().isEmpty()) {
            return "redirect:/cart?empty";
        }

        saleService.createSale(cart.getProducts());
        session.removeAttribute("cart");

        return "redirect:/checkout/success";
    }
    /* ============================================================
    HISTORIAL DE COMPRAS DEL USUARIO
    ============================================================ */

 @GetMapping("/purchases")
 public String viewPurchases(Model model) {

     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     String username = auth.getName();
     User user = userService.findByName(username);

     List<Sale> purchases = saleService.findByUser(user);

     model.addAttribute("purchases", purchases);

     return "profile/purchases";
 }

 @GetMapping("/purchases/{id}")
 public String viewPurchaseDetail(@PathVariable int id, Model model) {

     Sale sale = saleService.findById(id);

     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     String username = auth.getName();

     if (!sale.getUser().getName().equals(username)) {
         return "redirect:/purchases";
     }

     List<SaleDetail> details = saleDetailService.findBySale(sale);

     model.addAttribute("sale", sale);
     model.addAttribute("details", details);

     return "profile/purchase-detail";
 }


 /* ============================================================
    PERFIL DEL USUARIO
    ============================================================ */
 @GetMapping("/profile")
 public String profile(Model model) {
     User user = userService.getCurrentUser();
     model.addAttribute("user", user);

     if (user.getRol() == User.Rol.ADMIN) {
         return "profile/admin-profile";
     }

     return "profile/index";
 }

 @GetMapping("/profile/edit")
 public String editProfileForm(Model model) {
     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     String username = auth.getName();
     User user = userService.findByName(username);

     EditProfileDTO dto = new EditProfileDTO();
     dto.setName(user.getName());
     dto.setEmail(user.getEmail());

     model.addAttribute("editDTO", dto);

     return "profile/profileForm";
 }

 @PostMapping("/profile/edit")
 public String updateProfile(@ModelAttribute("editDTO") EditProfileDTO dto,
                             Model model) {

     Authentication auth = SecurityContextHolder.getContext().getAuthentication();
     String username = auth.getName();
     User user = userService.findByName(username);

     // Email duplicado
     User existing = userService.findByEmail(dto.getEmail());
     if (existing != null && existing.getId() != user.getId()) {
         model.addAttribute("error", "El email ya está en uso");
         return "profile/edit-profile";
     }

     // Cambio de contraseña
     if (!dto.getNewPassword().isEmpty()) {

         if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
             model.addAttribute("error", "La contraseña actual no es correcta");
             return "profile/edit-profile";
         }

         if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
             model.addAttribute("error", "Las nuevas contraseñas no coinciden");
             return "profile/edit-profile";
         }

         user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
     }

     user.setName(dto.getName());
     user.setEmail(dto.getEmail());

     userService.save(user);

     return "redirect:/profile";
 }
 
	 @GetMapping("/profile/purchases")
	 public String purchases(Model model) {
     User user = userService.getCurrentUser();
     model.addAttribute("purchases", saleService.findByUser(user));
     return "profile/purchases";
 }

	 
	 @GetMapping("/profile/purchases/{id}")
	 public String purchaseDetail(@PathVariable int id, Model model, Principal principal) {

	     // Obtener usuario logueado
	     User user = userService.findByName(principal.getName());

	     // Buscar venta
	     Sale sale = saleService.findById(id);

	     // Validar que existe y pertenece al usuario
	     if (sale == null || sale.getUser().getId() != user.getId()) {
	         return "redirect:/profile/purchases";
	     }

	     model.addAttribute("sale", sale);
	     model.addAttribute("details", sale.getDetails());

	     return "profile/purchase-detail";
	 }
}
    