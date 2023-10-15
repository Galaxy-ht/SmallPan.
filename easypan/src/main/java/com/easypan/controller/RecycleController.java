package com.easypan.controller;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.enums.FileDelFlagEnums;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.page.PageResult;
import com.easypan.query.FileInfoQuery;
import com.easypan.service.FileInfoService;
import com.easypan.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author Tao
 * @since 2023/10/14
 */
@RestController
@RequestMapping("/recycle")
public class RecycleController extends BaseController{

    @Resource
    private FileInfoService fileInfoService;

    @RequestMapping("/loadRecycleList")
    @GlobalInterceptor(checkParams = true)
    public Result<PageResult<FileInfoVO>> loadRecycleList(HttpSession session, Integer pageNo, Integer pageSize) {
        FileInfoQuery query = new FileInfoQuery();
        query.setPageSize(pageSize);
        query.setPageNo(pageNo);
        query.setUserId(getUserInfoFromSession(session).getUserId());
        query.setOrderBy("recovery_time desc");
        query.setDelFlag(FileDelFlagEnums.RECYCLE.getFlag());
        PageResult<FileInfoVO> page = fileInfoService.page(query);
        return Result.ok(page);
    }

    // 回收站文件还原到根目录
    @RequestMapping("/recoverFile")
    @GlobalInterceptor(checkParams = true)
    public Result<String> recoverFile(HttpSession session,
                                  @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.recoverFileBatch(webUserDto.getUserId(), fileIds);
        return Result.ok();
    }

    @RequestMapping("/delFile")
    @GlobalInterceptor(checkParams = true)
    public Result<String> delFile(HttpSession session,
                              @VerifyParam(required = true) String fileIds) {
        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
        fileInfoService.delFileBatch(webUserDto.getUserId(), fileIds, false);
        return Result.ok();
    }
}
