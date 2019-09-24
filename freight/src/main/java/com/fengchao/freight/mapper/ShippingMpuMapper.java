package com.fengchao.freight.mapper;

import com.fengchao.freight.model.ShippingMpu;
import com.fengchao.freight.model.ShippingMpuExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShippingMpuMapper {
    long countByExample(ShippingMpuExample example);

    int deleteByExample(ShippingMpuExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ShippingMpu record);

    int insertSelective(ShippingMpu record);

    List<ShippingMpu> selectByExample(ShippingMpuExample example);

    ShippingMpu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ShippingMpu record, @Param("example") ShippingMpuExample example);

    int updateByExample(@Param("record") ShippingMpu record, @Param("example") ShippingMpuExample example);

    int updateByPrimaryKeySelective(ShippingMpu record);

    int updateByPrimaryKey(ShippingMpu record);
}