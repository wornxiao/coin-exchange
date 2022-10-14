package com.worn.xiao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.worn.xiao.domain.Coin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CoinService extends IService<Coin>{



    /**
     * 数字货币的条件分页查询
     * @param name
     *   数字货币的名称
     * @param type
     *  数字货币类型的名称
     * @param status
     * 数字货币的状态
     * @param title
     * 字货币的标题
     * @param walletType
     * 树字货币的钱包类型名称
     * @param page
     * 分页参数
     * @return
     * 数据货币的分页数据
     */
    Page<Coin> findByPage(String name, String type, Byte status, String title, String walletType, Page<Coin> page);

    /**
     * 使用币种的状态查询所有的币种信息
     * @param status
     * @return
     */
    List<Coin> getCoinsByStatus(Byte status);
}
