package ro.org.common.DTOs;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TransactionPartDTO {

    @Size(min = 13, max = 13, message = "CNP invalid")
    @NotNull(message = "CNP missing")
    private String cnp;

    @Size(min = 24, max = 24, message = "IBAN invalid")
    @NotBlank(message = "IBAN missing")
    private String IBAN;

    @NotBlank(message = "Name invalid")
    private String name;

}
