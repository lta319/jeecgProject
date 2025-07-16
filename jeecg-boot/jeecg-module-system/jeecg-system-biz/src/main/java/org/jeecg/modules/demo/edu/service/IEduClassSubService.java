package org.jeecg.modules.demo.edu.service;

import org.jeecg.modules.demo.edu.entity.EduClassSub;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
public interface IEduClassSubService extends IService<EduClassSub> {

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId
   * @return List<EduClassSub>
   */
	public List<EduClassSub> selectByMainId(String mainId);
}
