import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '物料id',
    align:"center",
    dataIndex: 'wlId'
   },
   {
    title: '物料名称',
    align:"center",
    dataIndex: 'wlName'
   },
   {
    title: '图号',
    align:"center",
    dataIndex: 'wlTuhao'
   },
   {
    title: '物料规格',
    align:"center",
    dataIndex: 'wlGuige'
   },
   {
    title: '物料属性',
    align:"center",
    dataIndex: 'wlShuxing_dictText'
   },
   {
    title: '物料类型',
    align:"center",
    dataIndex: 'wlLeixing_dictText'
   },
   {
    title: '单位',
    align:"center",
    dataIndex: 'wlDanwei_dictText'
   },
   {
    title: '仓库',
    align:"center",
    dataIndex: 'wlCangku_dictText'
   },
   {
    title: '客户物料名称',
    align:"center",
    dataIndex: 'wlKehuName'
   },
   {
    title: '客户物料图号',
    align:"center",
    dataIndex: 'wlKehuTuhao'
   },
   {
    title: '客户物料规格',
    align:"center",
    dataIndex: 'wlKehuGuige'
   },
   {
    title: '材质',
    align:"center",
    dataIndex: 'wlCaizhi'
   },
   {
    title: '物料最高库存',
    align:"center",
    dataIndex: 'wlMaxkucun'
   },
   {
    title: '物料最低库存',
    align:"center",
    dataIndex: 'wlMinkucun'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "物料id",
    field: "wlId",
    component: 'JInput',
  },
  {
    label: "物料名称",
    field: "wlName",
    component: 'JInput',
  },
  {
    label: "图号",
    field: "wlTuhao",
    component: 'JInput',
  },
  {
    label: "物料规格",
    field: "wlGuige",
    component: 'JInput',
  },
	{
      label: "物料属性",
      field: 'wlShuxing',
      component: 'JSelectMultiple',
      componentProps:{
      },
      //colProps: {span: 6},
 	},
	{
      label: "物料类型",
      field: 'wlLeixing',
      component: 'JSelectMultiple',
      componentProps:{
      },
      //colProps: {span: 6},
 	},
	{
      label: "单位",
      field: 'wlDanwei',
      component: 'JSelectMultiple',
      componentProps:{
          dictCode:"danwei,wl_danwei,id"
      },
      //colProps: {span: 6},
 	},
	{
      label: "仓库",
      field: 'wlCangku',
      component: 'JSelectMultiple',
      componentProps:{
      },
      //colProps: {span: 6},
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '物料id',
    field: 'wlId',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入物料id!'},
          ];
     },
  },
  {
    label: '物料名称',
    field: 'wlName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入物料名称!'},
          ];
     },
  },
  {
    label: '图号',
    field: 'wlTuhao',
    component: 'Input',
  },
  {
    label: '物料规格',
    field: 'wlGuige',
    component: 'Input',
  },
  {
    label: '物料属性',
    field: 'wlShuxing',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:""
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入物料属性!'},
          ];
     },
  },
  {
    label: '物料类型',
    field: 'wlLeixing',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:""
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入物料类型!'},
          ];
     },
  },
  {
    label: '单位',
    field: 'wlDanwei',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"danwei,wl_danwei,id"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入单位!'},
          ];
     },
  },
  {
    label: '仓库',
    field: 'wlCangku',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:""
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入仓库!'},
          ];
     },
  },
  {
    label: '客户物料名称',
    field: 'wlKehuName',
    component: 'Input',
  },
  {
    label: '客户物料图号',
    field: 'wlKehuTuhao',
    component: 'Input',
  },
  {
    label: '客户物料规格',
    field: 'wlKehuGuige',
    component: 'Input',
  },
  {
    label: '材质',
    field: 'wlCaizhi',
    component: 'Input',
  },
  {
    label: '物料最高库存',
    field: 'wlMaxkucun',
    component: 'InputNumber',
  },
  {
    label: '物料最低库存',
    field: 'wlMinkucun',
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
  wlId: {title: '物料id',order: 0,view: 'text', type: 'string',},
  wlName: {title: '物料名称',order: 1,view: 'text', type: 'string',},
  wlTuhao: {title: '图号',order: 2,view: 'text', type: 'string',},
  wlGuige: {title: '物料规格',order: 3,view: 'text', type: 'string',},
  wlShuxing: {title: '物料属性',order: 4,view: 'list', type: 'string',dictCode: '',},
  wlLeixing: {title: '物料类型',order: 5,view: 'list', type: 'string',dictCode: '',},
  wlDanwei: {title: '单位',order: 6,view: 'list', type: 'string',dictTable: "danwei", dictCode: 'id', dictText: 'wl_danwei',},
  wlCangku: {title: '仓库',order: 7,view: 'list', type: 'string',dictCode: '',},
  wlKehuName: {title: '客户物料名称',order: 8,view: 'text', type: 'string',},
  wlKehuTuhao: {title: '客户物料图号',order: 9,view: 'text', type: 'string',},
  wlKehuGuige: {title: '客户物料规格',order: 10,view: 'text', type: 'string',},
  wlCaizhi: {title: '材质',order: 11,view: 'text', type: 'string',},
  wlMaxkucun: {title: '物料最高库存',order: 12,view: 'number', type: 'number',},
  wlMinkucun: {title: '物料最低库存',order: 13,view: 'number', type: 'number',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}