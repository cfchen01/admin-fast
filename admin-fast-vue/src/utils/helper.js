import Vue from 'vue'
import $http from '@/utils/httpRequest'
import {Loading} from 'element-ui'
Vue.prototype.openDialog = function(key){
  this[key] = true
}

Vue.prototype.openWindow = function(key){
  if(this.showMain)
    this.showMain = false
  this[key] = true
}

Vue.prototype.closeWindow = function(){
  if(this.showMain)
    this.showMain = true
  this.$emit('closeWin')
}

Date.prototype.format = function (fmt) {
  var o = {
    "M+": this.getMonth() + 1,                 //月份
    "d+": this.getDate(),                    //日
    "h+": this.getHours(),                   //小时
    "m+": this.getMinutes(),                 //分
    "s+": this.getSeconds(),                 //秒
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
    "z+": this.getMonth() > 5 ? '下' : '上', //半年
    "S": this.getMilliseconds()             //毫秒
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}

String.prototype.parseDate = function() {
  let arr = this.split(' ');
  let dt = arr[0].split('-');
  let tm = arr.length > 1 ? arr[1].split(':') : [];
  dt.push('1','1');
  tm.push('0','0','0');
  let year = parseInt(dt[0]), month = parseInt(dt[1]), day = parseInt(dt[2]);
  let hour = parseInt(tm[0]), minute = parseInt(tm[1]), second = parseInt(tm[2]);
  return new Date(year, month - 1, day, hour, minute, second);
}

Vue.prototype.$download = function (parm){
  let load = Loading.service({
    text:'正在进行导出，请耐心等待........'
  })
  parm = parm || {url:'',data:{}}
  if(parm.url) parm.url = this.$http.adornUrl(parm.url);
  var contentType = 'application/octet-stream; charset=utf-8';

  if(parm.data){
    if (isObject(parm.data)) {
      contentType  = 'application/json;charset=utf-8';
    }
    parm.data = isObject(parm.data) ? this.$http.adornData(parm.data) : JSON.stringify(parm.data)
  }
  let opt = merge({}, parm, {
    method: 'post',
    responseType: 'blob',
    headers: {
      'Content-Type': contentType
    }
  })
  return this.$http(opt).then(
      res => { // 处理返回的文件流
        let fileName = res.headers['content-disposition'] ? decodeURIComponent(res.headers['content-disposition'].split('=')[1]) : (parm.file || "导出文件") + ".xlsx"
        const blob = new Blob([res.data])
        const elink = document.createElement('a')
        elink.download = fileName
        elink.style.display = 'none'
        elink.href = URL.createObjectURL(blob)
        document.body.appendChild(elink)
        elink.click()
        URL.revokeObjectURL(elink.href) // 释放URL 对象
        document.body.removeChild(elink)
        load.close()
      })
}

export function merge(a){
  let o = isObject(a) ? {} : []
  for(let i = 0; i < arguments.length; i++){
    for(let key in arguments[i]){
      o[key] = arguments[i][key]
    }
  }
  return JSON.parse(JSON.stringify(o))
}

export function isObject(value) {
  const type = typeof value
  return value != null && (type == 'object' || type == 'function')
}
