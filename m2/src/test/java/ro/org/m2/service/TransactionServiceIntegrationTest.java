package ro.org.m2.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.org.common.DTOs.TransactionDTO;
import ro.org.common.DTOs.TransactionPartDTO;
import ro.org.common.enums.TransactionTypeEnum;

import java.math.BigDecimal;

@SpringBootTest
public class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionService transactionService;

    @Test
    void persistTransaction() {
        TransactionPartDTO payer = new TransactionPartDTO();
        payer.setCnp("1234567890123");
        payer.setIBAN("123456789012345678901234");
        payer.setName("payer");
        TransactionPartDTO payee = new TransactionPartDTO();
        payee.setCnp("1234567890123");
        payee.setIBAN("123456789012345678901234");
        payee.setName("payee");

        TransactionDTO transactionDTO = new TransactionDTO(payer, payee, TransactionTypeEnum.IBAN_TO_IBAN, new BigDecimal(100), "desc");
        transactionService.persistTransaction(transactionDTO);
    }

}
