package geektime.spring.springbucks.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;


@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Coffee implements Serializable {
    @Id
    private String id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String name;
    private Money price;
}
