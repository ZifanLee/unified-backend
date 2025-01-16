package com.zifan.dto.response;

import com.zifan.model.UserSignal;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SignalResponse {
    private String userEmail; // 用户email

    // 用于返回单独模块信号
    private String module;    // 模块名称
    private List<UserSignal.Signal> signals; // 信号列表

    // 用于返回多模块信号，模块: 信号列表
    private Map<String, List<UserSignal.Signal>> modules; // 模块划分的信号列表

}
