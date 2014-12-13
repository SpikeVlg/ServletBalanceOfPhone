package com.github.spikevlg.balanceofphone;

import com.github.spikevlg.balanceofphone.model.PhoneServiceRequest;
import com.github.spikevlg.balanceofphone.model.PhoneServiceResponse;
import com.github.spikevlg.balanceofphone.model.PhoneUser;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @RequestMapping("/hello")
    public String hello() { return "Hello, World!"; }

    @RequestMapping(value = "/requester", method = RequestMethod.POST)
    public String requester(@RequestBody String postRequest){
        return " sss " + postRequest;
    }


    @RequestMapping("/test")
    public PhoneUser test() {
        return new PhoneUser("user", "pass");
    }

    @RequestMapping(value = "/input", method = RequestMethod.POST)
    public String inputTest(@RequestBody PhoneUser postRequest) {
        return postRequest.getPassword() + " " + postRequest.getUser();
    }

    @RequestMapping(value = "/balance_of_phone", method = RequestMethod.POST)
    public PhoneServiceResponse processRequest(@RequestBody PhoneServiceRequest request) {
        PhoneServiceResponse response = new PhoneServiceResponse();
        response.setCode(1);
        response.setBalance(2);
        return response;
    }
}
