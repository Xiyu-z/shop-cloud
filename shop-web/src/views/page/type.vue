<template>
  <div class="body">
    <section class="product-list">
      <div class="container">
        <div class="search-form">
          <input type="text" :placeholder="$t('app.keyword')" v-model="query.name">
          <button type="submit" @click="getlist">{{ $t('app.select') }}</button>
        </div>
      </div>
    </section>

    <section v-for="(category, index) in products" :key="index" class="product-list">
      <div class="types">
        <h1>{{ category[0].type }}{{ $t('app.types') }}</h1>
      </div>
      <div class="container">
        <div class="product" v-for="product in category" :key="product.id" @click="router.push({ path: '/details/'+product.id+'' })">
          <div class="hot-sale-label" v-if="product.type != ''">{{ $t('app.isEcoFriendly') }}</div>
          <img :src="product.img" :alt="product.name">
          <h3 class="ellipsis-text">{{ product.name }}</h3>
          <p>￥{{ product.price }}</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
import {getType} from "@/api/type";
import {onMounted, reactive, ref} from 'vue';
import { useRouter, useRoute } from 'vue-router'
let router = useRouter();
let route = useRoute();
const title = ref('');
const products = ref([]);
const fromData = reactive({
  id: null,

});
const query = reactive({
  name: '',
});
const getlist = () => {
  getType(query).then(r => {
    console.log(r)
    products.value=r.data
  })
}
onMounted(() => {
  getlist()
})
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}


.body {
  font-family: Arial, sans-serif;
  line-height: 1.6;
  background-color: #ffffff;
  flex-direction: column;
  min-height: 100vh;
  display: flex;
}
.search-form {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.search-form input {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px 0 0 5px;
  width: 70%; /* 调整输入框宽度 */
}

.search-form button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 0 5px 5px 0;
  cursor: pointer;
}

.search-form button:hover {
  background-color: #0056b3;
}

.container {
  width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
.search-form {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.hero h2 {
  margin-bottom: 20px;
}

.hero p {
  font-size: 18px;
  margin-bottom: 30px;
}

.product-list {
  margin: 0 auto;
  padding: 20px 0;
  background-color: #fff;
}

.product-list .container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
}
.types {
  max-width: 1200px;
  margin: 0 auto;
}
.product {
  position: relative;
  width: 17%;
  margin-left: 13px;
  margin-bottom: 20px;
  padding: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  cursor: pointer;
}

.product img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.product h3 {
  margin-top: 10px;
  font-size: 16px;
}

.product p {
  color: #007bff;
  font-weight: bold;
  font-size: 18px;
  margin-top: 5px;
}

.ellipsis-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.hot-sale-label {
  position: absolute;
  top: 16px;
  right: 16px;
  background-color: #267912;
  color: white;
  font-size: 10px;
  font-weight: bold;
  padding: 5px 5px;
  border-radius: 5px;
  transform: translate(50%, -50%) rotate(45deg);
  z-index: 1;
}

</style>
