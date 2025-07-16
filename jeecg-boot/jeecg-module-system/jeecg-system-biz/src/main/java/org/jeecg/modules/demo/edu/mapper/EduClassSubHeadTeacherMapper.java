package org.jeecg.modules.demo.edu.mapper;

import java.util.List;
import org.jeecg.modules.demo.edu.entity.EduClassSubHeadTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 班主任表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
public interface EduClassSubHeadTeacherMapper extends BaseMapper<EduClassSubHeadTeacher> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

   /**
    * 通过主表id查询子表数据
    *
    * @param mainId 主表id
    * @return List<EduClassSubHeadTeacher>
    */
	public List<EduClassSubHeadTeacher> selectByMainId(@Param("mainId") String mainId);

}
