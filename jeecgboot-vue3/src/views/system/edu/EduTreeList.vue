<template>
  <div>
    <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :expandedRowKeys="expandedRowKeys" @expand="handleExpand" @fetch-success="onFetchSuccess">
      <!--插槽:table标题-->
      <template #tableTitle>
          <a-button type="primary" v-auth="'edu:edu_tree:add'" @click="handleCreate" preIcon="ant-design:plus-outlined"> 新增</a-button>
          <a-button  type="primary" v-auth="'edu:edu_tree:exportXls'"  preIcon="ant-design:export-outlined" @click="onExportXls"> 导出</a-button>
          <j-upload-button  type="primary" v-auth="'edu:edu_tree:importExcel'"  preIcon="ant-design:import-outlined" @click="onImportXls">导入</j-upload-button>
          <a-dropdown v-if="selectedRowKeys.length > 0">
          <template #overlay>
            <a-menu>
              <a-menu-item key="1" @click="batchHandleDelete">
                <Icon icon="ant-design:delete-outlined"></Icon>
                删除
              </a-menu-item>
            </a-menu>
          </template>
          <a-button  v-auth="'edu:edu_tree:deleteBatch'">批量操作
            <Icon icon="ant-design:down-outlined"></Icon>
          </a-button>
        </a-dropdown>
        <!-- 高级查询 -->
        <super-query :config="superQueryConfig" @search="handleSuperQuery" />
      </template>
      <!--操作栏-->
      <template #action="{ record }">
        <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)"/>
      </template>
      <!--字段回显插槽-->
      <template v-slot:bodyCell="{ column, record, index, text }">
      </template>
    </BasicTable>
    <!--字典弹窗-->
    <EduTreeModal @register="registerModal" @success="handleSuccess"/>
  </div>
</template>

<script lang="ts" name="edu-eduTree" setup>
  //ts语法
  import {ref, reactive, computed, unref, toRaw, nextTick} from 'vue';
  import {BasicTable, TableAction} from '/@/components/Table';
  import {useModal} from '/@/components/Modal';
  import { useListPage } from '/@/hooks/system/useListPage'
  import EduTreeModal from './components/EduTreeModal.vue';
  import {columns, searchFormSchema, superQuerySchema} from './EduTree.data';
  import { downloadFile } from '/@/utils/common/renderUtils';
  import { useMessage } from '/@/hooks/web/useMessage';
  import {list, deleteEduTree, batchDeleteEduTree, getExportUrl,getImportUrl, getChildList,getChildListBatch} from './EduTree.api';
  import { getDateByPicker } from '/@/utils';
  //日期个性化选择
  const fieldPickers = reactive({
  });
    const { createMessage } = useMessage();
  const queryParam = reactive<any>({});
  const expandedRowKeys = ref([]);
  //字典model
  const [registerModal, {openModal}] = useModal();
   //注册table数据
  const { prefixCls,tableContext,onExportXls,onImportXls } = useListPage({
    tableProps:{
         api: list,
         title: '商品物料表',
         columns,
         canResize:true,
         isTreeTable: true,
         formConfig: {
           //labelWidth: 120,
           schemas: searchFormSchema,
           autoSubmitOnEnter:true,
           showAdvancedButton:true,
           fieldMapToNumber: [
              ['total', ['total_begin', 'total_end']],
           ],
           fieldMapToTime: [
              ['scDate', ['scDate_begin', 'scDate_end'], 'YYYY-MM-DD'],
           ],
         },
         actionColumn: {
           width: 240,
           fixed:'right'
         },
         beforeFetch: (params) => {
           if (params && fieldPickers) {
             for (let key in fieldPickers) {
               if (params[key]) {
                 params[key] = getDateByPicker(params[key], fieldPickers[key]);
               }
             }
           }
           params.hasQuery = "true";
           return Object.assign(params, queryParam);
         },
    },
     exportConfig: {
          name:"商品物料表",
          url: getExportUrl,
          params: queryParam,
        },
        importConfig: {
          url: getImportUrl,
          success: importSuccess
        },
    })

  const [registerTable, {reload, collapseAll, updateTableDataRecord, findTableDataRecord,getDataSource},{ rowSelection, selectedRowKeys }] = tableContext

  // 高级查询配置
  const superQueryConfig = reactive(superQuerySchema);

  /**
   * 高级查询事件
   */
  function handleSuperQuery(params) {
    Object.keys(params).map((k) => {
      queryParam[k] = params[k];
    });
    reload();
  }

  /**
   * 新增事件
   */
  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  /**
   * 编辑事件
   */
  async function handleEdit(record) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  /**
   * 详情
   */
  async function handleDetail(record) {
    openModal(true, {
      record,
      isUpdate: true,
      hideFooter: true,
    });
  }

  /**
   * 删除事件
   */
  async function handleDelete(record) {
    await deleteEduTree({id: record.id}, importSuccess);
  }

  /**
   * 批量删除事件
   */
  async function batchHandleDelete() {
    const ids = selectedRowKeys.value.filter(item => !item.includes('loadChild'))
    await batchDeleteEduTree({id: ids}, importSuccess);
  }
  /**
   * 导入
   */
   function importSuccess() {
    (selectedRowKeys.value = []) && reload();
  }
  /**
   * 添加下级
   */
  function handleAddSub(record) {
    openModal(true, {
      record,
      isUpdate: false,
    });
  }
  /**
   * 成功回调
   */
  async function handleSuccess({isUpdate, values, expandedArr, changeParent}) {
    if (isUpdate) {
      if (changeParent) {
        reload();
      } else {
        let data = await list({ id: values.id, pageSize: 1, pageNo: 1, parentId: values['parentId'] });
        if (data && data.records && data.records.length > 0) {
          // 编辑回调
          updateTableDataRecord(values.id, data.records[0]);
        }else{
          updateTableDataRecord(values.id, values);
        }
      }
    } else {
        if(!values['id'] || !values['parentId']){
            //新增根节点
            reload();
        }else{
            //新增子集
            expandedRowKeys.value = [];
            for (let key of unref(expandedArr)) {
                await expandTreeNode(key)
            }
        }
    }
  }

  /**
   * 接口请求成功后回调
   */
  function onFetchSuccess(result) {
      getDataByResult(result.items)&&loadDataByExpandedRows();
  }
  /**
   * 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
   */
  async function loadDataByExpandedRows() {
      if (unref(expandedRowKeys).length > 0) {
          const res = await getChildListBatch({ parentIds: unref(expandedRowKeys).join(',')});
          if (res.success && res.result.records.length>0) {
              //已展开的数据批量子节点
              let records = res.result.records
              const listMap = new Map();
              for (let item of records) {
                  let pid = item['parentId'];
                  if (unref(expandedRowKeys).includes(pid)) {
                      let mapList = listMap.get(pid);
                      if (mapList == null) {
                          mapList = [];
                      }
                      mapList.push(item);
                      listMap.set(pid, mapList);
                  }
              }
              let childrenMap = listMap;
              let fn = (list) => {
                  if(list) {
                      list.forEach(data => {
                          if (unref(expandedRowKeys).includes(data.id)) {
                              data.children = getDataByResult(childrenMap.get(data.id))
                              fn(data.children)
                          }
                      })
                  }
              };
              fn(getDataSource())
          }
      }
  }
  /**
   * 处理数据集
   */
  function getDataByResult(result){
      if(result && result.length>0){
          return result.map(item=>{
              //判断是否标记了带有子节点
              if(item["hasChild"]=='1'){
                  let loadChild = { id: item.id+'_loadChild', name: 'loading...', isLoading: true }
                  item.children = [loadChild]
              }
              return item
          })
      }
  }
  /**
   *树节点展开合并
   * */
  async function handleExpand(expanded, record) {
    // 判断是否是展开状态，展开状态(expanded)并且存在子集(children)并且未加载过(isLoading)的就去查询子节点数据
    if (expanded) {
       expandedRowKeys.value.push(record.id)
      if (record.children.length > 0 && !!record.children[0].isLoading) {
        let result = await getChildList({parentId: record.id});
        result=result.records?result.records:result;
        if (result && result.length > 0) {
          record.children = getDataByResult(result);
        } else {
          record.children = null
          record.hasChild = '0'
        }
      }
    } else {
      let keyIndex = expandedRowKeys.value.indexOf(record.id)
      if (keyIndex >= 0) {
        expandedRowKeys.value.splice(keyIndex, 1);
      }
    }
  }
  /**
   *操作表格后处理树节点展开合并
   * */
  async function expandTreeNode(key) {
    let record = findTableDataRecord(key)
    expandedRowKeys.value.push(key);
      let result = await getChildList({parentId: key});
      if (result && result.length > 0) {
          record.children = getDataByResult(result);
      } else {
          record.children = null
          record.hasChild = '0'
      }
      updateTableDataRecord(key, record);
  }
  /**
   * 操作栏
   */
  function getTableAction(record) {
    return [
      {
        label: '编辑',
        onClick: handleEdit.bind(null, record),
        auth: 'edu:edu_tree:edit'
      },
      {
        label: '添加下级',
        onClick: handleAddSub.bind(null, {parentId: record.id}),
      }
    ]
  }
   /**
    * 下拉操作栏
    */
   function getDropDownAction(record){
     return [
       {
         label: '详情',
         onClick: handleDetail.bind(null, record),
       }, {
         label: '删除',
         popConfirm: {
           title: '确定删除吗?',
           confirm: handleDelete.bind(null, record),
           placement: 'topLeft'
         },
         auth: 'edu:edu_tree:delete'
       }
     ]
   }


</script>

<style lang="less" scoped>
  :deep(.ant-picker),:deep(.ant-input-number){
    width: 100%;
  }
</style>
