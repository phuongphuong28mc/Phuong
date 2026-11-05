package Tinh_Dong_Goi;

import java.math.BigDecimal;

public class MainBank {
    public static void main(String[] args) {
        BankAccount account1 = new BankAccount("100001", "Nguyen Van A",
                new BigDecimal("10000000"), "123456", new BigDecimal("5000000"));
        BankAccount account2 = new BankAccount("100002", "Tran Thi B",
                new BigDecimal("5000000"), "654321", new BigDecimal("3000000"));

        System.out.println("--- THÔNG TIN BAN ĐẦU ---");
        account1.showAccountInfo();
        account2.showAccountInfo();

        System.out.println("\n--- NẠP TIỀN ---");
        if (account1.deposit(new BigDecimal("2000000"))) {
            System.out.println("Nạp thành công cho tài khoản " + account1.getAccountNumber()
                    + ". Số dư còn lại: " + account1.getBalance());
        } else {
            System.out.println("Nạp tiền thất bại: tài khoản bị khóa hoặc số tiền không hợp lệ.");
        }

        System.out.println("\n--- RÚT TIỀN ---");
        if (account1.withdraw(new BigDecimal("1000000"), "123456")) {
            System.out.println("Rút tiền thành công. Số dư còn lại: " + account1.getBalance());
        } else {
            System.out.println("Rút tiền thất bại: kiểm tra PIN, số dư, trạng thái tài khoản và hạn mức.");
        }

        System.out.println("\n--- CHUYỂN KHOẢN ---");
        if (account1.transfer(account2, new BigDecimal("2000000"), "123456")) {
            System.out.println("Chuyển khoản thành công đến tài khoản " + account2.getAccountNumber()
                    + ". Số dư người gửi: " + account1.getBalance()
                    + ". Số dư người nhận: " + account2.getBalance());
        } else {
            System.out.println("Chuyển khoản thất bại: kiểm tra PIN, số dư, trạng thái tài khoản và hạn mức.");
        }

        System.out.println("\n--- KHÓA / MỞ KHÓA ---");
        boolean lockResult = account1.lockAccount("123456");
        if (lockResult) {
            System.out.println("Khóa tài khoản thành công.");
        } else {
            System.out.println("Không thể khóa tài khoản.");
        }

        boolean depositWhenLocked = account1.deposit(new BigDecimal("500000"));
        if (depositWhenLocked) {
            System.out.println("Nạp tiền thành công.");
        } else {
            System.out.println("Không thể nạp: tài khoản đang bị khóa.");
        }

        boolean unlockResult = account1.unlockAccount("123456");
        if (unlockResult) {
            System.out.println("Mở khóa tài khoản thành công.");
        } else {
            System.out.println("Mở khóa tài khoản thất bại.");
        }

        System.out.println("\n--- THÔNG TIN SAU GIAO DỊCH ---");
        account1.showAccountInfo();
        account2.showAccountInfo();
        System.out.println("\n--- LỊCH SỬ TÀI KHOẢN 1 ---");
        account1.showTransactionHistory();
        System.out.println("\n--- LỊCH SỬ TÀI KHOẢN 2 ---");
        account2.showTransactionHistory();
    }
}
