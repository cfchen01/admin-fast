package com.os.modules.exp.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.os.common.enums.OrderStatusEnum;
import com.os.common.enums.SettleStatusEnum;
import com.os.common.exception.RRException;
import com.os.common.utils.*;
import com.os.modules.exp.dao.ExpFileDao;
import com.os.modules.exp.dao.ExpOrderPicDao;
import com.os.modules.exp.dto.SettleDto;
import com.os.modules.exp.entity.*;
import com.os.modules.exp.service.*;
import com.os.modules.exp.vo.OrderObjVo;
import com.os.modules.exp.vo.OrderResumeVo;
import com.os.modules.sys.dao.SysUserDao;
import com.os.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.os.modules.exp.dao.ExpOrderDao;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.stream.Collectors.toList;


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
        //日期小于10位位按月查询
        String deliverDate = MapUtils.mstr(params, "deliverDate");
        if (StringUtils.isNotEmpty(deliverDate) && deliverDate.length()<10) {
            params.put("sDate", DateUtils.getFirstDayOfMonth(deliverDate).toString());
            params.put("eDate", DateUtils.getLastDayOfMonth(deliverDate).toString());
            params.remove("deliverDate");
        }

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

    @Override
    public void download(HttpServletRequest request, HttpServletResponse response){
        String status = request.getParameter("status");
        String deptId = request.getParameter("deptId");
        String settleCode = (String) request.getParameter("settleCode");
        String deliverDate = (String) request.getParameter("deliverDate");
        if (StringUtils.isEmpty(deliverDate)) {
            throw  new RRException("日期参数错误");
        }
        ExpDepartmentEntity expDepartmentEntity = null;
        if (!StringUtils.isEmpty(deptId)) {
            expDepartmentEntity = expDepartmentService.getById(deptId);
        }

        List<String> heads = new ArrayList<String>();
        heads.add("订单号");
        heads.add("订单码");
        heads.add("收货人电话");
        heads.add("货物名称");
        heads.add("件数");
        heads.add("包装");
        heads.add("订单类型");
        heads.add("运费");
        heads.add("垫费");
        heads.add("送货费");
        heads.add("备注");
        heads.add("客户签名");
        String condition = "";
        String name = "["+deliverDate+"]-货物订单";
        if (expDepartmentEntity != null) {
            condition = "网点：" + expDepartmentEntity.getDeptName() + "；\n";
        }
        condition = condition + "制表人：" + UserUtils.getUser().getRealname();

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        Map<String, Object> param = new HashMap<>();
        param.put("status", status);
        param.put("deptId", deptId);
        param.put("settleCode", settleCode);
        param.put("deliverDate", deliverDate);
        List<ExpOrderEntity> expOrderEntityList = baseMapper.selectOrderPage(param);
        if (!CollectionUtils.isEmpty(expOrderEntityList)) {
            list = expOrderEntityList.stream().map(x -> {
                Map<String, Object> map = new HashMap<>();
                map.put("订单号", x.getId());
                map.put("订单码", x.getOrdCode());
                map.put("收货人电话", x.getReceiverTel());
                map.put("货物名称", x.getGoodsName());
                map.put("件数", x.getOrdNum());
                map.put("包装", x.getPackingName());
                map.put("订单类型", x.getSettleName());
                map.put("运费", x.getFreight());
                map.put("垫费", x.getAdvance());
                map.put("送货费", x.getDelivery());
                map.put("备注", x.getRemark());
                map.put("客户签名", "");
                return map;
            }).collect(toList());
        }

        try {
            ExportExcel.download(request, response,list,heads,name,condition, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RRException("订单导出失败");
        }
    }

    private Page getPager(Map<String, Object> params){
        long no = params.containsKey("page") ? Long.valueOf(params.get("page").toString()) : 1;
        long limit = params.containsKey("limit") ? Long.valueOf(params.get("limit").toString()) : 10;
        Page pager = new Page(no, limit);
        return pager;
    }

    @Override
    public ExpOrderEntity getDetailById(String id) {
        ExpOrderEntity expOrderEntity = this.lambdaQuery()
                .eq(ExpOrderEntity::getId, id).or()
                .eq(ExpOrderEntity::getReceiverTel, id)
                .last("limit 1")
                .one();
        if (expOrderEntity == null) {
            return null;
        }
        List<ExpFileEntity> list = expFileDao.selectPicByOrder(expOrderEntity.getId());
        SysUserEntity sysUserEntity = sysUserDao.selectById(expOrderEntity.getUserId());
        expOrderEntity.setFileList(list);
        expOrderEntity.setRealname(sysUserEntity==null?"/":sysUserEntity.getRealname());
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
        ExpDepDaySettleEntity expDepDaySettleEntity = expDepDaySettleService.getByDeliverDate(expOrderEntity.getDeliverDate(), expOrderEntity.getDeptId());
        if (expDepDaySettleEntity != null && SettleStatusEnum.STATUS_PASS.getCode().equals(expDepDaySettleEntity.getStatus())) {
            throw  new RRException("[" + expOrderEntity.getDeliverDate() + "]该网点已结算，不允许修改");
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
    public OrderResumeVo getResume(Map<String, Object> params) {
        String deliverDate = MapUtils.mstr(params, "deliverDate");
        //日期是月不查询
        if (StringUtils.isNotEmpty(deliverDate) && deliverDate.length()<10) {
            return null;
        }
        SettleDto settleDto = new SettleDto();
        settleDto.setDeliverDate(deliverDate);
        settleDto.setStatus(MapUtils.mint(params, "status"));
        settleDto.setSettleCode(MapUtils.mstr(params, "settleCode"));
        SysUserEntity entity = sysUserDao.queryUserDetail(UserUtils.getUserId());
        //网点角色只能看到当前网点订单
        if (3 == entity.getRoleId()) {
            settleDto.setDeptId(entity.getDeptId());
        }
        //业务员只能查看自己的订单
        if (5 == entity.getRoleId()) {
            settleDto.setUserId(entity.getUserId());
        }
        if (4 == entity.getRoleId()) {
            String userId = MapUtils.mstr(params, "userId");
            if (!StringUtils.isEmpty(userId)) {
                settleDto.setUserId(Long.parseLong(userId));
            }
        }

        OrderResumeVo vo = baseMapper.getOrderResume(settleDto);
        return vo;
    }

    private String convertStatus(Integer status){
        if (0 == status) {
            return "未确认";
        }
        if (1 == status) {
            return "已确认";
        }
        if (2 == status) {
            return "返回";
        }
        if (3 == status) {
            return "作废";
        }
        return "";
    }
}
