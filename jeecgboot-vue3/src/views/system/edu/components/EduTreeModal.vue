<template>
  <BasicModal v-bind="$attrs" @register="registerModal" destroyOnClose :width="800" :title="getTitle" @ok="handleSubmit">
    <BasicForm @register="registerForm" name="EduTreeForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import {ref, computed, unref, reactive} from 'vue';
  import {BasicModal, useModalInner} from '/@/components/Modal';
  import {BasicForm, useForm} from '/@/components/Form';
  import {formSchema} from '../EduTree.data';
  import {loadTreeData, saveOrUpdateDict} from '../EduTree.api';
  import { useMessage } from '/@/hooks/web/useMessage';
  import { getDateByPicker } from '/@/utils';
  //日期个性化选择
  const fieldPickers = reactive({
  });
  const { createMessage } = useMessage();
  // 获取emit
  const emit = defineEmits(['register', 'success']);
  const isUpdate = ref(true);
  const isDetail = ref(false);
  const expandedRowKeys = ref([]);
  const treeData = ref([]);
  // 当前编辑的数据
  let model:Nullable<Recordable> = null;
  //表单配置
  const [registerForm, { setProps,resetFields, setFieldsValue, validate, updateSchema, scrollToField }] = useForm({
    schemas: formSchema,
    showActionButtonGroup: false,
    baseColProps: {span: 24},
    labelCol: {
      xs: { span: 24 },
      sm: { span: 4 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 18 },
    },
  });
  //表单赋值
  const [registerModal, {setModalProps, closeModal}] = useModalInner(async (data) => {
    //重置表单
    await resetFields();
    expandedRowKeys.value = [];
    setModalProps({confirmLoading: false, minHeight: 80 ,showOkBtn: !!!data?.hideFooter});
    isUpdate.value = !!data?.isUpdate;
    isDetail.value = !!data?.showFooter;
    if (data?.record) {
      model = data.record;
      //表单赋值
      await setFieldsValue({
        ...data.record,
      });
    } else {
      model = null;
    }
    //父级节点树信息
    treeData.value = await loadTreeData({'async': false,'pcode':''});
    // 隐藏底部时禁用整个表单
    setProps({ disabled: !!data?.hideFooter })
  });
  //设置标题
  const getTitle = computed(() => (!unref(isUpdate) ? '新增' : !unref(isDetail) ? '详情' : '编辑'));

  /**
   * 根据pid获取展开的节点
   * @param pid
   * @param arr
   */
  function getExpandKeysByPid(pid,arr){
    if(pid && arr && arr.length>0){
      for(let i=0;i<arr.length;i++){
        if(arr[i].key==pid && unref(expandedRowKeys).indexOf(pid)<0){
          expandedRowKeys.value.push(arr[i].key);
          getExpandKeysByPid(arr[i]['parentId'],unref(treeData))
        }else{
          getExpandKeysByPid(pid,arr[i].children)
        }
      }
    }
  }
  //表单提交事件
  async function handleSubmit() {
    try {
      let values = await validate();
      // 预处理日期数据
      changeDateValue(values);
      setModalProps({confirmLoading: true});
      //提交表单
      await saveOrUpdateDict(values, isUpdate.value);
      //关闭弹窗
      closeModal();
      //展开的节点信息
      await getExpandKeysByPid(values['parentId'],unref(treeData))
      //刷新列表(isUpdate:是否编辑;values:表单信息;expandedArr:展开的节点信息)
      emit('success', {
        isUpdate: unref(isUpdate),
        values: {...values},
        expandedArr: unref(expandedRowKeys).reverse(),
        // 是否更改了父级节点
        changeParent: model != null && (model['parentId'] != values['parentId']),
      });
    } catch ({ errorFields }) {
      if (errorFields) {
        const firstField = errorFields[0];
        if (firstField) {
          scrollToField(firstField.name, { behavior: 'smooth', block: 'center' });
        }
      }
      return Promise.reject(errorFields);
    } finally {
      setModalProps({confirmLoading: false});
    }
  }

  /**
   * 处理日期值
   * @param formData 表单数据
   */
  const changeDateValue = (formData) => {
      if (formData && fieldPickers) {
          for (let key in fieldPickers) {
              if (formData[key]) {
                  formData[key] = getDateByPicker(formData[key], fieldPickers[key]);
              }
          }
      }
  };

</script>
<style lang="less" scoped>
	/** 时间和数字输入框样式 */
  :deep(.ant-input-number) {
    width: 100%;
  }

  :deep(.ant-calendar-picker) {
    width: 100%;
  }
</style>