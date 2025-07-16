package org.jeecg.modules.demo.edu.mapper;

import java.util.List;
import org.jeecg.modules.demo.edu.entity.EduClassSubTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 教师表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
public interface EduClassSubTeacherMapper extends BaseMapper<EduClassSubTeacher> {

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
    * @return List<EduClassSubTeacher>
    */
	public List<EduClassSubTeacher> selectByMainId(@Param("mainId") String mainId);

}
