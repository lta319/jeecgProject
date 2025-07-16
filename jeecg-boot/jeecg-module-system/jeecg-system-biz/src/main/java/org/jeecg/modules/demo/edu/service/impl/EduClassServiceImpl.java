package org.jeecg.modules.demo.edu.service.impl;

import org.jeecg.modules.demo.edu.entity.EduClass;
import org.jeecg.modules.demo.edu.entity.EduClassSubHeadTeacher;
import org.jeecg.modules.demo.edu.entity.EduClassSub;
import org.jeecg.modules.demo.edu.entity.EduClassSubTeacher;
import org.jeecg.modules.demo.edu.mapper.EduClassSubHeadTeacherMapper;
import org.jeecg.modules.demo.edu.mapper.EduClassSubMapper;
import org.jeecg.modules.demo.edu.mapper.EduClassSubTeacherMapper;
import org.jeecg.modules.demo.edu.mapper.EduClassMapper;
import org.jeecg.modules.demo.edu.service.IEduClassService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 教室主表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Service
public class EduClassServiceImpl extends ServiceImpl<EduClassMapper, EduClass> implements IEduClassService {

	@Autowired
	private EduClassMapper eduClassMapper;
	@Autowired
	private EduClassSubHeadTeacherMapper eduClassSubHeadTeacherMapper;
	@Autowired
	private EduClassSubMapper eduClassSubMapper;
	@Autowired
	private EduClassSubTeacherMapper eduClassSubTeacherMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		eduClassSubHeadTeacherMapper.deleteByMainId(id);
		eduClassSubMapper.deleteByMainId(id);
		eduClassSubTeacherMapper.deleteByMainId(id);
		eduClassMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			eduClassSubHeadTeacherMapper.deleteByMainId(id.toString());
			eduClassSubMapper.deleteByMainId(id.toString());
			eduClassSubTeacherMapper.deleteByMainId(id.toString());
			eduClassMapper.deleteById(id);
		}
	}
	
}
