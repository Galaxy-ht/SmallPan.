package com.easypan.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Tao
 * @since 2023/08/21
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysSettingDto implements Serializable {
    private String registerEmailTitle = "邮箱验证码";
    private String registerEmailContent = "您好，您的邮箱验证码是：%s，15分钟有效！";
    private Integer userInitUseSpace = 5;
}
