package org.akriuchk.minishop;

import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Category;
import org.akriuchk.minishop.model.Product;
import org.akriuchk.minishop.repository.CategoriesRepository;
import org.akriuchk.minishop.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@SpringBootApplication(scanBasePackages = "org.akriuchk.minishop")
public class MiniShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(ProductRepository repo, CategoriesRepository catRepo) {
        AtomicLong id = new AtomicLong(0);
        Category category = new Category();
        category.setName("test_cat");
        category.setDisplayName("Display name");

        catRepo.save(category);

        return args -> {
            repo.saveAll(Arrays.asList(
                    new Product(id.incrementAndGet(), "a", true, false, true, false, category),
                    new Product(id.incrementAndGet(), "b", true, false, true, false, category),
                    new Product(id.incrementAndGet(), "c", true, false, true, false, category),
                    new Product(id.incrementAndGet(), "d", true, false, true, false, category)
            ));
        };
    }
}
