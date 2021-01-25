package geektime.spring.springbucks.convert;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.nio.charset.StandardCharsets;

/**
 * @author phantomwl
 * @version 2021/1/24
 */

@ReadingConverter
public class RedisReadCustomConverter implements Converter<byte[], Money> {

    @Override
    public Money convert(byte[] bytes) {
        String value = new String(bytes, StandardCharsets.UTF_8);
        return Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(value));
    }
}
