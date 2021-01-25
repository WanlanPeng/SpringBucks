package geektime.spring.springbucks.model;

import lombok.*;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;


@RedisHash(value = "springbucks-coffee", timeToLive = 6000)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoffeeCache {
    @Id
    private String id;
    @Indexed
    private String name;
    private Money price;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
