package org.akriuchk.minishop;

import org.akriuchk.minishop.model.BedSheet;
import org.akriuchk.minishop.repository.BedSheetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication(scanBasePackages = "org.akriuchk.minishop")
public class MiniShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniShopApplication.class, args);
    }

    @Bean
    CommandLineRunner init(BedSheetRepository userRepository) {
        return args -> {
            Stream.of("John", "Julie").forEach(name -> {
                BedSheet user = new BedSheet();
                user.setContent(name);
                userRepository.save(user);
            });
        };
    }

}
