<template>
  <div class="p-2 cgformErpList">
    <div class="content">
      <!--引用表格-->
      <BasicTable @register="registerTable" :rowSelection="rowSelection">
        <!--插槽:table标题-->
        <template #tableTitle>
          <a-button type="primary" v-auth="'edu:edu_class:add'" @click="handleAdd" preIcon="ant-design:plus-outlined">
            新增</a-button>
          <a-button type="primary" v-auth="'edu:edu_class:exportXls'" preIcon="ant-design:export-outlined"
            @click="onExportXls"> 导出</a-button>
          <j-upload-button type="primary" v-auth="'edu:edu_class:importExcel'" preIcon="ant-design:import-outlined"
            @click="onImportXls">导入</j-upload-button>
          <a-dropdown v-if="selectedRowKeys.length > 0">
            <template #overlay>
              <a-menu>
                <a-menu-item key="1" @click="batchHandleDelete">
                  <Icon icon="ant-design:delete-outlined"></Icon>
                  删除
                </a-menu-item>
              </a-menu>
            </template>
            <a-button v-auth="'edu:edu_class:deleteBatch'">批量操作
              <Icon icon="mdi:chevron-down"></Icon>
            </a-button>
          </a-dropdown>
          <!-- 高级查询 -->
          <super-query :config="superQueryConfig" @search="handleSuperQuery" />
        </template>
        <!--操作栏-->
        <template #action="{ record }">
          <TableAction :actions="getTableAction(record)" :dropDownActions="getDropDownAction(record)" />
        </template>
        <!--字段回显插槽-->
        <!-- <template v-slot:bodyCell="{ column, record, index, text }">
        </template> -->
      </BasicTable>
      <!--子表表格tab-->
      <a-tabs defaultActiveKey="1" style="margin: 10px">
        <a-tab-pane tab="班主任表" key="1">
          <EduClassSubHeadTeacherList />
        </a-tab-pane>
        <a-tab-pane tab="学生表" key="2" forceRender>
          <EduClassSubList />
        </a-tab-pane>
        <a-tab-pane tab="教师表" key="3" forceRender>
          <EduClassSubTeacherList />
        </a-tab-pane>
      </a-tabs>
    </div>
    <!-- 表单区域 -->
    <EduClassModal @register="registerModal" @success="handleSuccess"></EduClassModal>
  </div>
</template>

<script lang="ts" name="edu-eduClass" setup>
import { ref, reactive, computed, unref, provide } from 'vue';
import { BasicTable, useTable, TableAction } from '/@/components/Table';
import { useListPage } from '/@/hooks/system/useListPage'
import { useModal } from '/@/components/Modal';
import EduClassModal from './components/EduClassModal.vue'
import { useUserStore } from '/@/store/modules/user';
import { useMessage } from '/@/hooks/web/useMessage';
import EduClassSubHeadTeacherList from './EduClassSubHeadTeacherList.vue'
import EduClassSubList from './EduClassSubList.vue'
import EduClassSubTeacherList from './EduClassSubTeacherList.vue'
import { columns, searchFormSchema, superQuerySchema } from './EduClass.data';
import { list, deleteOne, batchDelete, getImportUrl, getExportUrl } from './EduClass.api';
import { downloadFile } from '/@/utils/common/renderUtils';
import { getDateByPicker } from '/@/utils';
//日期个性化选择
const fieldPickers = reactive({
});
const queryParam = reactive<any>({});
const { createMessage } = useMessage();
//注册model
const [registerModal, { openModal }] = useModal();
//注册table数据
const { prefixCls, tableContext, onExportXls, onImportXls } = useListPage({
  tableProps: {
    title: '教室主表',
    api: list,
    columns,
    canResize: true,
    clickToRowSelect: true,
    rowSelection: { type: 'radio' },
    formConfig: {
      schemas: searchFormSchema,
      fieldMapToNumber: [
        ['total', ['total_begin', 'total_end']],
        ['avg', ['avg_begin', 'avg_end']],
      ],
      fieldMapToTime: [
      ],
    },
    actionColumn: {
      width: 120,
      fixed: 'right'
    },
    beforeFetch: (params) => {
      if (params && fieldPickers) {
        for (let key in fieldPickers) {
          if (params[key]) {
            params[key] = getDateByPicker(params[key], fieldPickers[key]);
          }
        }
      }
      return Object.assign(params, queryParam);
    },
    pagination: {
      current: 1,
      pageSize: 5,
      pageSizeOptions: ['5', '10', '20'],
    }
  },
  exportConfig: {
    name: "教室主表",
    url: getExportUrl,
    params: queryParam,
  },
  importConfig: {
    url: getImportUrl,
    success: handleSuccess
  },
})
const userStore = useUserStore();
const [registerTable, { reload }, { rowSelection, selectedRowKeys }] = tableContext

const mainId = computed(() => (unref(selectedRowKeys).length > 0 ? unref(selectedRowKeys)[0] : ''));
//下发 mainId,子组件接收
provide('mainId', mainId);

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
function handleAdd() {
  openModal(true, {
    isUpdate: false,
    showFooter: true,
  });
}
/**
 * 编辑事件
 */
function handleEdit(record: Recordable) {
  openModal(true, {
    record,
    isUpdate: true,
    showFooter: true,
  });
}
/**
 * 详情
*/
function handleDetail(record: Recordable) {
  openModal(true, {
    record,
    isUpdate: true,
    showFooter: false,
  });
}
/**
 * 删除事件
 */
async function handleDelete(record) {
  await deleteOne({ id: record.id }, handleSuccess);
}
/**
 * 批量删除事件
 */
async function batchHandleDelete() {
  await batchDelete({ ids: selectedRowKeys.value }, handleSuccess);
}
/**
 * 成功回调
 */
function handleSuccess() {
  (selectedRowKeys.value = []) && reload();
}
/**
   * 操作栏
   */
function getTableAction(record) {
  return [
    {
      label: '编辑',
      onClick: handleEdit.bind(null, record),
      auth: 'edu:edu_class:edit'
    }
  ]
}


/**
 * 下拉操作栏
 */
function getDropDownAction(record) {
  return [
    {
      label: '详情',
      onClick: handleDetail.bind(null, record),
    }, {
      label: '删除',
      popConfirm: {
        title: '是否确认删除',
        confirm: handleDelete.bind(null, record),
        placement: 'topLeft'
      },
      auth: 'edu:edu_class:delete'
    }
  ]
}



</script>

<style lang="less" scoped>
html[data-theme='light'] {
  .cgformErpList {
    height: 100%;

    .content {
      background-color: #fff;
    }
  }
}

:deep(.ant-picker),
:deep(.ant-input-number) {
  width: 100%;
}
</style>