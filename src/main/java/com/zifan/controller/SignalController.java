package com.zifan.controller;

import com.zifan.dto.response.ApiResponse;
import com.zifan.dto.response.SignalResponse;
import com.zifan.model.UserSignal;
import com.zifan.model.enumType.SignalType;
import com.zifan.service.SignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/signals")
public class SignalController {

    @Autowired
    private SignalService signalService;


    // 该接口一般用于测试，生产环境不存在插入signal的需求
    @PostMapping("/add")
    public ApiResponse<SignalResponse> addSignal(@RequestParam("userEmail") String userEmail,
                                                 @RequestParam("module") String module,
                                                 @RequestParam("signalType") String signalType,
                                                 @RequestParam("id") String id){
        UserSignal.Signal signal = new UserSignal.Signal();
        signal.setId(id);
        signal.setType(SignalType.valueOf(signalType));
        SignalResponse response = new SignalResponse();
        response.setUserEmail(userEmail);
        if (signalService.addSignal(userEmail, module, signal)) {
            return ApiResponse.success(response, "Success insert one signal");
        } else {
            return ApiResponse.success(response, "Duplicate signal, refuse update");
        }
    }


    // HTTP: 获取用户的所有信号
    @GetMapping
    public ApiResponse<SignalResponse> getAllSignals(@RequestParam("userEmail") String userEmail) {
        Optional<UserSignal> userSignal = signalService.getAllSignals(userEmail);
        SignalResponse response = new SignalResponse();
        response.setUserEmail(userEmail);
        if (userSignal.isPresent()) {
            response.setModules(userSignal.get().getModules());
        }
        return ApiResponse.success(response);
    }

    // HTTP: 获取用户某个模块的信号
    @GetMapping("/module")
    public ApiResponse<SignalResponse> getSignalsByModule(@RequestParam("userEmail") String userEmail,
                                                          @RequestParam("module") String module) {
        Optional<UserSignal> userSignal = signalService.getSignalsByModule(userEmail, module);
        SignalResponse response = new SignalResponse();
        response.setUserEmail(userEmail);
        response.setModule(module);
        if (userSignal.isPresent()) {
            response.setSignals(userSignal.get().getModules().get(module));
            return ApiResponse.success(response);
        } else {
            return ApiResponse.success(response);
        }
    }

    // HTTP: 获取用户某个信号类型的信号
    @GetMapping("/type")
    public ApiResponse<List<UserSignal.Signal>> getSignalsByType(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("signalType") String signalType) {
        List<UserSignal.Signal> signals = signalService.getSignalsByType(userEmail, signalType);
        return ApiResponse.success(signals);
    }

    // HTTP: 删除用户某个模块的信号
    @DeleteMapping("/module")
    public ApiResponse<Void> deleteModule(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("module") String module) {
        signalService.deleteModule(userEmail, module);
        return ApiResponse.success(null);
    }

    // HTTP: 删除用户某个信号类型的信号
    @DeleteMapping("/type")
    public ApiResponse<Void> deleteSignalType(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("signalType") String signalType) {
        signalService.deleteSignalType(userEmail, signalType);
        return ApiResponse.success(null);
    }

    // HTTP: 清空用户的信号
    @DeleteMapping
    public ApiResponse<Void> clearSignals(@RequestParam("userEmail") String userEmail) {
        signalService.clearSignals(userEmail);
        return ApiResponse.success(null);
    }


    // WebSocket: 获取用户的所有信号
    @MessageMapping("/signals.getAll")
    public ApiResponse<SignalResponse> getAllSignalsWebSocket(@Header("userEmail") String userEmail) {
        Optional<UserSignal> userSignal = signalService.getAllSignals(userEmail);
        SignalResponse response = new SignalResponse();
        response.setUserEmail(userEmail);
        if (userSignal.isPresent()) {
            response.setModules(userSignal.get().getModules());
        }
        return ApiResponse.success(response);
    }

    // WebSocket: 获取用户某个模块的信号
    @MessageMapping("/signals.getByModule")
    public ApiResponse<SignalResponse> getSignalsByModuleWebSocket(
            @Header("userEmail") String userEmail,
            @Header("module") String module) {
        Optional<UserSignal> userSignal = signalService.getSignalsByModule(userEmail, module);
        SignalResponse response = new SignalResponse();
        response.setUserEmail(userEmail);
        response.setModule(module);
        if (userSignal.isPresent()) {
            response.setSignals(userSignal.get().getModules().get(module));
            return ApiResponse.success(response);
        } else {
            return ApiResponse.success(response);
        }
    }

    // WebSocket: 获取用户某个信号类型的信号
    @MessageMapping("/signals.getByType")
    public ApiResponse<List<UserSignal.Signal>> getSignalsByTypeWebSocket(
            @Header("userEmail") String userEmail,
            @Header("signalType") String signalType) {
        List<UserSignal.Signal> signals = signalService.getSignalsByType(userEmail, signalType);
        return ApiResponse.success(signals);
    }

    // WebSocket: 删除用户某个模块的信号
    @MessageMapping("/signals.deleteModule")
    public ApiResponse<Void> deleteModuleWebSocket(
            @Header("userEmail") String userEmail,
            @Header("module") String module) {
        signalService.deleteModule(userEmail, module);
        return ApiResponse.success(null);
    }

    // WebSocket: 删除用户某个信号类型的信号
    @MessageMapping("/signals.deleteSignalType")
    public ApiResponse<Void> deleteSignalTypeWebSocket(
            @Header("userEmail") String userEmail,
            @Header("signalType") String signalType) {
        signalService.deleteSignalType(userEmail, signalType);
        return ApiResponse.success(null);
    }

    // WebSocket: 清空用户的信号
    @MessageMapping("/signals.clear")
    public ApiResponse<Void> clearSignalsWebSocket(@Header("userEmail") String userEmail) {
        signalService.clearSignals(userEmail);
        return ApiResponse.success(null);
    }
}