package org.jeecg.modules.demo.edu.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 物料表
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
@Data
@TableName("wuliao")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description="物料表")
public class Wuliao implements Serializable {
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
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @Schema(description = "物料id")
    private java.lang.String wlId;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @Schema(description = "物料名称")
    private java.lang.String wlName;
	/**图号*/
	@Excel(name = "图号", width = 15)
    @Schema(description = "图号")
    private java.lang.String wlTuhao;
	/**物料规格*/
	@Excel(name = "物料规格", width = 15)
    @Schema(description = "物料规格")
    private java.lang.String wlGuige;
	/**物料属性*/
	@Excel(name = "物料属性", width = 15)
    @Schema(description = "物料属性")
    private java.lang.String wlShuxing;
	/**物料类型*/
	@Excel(name = "物料类型", width = 15)
    @Schema(description = "物料类型")
    private java.lang.String wlLeixing;
	/**单位*/
	@Excel(name = "单位", width = 15, dictTable = "danwei", dicText = "wl_danwei", dicCode = "id")
	@Dict(dictTable = "danwei", dicText = "wl_danwei", dicCode = "id")
    @Schema(description = "单位")
    private java.lang.String wlDanwei;
	/**仓库*/
	@Excel(name = "仓库", width = 15)
    @Schema(description = "仓库")
    private java.lang.String wlCangku;
	/**客户物料名称*/
	@Excel(name = "客户物料名称", width = 15)
    @Schema(description = "客户物料名称")
    private java.lang.String wlKehuName;
	/**客户物料图号*/
	@Excel(name = "客户物料图号", width = 15)
    @Schema(description = "客户物料图号")
    private java.lang.String wlKehuTuhao;
	/**客户物料规格*/
	@Excel(name = "客户物料规格", width = 15)
    @Schema(description = "客户物料规格")
    private java.lang.String wlKehuGuige;
	/**材质*/
	@Excel(name = "材质", width = 15)
    @Schema(description = "材质")
    private java.lang.String wlCaizhi;
	/**物料最高库存*/
	@Excel(name = "物料最高库存", width = 15)
    @Schema(description = "物料最高库存")
    private java.lang.Integer wlMaxkucun;
	/**物料最低库存*/
	@Excel(name = "物料最低库存", width = 15)
    @Schema(description = "物料最低库存")
    private java.lang.Integer wlMinkucun;
}
