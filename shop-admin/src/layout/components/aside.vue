<template>
<!--  2-->
<!--  左侧菜单-->
  <div v-if="isMenus" :style="{ background: $config.backgroundColor, height: '100vh' }">
    <el-scrollbar :style="{ background: $config.backgroundColor}">
      <div :style="{display:'flex',justifyContent: $config.logo != '' ? 'start' : 'center',alignItems: 'center'}">
        <img v-if="$config.logo != ''" :src="$config.logo" style="width: 50px;height: 50px;margin-left: 10px"/>
        <h3 :style="{ textAlign: 'center', marginLeft: $config.logo != '' ? '10px' :'0px', color: $config.textColor }">{{ $t('app.logo') }}</h3>
      </div>
      <el-menu
          :router="true"
          :default-active="route.path.replace('/','')"
          :collapse="isCollapse"
          :active-text-color="$config.activeTextColor"
          :background-color="$config.backgroundColor"
          class="el-menu-vertical-demo menu"
          default-active="2"
          :text-color="$config.textColor"
          :default-openeds="openeds"
      >
        <menus  v-for="(item,index) in newlistMenu" :data="item"></menus>

      </el-menu>
    </el-scrollbar>
  </div>

</template>

<script setup>
import menus from '@/layout/components/menus.vue'
import {reactive, ref, computed, onMounted, getCurrentInstance} from 'vue'
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
//获取pnina中的数据
import {routerStore} from "@/store/router";
import {userStore} from "@/store/user";
import {useRoute} from "vue-router";
const routerstore = routerStore();
const userstore = userStore();
const route = useRoute();
// vue3子组件接收父组件的值
const props = defineProps({
  isCollapse:{
    type: Boolean,
    default:false
  }
})
const isMenus = ref(false)
const newlist = computed({
  get() {
    return routerstore.menus;
  },
})
let newlistMenu = []
//默认展开
const openeds = ref(['1']);
// const openeds = ref(['1','2','3','4','5']);
//管理员
let newlist1 = [
  {
    id: 1,
    title:t('app.userInfo'),
    icon:'HomeFilled',
    url: 'index',
    children:[]
  },
  {
    id: 2,
    title:t('app.user'),
    icon:'UserFilled',
    url: 'user',
  },
  {
    id: 3,
    title:t('app.type'),
    icon:'Grid',
    url: 'type',
  },
  {
    id: 4,
    title:t('app.goods'),
    icon:'Goods',
    url: 'goods',
  },
  {
    id: 5,
    title:t('app.order'),
    icon:'Tickets',
    url: 'order',
  },

]
//普通用户
let newlist2 = [

]



onMounted(()=>{
  if (userstore.getUserInfo().role=='0') {
    newlistMenu = newlist1
    isMenus.value=true
  }
  if (userstore.getUserInfo().role=='2') {
    newlistMenu = newlist2
    isMenus.value=true
  }
})
</script>

<style scoped>

h3{
  color: white;
}
.menu{
  border: none;
}

</style>
