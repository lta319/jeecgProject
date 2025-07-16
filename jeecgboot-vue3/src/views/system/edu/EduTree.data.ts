import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '名称',
    align: 'left',
    dataIndex: 'name'
   },
   {
    title: '数量',
    align: 'center',
    dataIndex: 'total'
   },
   {
    title: '产品图片',
    align: 'center',
    dataIndex: 'img',
    customRender:render.renderImage,
   },
   {
    title: '价格',
    align: 'center',
    dataIndex: 'price'
   },
   {
    title: '生产日期',
    align: 'center',
    dataIndex: 'scDate',
    customRender:({text}) =>{
      text = !text ? "" : (text.length > 10 ? text.substr(0,10) : text);
      return text;
    },
   },
   {
    title: '产品描述',
    align: 'center',
    dataIndex: 'description'
   },
   {
    title: '保质期',
    align: 'center',
    dataIndex: 'period'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "名称",
    field: "name",
    component: 'JInput',
  },
     {
      label: "数量",
      field: "total",
      component: 'JRangeNumber',
      //colProps: {span: 6},
	},
	{
      label: "价格",
      field: "price",
      component: 'InputNumber',
      //colProps: {span: 6},
     },
     {
      label: "生产日期",
      field: "scDate",
      component: 'RangePicker',
      componentProps: {
        valueType: 'Date',
      },
      //colProps: {span: 6},
	},
	{
      label: "保质期",
      field: "period",
      component: 'InputNumber',
      //colProps: {span: 6},
     },
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '父级节点',
    field: 'parentId',
    component: 'JTreeSelect',
    componentProps: {
      dict: "edu_tree,name,id",
      pidField: "parent_id",
      pidValue: "0",
      hasChildField: "has_child",
    },
  },
  {
    label: '名称',
    field: 'name',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入名称!'},
          ];
     },
  },
  {
    label: '数量',
    field: 'total',
    component: 'InputNumber',
  },
  {
    label: '产品图片',
    field: 'img',
     component: 'JImageUpload',
     componentProps:{
        fileMax: 0
      },
  },
  {
    label: '价格',
    field: 'price',
    component: 'InputNumber',
  },
  {
    label: '生产日期',
    field: 'scDate',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD'
    },    
  },
  {
    label: '产品描述',
    field: 'description',
    component: 'InputTextArea',
  },
  {
    label: '保质期',
    field: 'period',
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
  name: {title: '名称',order: 1,view: 'text', type: 'string',},
  total: {title: '数量',order: 2,view: 'number', type: 'number',},
  img: {title: '产品图片',order: 3,view: 'image', type: 'string',},
  price: {title: '价格',order: 4,view: 'number', type: 'number',},
  scDate: {title: '生产日期',order: 5,view: 'date', type: 'string',},
  description: {title: '产品描述',order: 6,view: 'textarea', type: 'string',},
  period: {title: '保质期',order: 7,view: 'number', type: 'number',},
};


/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}
