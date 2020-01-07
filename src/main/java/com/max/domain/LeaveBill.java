package com.max.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveBill {
    private Long id;

    private Integer days;

    private String content;

    private String remark;

    private Date leavedate = new Date();

    private Integer state = 0;

    private Employee employee; // 请假人

}