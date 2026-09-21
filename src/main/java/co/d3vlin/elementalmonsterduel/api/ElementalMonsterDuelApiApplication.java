package co.d3vlin.elementalmonsterduel.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "co.d3vlin.elementalmonsterduel.entity")
@ComponentScan(basePackages = {
        "co.d3vlin.elementalmonsterduel.api",
        "co.d3vlin.elementalmonsterduel.mapper",
})
public class ElementalMonsterDuelApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElementalMonsterDuelApiApplication.class, args);
    }

}
