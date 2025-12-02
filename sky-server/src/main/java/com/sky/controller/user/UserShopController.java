package com.sky.controller.user;

import com.sky.config.DebugConfig;
import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shop")
@Api(tags = "店铺接口")
@Slf4j
public class UserShopController {
    @Autowired
    private ShopService shopService;
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result getStatus() {
        return Result.success(shopService.getStatus());
    }
}
