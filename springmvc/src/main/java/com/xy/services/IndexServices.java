package com.xy.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xy.dao.IndexDao;

import cn.hutool.core.lang.Dict;

@Service
public class IndexServices {
    
    @Autowired
    private IndexDao indexDao;

    public List<Map<String, Object>> query(Dict p) {
        // TODO Auto-generated method stub
        return indexDao.queryQxz(p);
    }
    

}
