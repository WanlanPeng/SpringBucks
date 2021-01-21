package geektime.spring.springbucks;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.model.CoffeeOrder;
import geektime.spring.springbucks.model.OrderState;
import geektime.spring.springbucks.repository.CoffeeRepository;
import geektime.spring.springbucks.service.CoffeeOrderService;
import geektime.spring.springbucks.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@Slf4j
@EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories
public class SpringBucksApplication implements ApplicationRunner {
	@Autowired
	private CoffeeRepository coffeeRepository;
	@Autowired
	private CoffeeService coffeeService;
	@Autowired
	private CoffeeOrderService orderService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBucksApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Coffee coffee = coffeeService.addCoffee("starbucks",25);
		log.info("All Coffee: {}", coffeeRepository.findAll());
		if(coffeeService.updatePrice(coffee,15)){
			log.info("Coffee {} has been updated: {}",coffee.getName(),coffee);
		}
		if(coffeeService.deleteCoffee(coffee)){
			log.info("After delete: {}", coffeeRepository.findAll());
		}



		//optional类(java8)是一个可以为null的容器对象，isPresent()判断是否为Null
		Optional<Coffee> latte = coffeeService.findOneCoffee("Latte");
		if (latte.isPresent()) {
			CoffeeOrder order = orderService.createOrder("Li Lei", latte.get());
			log.info("Update INIT to PAID: {}", orderService.updateState(order, OrderState.PAID));
			//log.info("Update PAID to INIT: {}", orderService.updateState(order, OrderState.INIT));
		}
		Iterable<CoffeeOrder> orders = orderService.findByCustomer("Li Lei");
	}
}

