import axios from 'axios'
import { ElMessage } from 'element-plus'

//创建一个axios实例
const instance = axios.create({
    baseURL: 'http://localhost:8081',
    timeout: 16000,     //超时时间
    //请求头格式类型
    headers: {
        'Content-Type': "application/json; charset=utf-8"
    }
});

//免登录白名单,不需要携带token参数
const whiteList = [
    '/code',
    '/login',
    '/register',
]

//请求拦截
instance.interceptors.request.use((config)=>{
    const token = localStorage.getItem("token");
    if (whiteList.indexOf(config.url) !== -1){
        return config;
    }
    if (token){
        config.headers['Authorization'] = token
    }
    return config;
},(error)=>{
    return Promise.reject(error);
})

//响应拦截
instance.interceptors.response.use((response)=>{
    console.log('响应结果：',response)
    if (response.data.code === 200){
        return response.data
    }else if (response.data.code === 401){
        ElMessage.error("请重新登录")
        window.location.href = "/login"
    }else {
        ElMessage.error(response.data.msg)
    }

},(error)=>{
    return Promise.reject(error)
})

export default instance;
