package com.zifan.service;

import com.zifan.model.UserSignal;
import com.zifan.repository.SignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SignalService {

    @Autowired
    private SignalRepository signalRepository;


    // 添加信号
    public boolean addSignal(String userEmail, String module, UserSignal.Signal signal) {
        Optional<UserSignal.Signal> singleSignal = getSingleSignalById(userEmail, module, signal.getId());
        if(singleSignal.isPresent()) {
            // 不再重复添加
            return false;
        } else {
            UserSignal userSignal = signalRepository.findByUserEmail(userEmail)
                    .orElse(new UserSignal(userEmail));
            signal.setTimestamp(LocalDateTime.now());
            Map<String, List<UserSignal.Signal>> modules = userSignal.getModules();

            if (modules.isEmpty() || modules.get(module) == null) {
                ArrayList<UserSignal.Signal> signalList = new ArrayList<>();
                signalList.add(signal);
                modules.put(module, signalList);
            } else {
                modules.get(module).add(signal);
            }
            userSignal.setModules(modules);
            signalRepository.save(userSignal);
            return true;
        }
    }

    // 获取用户的所有信号
    public Optional<UserSignal> getAllSignals(String userEmail) {
        return signalRepository.findByUserEmail(userEmail);
    }

    // 获取用户某个模块的信号, 只返回指定模块
    public Optional<UserSignal> getSignalsByModule(String userEmail, String module) {
        Optional<UserSignal> userSignal = signalRepository.findByUserEmailAndModule(userEmail, module);
        if (userSignal.isPresent()) {
            UserSignal ret = new UserSignal();
            HashMap<String, List<UserSignal.Signal>> hashmap = new HashMap<>();
            hashmap.put(module, userSignal.get().getModules().get(module));
            ret.setModules(hashmap);
            return Optional.of(ret);
        } else {
            return Optional.empty();
        }
    }

    // 获取用户某个信号类型的信号
    public List<UserSignal.Signal> getSignalsByType(String userEmail, String signalType) {
        return signalRepository.findByUserEmailAndSignalType(userEmail, signalType)
                .map(userSignal -> userSignal.getModules().values().stream()
                        .flatMap(List::stream)
                        .filter(signal -> signal.getType().name().equals(signalType))
                        .toList())
                .orElse(List.of());
    }

    // 查询某个用户某个模块下的某个 ID 是否存在
    public boolean existsById(String userEmail, String module, String id) {
        return signalRepository.findByUserEmailAndModuleAndId(userEmail, module, id).isPresent();
    }

    // 获取指定userEmail module ID的记录
    public Optional<UserSignal.Signal> getSingleSignalById(String userEmail, String module, String id) {
        Optional<UserSignal> userSignal = signalRepository.findByUserEmailAndModuleAndId(userEmail, module, id);
        if (userSignal.isPresent()) {
            List<UserSignal.Signal> signals = userSignal.get().getModules().get(module);
            for (UserSignal.Signal signal: signals) {
                if (signal.getId().equals(id)) {
                    return Optional.of(signal);
                }
            }
            return Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    // 删除指定用户模块 ID 的记录
    public void deleteById(String userEmail, String module, String id) {
        signalRepository.deleteByUserEmailAndModuleAndId(userEmail, module, id);
    }

    // 删除用户某个模块的信号
    public void deleteModule(String userEmail, String module) {
        signalRepository.deleteModuleByUserEmail(userEmail, module);
    }

    // 删除用户某个信号类型的信号
    public void deleteSignalType(String userEmail, String signalType) {
        signalRepository.deleteSignalTypeByUserEmail(userEmail, signalType);
    }

    // 清空用户的信号
    public void clearSignals(String userEmail) {
        signalRepository.deleteByUserEmail(userEmail);
    }
}