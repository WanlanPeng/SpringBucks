package geektime.spring.springbucks.service;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.model.CoffeeOrder;
import geektime.spring.springbucks.model.OrderState;
import geektime.spring.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    //查找菜单
    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Coffee Found: {}", coffee);
        return coffee;
    }

    //添加coffee品种
    public Coffee addCoffee(String name, int price) {
        if(findOneCoffee(name).isPresent()){
            log.warn("Duplicated coffee: {}",name);
            return null;
        }
        Coffee coffee = Coffee.builder().
                        name(name).
                        price(Money.of(CurrencyUnit.of("CNY"),price)).
                        build();
        Coffee saved = coffeeRepository.save(coffee);
        log.info("New Coffee: {}", saved);
        return saved;
    }

    //删除coffee品种
    public boolean deleteCoffee(Coffee coffee){
        if(!findOneCoffee(coffee.getName()).isPresent() || coffee == null){
            log.warn("Coffee: {} not exist",coffee.getName());
            return false;
        }
        String name = coffee.getName();
        coffeeRepository.delete(coffee);
        log.info("Coffee: {} has been deleted", name);
        return true;
    }

    //修改coffee价格
    public boolean updatePrice(Coffee coffee, int price){
        if(!findOneCoffee(coffee.getName()).isPresent()){
            log.warn("Coffee: {} not exist", coffee.getName());
            return false;
        }
        coffee.setPrice(Money.of(CurrencyUnit.of("CNY"),price));
        coffeeRepository.save(coffee);
        log.info("Updated Coffee: {}", coffee);
        return true;
    }
}
