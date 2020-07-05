package ro.org.m2.specific;

import lombok.Data;
import ro.org.common.enums.TransactionTypeEnum;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserReport {
    private String name;
    private String CNP;
    private String IBAN;
    private Map<TransactionTypeEnum, TransactionByTypeForReport> transactionByTypeForReportMap = new HashMap<>();
}
