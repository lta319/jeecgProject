package org.jeecg.modules.edu.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.edu.entity.Edu;
import org.jeecg.modules.edu.service.IEduService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Slf4j
@Tag(name = "demo")
@RestController
@RequestMapping("/sys/edu")
public class EduController {

    @Autowired
    private IEduService eduService;

    /**
     * 分页列表查询
     *
     * @param sysPosition
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "职务表-分页列表查询")
    @Operation(summary = "职务表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Edu>> queryPageList(Edu sysPosition,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        Result<IPage<Edu>> result = new Result<>();
        //------------------------------------------------------------------------------------------------
        //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
        if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
            sysPosition.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(),0));
        }
        //------------------------------------------------------------------------------------------------
        QueryWrapper<Edu> queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, req.getParameterMap());
        Page<Edu> page = new Page<>(pageNo, pageSize);
        IPage<Edu> pageList = eduService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysPosition
     * @return
     */
    @AutoLog(value = "职务表-添加")
    @Operation(summary = "职务表-添加")
    @PostMapping(value = "/add")
    public Result<Edu> add(@RequestBody Edu sysPosition) {
        Result<Edu> result = new Result<>();
        try {
            //update-end---author:wangshuai ---date:20230313  for：【QQYUN-4558】vue3职位功能调整，去掉编码和级别，可以先隐藏-------------
            eduService.save(sysPosition);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     * @param sysPosition
     * @return
     */
    @AutoLog(value = "职务表-编辑")
    @Operation(summary = "职务表-编辑")
    @RequestMapping(value = "/edit", method ={RequestMethod.PUT, RequestMethod.POST})
    public Result<Edu> edit(@RequestBody Edu sysPosition) {
        Result<Edu> result = new Result<>();
        Edu sysPositionEntity = eduService.getById(sysPosition.getId());
        if (sysPositionEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = eduService.updateById(sysPosition);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
            }
        }

        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "职务表-通过id删除")
    @Operation(summary = "职务表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        try {
            eduService.removeById(id);
        } catch (Exception e) {
            log.error("删除失败", e.getMessage());
            return Result.error("删除失败!");
        }
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "职务表-批量删除")
    @Operation(summary = "职务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<Edu> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<Edu> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.eduService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }
    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "职务表-通过id查询")
    @Operation(summary = "职务表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Edu> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<Edu> result = new Result<>();
        Edu sysPosition = eduService.getById(id);
        if (sysPosition == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysPosition);
            result.setSuccess(true);
        }
        return result;
    }
    /**
     * 导出excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(Edu sysPosition,HttpServletRequest request, HttpServletResponse response) {
        // Step.1 组装查询条件
        QueryWrapper<Edu> queryWrapper = null;
        try {
            String paramsStr = request.getParameter("paramsStr");
            if (oConvertUtils.isNotEmpty(paramsStr)) {
                String deString = URLDecoder.decode(paramsStr, "UTF-8");
                sysPosition = JSON.parseObject(deString, Edu.class);
                //------------------------------------------------------------------------------------------------
                //是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
                if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
                    sysPosition.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(),0));
                }
                //------------------------------------------------------------------------------------------------
            }
            queryWrapper = QueryGenerator.initQueryWrapper(sysPosition, request.getParameterMap());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //update-begin--Author:liusq  Date:20240715 for：[03]职务导出，如果选择数据则只导出相关数据--------------------
        String selections = request.getParameter("selections");
        if(!oConvertUtils.isEmpty(selections)){
            queryWrapper.in("id",selections.split(","));
        }
        //update-end--Author:liusq  Date:20240715 for：[03]职务导出，如果选择数据则只导出相关数据----------------------
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<Edu> pageList = eduService.list(queryWrapper);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "职务表列表");
        mv.addObject(NormalExcelConstants.CLASS, Edu.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("职务表列表数据", "导出人:"+user.getRealname(),"导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }
    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response)throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        List<String> errorMessage = new ArrayList<>();
        int successLines = 0, errorLines = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<Object>  listSysPositions = ExcelImportUtil.importExcel(file.getInputStream(), Edu.class, params);
                List<String> list = ImportExcelUtil.importDateSave(listSysPositions, IEduService.class, errorMessage,CommonConstant.SQL_INDEX_UNIQ_CODE);
                errorLines+=list.size();
                successLines+=(listSysPositions.size()-errorLines);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ImportExcelUtil.imporReturnRes(errorLines,successLines,errorMessage);
    }

}
