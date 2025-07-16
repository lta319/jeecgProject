package org.jeecg.modules.demo.edu.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.jeecg.common.constant.ProvinceCityArea;
import org.jeecg.common.util.SpringContextUtils;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 商品物料表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Data
@TableName("edu_tree")
@Schema(description="商品物料表")
public class EduTree implements Serializable {
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
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private java.util.Date updateTime;
	/**是否有子节点*/
	@Excel(name = "是否有子节点", width = 15, dicCode = "yn")
	@Dict(dicCode = "yn")
    @Schema(description = "是否有子节点")
    private java.lang.String hasChild;
	/**更新人*/
    @Schema(description = "更新人")
    private java.lang.String updateBy;
	/**父级节点*/
	@Excel(name = "父级节点", width = 15)
    @Schema(description = "父级节点")
    private java.lang.String parentId;
	/**名称*/
	@Excel(name = "名称", width = 15)
    @Schema(description = "名称")
    private java.lang.String name;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @Schema(description = "数量")
    private java.lang.Integer total;
	/**产品图片*/
	@Excel(name = "产品图片", width = 15)
    @Schema(description = "产品图片")
    private java.lang.String img;
	/**所属部门*/
    @Schema(description = "所属部门")
    private java.lang.String sysOrgCode;
	/**价格*/
	@Excel(name = "价格", width = 15)
    @Schema(description = "价格")
    private java.math.BigDecimal price;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Schema(description = "生产日期")
    private java.util.Date scDate;
	/**产品描述*/
	@Excel(name = "产品描述", width = 15)
    @Schema(description = "产品描述")
    private java.lang.String description;
	/**保质期*/
	@Excel(name = "保质期", width = 15)
    @Schema(description = "保质期")
    private java.lang.Integer period;
}
