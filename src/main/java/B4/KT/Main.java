package B4.KT;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // Tao du lieu mau, chay cac truong hop thanh toan va in tong ket so du.
    public static void main(String[] args) {

        // 1. Tao cac User
        User user1 = new User(
                "Nguyen",
                "nguyen@gmail.com",
                1000000
        );

        User user2 = new User(
                "Trung",
                "trung@gmail.com",
                2000000
        );

        User user3 = new User(
                "Linh",
                "linh@gmail.com",
                1500000
        );


        // 2. Tao cac doi tuong thanh toan
        CashPayment cash = new CashPayment(user1);

        CreditCardPayment card = new CreditCardPayment(user2);

        EWalletPayment wallet = new EWalletPayment(user3);


        // 3. Polymorphism - Da hinh
        System.out.println("===== DANH SACH PHUONG THUC THANH TOAN =====");

        List<PaymentMethod> payments = new ArrayList<>();

        payments.add(cash);
        payments.add(card);
        payments.add(wallet);


        // 4. Thuc hien thanh toan
        System.out.println("\n===== THANH TOAN =====");

        for (int i = 0; i < payments.size(); i++) {
            PaymentMethod payment = payments.get(i);
            payment.processPayment(200000);
        }


        // 5. Hoan tien
        System.out.println("\n===== HOAN TIEN =====");

        card.refund(100000);

        wallet.refund(50000);


        // 6. Kiem tra truong hop khong du so du
        System.out.println("\n===== KIEM TRA KHONG DU SO DU =====");

        User user4 = new User(
                "Pham Van D",
                "d@gmail.com",
                50000
        );

        CashPayment cashTest = new CashPayment(user4);

        cashTest.processPayment(200000);


        // 7. Kiem tra so tien thanh toan khong hop le
        System.out.println("\n===== KIEM TRA SO TIEN KHONG HOP LE =====");

        cash.processPayment(0);


        // 8. Hien thi so du cuoi cung
        System.out.println("\n===== TONG KET SO DU TAI KHOAN =====");

        printBalance(user1);
        printBalance(user2);
        printBalance(user3);
        printBalance(user4);
    }

    // In so du cuoi cung cua mot nguoi dung.
    private static void printBalance(User user) {
        System.out.println("\n------------------------------");
        System.out.println("User: " + user.getName());
        System.out.println("So du cuoi cung: " + user.getBalance());
        System.out.println("------------------------------");
    }
}
