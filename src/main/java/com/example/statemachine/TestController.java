package com.example.statemachine;


import com.example.statemachine.enums.ProcessEvent;
import com.example.statemachine.machine.StateContext;
import com.example.statemachine.service.TestMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestMachineService testMachineService;

    @GetMapping("/test")
    public String test(){
        StateContext<String> stateContext = new StateContext<>("hello world");
        stateContext.setAttribute("String", "hello wirld");
        testMachineService.fire(ProcessEvent.LOADING_TRANSFER,stateContext);
        testMachineService.fire(ProcessEvent.SUCCESS_TRANSFER,stateContext);
        return null;
    }

}
