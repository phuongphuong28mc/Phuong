package B4.KT;

public class EWalletPayment extends PaymentMethod {

    // Tao phuong thuc thanh toan vi dien tu cho nguoi dung.
    public EWalletPayment(User user) {
        super(user);
    }

    // Kiem tra so tien va so du, sau do thanh toan tu vi dien tu.
    @Override
    public void processPayment(double amount) {
        System.out.println("\n--- THANH TOAN VI DIEN TU ---");
        System.out.println("Nguoi thanh toan: " + getUser().getName());
        System.out.println("So tien thanh toan: " + amount);

        if (!validateAmount(amount)) {
            printInvalidAmount(amount);
            return;
        }
        if (!hasEnoughBalance(amount)) {
            printInsufficientBalance(amount);
            return;
        }

        double balanceBefore = getUser().getBalance();
        deductBalance(amount);
        System.out.println("So du truoc thanh toan: " + balanceBefore);
        System.out.println("So du sau thanh toan: " + getUser().getBalance());
        System.out.println("Ket qua: Thanh toan thanh cong!");
    }

    // Hoan tien vao vi dien tu neu so tien hoan hop le.
    public void refund(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("So tien hoan phai lon hon 0!");
            return;
        }

        double balanceBefore = getUser().getBalance();
        getUser().setBalance(balanceBefore + amount);
        System.out.println("\n--- HOAN TIEN VI DIEN TU ---");
        System.out.println("Nguoi duoc hoan tien: " + getUser().getName());
        System.out.println("So tien hoan: " + amount);
        System.out.println("So du truoc hoan tien: " + balanceBefore);
        System.out.println("So du sau hoan tien: " + getUser().getBalance());
        System.out.println("Ket qua: Hoan tien thanh cong!");
    }

    // In thong bao khi so tien thanh toan khong hop le.
    private void printInvalidAmount(double amount) {
        System.out.println("Ket qua: Thanh toan that bai!");
        System.out.println("So tien khong hop le: " + amount);
        System.out.println("Ly do: So tien phai lon hon 0!");
    }

    // In thong bao khi so du tai khoan khong du de thanh toan.
    private void printInsufficientBalance(double amount) {
        System.out.println("Ket qua: Thanh toan that bai!");
        System.out.println("So tien can thanh toan: " + amount);
        System.out.println("So du hien tai: " + getUser().getBalance());
        System.out.println("Ly do: So du khong du!");
    }
}
