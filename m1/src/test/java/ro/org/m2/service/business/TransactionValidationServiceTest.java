package ro.org.m2.service.business;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.org.common.DTOs.TransactionDTO;
import ro.org.common.DTOs.TransactionPartDTO;
import ro.org.common.enums.TransactionTypeEnum;
import ro.org.common.exceptions.AppValidationException;
import ro.org.m1.service.RabbitMQSender;
import ro.org.m1.service.business.TransactionValidationService;

import javax.validation.Validation;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionValidationServiceTest {

    @Mock
    private RabbitMQSender rabbitMQSender;

    @InjectMocks
    private TransactionValidationService transactionValidationService = new TransactionValidationService(rabbitMQSender, Validation.buildDefaultValidatorFactory().getValidator());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void validateTransactionDTO() {
        String[] expectedMessages = {"Payer is empty", "Payee is empty", "Transaction type is empty", "Amount missing", "Description invalid"};
        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateAndMakeTransaction(new TransactionDTO(null, null, null, null, null));
        });
        String actualMessage = ex.getMessage();
        for (String s : expectedMessages) {
            assertTrue(actualMessage.contains(s));
        }
    }

    @Test
    void validateTransactionNull() {
        String expectedMessage = "Transaction invalid";
        TransactionDTO transactionDTO = null;
        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateAndMakeTransaction(transactionDTO);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionMissingAmount() {
        TransactionPartDTO payer = new TransactionPartDTO();
        TransactionPartDTO payee = new TransactionPartDTO();
        String expectedMessage = "Amount missing";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateAndMakeTransaction(new TransactionDTO(payer, payee, TransactionTypeEnum.IBAN_TO_IBAN, null, "test"));
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionDetailsDTONegativeAmount() {
        TransactionPartDTO payer = new TransactionPartDTO();
        TransactionPartDTO payee = new TransactionPartDTO();
        String expectedMessage = "Amount invalid";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateAndMakeTransaction(new TransactionDTO(payer, payee, TransactionTypeEnum.IBAN_TO_IBAN, new BigDecimal(-1), "test"));
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionDetailsDTO() {
        TransactionPartDTO transactionPartDTO = new TransactionPartDTO();
        String[] expectedMessages = {"CNP missing", "IBAN missing", "Name invalid"};

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateTransactionDetailsDTO(transactionPartDTO);
        });
        String actualMessage = ex.getMessage();
        for (String s : expectedMessages) {
            assertTrue(actualMessage.contains(s));
        }
    }

    @Test
    void validateTransactionDetailsDTOCNPNull() {
        TransactionPartDTO transactionPartDTO = new TransactionPartDTO();
        // CNP missing
        transactionPartDTO.setCnp(null);
        transactionPartDTO.setIBAN("123456789012345678901234");
        transactionPartDTO.setName("test");
        String expectedMessage = "CNP missing";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateTransactionDetailsDTO(transactionPartDTO);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionDetailsDTOCNPMax() {
        TransactionPartDTO transactionPartDTO = new TransactionPartDTO();
        //14 CNP chars
        transactionPartDTO.setCnp("12345678901234");
        transactionPartDTO.setIBAN("123456789012345678901234");
        transactionPartDTO.setName("test");
        String expectedMessage = "CNP invalid";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateTransactionDetailsDTO(transactionPartDTO);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionDetailsDTOCNPMin() {
        TransactionPartDTO transactionPartDTO = new TransactionPartDTO();
        //12 CNP chars
        transactionPartDTO.setCnp("123456789012");
        transactionPartDTO.setIBAN("123456789012345678901234");
        transactionPartDTO.setName("test");
        String expectedMessage = "CNP invalid";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateTransactionDetailsDTO(transactionPartDTO);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionDetailsDTOIBANNull() {
        TransactionPartDTO transactionPartDTO = new TransactionPartDTO();
        transactionPartDTO.setCnp("1234567890123");
        // IBAN missing
        transactionPartDTO.setIBAN(null);
        transactionPartDTO.setName("test");
        String expectedMessage = "IBAN missing";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateTransactionDetailsDTO(transactionPartDTO);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionDetailsDTOIBANMax() {
        TransactionPartDTO transactionPartDTO = new TransactionPartDTO();
        transactionPartDTO.setCnp("1234567890123");
        // IBAN 25 chars
        transactionPartDTO.setIBAN("1234567890123456789012345");
        transactionPartDTO.setName("test");
        String expectedMessage = "IBAN invalid";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateTransactionDetailsDTO(transactionPartDTO);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void validateTransactionDetailsDTOIBANMin() {
        TransactionPartDTO transactionPartDTO = new TransactionPartDTO();
        transactionPartDTO.setCnp("1234567890123");
        // IBAN 23 chars
        transactionPartDTO.setIBAN("12345678901234567890123");
        transactionPartDTO.setName("test");
        String expectedMessage = "IBAN invalid";

        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionValidationService.validateTransactionDetailsDTO(transactionPartDTO);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

}