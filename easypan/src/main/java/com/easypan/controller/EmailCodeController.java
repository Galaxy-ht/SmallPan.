package com.easypan.controller;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.convert.EmailCodeConvert;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.po.EmailCode;
import com.easypan.exception.FastException;
import com.easypan.page.PageResult;
import com.easypan.query.EmailCodeQuery;
import com.easypan.service.EmailCodeService;
import com.easypan.utils.Result;
import com.easypan.entity.vo.EmailCodeVO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
* 邮箱验证码
*
* @author Tao MrHaoTao@gmail.com
* @since 1.0.0 2023-08-19
*/
@RestController
@RequestMapping("emailCode")
//@Tag(name="邮箱验证码")
@AllArgsConstructor
public class EmailCodeController {
    private final EmailCodeService emailCodeService;

    @RequestMapping("/sendEmailCode")
    @GlobalInterceptor(checkParams = true, checkLogin = false)
    public Result<String> sendEmailCode(HttpSession session,
                                        @VerifyParam(required = true) String email,
                                        @VerifyParam(required = true) String checkCode,
                                        @VerifyParam(required = true) Integer type) {
        try {
            if (!checkCode.equalsIgnoreCase((String) session.getAttribute(Constants.CHECK_CODE_KEY_EMAIL))) {
                throw new FastException("图片验证码不正确");
            }
            emailCodeService.sendEmailCode(email, type);
            return Result.ok("邮件发送成功");
        } finally {
            session.removeAttribute(Constants.CHECK_CODE_KEY_EMAIL);
        }
    }

    @GetMapping("page")
//    @Operation(summary = "分页")
//    @PreAuthorize("hasAuthority('emailcode:page')")
    public Result<PageResult<EmailCodeVO>> page(@Valid EmailCodeQuery query){
        PageResult<EmailCodeVO> page = emailCodeService.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
//    @Operation(summary = "信息")
//    @PreAuthorize("hasAuthority('emailcode:info')")
    public Result<EmailCodeVO> get(@PathVariable("id") Long id){
        EmailCode entity = emailCodeService.getById(id);

        return Result.ok(EmailCodeConvert.INSTANCE.convert(entity));
    }

    @PostMapping
//    @Operation(summary = "保存")
//    @PreAuthorize("hasAuthority('emailcode:save')")
    public Result<String> save(@RequestBody EmailCodeVO vo){
        emailCodeService.save(vo);

        return Result.ok();
    }

    @PutMapping
//    @Operation(summary = "修改")
//    @PreAuthorize("hasAuthority('emailcode:update')")
    public Result<String> update(@RequestBody @Valid EmailCodeVO vo){
        emailCodeService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
//    @Operation(summary = "删除")
//    @PreAuthorize("hasAuthority('emailcode:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        emailCodeService.delete(idList);

        return Result.ok();
    }
}