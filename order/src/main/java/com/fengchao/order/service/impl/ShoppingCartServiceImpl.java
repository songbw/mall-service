package com.fengchao.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengchao.order.bean.OperaResult;
import com.fengchao.order.bean.PageBean;
import com.fengchao.order.bean.ShoppingCartBean;
import com.fengchao.order.bean.ShoppingCartQueryBean;
import com.fengchao.order.dao.OrderDetailDao;
import com.fengchao.order.feign.EquityServiceClient;
import com.fengchao.order.feign.ProductService;
import com.fengchao.order.mapper.ShoppingCartMapper;
import com.fengchao.order.model.AoyiProdIndex;
import com.fengchao.order.model.ShoppingCart;
import com.fengchao.order.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        ShoppingCart temp = mapper.selectByOpenIdAndSku(bean) ;
        int perLimit = findPromotionBySku(bean.getMpu(), bean.getOpenId()) ;
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
            bean.setCount(1);
            mapper.insert(bean) ;
            result.getData().put("result", bean.getId()) ;
            return result ;
        } else {
            temp.setCount(temp.getCount() + 1);
            temp.setUpdatedAt(date);
            mapper.updateNumById(temp);
            result.getData().put("result", bean.getId()) ;
            return result;
        }
    }

    @Override
    public Integer delete(Integer id) {
        return mapper.updateIsDelById(id);
    }

    @Override
    public OperaResult modifyNum(ShoppingCart bean) {
        OperaResult result = new OperaResult();
        ShoppingCart temp = mapper.selectByPrimaryKey(bean.getId()) ;
//        int perLimit = findPromotionBySku(temp.getMpu(), temp.getOpenId()) ;
//        if (perLimit != -1) {
//            if (perLimit <= bean.getCount()) {
//                result.setCode(4000001);
//                result.setMsg("商品超过限购数量，无法添加。");
//                result.getData().put("mpu", bean.getMpu()) ;
//                return result;
//            }
//        }
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
        if (total > 0) {
            List<ShoppingCart> shoppingCartList = mapper.selectLimit(map) ;
            List<AoyiProdIndex> aoyiProdIndices = findProductByMpuList(shoppingCartList) ;
            shoppingCartList.forEach(shoppingCart -> {
                int perLimit = findPromotionBySku(shoppingCart.getMpu(), shoppingCart.getOpenId()) ;
                shoppingCart.setPerLimited(perLimit);
                ShoppingCartBean shoppingCartBean = new ShoppingCartBean() ;
                BeanUtils.copyProperties(shoppingCart, shoppingCartBean);
                aoyiProdIndices.forEach(aoyiProdIndex -> {
                    if (shoppingCart.getMpu().equals(aoyiProdIndex.getMpu())) {
                        BeanUtils.copyProperties(aoyiProdIndex, shoppingCartBean);
                    }
                });
                shoppingCarts.add(shoppingCartBean) ;
            });
        }
        pageBean = PageBean.build(pageBean, shoppingCarts, total, queryBean.getPageNo(), queryBean.getPageSize());
        return pageBean;
    }

    private List<AoyiProdIndex> findProductByMpuList(List<ShoppingCart> shoppingCarts) {
        List<String> mpus = new ArrayList<>();
        shoppingCarts.forEach(shoppingCart -> {
            mpus.add(shoppingCart.getMpu()) ;
        });
        OperaResult result = productService.selectProductListByMpuIdList(mpus);
        if (result.getCode() == 200) {
            Map<String, Object> data = result.getData();
            Object object = data.get("result");
            String jsonString = JSON.toJSONString(object);
            List<AoyiProdIndex> aoyiProdIndices = JSONObject.parseArray(jsonString, AoyiProdIndex.class);
            return aoyiProdIndices;
        }
        return null ;
    }

    private int findPromotionBySku(String mpu, String openId) {
//        AtomicInteger perLimit = new AtomicInteger(0);
//        List<String> mpus = new ArrayList<>() ;
//        mpus.add(mpu) ;
//        OperaResult result = equityService.findPromotionByMpuList(mpus);
//        if (result.getCode() == 200) {
//            Map<String, Object> data = result.getData() ;
//            Object object = data.get("result");
//            String jsonString = JSON.toJSONString(object);
//            List<PromotionMpuX> subOrderTS = JSONObject.parseArray(jsonString, PromotionMpuX.class);
//            if (subOrderTS != null && subOrderTS.size() > 0) {
//                PromotionMpuX promotionInfoBean = subOrderTS.get(0) ;
//                if (promotionInfoBean.getPerLimited() == -1) {
//                    return -1;
//                } else {
//                    List<OrderDetail> orderDetailList = orderDetailDao.selectOrderDetailsByOpenIdAndMpuAndPromotionId(openId, mpu, promotionInfoBean.getId()) ;
//                    if (orderDetailList != null && orderDetailList.size() > 0) {
//                        orderDetailList.forEach(orderDetail -> {
//                            perLimit.set(perLimit.get() + orderDetail.getNum());
//                        });
//                    }
//                    return promotionInfoBean.getPerLimited() - perLimit.get();
//                }
//            }
//            return -1;
//        }
        return -1;
    }
}
