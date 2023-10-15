package com.easypan.convert;

import com.easypan.entity.po.EmailCode;
import com.easypan.entity.vo.EmailCodeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 邮箱验证码
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-19
*/
@Mapper(componentModel = "spring")
public interface EmailCodeConvert {
    EmailCodeConvert INSTANCE = Mappers.getMapper(EmailCodeConvert.class);

    EmailCode convert(EmailCodeVO vo);

    EmailCodeVO convert(EmailCode entity);

    List<EmailCodeVO> convertList(List<EmailCode> list);

}