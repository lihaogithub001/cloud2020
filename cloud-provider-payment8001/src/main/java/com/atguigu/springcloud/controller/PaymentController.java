package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;
    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result=paymentService.create(payment);
        log.info("*****插入结果："+result);

        if (result>0){
            return new CommonResult(200,"插入成功,port:"+serverPort,result);
        }else {
            return new CommonResult(444,"插入失败",null);
        }

    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment payment=paymentService.getPaymentById(id);
        log.info("*****查询结果："+payment);

        if (payment!=null){
            return new CommonResult(200,"查询成功,port:"+serverPort,payment);
        }else {
            return new CommonResult(444,"查询失败",null);
        }

    }

    @GetMapping(value = "/payment/discovery")
    public DiscoveryClient discovery(){

        List<String> services = discoveryClient.getServices();
        for (String service:services){
            log.info("service:"+service);
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance:instances){
                log.info("--instance:"+instance);
                log.info("---instanceId:"+instance.getInstanceId());
                log.info("---instanceHost:"+instance.getHost());
                log.info("---instancePort:"+instance.getPort());
                log.info("---instanceUri:"+instance.getUri());
            }
        }
        return this.discoveryClient;
    }
}
