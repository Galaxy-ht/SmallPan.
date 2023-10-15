package com.easypan.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Tao
 * @since 2023/08/22
 */
@Data
public class SessionWebUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nickName;

    private String userId;

    private Boolean isAdmin;

    private String avatar;

}
