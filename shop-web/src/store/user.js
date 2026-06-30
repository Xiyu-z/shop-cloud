import {defineStore} from "pinia";
import { login} from "@/api/login";
import {ElMessage} from "element-plus";

export const userStore = defineStore("user",{
    state:()=>{
        return{
            token:localStorage.getItem("token") || '',
            userInfo:{},
            isrouters:false,
        }
    },
    getters:{

    },
    actions:{
        login(data){
            return new Promise((resolve, reject)=>{
                login(data).then(res=>{
                    console.log(res.userInfo)
                    localStorage.setItem("token", res.access_token);
                    localStorage.setItem("userInfo", JSON.stringify(res.userInfo));
                    this.token = res.access_token;
                    this.userInfo = res.userInfo;
                    resolve()
                }).catch(onerror=>{
                    //触发登录按钮禁止旋转
                    reject(onerror)
                })

            })
        },
        out(){
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
        },
        getUserInfo(){
            return this.userInfo
        },
        setUserInfo(){
            this.userInfo = JSON.parse(localStorage.getItem("userInfo"))
        },
    }

})
