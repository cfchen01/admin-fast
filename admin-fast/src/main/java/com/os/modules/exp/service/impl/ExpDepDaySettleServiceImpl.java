package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.os.common.enums.OrderStatusEnum;
import com.os.common.enums.SettleStatusEnum;
import com.os.common.exception.RRException;
import com.os.modules.exp.dao.ExpOrderDao;
import com.os.modules.exp.dao.ExpUserMoneyDao;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.ExpDepartmentEntity;
import com.os.modules.exp.entity.ExpOrderEntity;
import com.os.modules.exp.service.ExpDepartmentService;
import com.os.modules.exp.service.ExpOrderService;
import com.os.modules.exp.vo.OrderResumeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    @Autowired
    private ExpOrderService expOrderService;

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

        List<ExpOrderEntity> list = expOrderService.lambdaQuery().eq(ExpOrderEntity::getDeliverDate, settleDto.getDeliverDate())
                .eq(ExpOrderEntity::getDeptId, settleDto.getDeptId())
                .in(ExpOrderEntity::getStatus, Arrays.asList(0, 1)).list();

        if (CollectionUtils.isEmpty(list)) {
            expDepDaySettleEntity.setIsNull(true);
            return expDepDaySettleEntity;
        }

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
        OrderResumeVo  mapYjAll = expOrderDao.getOrderResume(paramYJ);

        //获取提付订单已收费用
        paramYJ.setStatus(1);
        OrderResumeVo  mapYjIn = expOrderDao.getOrderResume(paramYJ);

        //获取提付订单总费用
        SettleDto paramTF = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramTF);
        paramTF.setSettleCode("TF");
        OrderResumeVo  mapTFAll = expOrderDao.getOrderResume(paramTF);

        //获取提付订单已收费用
        paramTF.setStatus(1);
        OrderResumeVo  mapTFIn = expOrderDao.getOrderResume(paramTF);

        //获取回单订单总费用
        SettleDto paramHD = new SettleDto();
        BeanUtils.copyProperties(settleDto, paramHD);
        paramHD.setSettleCode("HD");
        OrderResumeVo  mapHDAll = expOrderDao.getOrderResume(paramHD);

        //获取提付订单已收费用
        paramHD.setStatus(1);
        OrderResumeVo  mapHDIn = expOrderDao.getOrderResume(paramHD);


        //已付订单总费用(运费加送货费)
        Integer paidMoney = 0;
        //已付订单已收费用
        Integer paidMoneyIn = 0;
        //提付订单总费用(运费加垫费)
        Integer arrivalMoney = 0;
        //提付订单已收费用(已确认的订单总费用)
        Integer advanceIn = 0;
        //月结订单总费用(运费加送货费)
        Integer monthMoney = 0;
        //月结订单已收费用(已确认的订单总费用)
        Integer monthMoneyIn = 0;
        //回单订单总费用(运费加送货费)
        Integer receiptMoney = 0;
        //回单订单已收费用(已确认的订单总费用)
        Integer receiptMoneyIn = 0;

        if (Objects.nonNull(mapYF)) {
            Integer freight = MapUtils.oint(mapYF.getFreight());
            Integer delivery = MapUtils.oint(mapYF.getDelivery());
            paidMoney = freight;
        }
        if (org.apache.commons.collections.MapUtils.isNotEmpty(mapYFIN)) {
            paidMoneyIn = MapUtils.mint(mapYFIN, "moneyIn", 0);
        }
        if (Objects.nonNull(mapYjAll)) {
            Integer freight = MapUtils.oint(mapYjAll.getFreight());
            Integer delivery = MapUtils.oint(mapYjAll.getDelivery());
            monthMoney = freight;
        }
        if (Objects.nonNull(mapYjIn)) {
            Integer freight = MapUtils.oint(mapYjIn.getFreight());
            Integer delivery = MapUtils.oint(mapYjIn.getDelivery());
            monthMoneyIn = freight;
        }
        if (Objects.nonNull(mapTFAll)) {
            Integer freight = MapUtils.oint(mapTFAll.getFreight());
            Integer delivery = MapUtils.oint(mapTFAll.getDelivery());
            Integer advance = MapUtils.oint(mapTFAll.getAdvance());
            arrivalMoney = freight + advance;
        }
        if (Objects.nonNull(mapTFIn)) {
            Integer freight = MapUtils.oint(mapTFIn.getFreight());
            Integer delivery = MapUtils.oint(mapTFIn.getDelivery());
            Integer advance = MapUtils.oint(mapTFIn.getAdvance());
            advanceIn = MapUtils.oint(freight + advance);
        }
        if (Objects.nonNull(mapHDAll)) {
            Integer freight = MapUtils.oint(mapHDAll.getFreight());
            receiptMoney = freight;
        }
        if (Objects.nonNull(mapHDIn)) {
            Integer freight = MapUtils.oint(mapHDIn.getFreight());
            receiptMoneyIn = freight;
        }
        //提付订单
        expDepDaySettleEntity.setArrivalMoney(arrivalMoney);
        expDepDaySettleEntity.setArrivalMoneyIn(advanceIn);
        //月结订单
        expDepDaySettleEntity.setMonthMoney(monthMoney);
        expDepDaySettleEntity.setMonthMoneyIn(monthMoneyIn);
        //已付订单
        expDepDaySettleEntity.setPaidMoney(paidMoney);
        expDepDaySettleEntity.setPaidMoneyIn(paidMoneyIn);
        //回单订单
        expDepDaySettleEntity.setReceiptMoney(receiptMoney);
        expDepDaySettleEntity.setReceiptMoneyIn(receiptMoneyIn);

        return expDepDaySettleEntity;
    }

    @Override
    public Boolean updateSettle(Integer id) {
        if (checkSettle(id)) {
            ExpDepDaySettleEntity expDepDaySettleEntity = this.getById(id);
            expDepDaySettleEntity.setStatus(SettleStatusEnum.STATUS_PASS.getCode());
            //根据日期和网点di获取网点运费
            SettleDto param = new SettleDto();
            param.setDeliverDate(expDepDaySettleEntity.getDeliverDate().toString());
            param.setDeptId(expDepDaySettleEntity.getDeptId());
            OrderResumeVo orderResumeVo = expOrderDao.getOrderResume(param);
            if (orderResumeVo != null) {
                expDepDaySettleEntity.setIncome(MapUtils.oint(orderResumeVo.getFreight()));
            }
            this.updateById(expDepDaySettleEntity);
        }
        return false;
    }

    /**
     * 校验是否可以核算
     * @param id
     * @return
     */
    private Boolean checkSettle(Integer id){

        ExpDepDaySettleEntity expDepDaySettleEntity = this.getById(id);
        if (SettleStatusEnum.STATUS_PASS.getCode().equals(expDepDaySettleEntity.getStatus())) {
            throw new RRException("当日账单已结算");
        }

        if(!LocalDate.now().isAfter(expDepDaySettleEntity.getDeliverDate())) {
            throw new RRException("只允许结算今日之前的时间");
        }

        List<ExpOrderEntity> entityList = expOrderService.lambdaQuery()
                .eq(ExpOrderEntity::getDeliverDate, expDepDaySettleEntity.getDeliverDate())
                .eq(ExpOrderEntity::getDeptId, expDepDaySettleEntity.getDeptId())
                .eq(ExpOrderEntity::getStatus, OrderStatusEnum.STATUS_NONE.getCode()).list();

        if (!CollectionUtils.isEmpty(entityList)) {
            throw new RRException("当日网点订单尚未确认完，不允许结算");
        }

        SettleDto settleDto = new SettleDto();
        settleDto.setDeliverDate(expDepDaySettleEntity.getDeliverDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        settleDto.setDeptId(expDepDaySettleEntity.getDeptId());

        expDepDaySettleEntity = this.getDeptSettle(settleDto);

        if (!expDepDaySettleEntity.getPaidMoneyIn().equals(expDepDaySettleEntity.getPaidMoney())) {
            throw new RRException("当日已付订单费用尚未收齐，不允许结算");
        }

        if (!expDepDaySettleEntity.getComMoneyIn().equals(expDepDaySettleEntity.getArrivalMoney())) {
            throw new RRException("当日提付订单费用尚未收齐，不允许结算");
        }

        return true;
    }
}
