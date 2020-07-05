package ro.org.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(
        shape = JsonFormat.Shape.OBJECT
)
public enum TransactionTypeEnum {
    IBAN_TO_IBAN,
    IBAN_TO_WALLET,
    WALLET_TO_IBAN,
    WALLET_TO_WALLET,
    ;
}
