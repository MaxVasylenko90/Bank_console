import javax.persistence.*;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    private Double summ;
    private String currency;

    public Transaction(){}

    public Transaction (Client client, Account account, double summ, String currency) {
        this.client = client;
        this.account = account;
        this.summ = summ;
        this.currency = currency;
    }

    public static Transaction createTransaction (Client client, Account account, double summ, String currency) {
        return ServiseClass.performTransaction(() -> {
            Transaction transaction = new Transaction(client, account, summ, currency);
            ServiseClass.getEm().persist(transaction);
            return transaction;
        });
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Double getSumm() {
        return summ;
    }

    public void setSumm(double summ) {
        this.summ = summ;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
