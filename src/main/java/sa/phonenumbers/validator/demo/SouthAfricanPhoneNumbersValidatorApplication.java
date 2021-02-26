package sa.phonenumbers.validator.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import sa.phonenumbers.validator.demo.storage.StorageProperties;
import sa.phonenumbers.validator.demo.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SouthAfricanPhoneNumbersValidatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SouthAfricanPhoneNumbersValidatorApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
