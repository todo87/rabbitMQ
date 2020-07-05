package ro.org.m2.service.business;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.org.common.DTOs.ReportDownloadDTO;
import ro.org.common.enums.TransactionTypeEnum;
import ro.org.common.exceptions.AppValidationException;
import ro.org.m2.model.Transaction;
import ro.org.m2.model.TransactionDetail;
import ro.org.m2.service.TransactionService;
import ro.org.m2.specific.TransactionByTypeForReport;
import ro.org.m2.specific.UserReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionBusinessServiceUnitTest {

    @Mock
    private  TransactionService transactionService;

    private final String templatePath = "templates/report_template.docx";

    @InjectMocks
    private TransactionBusinessService transactionBusinessService = new TransactionBusinessService(transactionService, templatePath);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void makeReport() {
        UserReport userReport = new UserReport();
        userReport.setName("payee");
        userReport.setCNP("1234567890123");
        userReport.setIBAN("123456789012345678901234");

        List<TransactionDetail> transactionDetails = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TransactionDetail transactionDetail = new TransactionDetail();
            transactionDetail.setDetails("TD " + i);
            transactionDetails.add(transactionDetail);
        }

        TransactionByTypeForReport tdIBAN = new TransactionByTypeForReport();
        tdIBAN.setSum(new BigDecimal(100));
        tdIBAN.setTransactionDetailList(transactionDetails);
        userReport.getTransactionByTypeForReportMap().put(TransactionTypeEnum.IBAN_TO_IBAN, tdIBAN);

        TransactionByTypeForReport tdWALLET= new TransactionByTypeForReport();
        tdWALLET.setSum(new BigDecimal(200));
        tdWALLET.setTransactionDetailList(transactionDetails);
        userReport.getTransactionByTypeForReportMap().put(TransactionTypeEnum.WALLET_TO_WALLET, tdWALLET);

        ReportDownloadDTO reportDownloadDTO = transactionBusinessService.makeReport(userReport);

        assertTrue(reportDownloadDTO.getFileFormat().equals(".docx"));
        assertTrue(reportDownloadDTO.getContent().length == 10469);

        /*try (FileOutputStream fos = new FileOutputStream("templates/aaaaaaaaaaaaaaaaaaaa.docx")) {
            fos.write(reportDownloadDTO.getContent());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Test
    void makeReportAggregationListEmpty() {
        List<Transaction> transactions = new ArrayList<>();
        String expectedMessage = "User has no transactions";
        Exception ex = assertThrows(AppValidationException.class, () -> {
            transactionBusinessService.makeReportAggregation(transactions);
        });
        String actualMessage = ex.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void makeReportAggregation() {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < 3;i++) {
            Transaction transaction = new Transaction();
            transaction.setTransactionType(TransactionTypeEnum.IBAN_TO_IBAN);
            transaction.setAmount(new BigDecimal(100));
            transaction.setPayerCNP("1234567890123");
            transaction.setPayerIBAN("123456789012345678901234");
            transaction.setPayerName("payer");
            transaction.setPayeeCNP("1234567890123");
            transaction.setPayeeIBAN("123456789012345678901234");
            transaction.setPayeeName("payee");
            transaction.setAmount(transaction.getAmount().add(new BigDecimal(i)));
            TransactionDetail td = new TransactionDetail();
            td.setDetails("Detail " + i);
            transaction.setTransactionDetail(td);
            transactions.add(transaction);
        }

        for (int i = 0; i < 3;i++) {
            Transaction transaction = new Transaction();
            transaction.setTransactionType(TransactionTypeEnum.WALLET_TO_WALLET);
            transaction.setAmount(new BigDecimal(200));
            transaction.setPayerCNP("1234567890123");
            transaction.setPayerIBAN("123456789012345678901234");
            transaction.setPayerName("payer");
            transaction.setPayeeCNP("1234567890123");
            transaction.setPayeeIBAN("123456789012345678901234");
            transaction.setPayeeName("payee");
            transaction.setAmount(transaction.getAmount().add(new BigDecimal(i)));
            TransactionDetail td = new TransactionDetail();
            td.setDetails("Detail " + i);
            transaction.setTransactionDetail(td);
            transactions.add(transaction);
        }

        UserReport userReport = transactionBusinessService.makeReportAggregation(transactions);

        assertTrue(userReport.getCNP().equals("1234567890123"));
        assertTrue(userReport.getIBAN().equals("123456789012345678901234"));
        assertTrue(userReport.getName().equals("payee"));

        assertTrue(userReport.getTransactionByTypeForReportMap().keySet().size() == 2);

        for (TransactionTypeEnum trType : userReport.getTransactionByTypeForReportMap().keySet()) {
            if (TransactionTypeEnum.IBAN_TO_IBAN.equals(trType)) {
                assertTrue(userReport.getTransactionByTypeForReportMap().get(trType).getSum().equals(new BigDecimal(303)));
                assertTrue(userReport.getTransactionByTypeForReportMap().get(trType).getTransactionDetailList().size() == 3);
            } else if (TransactionTypeEnum.WALLET_TO_WALLET.equals(trType)){
                assertTrue(userReport.getTransactionByTypeForReportMap().get(trType).getSum().equals(new BigDecimal(603)));
                assertTrue(userReport.getTransactionByTypeForReportMap().get(trType).getTransactionDetailList().size() == 3);
            }
        }
    }

}
