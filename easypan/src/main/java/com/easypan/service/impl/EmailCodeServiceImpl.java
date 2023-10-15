package com.easypan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.easypan.config.AppConfig;
import com.easypan.convert.EmailCodeConvert;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SysSettingDto;
import com.easypan.entity.po.EmailCode;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.vo.EmailCodeVO;
import com.easypan.exception.FastException;
import com.easypan.mappers.EmailCodeDao;
import com.easypan.mappers.UserInfoDao;
import com.easypan.page.PageResult;
import com.easypan.query.EmailCodeQuery;
import com.easypan.service.EmailCodeService;
import com.easypan.utils.RedisComponent;
import com.easypan.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 邮箱验证码
 *
 * @author Tao MrHaoTao@gmail.com
 * @since 1.0.0 2023-08-19
 */
@Service
@AllArgsConstructor
public class EmailCodeServiceImpl extends BaseServiceImpl<EmailCodeDao, EmailCode> implements EmailCodeService {

    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private EmailCodeDao emailCodeDao;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;

    @Override
    public PageResult<EmailCodeVO> page(EmailCodeQuery query) {
        IPage<EmailCode> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), EmailCodeConvert.INSTANCE.convertList(page.getRecords()));
    }

    private QueryWrapper<EmailCode> getWrapper(EmailCodeQuery query) {
        QueryWrapper<EmailCode> wrapper = new QueryWrapper<>();

        return wrapper;
    }

    @Override
    public void save(EmailCodeVO vo) {
        EmailCode entity = EmailCodeConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(EmailCodeVO vo) {
        EmailCode entity = EmailCodeConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) {
        if (Objects.equals(type, Constants.ZERO)) {
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("email", email);
            UserInfo userInfo = userInfoDao.selectOne(wrapper);
            if (null != userInfo) {
                throw new FastException("邮箱已经存在");
            }
        }

        String code = StringUtils.getRandomNumber(Constants.LENGTH_5);

        // 发送验证码
        sendMailCode(email, code);

        emailCodeDao.disableEmailCode(email);

        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreatTime(new Date());
        this.save(emailCode);
    }

    private void sendMailCode(String toEmail, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(appConfig.getSendUsername());
            helper.setTo(toEmail);
            SysSettingDto sysSettingDto = new SysSettingDto();
            helper.setSubject(sysSettingDto.getRegisterEmailTitle());
            helper.setText(String.format(sysSettingDto.getRegisterEmailContent(), code));
            helper.setSentDate(new Date());
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
            throw new FastException("邮件发送失败");
        }
    }

    @Override
    public void checkCode(String email, String code) {
        EmailCode emailCode = this.getOne
                (new QueryWrapper<EmailCode>()
                        .eq("email", email)
                        .eq("code", code));
        if (null == emailCode) {
            throw new FastException("邮箱验证码错误");
        }

        if (emailCode.getStatus() == 1 || System.currentTimeMillis() - emailCode.getCreatTime().getTime() > Constants.LENGTH_15 * 1000 * 60) {
            throw new FastException("邮箱验证码已失效");
        }

        emailCodeDao.disableEmailCode(null);
    }
}