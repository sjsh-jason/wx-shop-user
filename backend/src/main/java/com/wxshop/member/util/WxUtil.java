package com.wxshop.member.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wxshop.member.config.WxConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class WxUtil {

    @Resource
    private WxConfig wxConfig;

    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    public Map<String, String> code2Session(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", wxConfig.getAppid());
        params.put("secret", wxConfig.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");

        String response = HttpUtil.get(CODE2SESSION_URL, params);
        JSONObject json = JSONUtil.parseObj(response);

        Map<String, String> result = new HashMap<>();
        if (json.containsKey("openid")) {
            result.put("openid", json.getStr("openid"));
            result.put("sessionKey", json.getStr("session_key"));
            if (json.containsKey("unionid")) {
                result.put("unionid", json.getStr("unionid"));
            }
        } else {
            result.put("errcode", json.getStr("errcode"));
            result.put("errmsg", json.getStr("errmsg"));
        }
        return result;
    }
}
