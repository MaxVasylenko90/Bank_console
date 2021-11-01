import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ExchangeRates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double sell;
    private Double buy;

    public ExchangeRates() {}

    public static ExchangeRates createExchangeRates(String currency) {
        if (currency.equals("USD")) {
            return ServiseClass.performTransaction(() -> {
                ExchangeRates er = new ExchangeRates();
                er.setSell(26.5);
                er.setBuy(26.1);
                return er;
            });
        }
        else if (currency.equals("EUR")) {
            return ServiseClass.performTransaction(() -> {
                ExchangeRates er = new ExchangeRates();
                er.setSell(30.7);
                er.setBuy(30.1);
                return er;
            });
        }
        else throw new RuntimeException("Такой курс валют не существует.");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }
}
