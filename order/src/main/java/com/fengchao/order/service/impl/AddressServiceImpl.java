package com.fengchao.order.service.impl;

import com.fengchao.order.bean.AddressCodeBean;
import com.fengchao.order.bean.AddressQueryBean;
import com.fengchao.order.bean.GPSQueryBean;
import com.fengchao.order.db.annotation.DataSource;
import com.fengchao.order.db.config.DataSourceNames;
import com.fengchao.order.mapper.AoyiBaseFulladdressMapper;
import com.fengchao.order.model.AoyiBaseFulladdress;
import com.fengchao.order.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AoyiBaseFulladdressMapper mapper;

    @DataSource(DataSourceNames.TWO)
    @Override
    public List<AoyiBaseFulladdress> findByLevel(AddressQueryBean queryBean) {
        HashMap map = new HashMap();
        map.put("level",queryBean.getLevel());
        if (queryBean.getLevel().equals("1")) {
            map.put("pid", "CN") ;
        } else {
            map.put("pid", queryBean.getPid());
        }
        if (queryBean.getLevel().equals("4")) {
            map.put("cityId",queryBean.getCityId());
        }
        return mapper.selectByLevel(map) ;
    }

    @DataSource(DataSourceNames.TWO)
    @Override
    public AddressCodeBean findCode(GPSQueryBean queryBean) {
        AddressCodeBean addressCodeBean = new AddressCodeBean();
        HashMap map = new HashMap();
        if (queryBean.getProvince() != null && !"".equals(queryBean.getProvince())) {
            map.put("name", queryBean.getProvince()) ;
            map.put("level", "1") ;
            map.put("pid", "CN") ;
            AoyiBaseFulladdress fulladdress = mapper.selectByCode(map) ;
            if (fulladdress != null) {
                addressCodeBean.setProvinceId(fulladdress.getId());
                if (queryBean.getCity() != null && !"".equals(queryBean.getCity())) {
                    map = new HashMap();
                    map.put("name", queryBean.getCity()) ;
                    map.put("level", "2") ;
                    map.put("pid", addressCodeBean.getProvinceId()) ;
                    AoyiBaseFulladdress fulladdress1 = mapper.selectByCode(map) ;
                    if (fulladdress1 != null) {
                        addressCodeBean.setCityId(fulladdress1.getId());
                        if (queryBean.getCounty() != null && !"".equals(queryBean.getCounty())) {
                            map = new HashMap();
                            map.put("name", queryBean.getCounty()) ;
                            map.put("level", "3") ;
                            map.put("pid", addressCodeBean.getCityId()) ;
                            AoyiBaseFulladdress fulladdress2 = mapper.selectByCode(map) ;
                            if (fulladdress2 != null) {
                                addressCodeBean.setCountyId(fulladdress2.getId());
                                if (fulladdress2.getZipcode() != null) {
                                    addressCodeBean.setZipCode(fulladdress2.getZipcode());
                                } else {
                                    addressCodeBean.setZipCode(fulladdress1.getZipcode());
                                }
                            }
                        }
                    }
                }
            } else {
                if (queryBean.getCity() != null && !"".equals(queryBean.getCity())) {
                    map = new HashMap();
                    map.put("name", queryBean.getCity()) ;
                    map.put("level", "2") ;
                    AoyiBaseFulladdress fulladdress1 = mapper.selectByCode(map) ;
                    if (fulladdress1 != null) {
                        addressCodeBean.setProvinceId(fulladdress1.getPid());
                        addressCodeBean.setCityId(fulladdress1.getId());
                        if (queryBean.getCounty() != null && !"".equals(queryBean.getCounty())) {
                            map = new HashMap();
                            map.put("name", queryBean.getCounty()) ;
                            map.put("level", "3") ;
                            map.put("pid", addressCodeBean.getCityId()) ;
                            AoyiBaseFulladdress fulladdress2 = mapper.selectByCode(map) ;
                            if (fulladdress2 != null) {
                                addressCodeBean.setCountyId(fulladdress2.getId());
                                if (fulladdress2.getZipcode() != null) {
                                    addressCodeBean.setZipCode(fulladdress2.getZipcode());
                                } else {
                                    addressCodeBean.setZipCode(fulladdress1.getZipcode());
                                }
                            }
                        }
                    }
                }
            }
        }
        return addressCodeBean;
    }
}
