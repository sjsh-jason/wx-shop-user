package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.dto.LoginResponse;
import com.wxshop.member.entity.User;
import com.wxshop.member.mapper.UserMapper;
import com.wxshop.member.util.JwtUtil;
import com.wxshop.member.util.WxUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private WxUtil wxUtil;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private DrawChanceService drawChanceService;

    @Transactional
    public LoginResponse wechatLogin(String code) {
        Map<String, String> wxResult = wxUtil.code2Session(code);

        if (wxResult.containsKey("errcode")) {
            throw new RuntimeException("微信登录失败: " + wxResult.get("errmsg"));
        }

        String openid = wxResult.get("openid");
        String unionid = wxResult.get("unionid");

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);

        boolean isNewUser = false;
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUnionid(unionid);
            user.setNickname("微信用户");
            user.setPoints(0);
            user.setLevel(1);
            user.setTotalPoints(0);
            userMapper.insert(user);
            isNewUser = true;
            // 新用户注册赠送抽奖机会
            drawChanceService.handleNewUser(user.getId());
        }

        String token = jwtUtil.generateToken(user.getId());

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getNickname(),
                user.getAvatar(),
                user.getPoints(),
                user.getLevel()
        );

        return new LoginResponse(token, userInfo);
    }

    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Transactional
    public void updateUser(Long userId, String nickname, String avatar) {
        User user = new User();
        user.setId(userId);
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        userMapper.updateById(user);
    }
}
