package com.xiaolanhe.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.xiaolanhe.common.constant.ProductConstant;
import com.xiaolanhe.common.to.SkuReductionTo;
import com.xiaolanhe.common.to.SpuBoundTo;
import com.xiaolanhe.common.to.es.SkuEsModel;
import com.xiaolanhe.common.utils.R;
import com.xiaolanhe.gulimall.product.entity.AttrEntity;
import com.xiaolanhe.gulimall.product.entity.BrandEntity;
import com.xiaolanhe.gulimall.product.entity.CategoryEntity;
import com.xiaolanhe.gulimall.product.entity.ProductAttrValueEntity;
import com.xiaolanhe.gulimall.product.entity.SkuImagesEntity;
import com.xiaolanhe.gulimall.product.entity.SkuInfoEntity;
import com.xiaolanhe.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.xiaolanhe.gulimall.product.entity.SpuInfoDescEntity;
import com.xiaolanhe.gulimall.product.feign.CouponFeginService;
import com.xiaolanhe.gulimall.product.feign.SearchFeginService;
import com.xiaolanhe.gulimall.product.feign.WareFeginService;
import com.xiaolanhe.gulimall.product.service.AttrService;
import com.xiaolanhe.gulimall.product.service.BrandService;
import com.xiaolanhe.gulimall.product.service.CategoryService;
import com.xiaolanhe.gulimall.product.service.ProductAttrValueService;
import com.xiaolanhe.gulimall.product.service.SkuImagesService;
import com.xiaolanhe.gulimall.product.service.SkuInfoService;
import com.xiaolanhe.gulimall.product.service.SkuSaleAttrValueService;
import com.xiaolanhe.gulimall.product.service.SpuImagesService;
import com.xiaolanhe.gulimall.product.service.SpuInfoDescService;
import com.xiaolanhe.gulimall.product.vo.Attr;
import com.xiaolanhe.gulimall.product.vo.BaseAttrs;
import com.xiaolanhe.gulimall.product.vo.Bounds;
import com.xiaolanhe.gulimall.product.vo.Images;
import com.xiaolanhe.gulimall.product.vo.SkuHasStockVo;
import com.xiaolanhe.gulimall.product.vo.Skus;
import com.xiaolanhe.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolanhe.common.utils.PageUtils;
import com.xiaolanhe.common.utils.Query;

import com.xiaolanhe.gulimall.product.dao.SpuInfoDao;
import com.xiaolanhe.gulimall.product.entity.SpuInfoEntity;
import com.xiaolanhe.gulimall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService imagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeginService couponFeginService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeginService wareFeginService;

    @Autowired
    SearchFeginService searchFeginService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {

        // 1. ??????spu???????????? pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        // 2. ??????spu??????????????? pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuInfoEntity.getId());
        descEntity.setDecript(String.join("," ,decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        // 3. ??????spu???????????? pms_spu_images
        List<String> images = vo.getImages();
        imagesService.saveImages(spuInfoEntity.getId(), images);

        // 4. ??????spu??????????????? pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
            attrValueEntity.setAttrId(attr.getAttrId());
            attrValueEntity.setAttrValue(attr.getAttrValues());
            attrValueEntity.setQuickShow(attr.getShowDesc());
            attrValueEntity.setSpuId(spuInfoEntity.getId());

            AttrEntity id = attrService.getById(attr.getAttrId());
            attrValueEntity.setAttrName(id.getAttrName());
            return attrValueEntity;
        }).collect(Collectors.toList());

        attrValueService.saveProductAttr(collect);

        // 5. ????????????spu??????????????????sku??????
        List<Skus> skus = vo.getSkus();

        if(skus != null && skus.size() > 0){
            skus.forEach(item -> {

                // 5.1 sku??????????????? pms_sku_info
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());

                String defaultImg = "";
                for(Images image : item.getImages())
                {
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                skuInfoEntity.setSkuDefaultImg(defaultImg);

                skuInfoService.saveSkuInfo(skuInfoEntity);

                // sku ?????????????????????????????? id ??????????????????
                Long skuId = skuInfoEntity.getSkuId();

                // 5.2  sku??????????????? pms_sku_images
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity imagesEntity = new SkuImagesEntity();
                    imagesEntity.setSkuId(skuId);
                    imagesEntity.setImgUrl(img.getImgUrl());
                    imagesEntity.setDefaultImg(img.getDefaultImg());
                    return imagesEntity;
                }).filter(entity -> {
                    // ?????? true ??????????????? false????????????
                    return !StringUtils.isEmpty(entity.getImgUrl());

                }).collect(Collectors.toList());

                skuImagesService.saveBatch(imagesEntities);

                // 5.3  sku???????????????  pms_sku_sale_attr_value
                List<Attr> attrs = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // 5.4 sku??????????????????????????????gulimall_sms -> sms_sku_ladder  sms_sku_full_reduction sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                skuReductionTo.setSkuId(skuId);
                BeanUtils.copyProperties(item, skuReductionTo);
                if(skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1)
                {
                    R r = couponFeginService.saveSkuReduction(skuReductionTo);
                    if(r.getCode() != 0)
                    {
                        log.error("????????????sku??????????????????");
                    }
                }

            });
        }

        // 6. ??????spu???????????????  gulimall_sms  -> sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeginService.saveSpuBounds(spuBoundTo);
        if(r.getCode() != 0)
        {
            log.error("??????????????????????????????");
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }


    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name",key);
            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status))
        {
            queryWrapper.eq("publish_status", status);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId))
        {
            queryWrapper.eq("brand_id", brandId);
        }

        String catelogId = (String) params.get("catalogId");
        if(!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId))
        {
            queryWrapper.eq("catalog_id", catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params), queryWrapper);

        return new PageUtils(page);
    }

    @Override
    /**
     * desciption ????????????
      * @param spuId
    */
    public void up(Long spuId) {

        // 1. ????????????spuId???????????????sku??????????????????????????????
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);

        //????????????sku?????????????????????????????????????????????
        List<ProductAttrValueEntity> baseAttrs = attrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        // ??????????????????
        List<Long> searchAttrIds = attrService.selectSearchAttIds(attrIds);
        HashSet<Long> idSet = new HashSet<>(searchAttrIds);

        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());

        // 1.??????????????????????????????????????????????????????
        Map<Long, Boolean> stockMap = null;
        List<Long> skuIdList = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        try{
            R skuHasStock = wareFeginService.getSkuHasStock(skuIdList);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>(){};
            stockMap = skuHasStock.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId,item -> item.isHasStock()));
        }catch (Exception e){
            log.error("????????????????????????: ??????{}", e);
        }

        // 2. ????????????sku?????????
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skuInfoEntities.stream().map(sku -> {
            // ?????????????????????
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);

            // ????????????????????????????????????
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            // ??????????????????
            if(finalStockMap == null){
                skuEsModel.setHasStock(false);
            }else{
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            //????????????
            skuEsModel.setHotScore(0L);

            // 2.????????????????????????????????????
            BrandEntity brand = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());

            CategoryEntity category = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(category.getName());

            // ??????????????????
            skuEsModel.setAttrs(attrsList);

            return skuEsModel;
        }).collect(Collectors.toList());

        R res = searchFeginService.productStatusUp(upProducts);

        if(res.getCode() == 0)
        {
            // ??????????????????
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else {
            // ??????????????????
            // TODO ??????????????? ???????????????
        }
    }

}