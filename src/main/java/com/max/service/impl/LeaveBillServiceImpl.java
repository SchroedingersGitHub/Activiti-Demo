package com.max.service.impl;

import com.max.domain.LeaveBill;
import com.max.mapper.LeaveBillMapper;
import com.max.service.LeaveBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveBillServiceImpl implements LeaveBillService {


    @Autowired
    private LeaveBillMapper leaveBillMapper;

    @Override
    public List<LeaveBill> findLeaveBillListByUserID(Long id) {

        return leaveBillMapper.selectAllByUserID(id);
    }

    @Override
    public void save(LeaveBill leaveBill) {

        leaveBillMapper.insert(leaveBill);

    }

    @Override
    public LeaveBill findLeaveBillByprimaryKey(Long id) {

        return leaveBillMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(LeaveBill leaveBill) {
        leaveBillMapper.updateByPrimaryKey(leaveBill);
    }

    @Override
    public void deleteByPrimaryKey(Long id) {
        leaveBillMapper.deleteByPrimaryKey(id);
    }
}
