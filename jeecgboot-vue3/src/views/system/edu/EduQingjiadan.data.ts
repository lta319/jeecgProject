import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '请假人',
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
    title: '请假缘由',
    align:"center",
    dataIndex: 'remark',
   },
   {
    title: '开始时间',
    align:"center",
    dataIndex: 'beginDate'
   },
   {
    title: '结束时间',
    align:"center",
    dataIndex: 'endDate'
   },
   {
    title: '请假天数',
    align:"center",
    dataIndex: 'days'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "请假人",
    field: "name",
    component: 'JInput',
  },
	{
      label: "性别",
      field: 'sex',
      component: 'JSelectMultiple',
      componentProps:{
          dictCode:"sex"
      },
      //colProps: {span: 6},
 	},
     {
      label: "开始时间",
      field: "beginDate",
      component: 'RangePicker',
      componentProps: {
          valueType: 'Date',
          showTime:true
      },
      //colProps: {span: 6},
	},
	{
      label: "请假天数",
      field: 'days',
      component: 'InputNumber',
      //colProps: {span: 6},
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '请假人',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入请假人!'},
                 {...rules.duplicateCheckRule('edu_qingjiadan', 'name',model,schema)[0]},
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
    label: '请假缘由',
    field: 'remark',
    component: 'JEditor',
  },
  {
    label: '开始时间',
    field: 'beginDate',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '结束时间',
    field: 'endDate',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '请假天数',
    field: 'days',
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

// 高级查询数据
export const superQuerySchema = {
  name: {title: '请假人',order: 0,view: 'text', type: 'string',},
  age: {title: '年龄',order: 1,view: 'number', type: 'number',},
  sex: {title: '性别',order: 2,view: 'list', type: 'string',dictCode: 'sex',},
  remark: {title: '请假缘由',order: 3,view: 'umeditor', type: 'string',},
  beginDate: {title: '开始时间',order: 4,view: 'datetime', type: 'string',},
  endDate: {title: '结束时间',order: 5,view: 'datetime', type: 'string',},
  days: {title: '请假天数',order: 6,view: 'number', type: 'number',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}