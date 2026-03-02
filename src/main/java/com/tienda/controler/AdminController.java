package com.tienda.controler;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tienda.model.Product;
import com.tienda.model.Sale;
import com.tienda.model.Sale.Status;
import com.tienda.model.SaleDetail;
import com.tienda.model.User;
import com.tienda.service.ProductService;
import com.tienda.service.SaleDetailService;
import com.tienda.service.SaleService;
import com.tienda.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final SaleService saleService;
    private final SaleDetailService saleDetailService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(ProductService productService, SaleService saleService,
                           SaleDetailService saleDetailService, UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.saleService = saleService;
        this.saleDetailService = saleDetailService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /* ============================================================
       PRODUCTOS
       ============================================================ */

    @GetMapping("/products")
    public String listProducts(
            Model model,
            @RequestParam(defaultValue = "0") int page
    ) {
    	
    	
        int pageSize = 15;
        Page<Product> productPage = productService.getAllProductsPaginated(page, pageSize);
        int totalPages = productPage.getTotalPages();
        int currentPage = page;

        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages - 1, currentPage + 2);        
        
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        return "admin/products/list";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("mode", "create");
        return "admin/products/form";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.findProductById(id));
        model.addAttribute("mode", "edit");
        return "admin/products/form";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable int id, @ModelAttribute Product product) {
        product.setId(id);
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/view/{id}")
    public String viewProduct(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.findProductById(id));
        model.addAttribute("mode", "view");
        return "admin/products/form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProductConfirm(@PathVariable int id, Model model) {
        model.addAttribute("product", productService.findProductById(id));
        model.addAttribute("mode", "delete");
        return "admin/products/form";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }


    /* ============================================================
       VENTAS
       ============================================================ */

    
    @GetMapping("/sales")
    public String listSales(
            Model model,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 15;

        Page<Sale> salesPage = saleService.getAllSalesPaginated(page, pageSize);

        int totalPages = salesPage.getTotalPages();
        int currentPage = page;

        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages - 1, currentPage + 2);

        model.addAttribute("salesPage", salesPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "admin/sales/list";
    }

    @GetMapping("/sales/{id}")
    public String viewSale(@PathVariable int id, Model model) {
        Sale sale = saleService.findById(id);
        List<SaleDetail> details = saleDetailService.findBySale(sale);

        model.addAttribute("sale", sale);
        model.addAttribute("details", details);

        return "admin/sales/detail";
    }

    @PostMapping("/sales/{id}/status")
    public String updateStatus(@PathVariable int id, @RequestParam("status") Status status) {
        saleService.updateStatus(id, status);
        return "redirect:/admin/sales/" + id + "?updated";
    }


    /* ============================================================
       USUARIOS
       ============================================================ */

    @GetMapping("/users")
    public String listUsers(
            Model model,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 15;

        Page<User> usersPage = userService.getAllUsersPaginated(page, pageSize);

        int totalPages = usersPage.getTotalPages();
        int currentPage = page;

        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages - 1, currentPage + 2);

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "admin/users/list";
    }

    @GetMapping("/users/reset/{id}")
    public String resetPasswordForm(@PathVariable int id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/users/reset-password";
    }

    @PostMapping("/users/reset/{id}")
    public String resetPassword(@PathVariable int id,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("user", userService.findById(id));
            return "admin/users/reset-password";
        }

        User user = userService.findById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);

        return "redirect:/admin/users?resetSuccess";
    }
    @PostMapping("/users/role/{id}")
    public String updateRole(@PathVariable int id, @RequestParam User.Rol rol) {
        userService.updateRole(id, rol);
        return "redirect:/admin/users?roleUpdated";
    }
}