package org.jeecg.modules.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.modules.edu.entity.Edu;
import org.jeecg.modules.edu.mapper.EduMapper;
import org.jeecg.modules.edu.service.IEduService;
import org.jeecg.modules.system.entity.SysPosition;
import org.jeecg.modules.system.mapper.SysPositionMapper;
import org.jeecg.modules.system.service.ISysPositionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 职务表
 * @Author: jeecg-boot
 * @Date: 2019-09-19
 * @Version: V1.0
 */
@Service
public class EduServiceImpl extends ServiceImpl<EduMapper, Edu> implements IEduService {

}
