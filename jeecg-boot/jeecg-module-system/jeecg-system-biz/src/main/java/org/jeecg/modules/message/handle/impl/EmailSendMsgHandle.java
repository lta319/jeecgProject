package org.jeecg.modules.message.handle.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.StaticConfig;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.mapper.SysMessageMapper;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 邮箱发送信息
 * @author: jeecg-boot
 */
@Slf4j
@Component("emailSendMsgHandle")
public class EmailSendMsgHandle implements ISendMsgHandle {
    static String emailFrom;

    public static void setEmailFrom(String emailFrom) {
        EmailSendMsgHandle.emailFrom = emailFrom;
    }

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private SysMessageMapper sysMessageMapper;

    /**
     * 真实姓名变量
     */
    private static final String  realNameExp = "{REALNAME}";
    /**
     * 线程池用于异步发送消息
     */
    public static ExecutorService cachedThreadPool = new ThreadPoolExecutor(0, 1024, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());


    @Override
    public void sendMsg(String esReceiver, String esTitle, String esContent) {
        JavaMailSender mailSender = (JavaMailSender) SpringContextUtils.getBean("mailSender");
        MimeMessage message = mailSender.createMimeMessage();
        //update-begin-author：taoyan date:20200811 for:配置类数据获取
        if(oConvertUtils.isEmpty(emailFrom)){
            StaticConfig staticConfig = SpringContextUtils.getBean(StaticConfig.class);
            setEmailFrom(staticConfig.getEmailFrom());
        }
        //update-end-author：taoyan date:20200811 for:配置类数据获取
        cachedThreadPool.execute(()->{
            try {
                log.info("============> 开始邮件发送，接收人："+esReceiver);
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                // 设置发送方邮箱地址
                helper.setFrom(emailFrom);
                helper.setTo(esReceiver);
                helper.setSubject(esTitle);
                helper.setText(esContent, true);
                mailSender.send(message);
                log.info("============> 邮件发送成功，接收人："+esReceiver);
            } catch (MessagingException e) {
                log.error("============> 邮件发送失败，接收人："+esReceiver, e.getMessage());
            }
        });
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        String content = messageDTO.getContent();
        String title = messageDTO.getTitle();
        //update-begin---author:wangshuai---date:2024-11-20---for:【QQYUN-8523】敲敲云发邮件通知，不稳定---
        boolean timeJobSendEmail = this.isTimeJobSendEmail(messageDTO.getToUser(), title, content);
        if(timeJobSendEmail){
            return;
        }
        //update-end---author:wangshuai---date:2024-11-20---for:【QQYUN-8523】敲敲云发邮件通知，不稳定---
        this.sendEmailMessage(messageDTO);
    }

    /**
     * 直接发送邮件
     * 
     * @param messageDTO
     */
    public void sendEmailMessage(MessageDTO messageDTO) {
        String[] arr = messageDTO.getToUser().split(",");
        LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>().in(SysUser::getUsername, arr);
        List<SysUser> list = sysUserMapper.selectList(query);
        String content = messageDTO.getContent();
        String title = messageDTO.getTitle();
        for(SysUser user: list){
            String email = user.getEmail();
            if (ObjectUtils.isEmpty(email)) {
                continue;
            }
            content=replaceContent(user,content);
            log.info("邮件内容："+ content);
            sendMsg(email, title, content);
        }
        
        //update-begin-author:taoyan date:2023-6-20 for: QQYUN-5557【简流】通知节点 发送邮箱 表单上有一个邮箱字段，流程中，邮件发送节点，邮件接收人 不可选择邮箱
        Set<String> toEmailList = messageDTO.getToEmailList();
        if(toEmailList!=null && toEmailList.size()>0){
            for(String email: toEmailList){
                if (ObjectUtils.isEmpty(email)) {
                    continue;
                }
                log.info("邮件内容："+ content);
                sendMsg(email, title, content);
            }
        }
        //update-end-author:taoyan date:2023-6-20 for: QQYUN-5557【简流】通知节点 发送邮箱 表单上有一个邮箱字段，流程中，邮件发送节点，邮件接收人 不可选择邮箱
        
        //发送给抄送人
        sendMessageToCopyUser(messageDTO);
    }

    /**
     * 发送邮件给抄送人
     * @param messageDTO
     */
    public void sendMessageToCopyUser(MessageDTO messageDTO) {
        String copyToUser = messageDTO.getCopyToUser();
        if(ObjectUtils.isNotEmpty(copyToUser)) {
            LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<SysUser>().in(SysUser::getUsername, copyToUser.split(","));
            List<SysUser> list = sysUserMapper.selectList(query);
            String content = messageDTO.getContent();
            String title = messageDTO.getTitle();

            for (SysUser user : list) {
                String email = user.getEmail();
                if (ObjectUtils.isEmpty(email)) {
                    continue;
                }
                content=replaceContent(user,content);
                log.info("邮件内容：" + content);
                
            //update-begin-author:taoyan date:2023-6-20 for: QQYUN-5557【简流】通知节点 发送邮箱 表单上有一个邮箱字段，流程中，邮件发送节点，邮件接收人 不可选择邮箱
                sendEmail(email, content, title);
            }

            Set<String> ccEmailList = messageDTO.getCcEmailList();
            if(ccEmailList!=null && ccEmailList.size()>0){
                for(String email: ccEmailList){
                    if (ObjectUtils.isEmpty(email)) {
                        continue;
                    }
                    log.info("邮件内容："+ content);
                    sendEmail(email, content, title);
                }
            }
            
        }
    }

    /**
     * 发送邮件给抄送人调用
     * @param email
     * @param content
     * @param title
     */
    private void sendEmail(String email, String content, String title){
        JavaMailSender mailSender = (JavaMailSender) SpringContextUtils.getBean("mailSender");
        MimeMessage message = mailSender.createMimeMessage();
        if (oConvertUtils.isEmpty(emailFrom)) {
            StaticConfig staticConfig = SpringContextUtils.getBean(StaticConfig.class);
            setEmailFrom(staticConfig.getEmailFrom());
        }
        cachedThreadPool.execute(()->{
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                // 设置发送方邮箱地址
                helper.setFrom(emailFrom);
                helper.setTo(email);
                //设置抄送人
                helper.setCc(email);
                helper.setSubject(title);
                helper.setText(content, true);
                mailSender.send(message);
                log.info("============> 邮件发送成功，接收人："+email);
            } catch (MessagingException e) {
                log.warn("============> 邮件发送失败，接收人："+email, e.getMessage());
            }
        });
    }
    //update-end-author:taoyan date:2023-6-20 for: QQYUN-5557【简流】通知节点 发送邮箱 表单上有一个邮箱字段，流程中，邮件发送节点，邮件接收人 不可选择邮箱
    

    /**
     * 替换邮件内容变量
     * @param user
     * @param content
     * @return
     */
    private String replaceContent(SysUser user,String content){
        if (content.indexOf(realNameExp) > 0) {
            content = content.replace("$"+realNameExp,user.getRealname()).replace(realNameExp, user.getRealname());
        }
        if (content.indexOf(CommonConstant.LOGIN_TOKEN) > 0) {
            String token = getToken(user);
            try {
                content = content.replace(CommonConstant.LOGIN_TOKEN, URLEncoder.encode(token, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("邮件消息token编码失败", e.getMessage());
            }
        }
        return content;
    }

    /**
     * 获取token
     * @param user
     * @return
     */
    private String getToken(SysUser user) {
        // 生成token
        String token = JwtUtil.sign(user.getUsername(), user.getPassword());
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        // 设置超时时间 1个小时
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 1 / 1000);
        return token;
    }

    /**
     * 是否定时发送邮箱
     * @param toUser
     * @param title
     * @param content
     * @return
     */
    private boolean isTimeJobSendEmail(String toUser, String title, String content) {
        StaticConfig staticConfig = SpringContextUtils.getBean(StaticConfig.class);
        Boolean timeJobSend = staticConfig.getTimeJobSend();
        if(null != timeJobSend && timeJobSend){
            this.addSysSmsSend(toUser,title,content);
            return true;
        }
        return false;
    }
    
    /**
     * 保存到短信发送表
     */
    private void addSysSmsSend(String toUser, String title, String content) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setEsTitle(title);
        sysMessage.setEsContent(content);
        sysMessage.setEsReceiver(toUser);
        sysMessage.setEsSendStatus("0");
        sysMessage.setEsSendNum(0);
        sysMessage.setEsType(MessageTypeEnum.YJ.getType());
        sysMessageMapper.insert(sysMessage);
    }
}
