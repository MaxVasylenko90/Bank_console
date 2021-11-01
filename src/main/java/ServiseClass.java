import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.concurrent.Callable;

public class ServiseClass {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BankJPA");
    private static EntityManager em = emf.createEntityManager();;

    public ServiseClass() {}

    protected static <T> T performTransaction(Callable<T> action) {
        EntityTransaction transaction = getEm().getTransaction();
        transaction.begin();
        try {
            T result = action.call();
            transaction.commit();

            return result;
        } catch (Exception ex) {
            if (transaction.isActive())
                transaction.rollback();
            throw new RuntimeException("Случилась ошибка, транзакция отменена!");
        }
    }

    public static EntityManager getEm() {
        return em;
    }
}
