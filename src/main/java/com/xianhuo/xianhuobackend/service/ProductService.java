package com.xianhuo.xianhuobackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xianhuo.xianhuobackend.entity.*;

import java.util.List;

public interface ProductService extends IService<Product> {
    List<Category> allCategory();
    List<SellMode> allUsableSellMode();


}
