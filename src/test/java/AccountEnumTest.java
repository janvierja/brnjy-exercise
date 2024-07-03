import com.barrenjoey.java.bank.AccountActionEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountEnumTest {
    @Test
    void textToEnum() {
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("deposit"));
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("deposit  "));
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("  deposit"));
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("  deposit  "));
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("Deposit"));
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("Deposit  "));
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("  Deposit"));
        assertEquals(AccountActionEnum.DEPOSIT, AccountActionEnum.decode("  Deposit  "));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("withdraw"));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("withdraw  "));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("  withdraw"));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("  withdraw  "));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("Withdraw"));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("Withdraw  "));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("  Withdraw"));
        assertEquals(AccountActionEnum.WITHDRAW, AccountActionEnum.decode("  Withdraw  "));

        assertEquals(AccountActionEnum.UNKNOWN, AccountActionEnum.decode("Credit"));
        assertEquals(AccountActionEnum.UNKNOWN, AccountActionEnum.decode(""));
        assertEquals(AccountActionEnum.UNKNOWN, AccountActionEnum.decode(null));

    }
}
