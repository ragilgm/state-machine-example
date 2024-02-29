package com.example.statemachine.machine;

import com.example.statemachine.enums.ProcessEvent;
import com.example.statemachine.enums.ProcessStatus;
import org.springframework.stereotype.Service;
import org.squirrelframework.foundation.fsm.annotation.*;
import org.squirrelframework.foundation.fsm.impl.AbstractStateMachine;

@Service
@StateMachineParameters(stateType = ProcessStatus.class, eventType = ProcessEvent.class, contextType = StateContext.class)
@States({
        @State(name = "PENDING", initialState = true),                   //还款中
        @State(name = "PROCESSING"),                                        //还款确认中         //已逾期
        @State(name = "SUCCESS", isFinal = true)                        //已关闭
})
@Transitions({
        @Transit(from = "PENDING", to = "PROCESSING", on = "LOADING_TRANSFER", callMethod = "onLoadingTransfer"),        // 正常还款待确认         // 关闭账单
        @Transit(from = "PENDING", to = "SUCCESS", on = "SUCCESS_TRANSFER", callMethod = "onSuccessTransfer"),        // 正常还款待确认         // 关闭账单
})
public  class TestMachine extends AbstractStateMachine<TestMachine, ProcessStatus, ProcessEvent, StateContext<String>> {


    @Override
    protected void afterTransitionCausedException(ProcessStatus fromState, ProcessStatus toState, ProcessEvent event, StateContext<String> context) {
        System.out.println("exception is here");
    }

    @Override
    protected void afterTransitionCompleted(ProcessStatus fromState, ProcessStatus toState, ProcessEvent event, StateContext<String> context) {
        super.afterTransitionCompleted(fromState, toState, event, context);
    }

    public void onLoadingTransfer(ProcessStatus fromState, ProcessStatus toState, ProcessEvent event, StateContext<String> context){
        System.out.println("transfer is loading");
    }

    public void onSuccessTransfer(ProcessStatus fromState, ProcessStatus toState, ProcessEvent event, StateContext<String> context){
        System.out.println("transfer is success");
    }


}
