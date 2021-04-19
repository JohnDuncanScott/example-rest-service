package com.idm.service.controllers;

import com.idm.service.models.data.Product;
import com.idm.service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

// TODO: Not HATEOS compliant, this controller is unused currently and was meant to be used to return products
// for adding to packages
@CrossOrigin(origins = { "http://localhost:8081" })
@RestController
@RequestMapping(value = "/products")
public class ProductController {
    @Inject
    private ProductService productService;

    @GetMapping()
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        return productService.getById(id);
    }
}
