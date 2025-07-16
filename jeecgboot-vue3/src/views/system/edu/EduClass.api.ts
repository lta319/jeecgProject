import {defHttp} from '/@/utils/http/axios';
import { useMessage } from "/@/hooks/web/useMessage";

const { createConfirm } = useMessage();

enum Api {
  list = '/edu/eduClass/list',
  save='/edu/eduClass/add',
  edit='/edu/eduClass/edit',
  deleteOne = '/edu/eduClass/delete',
  deleteBatch = '/edu/eduClass/deleteBatch',
  importExcel = '/edu/eduClass/importExcel',
  exportXls = '/edu/eduClass/exportXls',
  eduClassSubHeadTeacherList = '/edu/eduClass/listEduClassSubHeadTeacherByMainId',
  eduClassSubHeadTeacherSave='/edu/eduClass/addEduClassSubHeadTeacher',
  eduClassSubHeadTeacherEdit='/edu/eduClass/editEduClassSubHeadTeacher',
  eduClassSubHeadTeacherDelete = '/edu/eduClass/deleteEduClassSubHeadTeacher',
  eduClassSubHeadTeacherDeleteBatch = '/edu/eduClass/deleteBatchEduClassSubHeadTeacher',
  eduClassSubList = '/edu/eduClass/listEduClassSubByMainId',
  eduClassSubSave='/edu/eduClass/addEduClassSub',
  eduClassSubEdit='/edu/eduClass/editEduClassSub',
  eduClassSubDelete = '/edu/eduClass/deleteEduClassSub',
  eduClassSubDeleteBatch = '/edu/eduClass/deleteBatchEduClassSub',
  eduClassSubTeacherList = '/edu/eduClass/listEduClassSubTeacherByMainId',
  eduClassSubTeacherSave='/edu/eduClass/addEduClassSubTeacher',
  eduClassSubTeacherEdit='/edu/eduClass/editEduClassSubTeacher',
  eduClassSubTeacherDelete = '/edu/eduClass/deleteEduClassSubTeacher',
  eduClassSubTeacherDeleteBatch = '/edu/eduClass/deleteBatchEduClassSubTeacher',
}
/**
 * 导出api
 * @param params
 */
export const getExportUrl = Api.exportXls;

/**
 * 导入api
 */
export const getImportUrl = Api.importExcel;
/**
 * 列表接口
 * @param params
 */
export const list = (params) =>
  defHttp.get({url: Api.list, params});

/**
 * 删除单个
 */
export const deleteOne = (params,handleSuccess) => {
  return defHttp.delete({url: Api.deleteOne, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const batchDelete = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.deleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const saveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({url: url, params});
}
/**
 * 列表接口
 * @param params
 */
export const eduClassSubHeadTeacherList = (params) => {
  if(params['mainId']){
    return defHttp.get({url: Api.eduClassSubHeadTeacherList, params});
  }
  return Promise.resolve({});
}


/**
 * 删除单个
 */
export const eduClassSubHeadTeacherDelete = (params,handleSuccess) => {
  return defHttp.delete({url: Api.eduClassSubHeadTeacherDelete, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const eduClassSubHeadTeacherDeleteBatch = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.eduClassSubHeadTeacherDeleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const  eduClassSubHeadTeacherSaveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.eduClassSubHeadTeacherEdit : Api.eduClassSubHeadTeacherSave;
  return defHttp.post({url: url, params});
}
/**
 * 导入
 */
export const eduClassSubHeadTeacherImportUrl = '/edu/eduClass/importEduClassSubHeadTeacher'

/**
 * 导出
 */
export const eduClassSubHeadTeacherExportXlsUrl = '/edu/eduClass/exportEduClassSubHeadTeacher'
/**
 * 列表接口
 * @param params
 */
export const eduClassSubList = (params) => {
  if(params['mainId']){
    return defHttp.get({url: Api.eduClassSubList, params});
  }
  return Promise.resolve({});
}


/**
 * 删除单个
 */
export const eduClassSubDelete = (params,handleSuccess) => {
  return defHttp.delete({url: Api.eduClassSubDelete, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const eduClassSubDeleteBatch = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.eduClassSubDeleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const  eduClassSubSaveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.eduClassSubEdit : Api.eduClassSubSave;
  return defHttp.post({url: url, params});
}
/**
 * 导入
 */
export const eduClassSubImportUrl = '/edu/eduClass/importEduClassSub'

/**
 * 导出
 */
export const eduClassSubExportXlsUrl = '/edu/eduClass/exportEduClassSub'
/**
 * 列表接口
 * @param params
 */
export const eduClassSubTeacherList = (params) => {
  if(params['mainId']){
    return defHttp.get({url: Api.eduClassSubTeacherList, params});
  }
  return Promise.resolve({});
}


/**
 * 删除单个
 */
export const eduClassSubTeacherDelete = (params,handleSuccess) => {
  return defHttp.delete({url: Api.eduClassSubTeacherDelete, params}, {joinParamsToUrl: true}).then(() => {
    handleSuccess();
  });
}
/**
 * 批量删除
 * @param params
 */
export const eduClassSubTeacherDeleteBatch = (params, handleSuccess) => {
  createConfirm({
    iconType: 'warning',
    title: '确认删除',
    content: '是否删除选中数据',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({url: Api.eduClassSubTeacherDeleteBatch, data: params}, {joinParamsToUrl: true}).then(() => {
        handleSuccess();
      });
    }
  });
}
/**
 * 保存或者更新
 * @param params
 */
export const  eduClassSubTeacherSaveOrUpdate = (params, isUpdate) => {
  let url = isUpdate ? Api.eduClassSubTeacherEdit : Api.eduClassSubTeacherSave;
  return defHttp.post({url: url, params});
}
/**
 * 导入
 */
export const eduClassSubTeacherImportUrl = '/edu/eduClass/importEduClassSubTeacher'

/**
 * 导出
 */
export const eduClassSubTeacherExportXlsUrl = '/edu/eduClass/exportEduClassSubTeacher'
