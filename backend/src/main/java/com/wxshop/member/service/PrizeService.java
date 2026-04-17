package com.wxshop.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxshop.member.entity.Prize;
import com.wxshop.member.mapper.PrizeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PrizeService {

    @Resource
    private PrizeMapper prizeMapper;

    public List<Prize> getActivePrizes() {
        LambdaQueryWrapper<Prize> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Prize::getStatus, 1)
                .orderByAsc(Prize::getSortOrder);
        return prizeMapper.selectList(wrapper);
    }

    public Prize getPrizeById(Long id) {
        return prizeMapper.selectById(id);
    }

    public List<Prize> getAllPrizes() {
        LambdaQueryWrapper<Prize> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Prize::getSortOrder);
        return prizeMapper.selectList(wrapper);
    }

    public Prize createPrize(Prize prize) {
        prize.setStatus(1);
        prizeMapper.insert(prize);
        return prize;
    }

    public Prize updatePrize(Long id, Prize prize) {
        prize.setId(id);
        prizeMapper.updateById(prize);
        return prize;
    }

    public void updatePrize(Prize prize) {
        prizeMapper.updateById(prize);
    }

    public void deletePrize(Long id) {
        prizeMapper.deleteById(id);
    }
}
