package com.easypan.service;

import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.po.UserInfo;
import com.easypan.page.PageResult;
import com.easypan.query.UserInfoQuery;
import com.easypan.entity.vo.UserInfoVO;

import java.util.List;

/**
 * 用户CRUD
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-16
 */
public interface UserInfoService extends BaseService<UserInfo> {

    PageResult<UserInfoVO> page(UserInfoQuery query);

    void save(UserInfoVO vo);

    void update(UserInfoVO vo);

    void delete(List<Long> idList);

    void register(String email, String nickName, String password, String emailCode);

    SessionWebUserDto login(String email, String password);

    void resetPwd(String email, String password, String emailCode);
}