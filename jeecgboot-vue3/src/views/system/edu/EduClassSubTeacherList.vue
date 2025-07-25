<template>
 <div>
     <!--引用表格-->
    <BasicTable @register="registerTable" :rowSelection="rowSelection" :searchInfo="searchInfo">
      <!--插槽:table标题-->
       <template #tableTitle>
           <a-button type="primary" @click="handleCreate" preIcon="ant-design:plus-outlined" v-if="mainId!=''"> 新增</a-button>
           <a-button  type="primary" preIcon="ant-design:export-outlined" @click="onExportXls" v-if="mainId!=''"> 导出</a-button>
           <j-upload-button  type="primary" preIcon="ant-design:import-outlined" @click="onImportXls" v-if="mainId!=''">导入</j-upload-button>
           <a-dropdown v-if="selectedRowKeys.length > 0">
               <template #overlay>
                 <a-menu>
                   <a-menu-item key="1" @click="batchHandleDelete">
                     <Icon icon="ant-design:delete-outlined"></Icon>
                     删除
                   </a-menu-item>
                 </a-menu>
               </template>
               <a-button>批量操作
                 <Icon icon="mdi:chevron-down"></Icon>
               </a-button>
         </a-dropdown>
       </template>
        <!--操作栏-->
       <template #action="{ record }">
         <TableAction :actions="getTableAction(record)"/>
       </template>
       <!--字段回显插槽-->
       <template v-slot:bodyCell="{ column, record, index, text }">
       </template>
     </BasicTable>

      <EduClassSubTeacherModal @register="registerModal" @success="handleSuccess"/>
   </div>
</template>

<script lang="ts" setup>
  import {ref, computed, unref,inject,watch} from 'vue';
  import {BasicTable, useTable, TableAction} from '/@/components/Table';
  import { useListPage } from '/@/hooks/system/useListPage'
  import {useModal} from '/@/components/Modal';
  import EduClassSubTeacherModal from './components/EduClassSubTeacherModal.vue'
  import {eduClassSubTeacherColumns} from './EduClass.data';
  import {eduClassSubTeacherList, eduClassSubTeacherDelete, eduClassSubTeacherDeleteBatch, eduClassSubTeacherExportXlsUrl, eduClassSubTeacherImportUrl } from './EduClass.api';
  import {isEmpty} from "/@/utils/is";
  import {useMessage} from '/@/hooks/web/useMessage';
  import {downloadFile} from '/@/utils/common/renderUtils';

    //接收主表id
    const mainId = inject('mainId') || '';
    //提示弹窗
    const $message = useMessage()
    //弹窗model
    const [registerModal, {openModal}] = useModal();
    const searchInfo = {};
    // 列表页面公共参数、方法
    const {prefixCls, tableContext, onImportXls, onExportXls} = useListPage({
        tableProps: {
            api: eduClassSubTeacherList,
            columns: eduClassSubTeacherColumns,
            canResize: true,
            useSearchForm: false,
            actionColumn: {
                width: 180,
                fixed:'right'
            },
            pagination:{
                current: 1,
                pageSize: 5,
                pageSizeOptions: ['5', '10', '20'],
            }
        },
        exportConfig: {
            name: '教师表',
            url: eduClassSubTeacherExportXlsUrl,
            params: {
                'mainId': mainId
            }
        },
        importConfig: {
            url: ()=>{
                return eduClassSubTeacherImportUrl + '/' + unref(mainId)
            }
        }
    });

    //注册table数据
    const [registerTable, {reload}, {rowSelection, selectedRowKeys}] = tableContext;

    watch(mainId, () => {
         searchInfo['mainId'] = unref(mainId);
         reload();
      }
    );

    /**
     * 新增事件
     */
    function handleCreate() {
        if (isEmpty(unref(mainId))) {
            $message.createMessage.warning('请选择一个主表信息')
            return;
        }
        openModal(true, {
            isUpdate: false,
            showFooter: true,
        });
    }

    /**
     * 编辑事件
     */
    async function handleEdit(record: Recordable) {
        openModal(true, {
            record,
            isUpdate: true,
            showFooter: true,
        });
    }

    /**
     * 删除事件
     */
    async function handleDelete(record) {
        await eduClassSubTeacherDelete({id: record.id}, handleSuccess);
    }

    /**
     * 批量删除事件
     */
    async function batchHandleDelete() {
        await eduClassSubTeacherDeleteBatch({ids: selectedRowKeys.value}, handleSuccess)
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
            }, {
                label: '删除',
                popConfirm: {
                    title: '是否确认删除',
                    confirm: handleDelete.bind(null, record),
                    placement: 'topLeft',
                },
            }
        ]
    }
</script>
