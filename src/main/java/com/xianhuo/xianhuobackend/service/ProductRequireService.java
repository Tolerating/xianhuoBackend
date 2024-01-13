package com.xianhuo.xianhuobackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.ProductRequire;

import java.util.List;

public interface ProductRequireService extends IService<ProductRequire> {
    List<ProductRequire> requireBySellAndDispatch(long sellId,long dispatchId);
}
