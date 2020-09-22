import Vue from 'vue'
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
