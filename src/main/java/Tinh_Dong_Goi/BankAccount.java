package Tinh_Dong_Goi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BankAccount {
    private static final int MAX_FAILED_PIN_ATTEMPTS = 3;

    private final String accountNumber;
    private final String ownerName;
    private BigDecimal balance;
    private final String pin;
    private boolean locked;
    private final BigDecimal dailyWithdrawLimit;
    private BigDecimal withdrawnToday;
    private LocalDate lastWithdrawDate;
    private int failedPinAttempts;
    private final List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String ownerName, BigDecimal initialBalance,
                       String pin, BigDecimal dailyWithdrawLimit) {
        // Khởi tạo giá trị an toàn thay vì ném IllegalArgumentException.
        if (accountNumber == null) {
            this.accountNumber = "UNKNOWN";
        } else if (accountNumber.isBlank()) {
            this.accountNumber = "UNKNOWN";
        } else {
            this.accountNumber = accountNumber;
        }
        if (ownerName == null) {
            this.ownerName = "Chưa cập nhật";
        } else if (ownerName.isBlank()) {
            this.ownerName = "Chưa cập nhật";
        } else {
            this.ownerName = ownerName;
        }
        if (initialBalance == null) {
            this.balance = BigDecimal.ZERO;
        } else if (initialBalance.signum() < 0) {
            this.balance = BigDecimal.ZERO;
        } else {
            this.balance = initialBalance;
        }
        if (isValidPin(pin)) {
            this.pin = pin;
        } else {
            this.pin = "000000";
        }
        if (dailyWithdrawLimit == null) {
            this.dailyWithdrawLimit = BigDecimal.ZERO;
        } else if (dailyWithdrawLimit.signum() <= 0) {
            this.dailyWithdrawLimit = BigDecimal.ZERO;
        } else {
            this.dailyWithdrawLimit = dailyWithdrawLimit;
        }
        this.locked = false;
        if (isValidPin(pin) == false) {
            this.locked = true;
        }
        if (this.dailyWithdrawLimit.signum() <= 0) {
            this.locked = true;
        }
        this.withdrawnToday = BigDecimal.ZERO;
        this.lastWithdrawDate = LocalDate.now();
        this.failedPinAttempts = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
    public boolean isLocked() { return locked; }
    public BigDecimal getDailyWithdrawLimit() { return dailyWithdrawLimit; }
    public BigDecimal getWithdrawnToday() { resetWithdrawnTodayIfNewDay(); return withdrawnToday; }
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(new ArrayList<>(transactionHistory));
    }

    private static boolean isValidPin(String value) {
        if (value == null) {
            return false;
        }
        if (value.matches("\\d{6}") == false) {
            return false;
        }
        return true;
    }

    private boolean authenticate(String inputPin) {
        if (locked) return false;
        if (pin.equals(inputPin)) {
            failedPinAttempts = 0;
            return true;
        }
        failedPinAttempts++;
        if (failedPinAttempts >= MAX_FAILED_PIN_ATTEMPTS) {
            locked = true;
            System.out.println("Tài khoản " + accountNumber + " đã bị khóa do nhập sai PIN 3 lần.");
        }
        return false;
    }

    private boolean validAmount(BigDecimal amount) {
        if (amount == null) {
            return false;
        }
        if (amount.signum() <= 0) {
            return false;
        }
        return true;
    }

    private void addTransaction(String type, BigDecimal amount, String description) {
        transactionHistory.add(new Transaction(UUID.randomUUID().toString(), type, amount, description));
    }

    private void resetWithdrawnTodayIfNewDay() {
        LocalDate today = LocalDate.now();
        if (today.equals(lastWithdrawDate) == false) {
            withdrawnToday = BigDecimal.ZERO;
            lastWithdrawDate = today;
        }
    }

    private boolean withinDailyLimit(BigDecimal amount) {
        resetWithdrawnTodayIfNewDay();
        if (amount.compareTo(dailyWithdrawLimit) > 0) {
            return false;
        }
        BigDecimal totalAfterTransaction = withdrawnToday.add(amount);
        if (totalAfterTransaction.compareTo(dailyWithdrawLimit) > 0) {
            return false;
        }
        return true;
    }

    public boolean deposit(BigDecimal amount) {
        if (locked) {
            return false;
        }
        if (validAmount(amount) == false) {
            return false;
        }
        balance = balance.add(amount);
        addTransaction("DEPOSIT", amount, "Nạp tiền vào tài khoản " + accountNumber);
        return true;
    }

    public boolean withdraw(BigDecimal amount, String inputPin) {
        if (locked) {
            return false;
        }
        if (validAmount(amount) == false) {
            return false;
        }
        if (authenticate(inputPin) == false) {
            return false;
        }
        if (amount.compareTo(balance) > 0) {
            return false;
        }
        if (withinDailyLimit(amount) == false) {
            return false;
        }
        balance = balance.subtract(amount);
        withdrawnToday = withdrawnToday.add(amount);
        addTransaction("WITHDRAW", amount, "Rút tiền khỏi tài khoản " + accountNumber);
        return true;
    }

    public boolean transfer(BankAccount receiver, BigDecimal amount, String inputPin) {
        if (locked) {
            return false;
        }
        if (validAmount(amount) == false) {
            return false;
        }
        if (authenticate(inputPin) == false) {
            return false;
        }
        if (receiver == null) {
            return false;
        }
        if (receiver == this) {
            return false;
        }
        if (receiver.locked) {
            return false;
        }
        if (amount.compareTo(balance) > 0) {
            return false;
        }
        if (withinDailyLimit(amount) == false) {
            return false;
        }
        balance = balance.subtract(amount);
        receiver.balance = receiver.balance.add(amount);
        withdrawnToday = withdrawnToday.add(amount);
        addTransaction("TRANSFER_OUT", amount, "Chuyển tiền đến " + receiver.accountNumber);
        receiver.addTransaction("TRANSFER_IN", amount, "Nhận tiền từ " + accountNumber);
        return true;
    }

    public boolean lockAccount(String inputPin) {
        if (locked) {
            return false;
        }
        if (authenticate(inputPin) == false) {
            return false;
        }
        locked = true;
        return true;
    }

    public boolean unlockAccount(String inputPin) {
        if (locked == false) {
            return false;
        }
        if (inputPin == null) {
            return false;
        }
        if (pin.equals(inputPin) == false) {
            return false;
        }
        locked = false;
        failedPinAttempts = 0;
        return true;
    }

    public void showAccountInfo() {
        System.out.println("===== THÔNG TIN TÀI KHOẢN =====");
        System.out.println("Số tài khoản: " + accountNumber);
        System.out.println("Chủ tài khoản: " + ownerName);
        System.out.println("Số dư: " + balance);
        if (locked) {
            System.out.println("Trạng thái: Đã khóa");
        } else {
            System.out.println("Trạng thái: Đang hoạt động");
        }
        System.out.println("Hạn mức rút/chuyển mỗi ngày: " + dailyWithdrawLimit);
        System.out.println("Đã rút/chuyển hôm nay: " + getWithdrawnToday());
        System.out.println("===============================");
    }

    public void showTransactionHistory() {
        System.out.println("===== LỊCH SỬ GIAO DỊCH =====");
        if (transactionHistory.isEmpty()) {
            System.out.println("Chưa có giao dịch");
            return;
        }
        for (Transaction transaction : transactionHistory) transaction.showTransaction();
    }
}
