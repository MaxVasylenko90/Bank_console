import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;
    private Double summ;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transaction = new ArrayList<>();

    public Account() {}

    public Account(String currency) {
        this.currency = currency;
        this.summ = 0.0;
    }

    public static Account createAccount (String currency) {
        return ServiseClass.performTransaction(() -> {
            Account account = new Account(currency);
            ServiseClass.getEm().persist(account);
            return account;
        });
    }

    public static void addMoney(Client client, Account account, int summ) {
        Transaction transaction = Transaction.createTransaction(client, account, summ, account.getCurrency());
        ServiseClass.performTransaction(()-> {
                    account.setSumm(account.getSumm() + summ) ;
                    return null;
                }
        );
    }

    public static void takeMoneyFromAccount(Client client, Account account, int summ) {
        Transaction transaction = Transaction.createTransaction(client, account, summ, account.getCurrency());
        ServiseClass.performTransaction(() -> {
            account.setSumm(account.getSumm() - summ);
            return null;
        });
    }

    public static void sendToAnotherAccount(Client client, Account from, Account to, int summ) {
        if(from.getCurrency().equals(to.getCurrency())) {
            Transaction transaction = Transaction.createTransaction(client, from, summ, from.getCurrency());
            Transaction transaction2 = Transaction.createTransaction(to.getClient(), to, summ, to.getCurrency());
            ServiseClass.performTransaction(() -> {
                        from.setSumm(from.getSumm() - summ);
                        to.setSumm(to.getSumm() + summ);
                        return null;
                    }
            );
        } else {
            String currencyFrom = from.getCurrency();
            String currencyTo = to.getCurrency();
            if(currencyFrom.equals("UAH")) {
                ExchangeRates er = ExchangeRates.createExchangeRates(currencyTo);
                double tmp = summ / er.getSell();
                Transaction transaction = Transaction.createTransaction(client, from, summ, from.getCurrency());
                Transaction transaction2 = Transaction.createTransaction(to.getClient(), to, summ, to.getCurrency());
                ServiseClass.performTransaction(() -> {
                    from.setSumm(from.getSumm() - summ);
                    to.setSumm(to.getSumm() + tmp);
                    return null;
                });
            } else if (currencyFrom.equals("USD") || currencyFrom.equals("EUR")) {
                ExchangeRates erFrom = ExchangeRates.createExchangeRates(currencyFrom);
                ExchangeRates erTo = ExchangeRates.createExchangeRates(currencyTo);
                double tmp = summ * erFrom.getBuy();
                double tmp2 = tmp / erTo.getSell();
                Transaction transaction = Transaction.createTransaction(client, from, summ, from.getCurrency());
                Transaction transaction2 = Transaction.createTransaction(to.getClient(), to, summ, to.getCurrency());
                ServiseClass.performTransaction(() -> {
                    from.setSumm(from.getSumm() - summ);
                    to.setSumm(to.getSumm() + tmp2);
                    return null;
                });
            }
            else throw new RuntimeException("Неверно введена валюта");

        }
    }

    public static double summFromAllClientCountsInUAH (Client client) {
        double res = 0;
        for(Account acc : client.getAccounts()) {
            if(acc.getCurrency().equals("UAH"))
                res += acc.getSumm();
            else {
                ExchangeRates er = ExchangeRates.createExchangeRates(acc.getCurrency());
                double tmp = acc.getSumm() * er.getBuy();
                res += tmp;
            }
        }
        return res;
    }


    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(Double summ) {
        this.summ = summ;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


}
