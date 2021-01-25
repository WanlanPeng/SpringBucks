package geektime.spring.springbucks.repository;

import geektime.spring.springbucks.model.CoffeeOrder;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CoffeeOrderRepository extends MongoRepository<CoffeeOrder, Long> {
}
