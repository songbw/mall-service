package com.fengchao.statistics.mapper;

import com.fengchao.statistics.model.BaiduStatis;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface BaiduStatisMapper {

    List<HashMap<String, Object>> queryAllUrls(HashMap map);
    Integer countAllUrls();

    HashMap<String, Object> getTotalPVandUv();

    List<HashMap<String, Object>> queryStatisticsData(HashMap map);
    Integer countStatisticsData(HashMap map);
}