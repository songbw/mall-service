package com.fengchao.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.*;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.feign.EquityServiceClient;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.mapper.ShoppingCartMapper;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.model.OrderDetail;
import com.fengchao.order.model.ShoppingCart;
import com.fengchao.order.service.ShoppingCartService;
import com.fengchao.order.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper mapper;
    @Autowired
    private EquityServiceClient equityService;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private ProductService productService;

    @Override
    public OperaResult add(ShoppingCart bean) {
        OperaResult result = new OperaResult();
        if (StringUtils.isEmpty(bean.getOpenId())) {
            result.setCode(4000002);
            result.setMsg("openId不能为空");
            return result ;
        }
        if (StringUtils.isEmpty(bean.getMpu())) {
            result.setCode(4000002);
            result.setMsg("mpu不能为空");
            return result ;
        }
        // 验证MPU是否存在
        AoyiProdIndex aoyiProdIndex = findProductByMpu(bean.getMpu(), bean.getAppId()) ;
        if (aoyiProdIndex == null || aoyiProdIndex.getMpu() == null) {
            result.setCode(4000002);
            result.setMsg(bean.getMpu() + " 商品不存在");
            return result ;
        }
        ShoppingCart temp = mapper.selectByOpenIdAndSku(bean) ;
        int perLimit = findPromotionBySku(bean.getMpu(), bean.getOpenId(), bean.getAppId()) ;
        if (perLimit != -1) {
            if (perLimit <= bean.getCount()) {
                result.setCode(4000001);
                result.setMsg("商品超过限购数量，无法添加。");
                result.getData().put("mpu", bean.getMpu()) ;
                return result;
            }
        }
        Date date = new Date() ;
        if (temp == null) {
            bean.setCreatedAt(date);
            bean.setUpdatedAt(date);
            if (bean.getCount() == null) {
                bean.setCount(1);
            }
            mapper.insert(bean) ;
            result.getData().put("result", bean.getId()) ;
            return result ;
        } else {
            if (bean.getCount() == null) {
                temp.setCount(temp.getCount() + 1);
            } else {
                temp.setCount(temp.getCount() + bean.getCount());
            }

            temp.setUpdatedAt(date);
            mapper.updateNumById(temp);
            result.getData().put("result", bean.getId()) ;
            return result;
        }
    }

    private AoyiProdIndex findProductByMpu(String mpu, String appId) {
        OperaResult productResult = productService.find(mpu, appId) ;
        if (productResult.getCode() == 200) {
            Map<String, Object> data = productResult.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            if (StringUtils.isEmpty(jsonString)) {
                return null;
            }
            AoyiProdIndex aoyiProdIndex = JSONObject.parseObject(jsonString, AoyiProdIndex.class) ;
            return aoyiProdIndex;
        }
        return null;
    }

    @Override
    public Integer delete(Integer id) {
        return mapper.updateIsDelById(id);
    }

    @Override
    public OperaResult modifyNum(ShoppingCart bean) {
        log.info("修改购物车商品数量，入参：{}", JSONUtil.toJsonString(bean));
        OperaResult result = new OperaResult();
        if (StringUtils.isEmpty(bean.getMpu())) {
            result.setCode(4000002);
            result.setMsg("mpu不能为空");
            return result ;
        }
        ShoppingCart temp = mapper.selectByPrimaryKey(bean.getId()) ;
        if (temp == null) {
            result.setCode(4000003);
            result.setMsg("ID不存在");
            return result ;
        }
        int perLimit = findPromotionBySku(temp.getMpu(), temp.getOpenId(), temp.getAppId()) ;
        if (perLimit != -1) {
            if (perLimit <= bean.getCount()) {
                result.setCode(4000001);
                result.setMsg("商品超过限购数量，无法添加。");
                result.getData().put("mpu", bean.getMpu()) ;
                return result;
            }
        }
        bean.setUpdatedAt(new Date());
        mapper.updateNumById(bean);
        result.getData().put("result", bean.getId()) ;
        return result;
    }

    @Override
    public PageBean findList(ShoppingCartQueryBean queryBean) {
        PageBean pageBean = new PageBean();
        int total = 0;
        int offset = PageBean.getOffset(queryBean.getPageNo(), queryBean.getPageSize());
        HashMap map = new HashMap();
        map.put("pageNo", offset);
        map.put("pageSize", queryBean.getPageSize());
        map.put("openId", queryBean.getOpenId()) ;
        List<ShoppingCartBean> shoppingCarts = new ArrayList<>();
        total = mapper.selectLimitCount(map);
        Map<String, Object> data = new HashMap<>() ;
        if (total > 0) {
            List<ShoppingCart> shoppingCartList = mapper.selectLimit(map) ;
            List<AoyiProdIndex> aoyiProdIndices = findProductByMpuList(shoppingCartList) ;
            List<CouponAndPromBean>  couponAndPromBeans =  findCouponListByMpuList(aoyiProdIndices, queryBean.getAppId()) ;
            shoppingCartList.forEach(shoppingCart -> {
                int perLimit = findPromotionBySku(shoppingCart.getMpu(), shoppingCart.getOpenId(), queryBean.getAppId()) ;
                shoppingCart.setPerLimited(perLimit);
                ShoppingCartBean shoppingCartBean = new ShoppingCartBean() ;
                BeanUtils.copyProperties(shoppingCart, shoppingCartBean);
                aoyiProdIndices.forEach(aoyiProdIndex -> {
                    if (shoppingCart.getMpu().equals(aoyiProdIndex.getMpu())) {
                        BeanUtils.copyProperties(aoyiProdIndex, shoppingCartBean);
                        shoppingCartBean.setId(shoppingCart.getId());
                    }
                });
                shoppingCarts.add(shoppingCartBean) ;
            });
            data.put("cart", shoppingCarts);
            data.put("couponProm", couponAndPromBeans) ;
        }
        pageBean = PageBean.build(pageBean, data, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    @Override
    public OperaResponse count(String openId) {
        OperaResponse response = new OperaResponse() ;
        int count = mapper.selectNumCount(openId) ;
        response.setData(count);
        return response;
    }

    private List<CouponAndPromBean> findCouponListByMpuList(List<AoyiProdIndex> beans, String appId) {
        List<AoyiProdIndex> aoyiProdIndices = new ArrayList<>() ;
        if (beans != null && beans.size() > 0) {
            beans.forEach(aoyiProdIndex -> {
                AoyiProdIndex prodIndex = new AoyiProdIndex() ;
                prodIndex.setCategory(aoyiProdIndex.getCategory());
                prodIndex.setMpu(aoyiProdIndex.getMpu());
                aoyiProdIndices.add(prodIndex) ;
            });
            OperaResult result = equityService.findCouponListByMpuList(aoyiProdIndices, appId) ;
            if (result != null && result.getCode() == 200) {
                Map<String, Object> data = result.getData();
                Object object = data.get("result");
                String jsonString = JSON.toJSONString(object);
                List<CouponAndPromBean>  couponAndPromBeans = JSONObject.parseArray(jsonString, CouponAndPromBean.class);
                return couponAndPromBeans;
            } else {
                log.error("批量查询优惠券和活动失败，返回结果： {}", JSONUtil.toJsonString(result));
            }
        }
        return null ;
    }

    private List<AoyiProdIndex> findProductByMpuList(List<ShoppingCart> shoppingCarts) {
        List<AoyiProdIndex> mpus = new ArrayList<>();
        shoppingCarts.forEach(shoppingCart -> {
            AoyiProdIndex aoyiProdIndex = new AoyiProdIndex() ;
            aoyiProdIndex.setMpu(shoppingCart.getMpu());
            aoyiProdIndex.setSkuid(shoppingCart.getSkuId());
            mpus.add(aoyiProdIndex) ;
        });
        OperaResponse result = productService.selectByMpuIdListAndSkuCodes(mpus);
        if (result.getCode() == 200) {
//            Map<String, Object> data = result.getData();
            Object object = result.getData();
            String jsonString = JSON.toJSONString(object);
            List<AoyiProdIndex> aoyiProdIndices = JSONObject.parseArray(jsonString, AoyiProdIndex.class);
            return aoyiProdIndices;
        }
        return null ;
    }

    private int findPromotionBySku(String mpu, String openId, String appId) {
        AtomicInteger perLimit = new AtomicInteger(0);
        List<String> mpus = new ArrayList<>() ;
        mpus.add(mpu) ;
        OperaResult result = equityService.findPromotionByMpuList(mpus, appId);
        log.info("promotion limit 返回结果： {}", JSONUtil.toJsonString(result));
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData() ;
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<PromotionMpuX> subOrderTS = JSONObject.parseArray(jsonString, PromotionMpuX.class);
            if (subOrderTS != null && subOrderTS.size() > 0) {
                PromotionMpuX promotionInfoBean = subOrderTS.get(0) ;
                if (promotionInfoBean.getPerLimited() == -1) {
                    return -1;
                } else {
                    List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsByOpenIdAndMpuAndPromotionId(openId, mpu, promotionInfoBean.getId()) ;
                    if (orderDetailList != null && orderDetailList.size() > 0) {
                        orderDetailList.forEach(orderDetail -> {
                            perLimit.set(perLimit.get() + orderDetail.getNum());
                        });
                    }
                    return promotionInfoBean.getPerLimited() - perLimit.get();
                }
            }
            return -1;
        }
        return -1;
    }
}
