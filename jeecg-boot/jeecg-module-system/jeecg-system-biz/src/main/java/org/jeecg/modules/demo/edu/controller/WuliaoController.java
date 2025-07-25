package org.jeecg.modules.demo.edu.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.edu.entity.Wuliao;
import org.jeecg.modules.demo.edu.service.IWuliaoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
 /**
 * @Description: 物料表
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
@Tag(name="物料表")
@RestController
@RequestMapping("/edu/wuliao")
@Slf4j
public class WuliaoController extends JeecgController<Wuliao, IWuliaoService> {
	@Autowired
	private IWuliaoService wuliaoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wuliao
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "物料表-分页列表查询")
	@Operation(summary="物料表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Wuliao>> queryPageList(Wuliao wuliao,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {


        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
        customeRuleMap.put("wlShuxing", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("wlLeixing", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("wlDanwei", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("wlCangku", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<Wuliao> queryWrapper = QueryGenerator.initQueryWrapper(wuliao, req.getParameterMap(),customeRuleMap);
		Page<Wuliao> page = new Page<Wuliao>(pageNo, pageSize);
		IPage<Wuliao> pageList = wuliaoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param wuliao
	 * @return
	 */
	@AutoLog(value = "物料表-添加")
	@Operation(summary="物料表-添加")
	@RequiresPermissions("edu:wuliao:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Wuliao wuliao) {
		wuliaoService.save(wuliao);

		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wuliao
	 * @return
	 */
	@AutoLog(value = "物料表-编辑")
	@Operation(summary="物料表-编辑")
	@RequiresPermissions("edu:wuliao:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Wuliao wuliao) {
		wuliaoService.updateById(wuliao);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "物料表-通过id删除")
	@Operation(summary="物料表-通过id删除")
	@RequiresPermissions("edu:wuliao:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		wuliaoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "物料表-批量删除")
	@Operation(summary="物料表-批量删除")
	@RequiresPermissions("edu:wuliao:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wuliaoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "物料表-通过id查询")
	@Operation(summary="物料表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Wuliao> queryById(@RequestParam(name="id",required=true) String id) {
		Wuliao wuliao = wuliaoService.getById(id);
		if(wuliao==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wuliao);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wuliao
    */
    @RequiresPermissions("edu:wuliao:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Wuliao wuliao) {
        return super.exportXls(request, wuliao, Wuliao.class, "物料表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("edu:wuliao:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Wuliao.class);
    }

}
