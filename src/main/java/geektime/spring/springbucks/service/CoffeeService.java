package geektime.spring.springbucks.service;

import geektime.spring.springbucks.model.Coffee;
import geektime.spring.springbucks.model.CoffeeCache;
import geektime.spring.springbucks.model.OrderState;
import geektime.spring.springbucks.repository.CoffeeCacheRepository;
import geektime.spring.springbucks.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private CoffeeCacheRepository cacheRepository;

    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    public void dropAllMongoData(){
        coffeeRepository.deleteAll();
        log.info("Already drop all MongoDB data");
    }

    public void dropAllRedisData(){
        cacheRepository.deleteAll();
        log.info("Already drop all Redis data");
    }

    //从数据库中查找菜单
    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(), matcher));
        log.info("Coffee Found: {}", coffee);
        return coffee;
    }

    //从redis缓存中查找菜单
    public Optional<Coffee> findSimpleCoffeeFromCache(String name) {
        Optional<CoffeeCache> cached = cacheRepository.findOneByName(name);
        if (cached.isPresent()) {
            CoffeeCache coffeeCache = cached.get();
            Coffee coffee = Coffee.builder()
                    .name(coffeeCache.getName())
                    .price(coffeeCache.getPrice())
                    .build();
            log.info("Coffee {} found in cache.", coffeeCache);
            return Optional.of(coffee);
        } else {
            Optional<Coffee> raw = findOneCoffee(name);
            raw.ifPresent(c -> {
                CoffeeCache coffeeCache = CoffeeCache.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .price(c.getPrice())
                        .build();
                log.info("Save Coffee {} to cache.", coffeeCache);
                cacheRepository.save(coffeeCache);
            });
            return raw;
        }
    }


    //添加coffee品种
    public Coffee addCoffee(String name, int price) {
        if(findSimpleCoffeeFromCache(name).isPresent()){
            log.warn("Duplicated coffee: {}",name);
            return null;
        }
        CoffeeCache coffeeCache = CoffeeCache.builder()
                .name(name)
                .price(Money.of(CurrencyUnit.of("CNY"),price))
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        log.info("Save Coffee {} to cache.", coffeeCache);
        cacheRepository.save(coffeeCache);
        Coffee coffee = Coffee.builder().
                        name(name).
                        price(Money.of(CurrencyUnit.of("CNY"),price))
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build();
        Coffee saved = coffeeRepository.save(coffee);
        log.info("New Coffee: {}", saved);
        return saved;
    }

    //删除coffee品种并删除缓存
    public boolean deleteCoffee(Coffee coffee){
        if(!findOneCoffee(coffee.getName()).isPresent()){
            log.warn("Coffee: {} not exist",coffee.getName());
            return false;
        }
        String name = coffee.getName();
        Optional<CoffeeCache> cache = cacheRepository.findOneByName(name);
        if(cache.isPresent()){
            cacheRepository.delete(cache.get());
        }
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
