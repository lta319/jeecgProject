package org.jeecg.modules.demo.edu.service;

import org.jeecg.modules.demo.edu.entity.EduClassSubHeadTeacher;
import org.jeecg.modules.demo.edu.entity.EduClassSub;
import org.jeecg.modules.demo.edu.entity.EduClassSubTeacher;
import org.jeecg.modules.demo.edu.entity.EduClass;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 教室主表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
public interface IEduClassService extends IService<EduClass> {

	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


}
