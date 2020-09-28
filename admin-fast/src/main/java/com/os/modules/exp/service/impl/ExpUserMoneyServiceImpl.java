package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.exception.RRException;
import com.os.common.utils.MapUtils;
import com.os.modules.exp.dao.ExpOrderDao;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpDepDaySettleEntity;
import com.os.modules.exp.service.ExpDepDaySettleService;
import com.os.modules.exp.vo.OrderResumeVo;
import com.os.modules.sys.entity.SysUserEntity;
import com.os.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpUserMoneyDao;
import com.os.modules.exp.entity.ExpUserMoneyEntity;
import com.os.modules.exp.service.ExpUserMoneyService;


@Service("expUserMoneyService")
public class ExpUserMoneyServiceImpl extends ServiceImpl<ExpUserMoneyDao, ExpUserMoneyEntity> implements ExpUserMoneyService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ExpDepDaySettleService expDepDaySettleService;

    @Autowired
    private ExpOrderDao expOrderDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpUserMoneyEntity> page = this.page(
                new Query<ExpUserMoneyEntity>().getPage(params),
                new LambdaQueryWrapper<ExpUserMoneyEntity>()

        );

        return new PageUtils(page);
    }

    @Override
    public Boolean updateUserMoney(ExpUserMoneyEntity expUserMoney) {

        if (expUserMoney.getId() == null) {
            throw new RRException("输入参数错误");
        }

        if (expUserMoney.getMoneyAdd() == null) {
            throw new RRException("输入金额错误");
        }

        ExpUserMoneyEntity record = this.getById(expUserMoney.getId());

        record.setMoneyIn(record.getMoneyIn() + expUserMoney.getMoneyAdd());
        return saveOrUpdate(record);
    }

    @Override
    public List<ExpUserMoneyEntity> getUserMoneyList(Integer daySettleId) {
        List<ExpUserMoneyEntity> list = new ArrayList<>();
        ExpDepDaySettleEntity expDepDaySettleEntity =  expDepDaySettleService.getById(daySettleId);
        //获取所有录单员
        List<SysUserEntity> userList = sysUserService.getUserAll(5L);
        userList.stream().forEach(x->{
            ExpUserMoneyEntity expUserMoneyEntity = this.lambdaQuery().eq(ExpUserMoneyEntity::getDaySettleId, daySettleId).eq(ExpUserMoneyEntity::getUserId, x.getUserId()).one();

            if (expUserMoneyEntity == null) {
                expUserMoneyEntity = new ExpUserMoneyEntity();
                expUserMoneyEntity.setMoneyIn(0);
                expUserMoneyEntity.setMoneyAll(0);
                expUserMoneyEntity.setDeptId(expDepDaySettleEntity.getDeptId());
                expUserMoneyEntity.setDaySettleId(daySettleId);
                expUserMoneyEntity.setUserId(x.getUserId());
                expUserMoneyEntity.setDeliverDate(expDepDaySettleEntity.getDeliverDate());

                this.saveOrUpdate(expUserMoneyEntity);
            }

            expUserMoneyEntity = convertById(expUserMoneyEntity.getId());
            expUserMoneyEntity.setRealname(x.getRealname());
            list.add(expUserMoneyEntity);
        });
        return list;
    }

    private ExpUserMoneyEntity convertById(Integer id){
        ExpUserMoneyEntity record = this.getById(id);

        //根据日期、用户、网点、订单类型 获取网点已付订单总费用
        SettleDto settleDto = new SettleDto();
        settleDto.setUserId(record.getUserId());
        settleDto.setDeliverDate(record.getDeliverDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        settleDto.setDeptId(record.getDeptId());
        settleDto.setSettleCode("YF");
        OrderResumeVo orderResumeVo = expOrderDao.getOrderResume(settleDto);
        if (orderResumeVo != null) {
            record.setMoneyAll(MapUtils.oint(orderResumeVo.getFreight()));
        }
        return record;
    }
}
