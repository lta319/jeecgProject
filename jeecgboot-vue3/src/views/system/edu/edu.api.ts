import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/sys/edu/list',
  save = '/sys/edu/add',
  edit = '/sys/edu/edit',
  get = '/sys/edu/queryById',
  delete = '/sys/edu/delete',
  importExcel = '/sys/edu/importExcel',
  exportXls = '/sys/edu/exportXls',
  deleteBatch = '/sys/edu/deleteBatch',
}
/**
 * 导出api
 */
export const getExportUrl = Api.exportXls;

export const getImportUrl = Api.importExcel;
/**
 * 查询列表
 * @param params
 */
export const getPositionList = (params) => {
  return defHttp.get({ url: Api.list, params });
};

/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdatePosition = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url: url, params });
};

/**
 * 查询详情
 * @param params
 */
export const getPositionById = (params) => {
  return defHttp.get({ url: Api.get, params });
};

/**
 * 单条删除
 * @param params
 */
export const deletePosition = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};
// export const deletePosition = async (params, handleSuccess) => {
//   await defHttp.delete({ url: Api.delete, data: params }, { joinParamsToUrl: true });
//   handleSuccess();
// };
/**
 * 批量删除
 * @param params
 */
export const batchDeletePosition = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};

/**
 * 自定义上传
 * @param customUpload
 */
export const customUpload = (params) => {
  defHttp.uploadFile({ url: Api.importExcel }, params);
};
