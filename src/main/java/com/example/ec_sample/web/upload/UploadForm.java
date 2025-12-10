package com.example.ec_sample.web.upload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadForm {

    @NotNull(message = "商品IDは必須です")
    private Long productId;

    @NotNull(message = "ファイルは必須です")
    private MultipartFile file;
}
