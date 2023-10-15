package com.easypan.service;

import com.easypan.entity.po.EmailCode;
import com.easypan.page.PageResult;
import com.easypan.query.EmailCodeQuery;
import com.easypan.entity.vo.EmailCodeVO;

import java.util.List;

/**
 * 邮箱验证码
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-19
 */
public interface EmailCodeService extends BaseService<EmailCode> {

    PageResult<EmailCodeVO> page(EmailCodeQuery query);

    void save(EmailCodeVO vo);

    void update(EmailCodeVO vo);

    void delete(List<Long> idList);

    void sendEmailCode(String email, Integer type);

    void checkCode(String email, String emailCode);
}