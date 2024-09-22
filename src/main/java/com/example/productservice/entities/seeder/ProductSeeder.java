package com.example.productservice.entities.seeder;

import com.example.productservice.entities.*;
import com.example.productservice.repositories.*;
import com.example.productservice.statics.enums.ProductSimpleStatus;
import com.example.productservice.util.StringHelper;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class ProductSeeder implements CommandLineRunner {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductImageRepository productImageRepository;
    Faker faker = new Faker();

    public ProductSeeder(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //check if any value in db, if not, seed data
        if (productRepository.count() == 0) {
            createProducts();
        }
    }

    private void createProducts() {
        List<ProductImage> productImages = new ArrayList<>();

        List<Category> categories = new ArrayList<>();
        if (categoryRepository.count() == 0) {
            List<String> categoriesName = Arrays.asList("Furniture", "Decorations", "Material");
            List<String> categoryChildNames0 = Arrays.asList("Apartment Interior", "Office Interior", "Townhouse Interior", "Villa Interior", "Restaurant Interior", "Showroom Interior");
            List<String> categoryChildNames1 = Arrays.asList("Wall Decor", "Table Decor", "Floor Decor", "Ceiling Decor", "Lighting Decor", "Furniture Decor");
            List<String> categoryChildNames2 = Arrays.asList("Wood", "Metal", "Plastic", "Glass", "Fabric", "Leather");

            for (int i = 0; i < categoriesName.toArray().length; i++) {
                Category categoryParent = new Category();
                categoryParent.setCategoryName(categoriesName.get(i));
                categoryParent.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                categories.add(categoryParent);
                if (i == 0) {
                    for (String categoryChildName : categoryChildNames0) {
                        Category categoryChild = new Category();
                        categoryChild.setCategoryName(categoryChildName);
                        categoryChild.setParentCategory(categoryParent);
                        categoryChild.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                        categories.add(categoryChild);
                    }
                } else if (i == 1) {
                    for (String categoryChildName : categoryChildNames1) {
                        Category categoryChild = new Category();
                        categoryChild.setCategoryName(categoryChildName);
                        categoryChild.setParentCategory(categoryParent);
                        categoryChild.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                        categories.add(categoryChild);
                    }
                }else {
                    for (String categoryChildName : categoryChildNames2) {
                        Category categoryChild = new Category();
                        categoryChild.setCategoryName(categoryChildName);
                        categoryChild.setParentCategory(categoryParent);
                        categoryChild.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
                        categories.add(categoryChild);
                    }
                }
            }

            categoryRepository.saveAll(categories);
        } else {
            categories = categoryRepository.findAll();
        }

        boolean nameExisting = false;
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String productName = faker.food().dish();

            for (Product product :
                    products) {
                if (product.getName().equals(productName)) {
                    nameExisting = true;
                    break;
                }
            }
            if (nameExisting) {
                i--;
                nameExisting = false;
                continue;
            }
//            String slug = StringHelper.toSlug(productName);
            String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
            double price = (faker.number().randomNumber(5, true));
            ProductSimpleStatus status = ProductSimpleStatus.ACTIVE;
            Product product = new Product();
            product.setName(productName);
            product.setCategory(categories.get(faker.number().numberBetween(0, 6)));
            product.setStockQuantity(faker.number().numberBetween(1, 100));
            product.setSoldQuantity(0L);
//            product.setSlug(slug);
            product.setDescription(description);
//            product.setThumbnails("demo-img.jpg");
            product.setPrice(BigDecimal.valueOf(price));
//            product.setStatus(status);
            product.setManufacturer(faker.company().name());
            product.setSize(faker.food().measurement());
            product.setWeight(faker.food().measurement());

            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());

            products.add(product);

            for (int j = 0; j < 2; j++) {
                ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setImageUrl("img-url-" + j + ".jpg");
                productImages.add(productImage);
            }
        }
        productRepository.saveAll(products);
        productImageRepository.saveAll(productImages);
    }

}
