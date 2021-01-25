package geektime.spring.springbucks.convert;

import org.bson.Document;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;



/**
 * @author phantomwl
 * @version 2021/1/24
 */
public class MongoReadCustomConverter implements Converter<Document, Money> {
    @Override
    public Money convert(Document document) {
        Document source = (Document) document.get("money");
        Document currency = (Document) source.get("currency");
        return Money.of(CurrencyUnit.of(String.valueOf(currency.get("code"))), Double.valueOf(source.get("amount").toString()));
    }
}
