package com.cnic.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.cnic.auth.service.SysUserService;
import com.cnic.common.jwt.JwtHelper;
import com.cnic.common.result.Result;
import com.cnic.model.system.SysUser;
import com.cnic.vo.wechat.BindPhoneVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin/wechat")
@Slf4j
@CrossOrigin
public class WechatController {

    @Resource
    private SysUserService sysUserService;

    @Autowired
    private WxMpService wxMpService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    private static final long serialVersionUID = 1L;

    public static final String TOKEN = "wechat";
    public static String checkSignature(String signature ,String timestamp, String nonce)
    {
        String[] src = {TOKEN,timestamp,nonce};
        List<String> list = Arrays.asList(src);
        Collections.sort(list);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++)
        {
            sb.append(list.get(i));
        }
        return SHA1(sb.toString());

    }


    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    // 微信公众号token验证接口
    @GetMapping("/check")
    protected void doget(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        System.out.println(signature + timestamp + nonce);

        PrintWriter out = response.getWriter();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (checkSignature(signature, timestamp, nonce).equals(signature)) {
            out.write(echostr);
            System.out.println("微信服务验证成功！"+echostr);
        }else {
            out.print(echostr);
            System.out.println("微信服务验证失败！"+echostr);
        }
        out.flush();
        out.close();
    }

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        //由于授权回调成功后，要返回原地址路径，原地址路径带“#”号，当前returnUrl获取带“#”的url获取不全，因此前端把“#”号替换为“guiguoa”了，这里要还原一下
        String redirectURL = wxMpService.getOAuth2Service().buildAuthorizationUrl(userInfoUrl, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl.replace("guiguoa", "#")));
        log.info("【微信网页授权】获取code,redirectURL={}", redirectURL);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws Exception {
        log.info("【微信网页授权】code={}", code);
        log.info("【微信网页授权】state={}", returnUrl);
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        String openId = accessToken.getOpenId();
        log.info("【微信网页授权】openId={}", openId);

        WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
        log.info("【微信网页授权】wxMpUser={}", JSON.toJSONString(wxMpUser));

        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOpenId, openId));
        String token = "";
        //null != sysUser 说明已经绑定，反之为建立账号绑定，去页面建立账号绑定
        if(null != sysUser) {
            token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        }
        if(returnUrl.indexOf("?") == -1) {
            return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
        } else {
            return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
        }
    }

    @ApiOperation(value = "微信账号绑定手机")
    @PostMapping("bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, bindPhoneVo.getPhone()));
        if(null != sysUser) {
            sysUser.setOpenId(bindPhoneVo.getOpenId());
            sysUserService.updateById(sysUser);

            String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
            return Result.ok(token);
        } else {
            return Result.fail("手机号码不存在，绑定失败");
        }
    }
}