import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '学校',
    align:"center",
    dataIndex: 'school'
   },
   {
    title: '教室名称',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '班级人数',
    align:"center",
    dataIndex: 'total'
   },
   {
    title: '班级平均分',
    align:"center",
    dataIndex: 'avg'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "学校",
    field: "school",
    component: 'JInput',
  },
  {
    label: "教室名称",
    field: "name",
    component: 'JInput',
  },
     {
      label: "班级人数",
      field: "total",
      component: 'JRangeNumber',
      //colProps: {span: 6},
	},
     {
      label: "班级平均分",
      field: "avg",
      component: 'JRangeNumber',
      //colProps: {span: 6},
	},
];

//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '学校',
    field: 'school',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入学校!'},
          ];
     },
  },
  {
    label: '教室名称',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入教室名称!'},
          ];
     },
  },
  {
    label: '班级人数',
    field: 'total',
    component: 'InputNumber',
  },
  {
    label: '班级平均分',
    field: 'avg',
    component: 'InputNumber',
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];

//子表列表数据
export const eduClassSubHeadTeacherColumns: BasicColumn[] = [
   {
    title: '班主任姓名',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '性别',
    align:"center",
    dataIndex: 'sex_dictText'
   },
   {
    title: '任教科目',
    align:"center",
    dataIndex: 'subject_dictText'
   },
   {
    title: '教龄',
    align:"center",
    dataIndex: 'tecExp'
   },
   {
    title: '联系方式',
    align:"center",
    dataIndex: 'phone'
   },
];
//子表表单数据
export const eduClassSubHeadTeacherFormSchema: FormSchema[] = [
  // TODO 子表隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false
  },
  {
    label: '班主任姓名',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入班主任姓名!'},
          ];
     },
  },
  {
    label: '性别',
    field: 'sex',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"sex"
     },
  },
  {
    label: '任教科目',
    field: 'subject',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"subject"
     },
  },
  {
    label: '教龄',
    field: 'tecExp',
    component: 'InputNumber',
  },
  {
    label: '联系方式',
    field: 'phone',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: false},
                 { pattern: /^1[3456789]\d{9}$/, message: '请输入正确的手机号码!'},
          ];
     },
  },
];
//子表列表数据
export const eduClassSubColumns: BasicColumn[] = [
   {
    title: '姓名',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '年龄',
    align:"center",
    dataIndex: 'age'
   },
   {
    title: '性别',
    align:"center",
    dataIndex: 'sex_dictText'
   },
   {
    title: '头像',
    align:"center",
    dataIndex: 'avater',
    customRender:render.renderImage,
   },
   {
    title: '学号',
    align:"center",
    dataIndex: 'no'
   },
];
//子表表单数据
export const eduClassSubFormSchema: FormSchema[] = [
  // TODO 子表隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false
  },
  {
    label: '姓名',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入姓名!'},
          ];
     },
  },
  {
    label: '年龄',
    field: 'age',
    component: 'InputNumber',
  },
  {
    label: '性别',
    field: 'sex',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"sex"
     },
  },
  {
    label: '头像',
    field: 'avater',
     component: 'JImageUpload',
     componentProps:{
        fileMax: 0
    },
  },
  {
    label: '学号',
    field: 'no',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入学号!'},
          ];
     },
  },
];
//子表列表数据
export const eduClassSubTeacherColumns: BasicColumn[] = [
   {
    title: '教师名称',
    align:"center",
    dataIndex: 'name'
   },
   {
    title: '年龄',
    align:"center",
    dataIndex: 'age'
   },
   {
    title: '性别',
    align:"center",
    dataIndex: 'sex_dictText'
   },
   {
    title: '任教科目',
    align:"center",
    dataIndex: 'subject_dictText'
   },
   {
    title: '联系方式',
    align:"center",
    dataIndex: 'phone'
   },
];
//子表表单数据
export const eduClassSubTeacherFormSchema: FormSchema[] = [
  // TODO 子表隐藏字段，目前写死为ID
  {
    label: '',
    field: 'id',
    component: 'Input',
    show: false
  },
  {
    label: '教师名称',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入教师名称!'},
          ];
     },
  },
  {
    label: '年龄',
    field: 'age',
    component: 'InputNumber',
  },
  {
    label: '性别',
    field: 'sex',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"sex"
     },
  },
  {
    label: '任教科目',
    field: 'subject',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"subject"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入任教科目!'},
          ];
     },
  },
  {
    label: '联系方式',
    field: 'phone',
    component: 'Input',
  },
];

// 高级查询数据
export const superQuerySchema = {
  school: {title: '学校',order: 0,view: 'text', type: 'string',},
  name: {title: '教室名称',order: 1,view: 'text', type: 'string',},
  total: {title: '班级人数',order: 2,view: 'number', type: 'number',},
  avg: {title: '班级平均分',order: 3,view: 'number', type: 'number',},
};
