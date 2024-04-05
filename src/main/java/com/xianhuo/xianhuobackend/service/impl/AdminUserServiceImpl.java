package com.xianhuo.xianhuobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xianhuo.xianhuobackend.entity.*;
import com.xianhuo.xianhuobackend.mapper.*;
import com.xianhuo.xianhuobackend.service.AdminUserService;
import com.xianhuo.xianhuobackend.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * (AdminUser)表服务实现类
 *
 * @author makejava
 * @since 2024-03-10 14:45:56
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Resource
    private ComplainMapper complainMapper;
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private AfterServiceMapper afterServiceMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private ProductRequireMapper productRequireMapper;

    @Override
    public IPage<Complain> getComplainWaitForPost(Page<Complain> page, String startTime, String endTime, Boolean history) {
        IPage<Complain> pages = complainMapper.selectPage(page, new LambdaQueryWrapper<Complain>()
                .between(startTime != null, Complain::getCreateTime, startTime, endTime)
                .eq(Complain::getType, 0)
                .isNull(!history,Complain::getDealTime));
        IPage<Complain> complainIPage = pages.convert(Complain::new);
        if (!complainIPage.getRecords().isEmpty()) {
            List<Long> complaintId = complainIPage.getRecords().stream().map(Complain::getComplainantId).collect(Collectors.toList());
            List<Users> complaintList = usersMapper.selectBatchIds(complaintId);
            Map<Long, String> complaintMap = complaintList.stream().collect(toMap(Users::getId, Users::getName));
            complainIPage.getRecords().forEach(item -> item.setComplaintName(complaintMap.get(item.getComplainantId())));
            Set<Long> seller = complainIPage.getRecords().stream().map(Complain::getSellerId).collect(Collectors.toSet());
            List<Users> sellerList = usersMapper.selectList(Wrappers.lambdaQuery(Users.class).in(Users::getId, seller));
            Map<Long, String> sellerMap = sellerList.stream().collect(toMap(Users::getId, Users::getName));
            if(history){
//                历史记录，批量获取处理人名字
                List<Long> dealId = complainIPage.getRecords().stream().map(Complain::getDealUser).collect(Collectors.toList());
                List<AdminUser> dealList = adminUserMapper.selectBatchIds(dealId);
                Map<Long, String> dealMap = dealList.stream().collect(toMap(AdminUser::getId, AdminUser::getAccount));
                complainIPage.convert(e -> {
                    e.setSellerName(sellerMap.get(e.getSellerId()));
                    e.setComplaintName(complaintMap.get(e.getComplainantId()));
                    e.setDealUserName(dealMap.get(e.getDealUser()));
                    return e;
                });
            }else{
                complainIPage.convert(e -> {
                    e.setSellerName(sellerMap.get(e.getSellerId()));
                    e.setComplaintName(complaintMap.get(e.getComplainantId()));
                    return e;
                });
            }
        }
        return complainIPage;
    }

    @Override
    public IPage<Complain> getComplainWaitForProduct(Page<Complain> page, String startTime, String endTime, Boolean history) {
        IPage<Complain> pages = complainMapper.selectPage(page, new LambdaQueryWrapper<Complain>()
                .between(startTime != null, Complain::getCreateTime, startTime, endTime)
                .eq(Complain::getType, 1)
                .isNull(!history,Complain::getDealTime));
        IPage<Complain> complainIPage = pages.convert(Complain::new);
        if (!complainIPage.getRecords().isEmpty()) {
            List<Long> complaintId = complainIPage.getRecords().stream().map(Complain::getComplainantId).collect(Collectors.toList());
            List<Users> complaintList = usersMapper.selectBatchIds(complaintId);
            Map<Long, String> complaintMap = complaintList.stream().collect(toMap(Users::getId, Users::getName));
            complainIPage.getRecords().forEach(item -> item.setComplaintName(complaintMap.get(item.getComplainantId())));
            Set<Long> seller = complainIPage.getRecords().stream().map(Complain::getSellerId).collect(Collectors.toSet());
            List<Users> sellerList = usersMapper.selectList(Wrappers.lambdaQuery(Users.class).in(Users::getId, seller));
            Map<Long, String> sellerMap = sellerList.stream().collect(toMap(Users::getId, Users::getName));
            if(history){
//                历史记录，批量获取处理人名字
                List<Long> dealId = complainIPage.getRecords().stream().map(Complain::getDealUser).collect(Collectors.toList());
                List<AdminUser> dealList = adminUserMapper.selectBatchIds(dealId);
                Map<Long, String> dealMap = dealList.stream().collect(toMap(AdminUser::getId, AdminUser::getAccount));
                complainIPage.convert(e -> {
                    e.setSellerName(sellerMap.get(e.getSellerId()));
                    e.setComplaintName(complaintMap.get(e.getComplainantId()));
                    e.setDealUserName(dealMap.get(e.getDealUser()));
                    return e;
                });
            }else{
                complainIPage.convert(e -> {
                    e.setSellerName(sellerMap.get(e.getSellerId()));
                    e.setComplaintName(complaintMap.get(e.getComplainantId()));
                    return e;
                });
            }

        }
        return complainIPage;
    }

//    分页获取商品待处理售后或历史
    @Override
    public IPage<AfterService> afterServiceWaitDealList(Page<AfterService> page, String startTime, String endTime,Boolean history) {
        IPage<AfterService> pages = afterServiceMapper.selectPage(page, new LambdaQueryWrapper<AfterService>()
                .between(startTime != null, AfterService::getCreateTime, startTime, endTime)
                .eq(!history,AfterService::getPlatformStatus, 0)
                .ne(history,AfterService::getStatus,0));
        IPage<AfterService> afterIPage = pages.convert(AfterService::new);
        if (!afterIPage.getRecords().isEmpty()) {
            List<Long> buyerIds = afterIPage.getRecords().stream().map(AfterService::getBuyerId).collect(Collectors.toList());
            Map<Long, String> buyerMap = usersMapper.selectBatchIds(buyerIds).stream().collect(toMap(Users::getId, Users::getName));
            Set<Long> sellerIds = afterIPage.getRecords().stream().map(AfterService::getSellerId).collect(Collectors.toSet());
            Map<Long, String> sellerMap = usersMapper.selectBatchIds(sellerIds).stream().collect(toMap(Users::getId, Users::getName));
            if(history){
//                历史记录，批量获取处理人名字
                List<Long> dealId = afterIPage.getRecords().stream().map(AfterService::getPlatformUser).collect(Collectors.toList());
                List<AdminUser> dealList = adminUserMapper.selectBatchIds(dealId);
                Map<Long, String> dealMap = dealList.stream().collect(toMap(AdminUser::getId, AdminUser::getAccount));
                afterIPage.convert(e -> {
                    e.setSellerName(sellerMap.get(e.getSellerId()));
                    e.setBuyerName(buyerMap.get(e.getBuyerId()));
                    e.setPlatformUserName(dealMap.get(e.getPlatformUser()));
                    return e;
                });
            }else{
                afterIPage.convert(e -> {
                    e.setSellerName(sellerMap.get(e.getSellerId()));
                    e.setBuyerName(buyerMap.get(e.getBuyerId()));
                    return e;
                });
            }

        }
        return afterIPage;
    }

//    按条件查询商品，分页返回
    @Override
    public IPage<Product> getAllProducts(Page<Product> page, String startTime, String endTime, String deatil) {
        IPage<Product> productPage = productMapper.selectPage(page, new LambdaQueryWrapper<Product>()
                .between(startTime != null, Product::getCreateTime, startTime, endTime)
                .like(deatil != null, Product::getDetail, deatil));
        IPage<Product> converted = productPage.convert(Product::new);
        if (!converted.getRecords().isEmpty()) {
            List<ProductRequire> list = productRequireMapper.selectList(Wrappers.emptyWrapper());
            Map<Long, String> map = list.stream().collect(toMap(ProductRequire::getId, ProductRequire::getName));
            List<Category> categoryList = categoryMapper.selectList(Wrappers.emptyWrapper());
            Map<Long, String> categoryMaps = categoryList.stream().collect(toMap(Category::getId, Category::getName));
            List<Long> userIds = converted.getRecords().stream().map(Product::getUserId).collect(Collectors.toList());
            Map<Long, String> buyerMap = usersMapper.selectBatchIds(userIds).stream().collect(toMap(Users::getId, Users::getName));
            Set<Long> sellerIds = converted.getRecords().stream().map(Product::getUserId).collect(Collectors.toSet());
            Map<Long, String> sellerMap = usersMapper.selectBatchIds(sellerIds).stream().collect(toMap(Users::getId, Users::getName));
            converted.convert(e -> {
                e.setPublisher(sellerMap.get(e.getUserId()));
                e.setRequireNames(Arrays.stream(e.getProductRequireId().split(",")).map(id -> map.get(Long.valueOf(id))).collect(Collectors.joining(",")));
                e.setCategoryName(categoryMaps.get(e.getCategoryId()));
                return e;
            });


        }
        return page;
    }
}

