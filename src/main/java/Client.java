import javax.persistence.*;
import java.util.*;

@Entity
@Table (name = "Clients")
public class Client {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String name;
    private String surname;
    private Integer inn;

    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL)
    List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    List<Transaction> transactions = new ArrayList<>();

    public static Client createClient (String name, String surname, int inn) {
        return ServiseClass.performTransaction(() -> {
            Client client = new Client(name, surname, inn);
            ServiseClass.getEm().persist(client);
            return client;
        });
    }

    public void addAccount(Account account) {
        if(!accounts.contains(account)) {
            accounts.add(account);
            account.setClient(this);
        }
    }

    public void addTransaction(Transaction transaction) {
        if(!transactions.contains(transaction)) {
            transactions.add(transaction);
            transaction.setClient(this);
        }
    }

    public Account getAccount(int index) {
        return accounts.get(index);
    }

    public Client () {}

    public Client(String name, String surname, Integer inn) {
        this.name = name;
        this.surname = surname;
        this.inn = inn;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getInn() {
        return inn;
    }

    public void setInn(Integer inn) {
        this.inn = inn;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", inn=" + inn +
                ", accounts=" + accounts +
                ", transactions=" + transactions +
                '}';
    }
}

