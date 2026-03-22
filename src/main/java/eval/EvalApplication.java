package eval;

import org.h2.tools.Server;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.*;
import jakarta.annotation.Nonnull;
import org.springframework.lang.NonNull;

import java.sql.SQLException;

import javax.sql.DataSource;
@SpringBootApplication
public class EvalApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvalApplication.class, args);
    }

    @GetMapping("/db-check")
    public String dbCheck(@Autowired DataSource ds) throws Exception {
        return ds.getConnection().getMetaData().getURL();
    }

}