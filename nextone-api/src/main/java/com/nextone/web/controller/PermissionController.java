package com.nextone.web.controller;

import com.nextone.service.PermissionService;
import com.nextone.utils.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 徐塬峰
 * @email 986771570@qq.com
 * @date 2019-06-08
 * @description
 **/
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "查询所有的权限能力")
    @RequestMapping("/queryAll")
    public JsonResult queryAll() {
        return JsonResult.ok(permissionService.queryAll());
    }
}
