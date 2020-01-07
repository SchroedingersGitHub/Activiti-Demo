package com.max.mapper;

import com.max.domain.LeaveBill;
import java.util.List;

public interface LeaveBillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LeaveBill record);

    LeaveBill selectByPrimaryKey(Long id);

    List<LeaveBill> selectAll();

    int updateByPrimaryKey(LeaveBill record);

    List<LeaveBill> selectAllByUserID(Long id);
}