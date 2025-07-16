package org.jeecg.modules.demo.edu.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 班主任表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Data
@TableName("edu_class_sub_head_teacher")
@Schema(description="班主任表")
public class EduClassSubHeadTeacher implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键")
    private java.lang.String id;
	/**创建人*/
    @Schema(description = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**班主任姓名*/
	@Excel(name = "班主任姓名", width = 15)
    @Schema(description = "班主任姓名")
    private java.lang.String name;
	/**性别*/
	@Excel(name = "性别", width = 15)
    @Dict(dicCode = "sex")
    @Schema(description = "性别")
    private java.lang.String sex;
	/**任教科目*/
	@Excel(name = "任教科目", width = 15)
    @Dict(dicCode = "subject")
    @Schema(description = "任教科目")
    private java.lang.String subject;
	/**教龄*/
	@Excel(name = "教龄", width = 15)
    @Schema(description = "教龄")
    private java.lang.Integer tecExp;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @Schema(description = "联系方式")
    private java.lang.String phone;
	/**主表id*/
    @Schema(description = "主表id")
    private java.lang.String mainId;
}
