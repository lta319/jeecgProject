package org.jeecg.modules.demo.edu.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.system.query.QueryRuleEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import java.util.HashMap;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.edu.entity.EduClassSubHeadTeacher;
import org.jeecg.modules.demo.edu.entity.EduClassSub;
import org.jeecg.modules.demo.edu.entity.EduClassSubTeacher;
import org.jeecg.modules.demo.edu.entity.EduClass;
import org.jeecg.modules.demo.edu.service.IEduClassService;
import org.jeecg.modules.demo.edu.service.IEduClassSubHeadTeacherService;
import org.jeecg.modules.demo.edu.service.IEduClassSubService;
import org.jeecg.modules.demo.edu.service.IEduClassSubTeacherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.shiro.authz.annotation.RequiresPermissions;
 /**
 * @Description: 教室主表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Tag(name="教室主表")
@RestController
@RequestMapping("/edu/eduClass")
@Slf4j
public class EduClassController extends JeecgController<EduClass, IEduClassService> {

	@Autowired
	private IEduClassService eduClassService;

	@Autowired
	private IEduClassSubHeadTeacherService eduClassSubHeadTeacherService;

	@Autowired
	private IEduClassSubService eduClassSubService;

	@Autowired
	private IEduClassSubTeacherService eduClassSubTeacherService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param eduClass
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "教室主表-分页列表查询")
	@Operation(summary="教室主表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<EduClass>> queryPageList(EduClass eduClass,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
      	QueryWrapper<EduClass> queryWrapper = QueryGenerator.initQueryWrapper(eduClass, req.getParameterMap());
		Page<EduClass> page = new Page<EduClass>(pageNo, pageSize);
		IPage<EduClass> pageList = eduClassService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
     *   添加
     * @param eduClass
     * @return
     */
    @AutoLog(value = "教室主表-添加")
    @Operation(summary="教室主表-添加")
    @RequiresPermissions("edu:edu_class:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody EduClass eduClass) {
        eduClassService.save(eduClass);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     * @param eduClass
     * @return
     */
    @AutoLog(value = "教室主表-编辑")
    @Operation(summary="教室主表-编辑")
    @RequiresPermissions("edu:edu_class:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody EduClass eduClass) {
        eduClassService.updateById(eduClass);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @AutoLog(value = "教室主表-通过id删除")
    @Operation(summary="教室主表-通过id删除")
    @RequiresPermissions("edu:edu_class:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        eduClassService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @AutoLog(value = "教室主表-批量删除")
    @Operation(summary="教室主表-批量删除")
    @RequiresPermissions("edu:edu_class:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.eduClassService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequiresPermissions("edu:edu_class:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EduClass eduClass) {
        return super.exportXls(request, eduClass, EduClass.class, "教室主表");
    }

    /**
     * 导入
     * @return
     */
    @RequiresPermissions("edu:edu_class:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EduClass.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-班主任表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	//@AutoLog(value = "班主任表-通过主表ID查询")
	@Operation(summary="班主任表-通过主表ID查询")
	@GetMapping(value = "/listEduClassSubHeadTeacherByMainId")
    public Result<IPage<EduClassSubHeadTeacher>> listEduClassSubHeadTeacherByMainId(EduClassSubHeadTeacher eduClassSubHeadTeacher,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<EduClassSubHeadTeacher> queryWrapper = QueryGenerator.initQueryWrapper(eduClassSubHeadTeacher, req.getParameterMap());
        Page<EduClassSubHeadTeacher> page = new Page<EduClassSubHeadTeacher>(pageNo, pageSize);
        IPage<EduClassSubHeadTeacher> pageList = eduClassSubHeadTeacherService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param eduClassSubHeadTeacher
	 * @return
	 */
	@AutoLog(value = "班主任表-添加")
	@Operation(summary="班主任表-添加")
	@PostMapping(value = "/addEduClassSubHeadTeacher")
	public Result<String> addEduClassSubHeadTeacher(@RequestBody EduClassSubHeadTeacher eduClassSubHeadTeacher) {
		eduClassSubHeadTeacherService.save(eduClassSubHeadTeacher);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param eduClassSubHeadTeacher
	 * @return
	 */
	@AutoLog(value = "班主任表-编辑")
	@Operation(summary="班主任表-编辑")
	@RequestMapping(value = "/editEduClassSubHeadTeacher", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> editEduClassSubHeadTeacher(@RequestBody EduClassSubHeadTeacher eduClassSubHeadTeacher) {
		eduClassSubHeadTeacherService.updateById(eduClassSubHeadTeacher);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班主任表-通过id删除")
	@Operation(summary="班主任表-通过id删除")
	@DeleteMapping(value = "/deleteEduClassSubHeadTeacher")
	public Result<String> deleteEduClassSubHeadTeacher(@RequestParam(name="id",required=true) String id) {
		eduClassSubHeadTeacherService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "班主任表-批量删除")
	@Operation(summary="班主任表-批量删除")
	@DeleteMapping(value = "/deleteBatchEduClassSubHeadTeacher")
	public Result<String> deleteBatchEduClassSubHeadTeacher(@RequestParam(name="ids",required=true) String ids) {
	    this.eduClassSubHeadTeacherService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportEduClassSubHeadTeacher")
    public ModelAndView exportEduClassSubHeadTeacher(HttpServletRequest request, EduClassSubHeadTeacher eduClassSubHeadTeacher) {
		 // Step.1 组装查询条件
		 QueryWrapper<EduClassSubHeadTeacher> queryWrapper = QueryGenerator.initQueryWrapper(eduClassSubHeadTeacher, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<EduClassSubHeadTeacher> pageList = eduClassSubHeadTeacherService.list(queryWrapper);
		 List<EduClassSubHeadTeacher> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 //此处设置的filename无效,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.FILE_NAME, "班主任表");
		 mv.addObject(NormalExcelConstants.CLASS, EduClassSubHeadTeacher.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("班主任表报表", "导出人:" + sysUser.getRealname(), "班主任表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importEduClassSubHeadTeacher/{mainId}")
    public Result<?> importEduClassSubHeadTeacher(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
       // 获取上传文件对象
			 MultipartFile file = entity.getValue();
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<EduClassSubHeadTeacher> list = ExcelImportUtil.importExcel(file.getInputStream(), EduClassSubHeadTeacher.class, params);
				 for (EduClassSubHeadTeacher temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 eduClassSubHeadTeacherService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
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
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-班主任表-end----------------------------------------------*/

    /*--------------------------------子表处理-学生表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	//@AutoLog(value = "学生表-通过主表ID查询")
	@Operation(summary="学生表-通过主表ID查询")
	@GetMapping(value = "/listEduClassSubByMainId")
    public Result<IPage<EduClassSub>> listEduClassSubByMainId(EduClassSub eduClassSub,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<EduClassSub> queryWrapper = QueryGenerator.initQueryWrapper(eduClassSub, req.getParameterMap());
        Page<EduClassSub> page = new Page<EduClassSub>(pageNo, pageSize);
        IPage<EduClassSub> pageList = eduClassSubService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param eduClassSub
	 * @return
	 */
	@AutoLog(value = "学生表-添加")
	@Operation(summary="学生表-添加")
	@PostMapping(value = "/addEduClassSub")
	public Result<String> addEduClassSub(@RequestBody EduClassSub eduClassSub) {
		eduClassSubService.save(eduClassSub);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param eduClassSub
	 * @return
	 */
	@AutoLog(value = "学生表-编辑")
	@Operation(summary="学生表-编辑")
	@RequestMapping(value = "/editEduClassSub", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> editEduClassSub(@RequestBody EduClassSub eduClassSub) {
		eduClassSubService.updateById(eduClassSub);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学生表-通过id删除")
	@Operation(summary="学生表-通过id删除")
	@DeleteMapping(value = "/deleteEduClassSub")
	public Result<String> deleteEduClassSub(@RequestParam(name="id",required=true) String id) {
		eduClassSubService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学生表-批量删除")
	@Operation(summary="学生表-批量删除")
	@DeleteMapping(value = "/deleteBatchEduClassSub")
	public Result<String> deleteBatchEduClassSub(@RequestParam(name="ids",required=true) String ids) {
	    this.eduClassSubService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportEduClassSub")
    public ModelAndView exportEduClassSub(HttpServletRequest request, EduClassSub eduClassSub) {
		 // Step.1 组装查询条件
		 QueryWrapper<EduClassSub> queryWrapper = QueryGenerator.initQueryWrapper(eduClassSub, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<EduClassSub> pageList = eduClassSubService.list(queryWrapper);
		 List<EduClassSub> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 //此处设置的filename无效,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.FILE_NAME, "学生表");
		 mv.addObject(NormalExcelConstants.CLASS, EduClassSub.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("学生表报表", "导出人:" + sysUser.getRealname(), "学生表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importEduClassSub/{mainId}")
    public Result<?> importEduClassSub(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
       // 获取上传文件对象
			 MultipartFile file = entity.getValue();
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<EduClassSub> list = ExcelImportUtil.importExcel(file.getInputStream(), EduClassSub.class, params);
				 for (EduClassSub temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 eduClassSubService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
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
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-学生表-end----------------------------------------------*/

    /*--------------------------------子表处理-教师表-begin----------------------------------------------*/
	/**
	 * 通过主表ID查询
	 * @return
	 */
	//@AutoLog(value = "教师表-通过主表ID查询")
	@Operation(summary="教师表-通过主表ID查询")
	@GetMapping(value = "/listEduClassSubTeacherByMainId")
    public Result<IPage<EduClassSubTeacher>> listEduClassSubTeacherByMainId(EduClassSubTeacher eduClassSubTeacher,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<EduClassSubTeacher> queryWrapper = QueryGenerator.initQueryWrapper(eduClassSubTeacher, req.getParameterMap());
        Page<EduClassSubTeacher> page = new Page<EduClassSubTeacher>(pageNo, pageSize);
        IPage<EduClassSubTeacher> pageList = eduClassSubTeacherService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

	/**
	 * 添加
	 * @param eduClassSubTeacher
	 * @return
	 */
	@AutoLog(value = "教师表-添加")
	@Operation(summary="教师表-添加")
	@PostMapping(value = "/addEduClassSubTeacher")
	public Result<String> addEduClassSubTeacher(@RequestBody EduClassSubTeacher eduClassSubTeacher) {
		eduClassSubTeacherService.save(eduClassSubTeacher);
		return Result.OK("添加成功！");
	}

    /**
	 * 编辑
	 * @param eduClassSubTeacher
	 * @return
	 */
	@AutoLog(value = "教师表-编辑")
	@Operation(summary="教师表-编辑")
	@RequestMapping(value = "/editEduClassSubTeacher", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> editEduClassSubTeacher(@RequestBody EduClassSubTeacher eduClassSubTeacher) {
		eduClassSubTeacherService.updateById(eduClassSubTeacher);
		return Result.OK("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "教师表-通过id删除")
	@Operation(summary="教师表-通过id删除")
	@DeleteMapping(value = "/deleteEduClassSubTeacher")
	public Result<String> deleteEduClassSubTeacher(@RequestParam(name="id",required=true) String id) {
		eduClassSubTeacherService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "教师表-批量删除")
	@Operation(summary="教师表-批量删除")
	@DeleteMapping(value = "/deleteBatchEduClassSubTeacher")
	public Result<String> deleteBatchEduClassSubTeacher(@RequestParam(name="ids",required=true) String ids) {
	    this.eduClassSubTeacherService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportEduClassSubTeacher")
    public ModelAndView exportEduClassSubTeacher(HttpServletRequest request, EduClassSubTeacher eduClassSubTeacher) {
		 // Step.1 组装查询条件
		 QueryWrapper<EduClassSubTeacher> queryWrapper = QueryGenerator.initQueryWrapper(eduClassSubTeacher, request.getParameterMap());
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<EduClassSubTeacher> pageList = eduClassSubTeacherService.list(queryWrapper);
		 List<EduClassSubTeacher> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 //此处设置的filename无效,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.FILE_NAME, "教师表");
		 mv.addObject(NormalExcelConstants.CLASS, EduClassSubTeacher.class);
		 mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("教师表报表", "导出人:" + sysUser.getRealname(), "教师表"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		 return mv;
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importEduClassSubTeacher/{mainId}")
    public Result<?> importEduClassSubTeacher(HttpServletRequest request, HttpServletResponse response, @PathVariable("mainId") String mainId) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
       // 获取上传文件对象
			 MultipartFile file = entity.getValue();
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
				 List<EduClassSubTeacher> list = ExcelImportUtil.importExcel(file.getInputStream(), EduClassSubTeacher.class, params);
				 for (EduClassSubTeacher temp : list) {
                    temp.setMainId(mainId);
				 }
				 long start = System.currentTimeMillis();
				 eduClassSubTeacherService.saveBatch(list);
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 return Result.OK("文件导入成功！数据行数：" + list.size());
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
		 return Result.error("文件导入失败！");
    }

    /*--------------------------------子表处理-教师表-end----------------------------------------------*/




}
