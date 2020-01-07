package com.max.service;

import com.max.domain.LeaveBill;

import java.util.List;

public interface LeaveBillService {
    List<LeaveBill> findLeaveBillListByUserID(Long id);

    void save(LeaveBill leaveBill);

    LeaveBill findLeaveBillByprimaryKey(Long id);

    void update(LeaveBill leaveBill);

    void deleteByPrimaryKey(Long id);
}
