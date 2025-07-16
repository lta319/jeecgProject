package org.jeecg.modules.demo.edu.service.impl;

import org.jeecg.modules.demo.edu.entity.EduClassSub;
import org.jeecg.modules.demo.edu.mapper.EduClassSubMapper;
import org.jeecg.modules.demo.edu.service.IEduClassSubService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 学生表
 * @Author: jeecg-boot
 * @Date:   2025-07-16
 * @Version: V1.0
 */
@Service
public class EduClassSubServiceImpl extends ServiceImpl<EduClassSubMapper, EduClassSub> implements IEduClassSubService {
	
	@Autowired
	private EduClassSubMapper eduClassSubMapper;
	
	@Override
	public List<EduClassSub> selectByMainId(String mainId) {
		return eduClassSubMapper.selectByMainId(mainId);
	}
}
