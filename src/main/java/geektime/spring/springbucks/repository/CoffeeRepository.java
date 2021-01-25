package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.Coffee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface CoffeeRepository extends MongoRepository<Coffee, Long> {

    List<Coffee> findByName(String name);
}
