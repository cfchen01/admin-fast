package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.os.common.enums.OrderStatusEnum;
import com.os.common.enums.SettleStatusEnum;
import com.os.common.exception.RRException;
import com.os.common.utils.UserUtils;
import com.os.modules.exp.dao.ExpFileDao;
import com.os.modules.exp.dao.ExpOrderPicDao;
import com.os.modules.exp.entity.*;
import com.os.modules.exp.service.*;
import com.os.modules.exp.vo.OrderObjVo;
import com.os.modules.sys.dao.SysUserDao;
import com.os.modules.sys.entity.SysUserEntity;
import com.os.modules.sys.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.os.common.utils.MapUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.os.common.utils.PageUtils;
import com.os.common.utils.Query;

import com.os.modules.exp.dao.ExpOrderDao;
import org.springframework.transaction.annotation.Transactional;


@Service("expOrderService")
public class ExpOrderServiceImpl extends ServiceImpl<ExpOrderDao, ExpOrderEntity> implements ExpOrderService {

    @Autowired
    private ExpComDaySettleService expComDaySettleService;

    @Autowired
    private ExpDepDaySettleService expDepDaySettleService;

    @Autowired
    private ExpGoodsService goodsService;
    @Autowired
    private ExpSettleService expSettleService;
    @Autowired
    private ExpDepartmentService expDepartmentService;
    @Autowired
    private ExpPackingService expPackingService;

    @Autowired
    private ExpOrderPicService expOrderPicService;
    @Autowired
    private ExpFileDao expFileDao;
    @Autowired
    private ExpOrderPicDao expOrderPicDao;
    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

//        baseMapper.selectOrderPage(params);
//        IPage<ExpOrderEntity> page = this.page(
//                new Query<ExpOrderEntity>().getPage(params),
//                new LambdaQueryWrapper<ExpOrderEntity>()
//                    .eq(MapUtils.mint(params, "settleId") != null, ExpOrderEntity::getSettleId, MapUtils.mint(params, "settleId"))
//                    .eq(MapUtils.mint(params, "status") != null, ExpOrderEntity::getStatus, MapUtils.mint(params, "status"))
//                    .eq(StringUtils.isNotEmpty(MapUtils.mstr(params, "deliverDate")), ExpOrderEntity::getDeliverDate, MapUtils.mstr(params, "deliverDate"))
//
//        );
        SysUserEntity entity = sysUserDao.queryUserDetail(UserUtils.getUserId());
        //网点角色只能看到当前网点订单
        if (3 == entity.getRoleId()) {
            params.put("deptId", entity.getDeptId());
        }
        //业务员只能查看自己的订单
        if (5 == entity.getRoleId()) {
            params.put("userId", entity.getUserId());
        }
        IPage<ExpOrderEntity> page = baseMapper.selectOrderPage(getPager(params), params);
        return new PageUtils(page);
    }

    private Page getPager(Map<String, Object> params){
        long no = params.containsKey("page") ? Long.valueOf(params.get("page").toString()) : 1;
        long limit = params.containsKey("limit") ? Long.valueOf(params.get("limit").toString()) : 10;
        Page pager = new Page(no, limit);
        return pager;
    }

    @Override
    public ExpOrderEntity getDetailById(Integer id) {
        ExpOrderEntity expOrderEntity = this.getById(id);
        List<ExpFileEntity> list = expFileDao.selectPicByOrder(id);
        expOrderEntity.setFileList(list);
        return expOrderEntity;
    }

    @Transactional
    @Override
    public Boolean saveOrder(ExpOrderEntity expOrderEntity) {
        expOrderEntity.setUserId(UserUtils.getUserId());
        expOrderEntity.setStatus(OrderStatusEnum.STATUS_NONE.getCode());
        expOrderEntity.setCreateTime(new Date());
        expOrderEntity.setAdvanceIn(0);

        this.save(expOrderEntity);

        if (CollectionUtils.isNotEmpty(expOrderEntity.getFileList())) {
            ExpOrderPicEntity expOrderPicEntity = new ExpOrderPicEntity();
            expOrderEntity.getFileList().stream().forEach(x->{
                expOrderPicEntity.setFileId(x.getId());
                expOrderPicEntity.setOrderId(expOrderEntity.getId());
                expOrderPicService.save(expOrderPicEntity);
            });
        }

        return true;
    }

    @Override
    public Boolean updateOrder(ExpOrderEntity expOrderEntity) {

        //公司当日已结算不允许修改
        ExpComDaySettleEntity expComDaySettleEntity = expComDaySettleService.getByDeliverDate(expOrderEntity.getDeliverDate());
        if (expComDaySettleEntity != null && SettleStatusEnum.STATUS_PASS.getCode().equals(expComDaySettleEntity.getStatus())) {
            throw  new RRException("[" + expOrderEntity.getDeliverDate() + "]财务已结算，不允许修改");
        }
        //当前网点时间已结算不允许修改
        ExpDepDaySettleEntity expDepDaySettleEntity = expDepDaySettleService.getByDeliverDate(expOrderEntity.getDeliverDate());
        if (expDepDaySettleEntity != null && SettleStatusEnum.STATUS_PASS.getCode().equals(expDepDaySettleEntity.getStatus())
        && expDepDaySettleEntity.getDeptId().equals(expOrderEntity.getDeptId())) {
            throw  new RRException("[" + expOrderEntity.getDeliverDate() + "]网点已结算，不允许修改");
        }
        //非未确认订单不允许修改
        if (!OrderStatusEnum.STATUS_NONE.getCode().equals(expOrderEntity.getStatus())) {
            throw  new RRException("当前订单状态不允许修改");
        }
        ExpOrderEntity record = this.getById(expOrderEntity.getId());
        if (!record.getOrdCode().equals(expOrderEntity.getOrdCode())) {
            throw  new RRException("订单码[" + expOrderEntity.getOrdCode() + "]已存在");
        }

        Map<String, Object> map = new HashMap();
        map.put("order_id", expOrderEntity.getId());
        expOrderPicDao.deleteByMap(map);
//        expOrderPicService.remove(expOrderPicService.lambdaQuery().eq(ExpOrderPicEntity::getOrderId, expOrderEntity.getId()));

        if (CollectionUtils.isNotEmpty(expOrderEntity.getFileList())) {
            ExpOrderPicEntity expOrderPicEntity = new ExpOrderPicEntity();
            expOrderEntity.getFileList().stream().forEach(x->{
                expOrderPicEntity.setId(null);
                expOrderPicEntity.setFileId(x.getId());
                expOrderPicEntity.setOrderId(expOrderEntity.getId());
                expOrderPicService.saveOrUpdate(expOrderPicEntity);
            });
        }

        return this.updateById(expOrderEntity);
    }

    @Override
    public ExpOrderEntity getByCode(String ordCode) {
        return this.lambdaQuery().eq(ExpOrderEntity::getOrdCode, ordCode).one();
    }

    @Override
    public Integer getUserMoneyInTf(ExpUserMoneyEntity expUserMoneyEntity) {
        return null;
    }

    @Override
    public OrderObjVo getOrderObjVo() {
        OrderObjVo orderObjVo = new OrderObjVo();
        orderObjVo.setGoodsList(goodsService.list());
        orderObjVo.setDeptList(expDepartmentService.list());
        orderObjVo.setPackingList(expPackingService.list());
        orderObjVo.setSettleList(expSettleService.list());
        return orderObjVo;
    }

    @Override
    public Boolean updateStatus(Map<String, Object> params) {
        Integer status = MapUtils.mint(params, "status");
        Integer id = MapUtils.mint(params, "id");
        if (status == null || id == null) {
            throw new RRException("输入参数有误");
        }
        ExpOrderEntity record = getById(id);
        if (!OrderStatusEnum.STATUS_NONE.getCode().equals(record.getStatus())) {
            throw new RRException("当前状态不允许修改");
        }
        record.setStatus(status);
        return this.updateById(record);
    }

    @Override
    public Integer getResume(Map<String, Object> params) {
        return baseMapper.getResume(params);
    }
}
