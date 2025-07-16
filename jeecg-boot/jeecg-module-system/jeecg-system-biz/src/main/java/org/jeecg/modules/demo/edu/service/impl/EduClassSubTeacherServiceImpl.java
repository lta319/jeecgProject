package org.jeecg.modules.demo.edu.service.impl;

import org.jeecg.modules.demo.edu.entity.EduClassSubTeacher;
import org.jeecg.modules.demo.edu.mapper.EduClassSubTeacherMapper;
import org.jeecg.modules.demo.edu.service.IEduClassSubTeacherService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 教师表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Service
public class EduClassSubTeacherServiceImpl extends ServiceImpl<EduClassSubTeacherMapper, EduClassSubTeacher> implements IEduClassSubTeacherService {
	
	@Autowired
	private EduClassSubTeacherMapper eduClassSubTeacherMapper;
	
	@Override
	public List<EduClassSubTeacher> selectByMainId(String mainId) {
		return eduClassSubTeacherMapper.selectByMainId(mainId);
	}
}
