package com.xy.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.hutool.core.lang.Dict;

@Repository
public class IndexDao {
    
    @Autowired
    private JdbcTemplate jt ;
    
    private NamedParameterJdbcTemplate njd;
    
    public List<Map<String,Object>> queryQxz(Dict params) {
        String sql = " select * from station where Sta_area = ?";
        return jt.queryForList(sql, new Object[]{params.get("xc")});
    }
}
