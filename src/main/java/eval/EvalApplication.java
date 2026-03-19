package eval;

import org.h2.tools.Server;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.*;
import jakarta.annotation.Nonnull;
import org.springframework.lang.NonNull;

import java.sql.SQLException;
@SpringBootApplication
public class EvalApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvalApplication.class, args);
    }

}