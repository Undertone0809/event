export default{
  // 增加前导0
  addZero(num) {
    if (parseInt(num) < 10) {
      num = '0' + num;
    }
    return num;
  },
  // 去除前导0
  trimZero(num) {
    var reg = /^0+/g;
    return num.replace(reg, "");
  },
  // 检查是否开启定位
  checkOpenGPSService() {
    let systemType = uni.getSystemInfoSync().platform;
    // 安卓
    if (systemType === 'android') {
      var context = plus.android.importClass('android.content.Context');
      var locationManager = plus.android.importClass('android.location.LocationManager');
      var main = plus.android.runtimeMainActivity();
      var mainSvr = main.getSystemService(context.LOCATION_SERVICE);
      if (!mainSvr.isProviderEnabled(locationManager.GPS_PROVIDER)) {
        uni.showModal({
          title: '提示',
          content: '请打开定位服务功能',
          showCancel: false,
          success() {
            if (!mainSvr.isProviderEnabled(locationManager.GPS_PROVIDER)) {
              var Intent = plus.android.importClass('android.content.Intent');
              var Settings = plus.android.importClass('android.provider.Settings');
              var intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
              main.startActivity(intent); // 打开系统设置定位服务功能页面
            } else {
              console.log('定位服务功能已开启');
            }
          }
        })
      }
    }
  },
  // 输入格式化的时间，返回距离当前的时间
  formatDateDiff(pTime){       
    var d_minutes,d_hours,d_days,d;
    var timeNow = parseInt(new Date().getTime()/1000);
    let pTime_new = new Date(pTime).getTime()/1000;
    d = timeNow - pTime_new;       
    d_days = parseInt(d/86400);       
    d_hours = parseInt(d/3600);       
    d_minutes = parseInt(d/60);    
    if(d_days>0 && d_days<4){       
        return d_days+"天前";       
    }else if(d_days<=0 && d_hours>0){       
        return d_hours+"小时前";       
    }else if(d_hours<=0 && d_minutes>0){       
        return d_minutes+"分钟前";       
    }else{
        return pTime;
    }
  },
  /**
   * 时间戳转格式化时间
   * 
   * @param <Number> params 13位时间戳 timestamp
   * @return <String> type "yyyy/mm/dd"，"yyyy/mm/dd hh:MM:ss"，"yyyy-mm-dd"，"yyyy-mm-dd hh:MM:ss"
   */
  timestampFormat(params, type){
    if (params == 0 || params == undefined) {
        return '--';
    }
    //举例param=1592532629012,则date = Fri Jun 19 2020 10:10:29 GMT+0800 (中国标准时间)
    const date = new Date(params);
    //从 Date 对象以四位数字返回年份
    let yyyy = date.getFullYear();
    //从 Date 对象返回月份 (0 ~ 11) 故+1
    let mm = date.getMonth() + 1;
    //10月前输出01-09月,10月后输出10,11,12月
    mm = mm < 10 ? ('0' + mm) : mm;
    //从 Date 对象返回一个月中的某一天 (1 ~ 31)
    let dd = date.getDate();
    //10日前输出01-09日,10月后输出10-31日
    dd = dd < 10 ? ('0' + dd) : dd;
    //返回 Date 对象的小时 (0 ~ 23)
    let hh = date.getHours();
    //10时前输出01-09时,10月后输出10-23时
    hh = hh < 10 ? ('0' + hh) : hh;
    //返回 Date 对象的分钟 (0 ~ 59)
    let MM = date.getMinutes();
    //10分前输出01-09分,10分后输出10-59分
    MM = MM < 10 ? ('0' + MM) : MM;
    //	返回 Date 对象的秒数 (0 ~ 59)
    let ss = date.getSeconds();
    //10秒前输出01-09分,10秒后输出10-59秒
    ss = ss < 10 ? ('0' + ss) : ss;
    switch(type)
    {
      //type类型可按需增加
        case 'yyyy/mm/dd':
            return yyyy + '/' + mm + '/' + dd;
            break;
        case 'yyyy-mm-dd':
            return yyyy + '-' + mm + '-' + dd;
            break;
        case 'yyyy/mm/dd hh:MM:ss':
            return yyyy + '/' + mm + '/' + dd + ' ' + hh + ':' + MM + ':' + ss;
            break;
        case 'yyyy-mm-dd hh:MM:ss':
            return yyyy + '-' + mm + '-' + dd + ' ' + hh + ':' + MM + ':' + ss;
            break;
        case 'hh:MM:ss':
            return hh + ':' + MM + ':' + ss;
            break;
        case 'yyyy/mm/dd hh:MM':
            return yyyy + '/' + mm + '/' + dd + ' ' + hh + ':' + MM;
            break;
        case 'yyyy-mm-dd hh:MM':
            return yyyy + '-' + mm + '-' + dd + ' ' + hh + ':' + MM;
            break;
        case 'hh:MM':
            return hh + ':' + MM;
            break;
        case 'mm-dd hh:MM':
            return mm + '-' + dd + ' ' + hh + ':' + MM;
            break;
        case 'mm-dd':
            return mm + '-' + dd;
            break;
        default:
            return '请输入正确的type类型';
    }
  },

  /**
  * 时间转时间戳
  *
  * @param <Number> moment 时间
  */
  getTimeStamp (moment){
  // 使用时间戳转格式化时间方法timestampFromat
    var time = timestampFromat(moment, 'yyyy-mm-dd hh:MM:ss')
    //举例time=2020-06-19 10:10:29,则date = Fri Jun 19 2020 10:10:29 GMT+0800 (中国标准时间)
    var date = new Date(time);
    //	返回 1970 年 1 月 1 日至今的毫秒数
    var timeStamp = date.getTime();
    return timeStamp;
  },
  printInfo(options){
    if(typeof(options)=='object'){
      console.log(`[debug info] ${JSON.stringify(options)}`)
    }else if(typeof(options)=='string'){
      console.log(`[debug info] ${options}`)
    }
  }

  
}