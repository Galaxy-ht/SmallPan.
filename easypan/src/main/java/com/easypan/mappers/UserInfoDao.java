package com.easypan.mappers;

import com.easypan.entity.po.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
* 用户CRUD
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-16
*/

public interface UserInfoDao extends BaseDao<UserInfo> {

    Integer updateUseSpace(@Param("userId") String userId, @Param("useSpace") Long useSpace, @Param("totalSpace") Long totalSpace);
}