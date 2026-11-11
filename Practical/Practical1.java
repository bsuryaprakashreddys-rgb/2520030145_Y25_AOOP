import java.util.UUID;
import java.time.LocalDateTime;
class LedgerEntry {
    private final String transactionId;
    private final String transactionType;
    private final Money amount;
    private final String timestamp;
public LedgerEntry(String transactionType, Money amount) {
    this.transactionId = UUID.randomUUID().toString();
    this.transactionType = transactionType;
    this.amount = amount;
    this.timestamp = LocalDateTime.now().toString();
}

public String getTransactionId() {
    return transactionId;
}

public String getTransactionType() {
    return transactionType;
}

public Money getAmount() {
    return amount;
}

public String getTimestamp() {
    return timestamp;
}

public void display() {
    System.out.println("Transaction ID: " + transactionId);
    System.out.println("Type: " + transactionType);
    System.out.println("Amount: " + amount);
    System.out.println("Timestamp: " + timestamp);
}
}
class FundTransfer {
    private final String transferId;
    private final BankAccount source;
    private final BankAccount destination;
    private final Money amount;
public FundTransfer(BankAccount source, BankAccount destination, Money amount) {
    this.transferId = UUID.randomUUID().toString();
    this.source = source;
    this.destination = destination;
    this.amount = amount;
}

public void transfer() {
    if (source == null || destination == null || amount == null) {
        throw new IllegalArgumentException("Invalid transfer details");
    }

    if (source == destination) {
        throw new IllegalArgumentException("Cannot transfer to the same account");
    }

    if (!amount.isPositive()) {
        throw new IllegalArgumentException("Transfer amount must be positive");
    }

    if (source.getStatus() != AccountStatus.ACTIVE ||
        destination.getStatus() != AccountStatus.ACTIVE) {
        throw new IllegalStateException("Both accounts must be active");
    }

    if (!source.getBalance().getCurrency().equals(amount.getCurrency()) ||
        !destination.getBalance().getCurrency().equals(amount.getCurrency())) {
        throw new IllegalArgumentException("Currency mismatch");
    }

    source.withdraw(amount);
    destination.deposit(amount);

    System.out.println("Transfer successful!");
    System.out.println("Transfer ID: " + transferId);
}
}
public class Main {
    public static void main(String[] args) {
    Money overdraft = new Money(500000, "INR");
    Money depositAmount = new Money(1000000, "INR");
    Money withdrawAmount = new Money(200000, "INR");
    Money transferAmount = new Money(300000, "INR");

    BankAccount account1 =
        new BankAccount("1001", "INR", overdraft);

    BankAccount account2 =
        new BankAccount("1002", "INR", overdraft);

    account1.deposit(depositAmount);
    account2.deposit(new Money(500000, "INR"));

    account1.withdraw(withdrawAmount);

    FundTransfer transfer =
        new FundTransfer(account1, account2, transferAmount);

    transfer.transfer();

    System.out.println("\n----- ACCOUNT DETAILS -----");
    System.out.println("Account 1: " + account1.getBalance());
    System.out.println("Account 2: " + account2.getBalance());

    account1.freeze();
    account1.unfreeze();

    System.out.println("Account Status: " + account1.getStatus());

    LedgerEntry entry =
        new LedgerEntry("DEPOSIT", depositAmount);

    System.out.println("\n----- LEDGER ENTRY -----");
    entry.display();
}
}