package ro.org.common.DTOs;

import lombok.Data;
import ro.org.common.enums.TransactionTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
public class TransactionDTO {

    @NotNull(message = "Payer is empty")
    private final TransactionPartDTO payer;

    @NotNull(message = "Payee is empty")
    private final TransactionPartDTO payee;

    @NotNull(message = "Transaction type is empty")
    private final TransactionTypeEnum transactionType;


    @PositiveOrZero(message = "Amount invalid")
    @NotNull(message = "Amount missing")
    private final BigDecimal amount;

    @NotBlank(message = "Description invalid")
    private final String description;

}
