package ro.org.m2.service.business;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.org.common.DTOs.ReportDownloadDTO;
import ro.org.common.enums.TransactionTypeEnum;
import ro.org.common.exceptions.AppValidationException;
import ro.org.m2.model.Transaction;
import ro.org.m2.model.TransactionDetail;
import ro.org.m2.service.TransactionService;
import ro.org.m2.specific.TransactionByTypeForReport;
import ro.org.m2.specific.UserReport;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Service
public class TransactionBusinessService {

    private final TransactionService transactionService;

    private final String templatePath;

    public TransactionBusinessService(TransactionService transactionService,
                                      @Value("${templatePath}") String templatePath) {
        this.transactionService = transactionService;
        this.templatePath = templatePath;
    }

    public ReportDownloadDTO downloadReport(String payeeName) {
        List<Transaction> transactions;
        transactions = transactionService.findAllByPayeeNameOrderByTransactionTypeAsc(payeeName);
        UserReport userReport = makeReportAggregation(transactions);
        return makeReport(userReport);
    }

    public UserReport makeReportAggregation(List<Transaction> transactions) {
        if (transactions.isEmpty()){
            throw new AppValidationException("User has no transactions");
        }
        UserReport userReport = new UserReport();
        userReport.setCNP(transactions.get(0).getPayeeCNP());
        userReport.setIBAN(transactions.get(0).getPayerIBAN());
        userReport.setName(transactions.get(0).getPayeeName());
        transactions.forEach(el -> {
            TransactionByTypeForReport tfr;
            if (userReport.getTransactionByTypeForReportMap().containsKey(el.getTransactionType())) {
                tfr = userReport.getTransactionByTypeForReportMap().get(el.getTransactionType());
            } else {
                tfr = new TransactionByTypeForReport();
                userReport.getTransactionByTypeForReportMap().put(el.getTransactionType(), tfr);
            }
            tfr.setSum(tfr.getSum().add(el.getAmount()));
            tfr.getTransactionDetailList().add(el.getTransactionDetail());
        });
        return userReport;
    }

    public ReportDownloadDTO makeReport(UserReport userReport) {
        ReportDownloadDTO reportDownloadDTO = new ReportDownloadDTO();
        try {
            XWPFDocument doc = new XWPFDocument(new FileInputStream(templatePath));
            doc = replaceText(doc, "${NAME}", userReport.getName());
            doc = replaceText(doc, "${CNP}", userReport.getCNP());
            doc = replaceText(doc, "${IBAN}", userReport.getIBAN());

            reportDownloadDTO.setFileName(userReport.getName() + ".docx");
            reportDownloadDTO.setFileFormat(".docx");

            int counterTransactionType = 1;
            for (TransactionTypeEnum value : TransactionTypeEnum.values()) {
                XWPFParagraph xwpfParagraph = doc.createParagraph();
                XWPFRun run = xwpfParagraph.createRun();
                StringBuilder transactions = new StringBuilder();
                if (userReport.getTransactionByTypeForReportMap().containsKey(value)) {
                    TransactionByTypeForReport transactionByTypeForReport = userReport.getTransactionByTypeForReportMap().get(value);
                    transactions.append(counterTransactionType++);
                    transactions.append(". ");
                    transactions.append(value);
                    transactions.append(" | ");
                    transactions.append(transactionByTypeForReport.getTransactionDetailList().size());
                    transactions.append(" tranzactii ");
                    transactions.append(" | ");
                    transactions.append(transactionByTypeForReport.getSum());
                    transactions.append(" RON ");
                    run.setText(transactions.toString());
                    int counterTransactionDetail = 1;
                    for (TransactionDetail transactionDetail : transactionByTypeForReport.getTransactionDetailList()) {
                        StringBuilder detailsSB = new StringBuilder();
                        XWPFParagraph detailParagraph = doc.createParagraph();
                        XWPFRun detailsRun = detailParagraph.createRun();
                        detailsRun.getCTR().addNewTab();
                        detailsSB.append(counterTransactionDetail++);
                        detailsSB.append(". ");
                        detailsSB.append(transactionDetail.getDetails());
                        detailsRun.setText(detailsSB.toString());
                    }
                } else {
                    transactions.append(counterTransactionType++);
                    transactions.append(". ");
                    transactions.append(value);
                    transactions.append(" | fara tranzactii ");
                    run.setText(transactions.toString());
                }
            }
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            doc.write(b);
            reportDownloadDTO.setContent(b.toByteArray());
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return reportDownloadDTO;
    }

    private XWPFDocument replaceText(XWPFDocument doc, String findText, String replaceText){
        Iterator<XWPFParagraph> paragraphsIterator = doc.getParagraphsIterator();
        paragraphsIterator.forEachRemaining(paragraph -> {
            for (int i = 0; i < paragraph.getRuns().size(); i++) {
                XWPFRun run = paragraph.getRuns().get(i);
                String text = run.text();
                if (text != null && text.contains(findText)) {
                    text = text.replace(findText, replaceText);
                    run.setText(text, 0);
                }
            }
        });
        return doc;
    }

}
