package org.jeecg.modules.demo.edu.service.impl;

import org.jeecg.modules.demo.edu.entity.EduClassSubHeadTeacher;
import org.jeecg.modules.demo.edu.mapper.EduClassSubHeadTeacherMapper;
import org.jeecg.modules.demo.edu.service.IEduClassSubHeadTeacherService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 班主任表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Service
public class EduClassSubHeadTeacherServiceImpl extends ServiceImpl<EduClassSubHeadTeacherMapper, EduClassSubHeadTeacher> implements IEduClassSubHeadTeacherService {
	
	@Autowired
	private EduClassSubHeadTeacherMapper eduClassSubHeadTeacherMapper;
	
	@Override
	public List<EduClassSubHeadTeacher> selectByMainId(String mainId) {
		return eduClassSubHeadTeacherMapper.selectByMainId(mainId);
	}
}
