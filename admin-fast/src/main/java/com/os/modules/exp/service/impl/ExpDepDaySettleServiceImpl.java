package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.enums.SettleStatusEnum;
import com.os.modules.exp.dao.ExpOrderDao;
import com.os.modules.exp.dao.ExpUserMoneyDao;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpDepartmentEntity;
import com.os.modules.exp.service.ExpDepartmentService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpDepDaySettleDao;
import com.os.modules.exp.entity.ExpDepDaySettleEntity;
import com.os.modules.exp.service.ExpDepDaySettleService;


@Service("expDepDaySettleService")
public class ExpDepDaySettleServiceImpl extends ServiceImpl<ExpDepDaySettleDao, ExpDepDaySettleEntity> implements ExpDepDaySettleService {

    @Autowired
    private ExpDepartmentService expDepartmentService;

    @Autowired
    private ExpOrderDao expOrderDao;

    @Autowired
    private ExpUserMoneyDao expUserMoneyDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExpDepDaySettleEntity> page = this.page(
                new Query<ExpDepDaySettleEntity>().getPage(params),
                new LambdaQueryWrapper<ExpDepDaySettleEntity>()

        );

        return new PageUtils(page);
    }

    @Override
    public ExpDepDaySettleEntity getByDeliverDate(LocalDate deliverDate) {
        return this.lambdaQuery().eq(ExpDepDaySettleEntity::getDeliverDate, deliverDate).one();
    }

    @Override
    public List<ExpDepDaySettleEntity> getSettleList(SettleDto settleDto) {
        List<ExpDepartmentEntity> deptList = expDepartmentService.lambdaQuery().list();
        List<ExpDepDaySettleEntity> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deptList)) {
            deptList.stream().forEach(x->{
                if (settleDto.getType() == 0) {
                    ExpDepDaySettleEntity expDepDaySettleEntity = this.lambdaQuery()
                            .eq(ExpDepDaySettleEntity::getDeliverDate, settleDto.getDeliverDate())
                            .eq(ExpDepDaySettleEntity::getDeptId, x.getId())
                            .one();
                    if (expDepDaySettleEntity == null) {
                        expDepDaySettleEntity = new ExpDepDaySettleEntity();
                        expDepDaySettleEntity.setDeliverDate(LocalDate.parse(settleDto.getDeliverDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        expDepDaySettleEntity.setDeliverMonth(settleDto.getDeliverDate().substring(0, 7));
                        expDepDaySettleEntity.setStatus(SettleStatusEnum.STATUS_NONE.getCode());
                        expDepDaySettleEntity.setPaidMoney(0);
                        expDepDaySettleEntity.setPaidMoneyIn(0);
                        expDepDaySettleEntity.setArrivalMoneyIn(0);
                        expDepDaySettleEntity.setArrivalMoney(0);
                        expDepDaySettleEntity.setComMoneyIn(0);
                        expDepDaySettleEntity.setMonthMoney(0);
                        expDepDaySettleEntity.setMonthMoneyIn(0);
                        expDepDaySettleEntity.setIncome(0);
                        expDepDaySettleEntity.setDeptId(x.getId());

                        this.save(expDepDaySettleEntity);

                    }

                    if (SettleStatusEnum.STATUS_PASS.getCode().equals(expDepDaySettleEntity.getStatus())) {
                        expDepDaySettleEntity.setDeptName(x.getDeptName());
                        list.add(expDepDaySettleEntity);
                    } else {

                        SettleDto param = new SettleDto();
                        param.setDeliverDate(settleDto.getDeliverDate());
                        param.setDeptId(x.getId());
                        Map<String, Object>  map = expOrderDao.getOrderResume(param);
                        if (org.apache.commons.collections.MapUtils.isNotEmpty(map)) {
                            Integer freight = MapUtils.mint(map, "freight") == null?0:MapUtils.mint(map, "freight");
                            expDepDaySettleEntity.setIncome(freight);
                        }

                        expDepDaySettleEntity.setDeptName(x.getDeptName());
                        list.add(expDepDaySettleEntity);
                    }
                } else {
                    SettleDto param = new SettleDto();
                    param.setDeliverDate(settleDto.getDeliverDate());
                    param.setDeptId(x.getId());
                    ExpDepDaySettleEntity expDepDaySettleEntity = baseMapper.getDeptMonthSettle(param);
                    if (expDepDaySettleEntity == null) {
                        expDepDaySettleEntity = new ExpDepDaySettleEntity();
                        expDepDaySettleEntity.setDeliverMonth(settleDto.getDeliverDate());
                        expDepDaySettleEntity.setStatus(SettleStatusEnum.STATUS_NONE.getCode());
                    }
                    expDepDaySettleEntity.setDeptName(x.getDeptName());
                    list.add(expDepDaySettleEntity);
                }
            });
        }
        return list;
    }

    private ExpDepDaySettleEntity getDeptSetle(SettleDto settleDto){
        ExpDepDaySettleEntity expDepDaySettleEntity = this.lambdaQuery().eq(ExpDepDaySettleEntity::getDeliverDate, settleDto.getDeliverDate()).one();

        //获取已付订单总费用
        SettleDto paramYF = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramYF);
        paramYF.setSettleCode("YF");
        Map<String, Object>  mapYF = expOrderDao.getOrderResume(paramYF);

        //获取已付订单已收费用
        Map<String, Object>  mapYFIN = expUserMoneyDao.getUserSettle(settleDto);

        //获取月结订单总费用
        SettleDto paramYJ = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramYJ);
        paramYJ.setSettleCode("YJ");
        Map<String, Object>  mapYj = expOrderDao.getOrderResume(paramYJ);

        //获取提付订单总费用
        SettleDto paramTF = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramTF);
        paramTF.setSettleCode("TF");
        Map<String, Object>  mapTF = expOrderDao.getOrderResume(paramTF);


        //已付订单总费用(运费加送货费)
        Integer paidMoney = 0;
        //已付订单已收费用
        Integer paidMoneyIn = 0;
        //提付订单总费用(运费加送货费加垫费)
        Integer arrivalMoney = 0;
        //提付订单已收费用
        Integer advanceIn = 0;
        //月结订单总费用(运费加送货费)
        Integer monthMoney = 0;


        if (org.apache.commons.collections.MapUtils.isNotEmpty(mapYF)) {
            Integer freight = MapUtils.mint(mapYF, "freight") == null?0:MapUtils.mint(mapYF, "freight");
            Integer delivery = MapUtils.mint(mapYF, "delivery") == null?0:MapUtils.mint(mapYF, "delivery");
            paidMoney = freight + delivery;
        }
        if (org.apache.commons.collections.MapUtils.isNotEmpty(mapYFIN)) {
            paidMoneyIn = MapUtils.mint(mapYFIN, "moneyIn") == null?0:MapUtils.mint(mapYFIN, "moneyIn");
        }
        if (org.apache.commons.collections.MapUtils.isNotEmpty(mapYj)) {
            Integer freight = MapUtils.mint(mapYj, "freight") == null?0:MapUtils.mint(mapYj, "freight");
            Integer delivery = MapUtils.mint(mapYj, "delivery") == null?0:MapUtils.mint(mapYj, "delivery");
            monthMoney = freight + delivery;
        }
        if (org.apache.commons.collections.MapUtils.isNotEmpty(mapTF)) {
            Integer freight = MapUtils.mint(mapTF, "freight") == null?0:MapUtils.mint(mapTF, "freight");
            Integer delivery = MapUtils.mint(mapTF, "delivery") == null?0:MapUtils.mint(mapTF, "delivery");
            Integer advance = MapUtils.mint(mapTF, "advance") == null?0:MapUtils.mint(mapTF, "advance");
            advanceIn = MapUtils.mint(mapTF, "advanceIn") == null?0:MapUtils.mint(mapTF, "advanceIn");
            arrivalMoney = freight + delivery + advance;
        }
        //提付订单
        expDepDaySettleEntity.setArrivalMoney(arrivalMoney);
        expDepDaySettleEntity.setArrivalMoneyIn(advanceIn);
        //月结订单
        expDepDaySettleEntity.setMonthMoney(monthMoney);
        //已付订单
        expDepDaySettleEntity.setPaidMoney(paidMoney);
        expDepDaySettleEntity.setPaidMoneyIn(paidMoneyIn);
        return expDepDaySettleEntity;
    }
}
