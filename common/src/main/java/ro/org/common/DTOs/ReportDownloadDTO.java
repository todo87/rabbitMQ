package ro.org.common.DTOs;

import lombok.Data;

@Data
public class ReportDownloadDTO {

    private String fileName;
    private String fileFormat;
    private byte[] content;
}
