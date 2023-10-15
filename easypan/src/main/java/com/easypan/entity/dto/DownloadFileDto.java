package com.easypan.entity.dto;

import lombok.Data;

/**
 * @author Tao
 * @since 2023/10/14
 */

@Data
public class DownloadFileDto {
    private String downloadCode;
    private String fileName;
    private String filePath;
}