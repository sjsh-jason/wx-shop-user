package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.MerchantWhitelist;
import com.wxshop.member.entity.User;
import com.wxshop.member.mapper.MerchantWhitelistMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MerchantWhitelistService {

    @Resource
    private MerchantWhitelistMapper merchantWhitelistMapper;

    public boolean isMerchant(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        LambdaQueryWrapper<MerchantWhitelist> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantWhitelist::getPhone, phone)
                .eq(MerchantWhitelist::getStatus, 1);
        return merchantWhitelistMapper.selectCount(wrapper) > 0;
    }

    public boolean isMerchantUser(User user) {
        if (user == null) {
            return false;
        }
        return isMerchant(user.getPhone());
    }
}
