import javax.persistence.*;

public class BL {
    public static void main(String [] args) {
       Client client1 = Client.createClient("Ruslan", "Vasylenko", 11111);
       Client client2 = Client.createClient("Max", "Vasylenko", 22222);
       Account  cl1USD = Account.createAccount("USD");
       client1.addAccount(cl1USD);
       Account  cl1UAH = Account.createAccount("UAH");
       client1.addAccount(cl1UAH);
       Account  cl1EUR = Account.createAccount("EUR");
       client1.addAccount(cl1EUR);
       Account  cl2USD = Account.createAccount("USD");
       client2.addAccount(cl2USD);
       Account  cl2UAH = Account.createAccount("UAH");
       client2.addAccount(cl2UAH);
       Account.addMoney(client1, cl1UAH, 5000);
       Account.takeMoneyFromAccount(client1, cl1UAH, 500 );
       Account.addMoney(client1, cl1UAH, 2000);
       Account.sendToAnotherAccount(client1, cl1UAH, cl2UAH, 1000);
       Account.sendToAnotherAccount(client1, cl1UAH, cl2USD, 1000);
       Account.sendToAnotherAccount(client2, cl2USD, cl1EUR, 35);


       ServiseClass.getEm().clear();
       long id = client1.getId();
       TypedQuery <Long> query = ServiseClass.getEm().createQuery("SELECT COUNT (a) FROM Transaction a WHERE a.client = :id" , Long.class);
       query.setParameter("id", id);
       Long res = query.getSingleResult();
       System.out.println(res);
       System.out.println("Сумма всех денег в грн по клиенту = " + Account.summFromAllClientCountsInUAH(client2));
    }
}
