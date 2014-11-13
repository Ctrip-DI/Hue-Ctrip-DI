		
		function getTimeStr(year, month,day ,hour,mi){
			return month +"%2F"+ day +"%2F"+year+"+"+hour+"%3A"+mi;
		}	
  
  		function getTimeStrNormal(year, month,day ,hour,mi){
			return month +"/"+ day +"/"+year+"+"+hour+":"+mi;
		}	
  
  Date.prototype.format = function (format) {
        var o = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S": this.getMilliseconds()
        }
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }

        return format;
    }

    function getSmpFormatDate(date, isFull) {
        var pattern = "";
        if (isFull == true || isFull == undefined) {
            pattern = "yyyy-MM-dd hh:mm:ss";
        } else {
            pattern = "yyyy-MM-dd";
        }
        return getFormatDate(date, pattern);
    }

    function getSmpFormatNowDate(isFull) {
        return getSmpFormatDate(new Date(), isFull);
    }

    function getSmpFormatDateByLong(l, isFull) {

        return getSmpFormatDate(new Date(l*1000), isFull);
    }
 
    function getFormatDateByLong(l, pattern) {
        return getFormatDate(new Date(l), pattern);
    }

    function getFormatDate(date, pattern) {
        if (date == undefined) {
            date = new Date();
        }
        if (pattern == undefined) {
            pattern = "yyyy-MM-dd hh:mm:ss";
        }
        return date.format(pattern);
    }
