package com.fengchao.base.service.impl;

import com.fengchao.base.service.TagsService;
import com.fengchao.base.mapper.TagsMapper;
import com.fengchao.base.model.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsServiceImpl implements TagsService {

    @Autowired
    private TagsMapper mapper;


    @Override
    public Tags findById(int id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public Tags findByAppId(String appId) {
        List<Tags> list = mapper.selectByAppId(appId) ;
        if (list != null && list.size() > 0) {
            return list.get(0) ;
        }
        return null;
    }
}
