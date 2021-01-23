package org.akriuchk.minishop;

import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.model.Image;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.CategoriesRepository;
import org.akriuchk.minishop.repository.ImageRepository;
import org.akriuchk.minishop.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.akriuchk.minishop")
public class MiniShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(ProductRepository repo,
                                      CategoriesRepository catRepo,
                                      ImageRepository imageRepo) {
        return args -> {
            Category category = new Category();
            category.setName("test_cat");
            category.setDisplayName("Display name");
            catRepo.save(category);

            AtomicLong id = new AtomicLong(0);
            Product product = new Product(id.incrementAndGet(), "a", true, false, true, false, null, category);
            repo.saveAll(Arrays.asList(
                    product,
                    new Product(id.incrementAndGet(), "b", true, false, true, false, null, category),
                    new Product(id.incrementAndGet(), "c", true, false, true, false, null, category),
                    new Product(id.incrementAndGet(), "d", true, false, true, false, null, category)
            ));

            Path imgPath = Paths.get("initFiles/анамур.jpg");
            Image img = new Image();
            img.setProduct(product);
            img.setFilename(imgPath.getFileName().toString());
            try {
                img.setContent(Files.readAllBytes(imgPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            img.setAssigned(true);
            imageRepo.save(img);
        };
    }
}
