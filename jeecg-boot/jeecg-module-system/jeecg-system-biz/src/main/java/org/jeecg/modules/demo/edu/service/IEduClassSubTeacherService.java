package org.jeecg.modules.demo.edu.service;

import org.jeecg.modules.demo.edu.entity.EduClassSubTeacher;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 教师表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
public interface IEduClassSubTeacherService extends IService<EduClassSubTeacher> {

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId
   * @return List<EduClassSubTeacher>
   */
	public List<EduClassSubTeacher> selectByMainId(String mainId);
}
