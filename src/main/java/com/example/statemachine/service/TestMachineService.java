package com.example.statemachine.service;


import com.example.statemachine.enums.ProcessEvent;
import com.example.statemachine.enums.ProcessStatus;
import com.example.statemachine.machine.AbstractStateMachineEngine;
import com.example.statemachine.machine.StateContext;
import com.example.statemachine.machine.TestMachine;
import org.springframework.stereotype.Service;

@Service
public class TestMachineService extends AbstractStateMachineEngine<TestMachine, ProcessStatus, ProcessEvent, StateContext<String>> {
    @Override
    protected ProcessStatus getInitialStatus(StateContext<String> context) {
        System.out.println("get initial status");
        return ProcessStatus.PENDING;
    }

    @Override
    protected String getDistributionLockId(StateContext<String> context) {
        return super.getDistributionLockId(context);
    }

    @Override
    public void fire(ProcessEvent event, StateContext<String> context) {
        super.fire(event, context);
    }
}

