package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.enums.SettleStatusEnum;
import com.os.common.exception.RRException;
import com.os.modules.exp.dao.ExpOrderDao;
import com.os.modules.exp.dao.ExpUserMoneyDao;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpDepartmentEntity;
import com.os.modules.exp.service.ExpDepartmentService;
import com.os.modules.exp.vo.OrderResumeVo;
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
import java.util.Objects;

import com.os.common.utils.MapUtils;
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
    public ExpDepDaySettleEntity getByDeliverDate(LocalDate deliverDate, Integer deptId) {
        return this.lambdaQuery().eq(ExpDepDaySettleEntity::getDeliverDate, deliverDate).eq(ExpDepDaySettleEntity::getDeptId, deptId).one();
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

                        //根据日期和网点di获取网点运费
                        SettleDto param = new SettleDto();
                        param.setDeliverDate(settleDto.getDeliverDate());
                        param.setDeptId(x.getId());
                        OrderResumeVo orderResumeVo = expOrderDao.getOrderResume(param);
                        if (orderResumeVo != null) {
                            expDepDaySettleEntity.setIncome(MapUtils.oint(orderResumeVo.getFreight()));
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

    @Override
    public Boolean updateComMoneyIn(Map<String, Object> params) {

        String dateStr = MapUtils.mstr(params, "deliverDate");
        String expDesc = MapUtils.mstr(params, "expDesc");
        String moneyStr = MapUtils.mstr(params, "money");
        Integer deptId = MapUtils.mint(params, "deptId");
        Integer money = 0;

        if (StringUtils.isEmpty(dateStr) || deptId == null) {
            throw new RRException("输入参数错误");
        }
        try {
            money = Integer.parseInt(moneyStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("输入金额错误");
        }
        LocalDate deliverDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ExpDepDaySettleEntity expDepDaySettleEntity = this.getByDeliverDate(deliverDate, deptId);
        if (SettleStatusEnum.STATUS_PASS.getCode().equals(expDepDaySettleEntity.getStatus())) {
            throw new RRException("当前日期已结算，不允许修改");
        }

        Integer count = expDepDaySettleEntity.getComMoneyIn() == null?0:expDepDaySettleEntity.getComMoneyIn();
        expDepDaySettleEntity.setComMoneyIn(count + money);
        //TODO 添加明细
        return this.saveOrUpdate(expDepDaySettleEntity);
    }

    @Override
    public ExpDepDaySettleEntity getDeptSettle(SettleDto settleDto) {
        LocalDate deliverDate = LocalDate.parse(settleDto.getDeliverDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ExpDepDaySettleEntity expDepDaySettleEntity = this.getByDeliverDate(deliverDate, settleDto.getDeptId());

        //获取已付订单总费用
        SettleDto paramYF = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramYF);
        paramYF.setSettleCode("YF");
        OrderResumeVo  mapYF = expOrderDao.getOrderResume(paramYF);

        //获取已付订单已收费用
        Map<String, Object>  mapYFIN = expUserMoneyDao.getUserSettle(settleDto);

        //获取月结订单总费用
        SettleDto paramYJ = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramYJ);
        paramYJ.setSettleCode("YJ");
        OrderResumeVo  mapYj = expOrderDao.getOrderResume(paramYJ);

        //获取提付订单总费用
        SettleDto paramTF = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramTF);
        paramTF.setSettleCode("TF");
        OrderResumeVo  mapTFAll = expOrderDao.getOrderResume(paramTF);

        //获取提付订单已收费用
        paramTF.setStatus(1);
        OrderResumeVo  mapTFIn = expOrderDao.getOrderResume(paramTF);


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


        if (Objects.nonNull(mapYF)) {
            Integer freight = MapUtils.oint(mapYF.getFreight());
            Integer delivery = MapUtils.oint(mapYF.getDelivery());
            paidMoney = freight;
        }
        if (org.apache.commons.collections.MapUtils.isNotEmpty(mapYFIN)) {
            paidMoneyIn = MapUtils.mint(mapYFIN, "moneyIn", 0);
        }
        if (Objects.nonNull(mapYj)) {
            Integer freight = MapUtils.oint(mapYj.getFreight());
            Integer delivery = MapUtils.oint(mapYj.getDelivery());
            monthMoney = freight;
        }
        if (Objects.nonNull(mapTFAll)) {
            Integer freight = MapUtils.oint(mapTFAll.getFreight());
            Integer delivery = MapUtils.oint(mapTFAll.getDelivery());
            Integer advance = MapUtils.oint(mapTFAll.getAdvance());
            arrivalMoney = freight + advance;
        }
        if (Objects.nonNull(mapTFIn)) {
            advanceIn = MapUtils.oint(mapTFIn.getAdvanceIn());
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

    @Override
    public Boolean updateSettle(Integer id) {
        return null;
    }
}
