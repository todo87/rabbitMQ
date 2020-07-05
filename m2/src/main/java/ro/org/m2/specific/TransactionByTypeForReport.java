package ro.org.m2.specific;

import lombok.Data;
import ro.org.m2.model.TransactionDetail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionByTypeForReport {

    private BigDecimal sum = new BigDecimal(0);
    private List<TransactionDetail> transactionDetailList = new ArrayList<>();
}
