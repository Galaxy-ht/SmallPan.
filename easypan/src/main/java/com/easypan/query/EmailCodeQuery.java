package com.easypan.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 邮箱验证码查询
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-19
*/
@Data
@EqualsAndHashCode(callSuper = false)
//@Schema(description = "邮箱验证码查询")
public class EmailCodeQuery extends Query {
}