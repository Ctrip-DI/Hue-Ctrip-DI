<%!
from desktop.views import commonheader, commonfooter
from monitor.conf import DATA_SERVICE
%>

${commonheader("Di Portal", "monitor", user, "100px")|n,unicode}

## Use double hashes for a mako template comment
## Main body

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=EDGE">
<link type="text/css" rel="stylesheet" href="/monitor/static/css/default.css">
<link type="text/css" rel="stylesheet" href="/monitor/static/css/jquery.autocomplete.css">
<link type="text/css" rel="stylesheet" href="/monitor/static/css/jquery-ui-1.10.2.custom.min.css">
<link type="text/css" rel="stylesheet" href="/monitor/static/css/bootstrap.css">

<input id="data_service_url" type="hidden" value="${DATA_SERVICE.DI_DATA_SERVICE_URL.get() | n,unicode }">

 <div class="subnav subnav-fixed">
    <div class="container-fluid">
      <ul class="nav nav-pills">
      		<li class=""><a href="${url('monitor.views.index')}">统计报表</a></li>
	        <li class="active"><a href="${url('monitor.views.metrics')}">Metrics监控</a></li>
		</ul>
		<ul class="dropdown-menu" role="menu" aria-labelledby="reportsMenu" id="menu">
			<li role="presentation" ><a role="menuitem" tabindex="-1" href="javascript:void(0);" onclick="showHdfs(true);">HDFS</a></li>
			<li role="presentation" ><a role="menuitem" tabindex="-1" href="javascript:void(0);" onclick="showHdfs(false);">YARN</a></li>
		</ul>
      </ul>
    </div>
  </div>
<div id="conditions" class="panel panel-default" style="margin-top:30px;"> 
	<div class="panel-heading">Conditions</div>
	<div class="panel-body">
		<span id="clusterList">&nbsp;Cluster&nbsp;&gt;
			<select name="clusters" style="font-size:14px;" id="clusters" onChange="clusterChange()" >
			</select>
		</span>
		<span id="hosts" >&nbsp;Node&nbsp;&gt;		
			<select name="hosts" id="hostList" style="font-size:14px;"  onChange="hostChange()" >
			</select>
		</span>
		<span id="metrics" >&nbsp;Metrics&nbsp;&gt;	
			<select name="m" id="likeQuery" >
				<option vlaue="load_one">load_one</option>
			</select>
		</span>
		
		<span id="times" >
			&nbsp;&nbsp;From 
			<input id="starttime" class="times" value="" type="text" >
			&nbsp;&nbsp;To&nbsp;&nbsp;
			<input value="" class="times" type="text" id="endtime" > 
						
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="button" value="Search" onclick="searchMetrics()" style="margin-top:2px;margin-left:5px;">
		</span>
	</div>
</div>

<span style="float:center;width:100%;color:red;height:30px;" id="metricsSearchError"></span>

<div id="host_metic_graphs1"  class="panel panel-default">
	<div class="panel-heading">Metric Data
		<div id="pages1" style="float:right;margin-bottom:2px;margin-right:10px;">
			<strong>Lines</strong>
			<select id="pagesL1" style="width: 55px;" onChange="showHost(1)">
				<option value=1>1</option>
				<option value=2>2</option>
				<option value=3 selected>3</option>
				<option value=4 >4</option>
				<option value=5 >5</option>				
				<option value=6 >6</option>
				<option value=7 >7</option>				
				<option value=8 >8</option>
			</select>
			<a href="javascript:void(0)" id="firstL1" onclick="goFirstLine(1)">首页</a>
			<a href="javascript:void(0)" id="previousL1" onclick="goPreviousLine(1)">上一页</a>
			<a href="javascript:void(0)" id="lastL1" onclick="goNextLine(1)">下一页</a>
			<span style="float:center;width:100%;color:red;height:30px;" id="previousLError1"></span>
		</div>	
		<div style="float:right;margin-bottom:2px;margin-right:5px;">
			<strong>Columns</strong>
			<select id="pagesH1" style="width: 55px;" onChange="showHost(1)">
				<option value=1>1</option>
				<option value=2>2</option>
				<option value=3>3</option>
				<option value=4 selected>4</option>
			</select>
		</div>
		<div class="btns" >
			<strong class="btnstyle">Zoom</strong>
			<button class="btnstyle" id="button0" >reset</button>
			<button class="btnstyle" id="button1">2h</button>
			<button class="btnstyle" id="button2">4h</button>
			<button class="btnstyle" id="button3">1d</button>
			<button class="btnstyle" id="button4">1w</button>
			<!--button class="btnstyle" id="button5">2w</button-->
			<button class="btnstyle" id="button6">1m</button>
			<button class="btnstyle" id="button7">3m</button>
			<button class="btnstyle" id="button8">6m</button>
		</div>
	</div>
</div>

<div id="cluster_reports" class="panel panel-default"> 
	<div class="panel-heading">All Report of Cluster 
		<div class="btns">					
			<strong class="btnstyle">Zoom</strong>
			<button class="btnstyle" id="reportBtn0" >reset</button>
			<button class="btnstyle" id="reportBtn1">2h</button>
			<button class="btnstyle" id="reportBtn2">4h</button>
			<button class="btnstyle" id="reportBtn3">1d</button>
			<button class="btnstyle" id="reportBtn4">1w</button>
			<button class="btnstyle" id="reportBtn7">2w</button>
			<button class="btnstyle" id="reportBtn5">1m</button>
			<button class="btnstyle" id="reportBtn6">3m</button>
		</div>		
	</div> 
	<div class="panel-body" id="monitors">
		<!-- div id="cluster_metric_graphs">
			<center>
				<table border="0" cellspacing="4" style="table-layout:fixed;width:96%;" >
					<tbody>
						<tr>
							<td style="width:48%;vertical-align:top;">								
								<div class="panel panel-info" id="cpu_report" style="width:98%;">
									<div class="panel-heading">CPU监控统计
										<a class="pages" href="javascript:void(0);" onclick="isFold('cpu_report','cpu_report_body','470px')" title="折叠/展开" style="float:right;">
											<span id="cpu_report_icon" class="glyphicon glyphicon-chevron-up"></span>
										</a>
									</div>
									<div class="panel-body" style="height:420px;" id="cpu_report_body">
										<div class="widget-box clog-highchart-widget" style="height:400px;" id="chartDemo1"></div>
									</div>
								</div>
							</td>
							<td style="width:48%;vertical-align:top;">	
								<div class="panel panel-info" style="width:98%;" id="mem_report">
									<div class="panel-heading">Memory监控统计
										<a class="pages" href="javascript:void(0);" onclick="isFold('mem_report','mem_report_body','470px')" title="折叠/展开" style="float:right;">
											<span id="mem_report_icon" class="glyphicon glyphicon-chevron-up"></span>
										</a>
									</div>
									<div class="panel-body" style="height:420px;" id="mem_report_body">			
										<div class="widget-box clog-highchart-widget" style="height:400px;" id="chartDemo2"></div>
									</div>
								</div>
							</td>
						</tr>
						
						<tr>
							<td style="width:48%;vertical-align:top;">	
								<div class="panel panel-info" style="width:98%;" id="load_report">
									<div class="panel-heading">Load监控统计
										<a class="pages" href="javascript:void(0);" onclick="isFold('load_report','load_report_body','470px')" title="折叠/展开" style="float:right;">
											<span id="load_report_icon" class="glyphicon glyphicon-chevron-up"></span>
										</a>
									</div>
									<div class="panel-body" style="height:420px;" id="load_report_body"> 			
										<div class="widget-box clog-highchart-widget" style="height:400px;" id="chartDemo3"></div>
									</div>
								</div>
							</td>
							<td style="width:48%;vertical-align:top;">	
								<div class="panel panel-info" style="width:98%;" id="network_report">
									<div class="panel-heading">Network监控统计
										<a class="pages" href="javascript:void(0);" onclick="isFold('network_report','network_report_body','470px')" title="折叠/展开" style="float:right;">
											<span id="network_report_icon" class="glyphicon glyphicon-chevron-up"></span>
										</a>
									</div>
									<div class="panel-body" style="height:420px;" id="network_report_body">			
										<div class="widget-box clog-highchart-widget" style="height:400px;" id="chartDemo4"></div>
									</div>
								</div>
							</td>
						</tr>
					</tbody> 
				</table>
			</center>
		</div -->  
	</div>
</div>


<div id="cluster_Common_Metrics" class="panel panel-default"> 
	<div class="panel-heading">Critical Metrics of Cluster
		<div class="btns">
			<strong class="btnstyle">Zoom</strong>
			<button class="btnstyle" id="cmcBtn0" >reset</button>
			<button class="btnstyle" id="cmcBtn1">1d</button>
			<button class="btnstyle" id="cmcBtn2">1w</button>
			<button class="btnstyle" id="cmcBtn3">2w</button>
			<button class="btnstyle" id="cmcBtn4">1m</button>
			<button class="btnstyle" id="cmcBtn5">3m</button>
			<button class="btnstyle" id="cmcBtn6">6m</button>
			<button class="btnstyle" id="cmcBtn7">1y</button>
		</div>
	</div>
	<div class="panel-body" id="common-metrics">
		<div id="common_charts"></div>
	</div>
</div>

<div id="host_metic_graphs2" class="panel panel-default">

	<div class="panel-heading">Metrics Data	
		<span style="float:center;width:100%;color:red;height:30px;" id="metricsSearchError2"></span>
		<div id="pages" style="float:right;margin-bottom:2px;margin-right:10px;">
			<strong>Lines</strong>
			<select id="pagesL2" style="width: 55px;" onChange="showHost(2)">
				<option value=1>1</option>
				<option value=2>2</option>
				<option value=3 selected>3</option>
				<option value=4 >4</option>
				<option value=5 >5</option>				
				<option value=6 >6</option>
				<option value=7 >7</option>				
				<option value=8 >8</option>
			</select>
			<a href="javascript:void(0)" id="firstL2" onclick="goFirstLine(2)">首页</a>
			<a href="javascript:void(0)" id="previousL2" onclick="goPreviousLine(2)">上一页</a>
			<a href="javascript:void(0)" id="lastL2" onclick="goNextLine(2)">下一页</a>
			<span style="float:center;width:100%;color:red;height:30px;" id="previousLError2"></span>
		</div>	
		<div style="float:right;margin-bottom:2px;margin-right:5px;">
			<strong>Columns</strong>
			<select id="pagesH2" style="width: 55px;" onChange="showHost(2)">
				<option value=1>1</option>
				<option value=2>2</option>
				<option value=3>3</option>
				<option value=4 selected>4</option>
			</select>
		</div>
		<div class="btns" style="margin-right:0px !important ; width:350px !important ;">
			<strong class="btnstyle">Zoom</strong>
			<button class="btnstyle" id="button00" >reset</button>
			<button class="btnstyle" id="button01">2h</button>
			<button class="btnstyle" id="button02">4h</button>
			<button class="btnstyle" id="button03">1d</button>
			<button class="btnstyle" id="button04">1w</button>
			<!--button class="btnstyle" id="button05">2w</button -->
			<button class="btnstyle" id="button06">1m</button>
			<button class="btnstyle" id="button07">3m</button>
			<button class="btnstyle" id="button08">6m</button>
		</div>
	</div>	
</div>

<div id="scrollUp" title="返回顶部" style="display: block;"></div>

<script src="http://svn.ui.sh.ctripcorp.com/istyle/code/istyle.30626.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/jquery.autocomplete.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/jquery-ui-1.10.2.custom.min.js" type="text/javascript" charset="utf-8"></script> 
<script src="/monitor/static/js/jquery-ui-timepicker-addon.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/bootstrap.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/highstock.js" type="text/javascript" charset="utf-8"></script> 
<script src="/monitor/static/js/exporting.js" type="text/javascript" charset="utf-8"></script>     
<script src="/monitor/static/js/date.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/charts.js" type="text/javascript" charset="utf-8"></script> 
<script src="/monitor/static/js/comm.js" type="text/javascript" charset="utf-8"></script>   	  
<script src="/monitor/static/js/jquery.livesearch.min.js" type="text/javascript" charset="utf-8"></script> 
<script src="/monitor/static/js/jquery.cookie.js" type="text/javascript" charset="utf-8"></script>  	
<script type="text/javascript" charset="utf-8">  
	//滚动监控
	$(window).scroll(function(){
	   var sc=$(window).scrollTop();
	   if(sc>300){   //设置下拉滚动超过多少后出现返回按钮
			$("#scrollUp").fadeIn();
	   }else{
			$("#scrollUp").fadeOut();
	   }
	});
	
	$("#scrollUp").fadeOut();
		
	$(window).load(function() {
		
		//返回顶部按钮
		$("#scrollUp").click(function(){
			$('body,html').animate({scrollTop:0},500);  //返回顶部动画设置，500为返回时间，单位毫秒
		});
	});	
		
	var names = 'r=day&s=by%20name&mc=2&json=1&hc=4&mc=2&z=large' ,
		initHost = 'r=hour&z=default&jr=&js=&v=0.22&vl=%20&ti=One%20Minute%20Load%20Average&json=1' ,
		clusterJson = null ,initCluster = ''; 	
	var ds_url = $("#data_service_url").val();			
	var path = ds_url + "/ganglia/graphjson?"  , 
		metricPath =  ds_url + '/metric/getmetriclist' ,
		gangliaPath = ds_url + '/ganglia/clusterinfo' ,
		allMetricsPath = ds_url + '/metric/getallmetriclist';	
				
	var reports = ['cpu_report','mem_report','load_report','network_report'] ,	
		months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
		
	var today = new Date();
	var year = today.getFullYear(),
		month = today.getMonth()+1,
		day = today.getDate(), 		
		hour = today.getHours(),
		mi = today.getMinutes(),		
		initColumns = 4,
		initLines = 3;	
		
	Highcharts.setOptions({
		global: {
       		useUTC: false
   	  	},
		lang:{
			months:months,
			shortMonths:months,
			weekdays:['星期一','星期二','星期三','星期四','星期五','星期六','星期七'],
			loading:"表捉急，我们正在努力加载数据..."
		}
	});
		
	function initTime(){
		$("#starttime").datetimepicker({
			altField:"#starttime_alt",
			altFieldTimeOnly:false
		});		
			
		$("#endtime").datetimepicker({
			altField:"#endtime_alt",
			altFieldTimeOnly:false
		});
	}		

	function initClusters(){
		var searchArray = new Array();
		searchArray[0] = "load_one";
		var totalCount = 0 ;
		
		$.ajax({
			url:gangliaPath,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
			success: function(result) { 
				var message = result.message;
				clusterJson = message.clusters;
				if(clusterJson == null || "undefined" == typeof(clusterJson)) return false ;				
				
				var count = 0 ;
				$("#hostList").append("<option value=''>-- Choose a Source --</option>");
				var parentDiv = document.getElementById('host-panel-body');
				
				for(var key in clusterJson ){
					
					if(count == 0 ) firstKey(key,parentDiv,searchArray);		
					count ++ ;						
						
					$("#clusters").append("<option value='"+key+"'>"+key+"</option>");		
					
					totalCount = hosts.length ;		
				}					
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				$("#clusters").append("<option >-- Choose a Source --</option>");
			}
		});	
	}
	
	function firstKey(key,parentDiv, searchArray){
		initCluster = key ;
		var name = names + '&c='+key ;
										
		var nameList = getNameList(name); 						
		loads(nameList,reports,key);
		
		name += '&m=load_one';
		var hosts = clusterJson[key];  
		nextNodes = hosts ;
		$("#host-panel-body").html("");
		
		$.each(hosts,function(ii , cs){							
			$("#hostList").append("<option value='"+cs+"'>"+cs+"</option>"); 
		});	
										
		serverChart(path +  name , 'area',parentDiv, 'load_one',true ,initColumns , initLines ,hosts,0);
		nextNodes = hosts.slice(initColumns*initLines , hosts.length);
							
		initMetricList(searchArray , key , false , null  );	
	}
		
	function initMetricList(searchArray , clusterName , isRedraw  , param ){ 	
		var sealen = 1;
		
		$.ajax({
			url:metricPath ,
			type:'get',
			data:{
				clustername:clusterName
			},
			dataType:'jsonp',
			jsonp:'jsonpCallback',
			success:function(result){
				var metrics = result.metrics;
				$("#likeQuery").html("");
				$("#likeQuery").append("<option value='"+searchArray[0]+"'>"+searchArray[0]+"</option>"); 
				
				if(metrics != null && "undefined" != typeof(metrics)) initCritical(metrics, searchArray , isRedraw);				
					
				$("#common-metrics").html("");
				var date = new Date().getTime();
				var name = names + '&c=' + clusterName  ; 
				
				sealen = searchArray.length ;
				var parentDiv = document.getElementById('common-metrics');					
				
				var str = path + name ; 
				if(isRedraw) str += param ;
				else str += '&st=' + date;
				
				str += '&m=' ;		
				
				changeCharts(str ,parentDiv,searchArray,'comm_mtc_',months,2 ,searchArray, 0);	
				
				if(!isRedraw) initAllMetrics(searchArray ,clusterName);	
			}
		});	
	}
	
	function initCritical(metrics, searchArray , isRedraw){
		$.each(metrics,function(index,metric){
			var isExist = $.inArray(metric,searchArray);
			
			if(isExist  == -1) {
				searchArray[index] = metric ;	
				if(!isRedraw)		
					$("#likeQuery").append("<option value='"+metric+"'>"+metric+"</option>"); 				
			}						
		});	
	}
	
	$(window).load(function(){		
		createNodePanel('1' , '2' , false);						
		initTime();
		initClusters();			
		
		$("#previousL1").hide();
		$("#previousL2").hide();
	});
		
	function loads(nameList,mnames,prefix){
		$("#monitors").html("");
		var parentDiv = document.getElementById('monitors');
		
		changeCharts(path ,parentDiv,mnames,prefix,months,2 ,nameList, 0);		
	}
		
	function clusterChange(){
		var cluster = $("#clusters").val();	
		var likeQuery = 'load_one' , hostList = []  ,  hosts ; 
		
		if(cluster =='')return;
		
		createNodePanel('1' , '2' , false);	
					
		var name = names + '&c='+cluster;	
		var currentHost = initHost + '&c='+cluster ;
		$("#host-panel-body").html("");		
		
		$("#hostList").html("<option value=''>-- Choose a Source --</option>");
		var parentDiv = document.getElementById('host-panel-body');
		
		if(cluster == null || "undefined" == typeof(cluster) || "undefined" == typeof(clusterJson[cluster]) ) return false ;
		
		hosts = clusterJson[cluster];
		nextNodes = hosts ;
		
		cancelInterval();
		
		$.each(hosts , function(i , hname){				
			$("#hostList").append("<option value='"+ hname +"'>"+ hname +"</option>");
		});	
		
		var str = path +  currentHost + "&m="+likeQuery;			
		serverChart(str , 'area',parentDiv,likeQuery,true , initColumns , initLines ,hosts,0);
		nextNodes = hosts.slice(initColumns*initLines , hosts.length);
		
		var searchArray = new Array();		
		searchArray[0] = "load_one";		
		initMetricList(searchArray , cluster , false , null  );	
		$("#autoSearch").val("load_one");
		
		$("#hosts").show();
		
		var nameList = getNameList(name);
		loads(nameList,reports,cluster);				
	}

	function hostChange(){		
		var cluster = $("#clusters").val();
		var host = $("#hostList").val();
		
		if(host =='')return ;
		
		createNodePanel('2' , '1' , false);
		
		var likeQuery = $("#likeQuery").val();
		var name = names + '&c='+cluster ;
											
		$("#host-panel-body").html("");
		$("#cmc-panel-body").html("");			
				
		if(likeQuery =='' || likeQuery == null ) likeQuery = 'load_one';
			
		var parentDiv = document.getElementById('host-panel-body');	
							
		var str = path + name + '&m='+ likeQuery; 
		changeCharts(str ,parentDiv,[cluster + " " + likeQuery],'mtc_',months,1 ,[''], 0);		
	}
		
	function searchMetrics(){
		var cluster = $("#clusters").val();
		if(cluster =='' || cluster == null)return ;
					
		var host = $("#hostList").val();
		var flag = (host == '' || host == null ) ? true : false ;
		var name = names + '&c='+cluster , currentHost = initHost + '&c=' + cluster;
		
		createNodePanel('2' , '1' , flag);		
		
		var starttime = $("#starttime").val();
		var endtime = $("#endtime").val();
		var likeQuery = $("#likeQuery").val();			
		
		if(likeQuery =='' || likeQuery == null ) likeQuery = 'load_one';
					
		if(starttime !='' && starttime != null && endtime != '' && endtime != null ){
			starttime = starttime.replace(' ','+');
			endtime = endtime.replace(' ','+');
			
			while(starttime.indexOf('/') != -1) starttime = starttime.replace('/','%2F');
			while(endtime.indexOf('/') != -1) endtime = endtime.replace('/','%2F');
			
			starttime = starttime.replace(':','%3A');
			endtime = endtime.replace(':','%3A');
			
			name += '&cs='+starttime+'&ce='+endtime;
			
			currentHost +=  '&cs='+starttime+'&ce='+endtime;
		}else {
			name += '&st=' + new Date().getTime();
			currentHost += '&st=' + new Date().getTime();
		}		
		
		var resFlag = true ;
		var parentDiv = document.getElementById('host-panel-body') , 
			cmcPDiv = document.getElementById('cmc-panel-body');	
			
		if(flag ){
			hosts = clusterJson[cluster];								
			hostList = getHostList(hosts,currentHost);		
						
			$("#host-panel-body").html("");	
	
			var str = path + name + "&m=" + likeQuery ;				
			serverChart(str , 'area',parentDiv,likeQuery ,true , initColumns , initLines ,hosts,0);
			
			$("#cmc-panel-body").html("");
			var cmcPath = path + name + '&m=' + likeQuery ; 			
			changeCharts(cmcPath ,cmcPDiv,[cluster + " " + likeQuery],'cmc_',months,1 ,[''], 0);		
		}else{						
			$("#host-panel-body").html("");							
			var str = path + name + '&h='+host +'&m=' + likeQuery; 
			changeCharts(str ,parentDiv,[likeQuery],'mtc_',months,1 ,[''], 0);			
		}	
	}
	nextNodes = [] , nodeOption = 1 , nodePage = 1 ;
	
	function showHost(id){
		 nodeOption = 1 , nodePage = 1 ;
		 changeHost(id);
	}
	
	function changeHost(id ){		
		var lines = $("#pagesL"+id).val();	
		var columns = $("#pagesH"+id).val();		
		var cluster = $("#clusters").val();	
		var likeQuery = $("#likeQuery").val();
		var host = $("#hostList").val();
			
		var hostList = []  ,  hosts ; 
		
		if(cluster == "" || "undefined" == typeof(clusterJson[cluster]))return;
		if(likeQuery =='' || likeQuery == null ) likeQuery = 'load_one';
		var flag = (host == '' || host == null ) ? true : false ;
		
		if(id == 1 ) createNodePanel('2' , '1' , flag);
		else if(id == 2) createNodePanel('1' , '2' , false);	
	
		var name = names + '&c='+cluster , currentHost = initHost + '&c='+cluster ;
		var parentDiv = document.getElementById('host-panel-body'), 
			cmcPDiv = document.getElementById('cmc-panel-body');	
		
		$("#host-panel-body").html("");		
		hosts = clusterJson[cluster];
		
		var title ='';
		$("#previousLError"+id).text('');
		$("#previousLError"+id).hide();		
			
		if(nodeOption == 1)	{
			var length = hosts.length ;
			var count = length%columns>0 ? Math.floor(length/columns +1) : length/columns;	
	
			nextNodes = hosts ;
			
			var str = path +  currentHost + "&m="+likeQuery;
			serverChart(str , 'area',parentDiv,likeQuery,true , columns,lines ,hosts,0);
						
			nextNodes = hosts.slice(columns*lines , hosts.length);
			$("#previousL1").hide();
			$("#previousL2").hide();
		}else if(nodeOption == 2 ){
			if(nextNodes == null || nextNodes.length == 0 || typeof(nextNodes) == 'undefined') {
				
				title = "没找到其他数据." ;	
				$("#previousLError"+id).text(title);
				$("#previousLError"+id).show();
				$("#lastL"+id).hide();
				
				return false ;
			} 
			
			$("#previousLError"+id).text('');
			$("#previousLError"+id).hide();
			$("#lastL"+id).show();
			$("#previousL"+id).show();				
			
			var str = path +  currentHost +"&m="+likeQuery;
			serverChart(str , 'area',parentDiv,likeQuery,true , columns,lines ,nextNodes,0);
			
			nextNodes = nextNodes.slice(columns*lines , nextNodes.length);			
			
		}else if(nodeOption == 3 ){
			var shows = lines*columns ;
			var end = (nodePage - 1 ) * shows ,start = (nodePage-2) * shows ;					
			nextNodes =  [] ;
			var currentNodes = [] , index = 0 ;
						
			for(i = start ; i < end ;i ++){
				currentNodes[index] = hosts[i];
				
				if(i> hosts.length) break ;
				
				if(currentNodes[index] == null || typeof(currentNodes[index]) == 'undefined'){
					title = "没找到其他数据." ;	
					$("#previousLError"+id).text(title);
					$("#previousLError"+id).show();
					$("#previousL"+id).hide();
					
					return false ;
				}				
				index ++ ;							
			}
			
			var str = path +  currentHost + "&m="+likeQuery;
			serverChart(str , 'area',parentDiv,likeQuery,true , columns,lines ,currentNodes,0);
			
			var count = 0 ;
			for(i = 0 ; i <start;i++){
				nextNodes[count] = hosts[i];
				count ++ ;
			}
			
			for(i = end ; i<hosts.length;i++){
				nextNodes[count] = hosts[i];
				count ++ ;
			}
			
			$("#lastL"+id).show();
			$("#previousL"+id).show();
		}
	
	}
	function goFirstLine(id ){
		nodeOption = 1 ;
		if(nodePage == 1 ){
			title = "已经是第一页." ;	
			$("#previousLError"+id).text(title);
			$("#previousLError"+id).show();
			$("#previousL1").hide();
			$("#previousL2").hide();
			
			return false ;
		}else{
			$("#previousLError"+id).text('');
			$("#previousLError"+id).hide();
		}
		
		$("#lastL"+id).show();
		
		nodePage = 1 ;
		changeHost(id);
	}
	function goPreviousLine(id){
		nodeOption = 3 ;
		
		if(nodePage == 1 ){
			title = "已经是第一页." ;	
			$("#previousLError"+id).text(title);
			$("#previousLError"+id).show();
			$("#previousL1").hide();
			$("#previousL2").hide();
			$("#lastL"+id).show();
			
			return false ;
		}else{
			$("#previousLError"+id).text('');
			$("#previousLError"+id).hide();
		}
		changeHost(id);
		nodePage -- ;
	}
	function goNextLine(id){
		nodeOption = 2 ; 
		changeHost(id);	
		nodePage ++ ;	
	}
	
	function redawChart(hours , option , isCMCShow){
		
		var before = today.getTime() - hours*60*60*1000;
		var se = new Date(before);
		var seyear = se.getFullYear();
		var semonth = se.getMonth()+1;
		var seday = se.getDate();		
		var sehour = se.getHours();
		var semi = se.getMinutes();
		
		var st = getTimeStrNormal(seyear,semonth,seday,sehour,semi) ,
			se = getTimeStrNormal(year,month,day,hour,mi) ;
		
		var param =  '&cs='+st+'&ce='+se;		
		var cluster = $("#clusters").val();
		var likeQuery = $("#likeQuery").val();
		var host = $("#hostList").val();
		
		var flag = (host == '' || host == null ) ? true : false ;		
		if(likeQuery =='' || likeQuery == null ) likeQuery = 'load_one';
		var name = '&c=' + cluster  + "&m=" + likeQuery + param ;
				
		var parentDiv = document.getElementById('host-panel-body') , 
			cmcPDiv = document.getElementById('cmc-panel-body');
		
		if(option == 1)	 {					
			if(!isCMCShow) createNodePanel('1' , '2' , false);	
			else{
				createNodePanel('2' , '1' , flag);
				
				$("#starttime").val(st);
				$("#endtime").val(se);
			}
				
			if(flag){
				var hosts = clusterJson[cluster];				
					
				$("#host-panel-body").html("");
														
				var str = path + initHost + '&c='+cluster + param + '&m='+ likeQuery;
				
				serverChart(str , 'area',parentDiv,likeQuery,true , initColumns,initLines ,hosts,0);
							
				$("#cmc-panel-body").html("");
				var cmcPath = path + names + name ; 
				changeCharts(cmcPath ,cmcPDiv,[cluster + " " +likeQuery],'cmc_',months,1 ,[''], 0);	
			}else{
				$("#host-panel-body").html("");
							
				var str = path + names + name + '&h='+host ; 
				changeCharts(str ,parentDiv,[likeQuery],'mtc_',months,1 ,[''], 0);	
			}			
		}else if (option == 3 ){
			var name = names + '&c='+cluster  + param;	
			
			var nameList = getNameList(name);	
			loads(nameList,reports,cluster);
			
		}else if (option == 4 ){
			var searchArray = new Array();
			searchArray[0] = "load_one";
			initMetricList(searchArray , cluster , true , param )
		}
	}
		
	/**Nodes metrics charts redraw [hour]*/		
	
	$('#button00').bind("click", function()  {	
		
		redawChart(1,1,false);
	});
		
	$('#button01').bind("click", function()  {
		redawChart(2,1,false);
	});
	
	$('#button02').bind("click", function()  {
		redawChart(4,1,false);
	}); 
	
	$('#button03').bind("click", function()  {
		redawChart(24,1,false);
	}); 
	
	$('#button04').bind("click", function()  {
		redawChart(24*7,1,false);
	}); 
			
	$('#button05').bind("click", function()  {
		redawChart(24*14,1,false);
	}); 	
			
	$('#button06').bind("click", function()  {
		redawChart(24*30,1,false);
	});	
			
	$('#button07').bind("click", function()  {
		redawChart(24*30*3,1,false);
	});	
			
	$('#button08').bind("click", function()  {
		redawChart(24*30*6,1,false);
	});

	$('#button0').bind("click", function()  {
		redawChart(1,1,true);
	});
		
	$('#button1').bind("click", function()  {
		redawChart(2,1,true);
	});
	
	$('#button2').bind("click", function()  {
		redawChart(4,1,true);
	}); 
	
	$('#button3').bind("click", function()  {
		redawChart(24,1,true);
	}); 
	
	$('#button4').bind("click", function()  {
		redawChart(24*7,1,true);
	}); 
			
	$('#button5').bind("click", function()  {
		redawChart(24*14,1,true);
	}); 	
			
	$('#button6').bind("click", function()  {
		redawChart(24*30,1,true);
	});	
			
	$('#button7').bind("click", function()  {
		redawChart(24*30*3,1,true);
	});	
			
	$('#button8').bind("click", function()  {
		redawChart(24*30*6,1,true);
	});	
		
	/**cluster_Report_Metrics charts redraw [hour] */
	$('#reportBtn0').bind("click", function()  {
		redawChart(1,3,true);
	});
	
	$('#reportBtn1').bind("click", function()  {
		redawChart(2,3,true);
	});
	
	$('#reportBtn2').bind("click", function()  {
		redawChart(4,3,true);
	});
		
	$('#reportBtn3').bind("click", function()  {
		redawChart(24,3,true);
	});
	
	$('#reportBtn4').bind("click", function()  {
		redawChart(24*7,3,true);
	}); 
	
	$('#reportBtn7').bind("click", function()  {
		redawChart(24*7*2,3,true);
	});
	
	$('#reportBtn5').bind("click", function()  {
		redawChart(24*30,3,true);
	}); 
	
	$('#reportBtn6').bind("click", function()  {
		redawChart(24*30*3,3,true);
	}); 

	/**cluster_Common_Metrics charts redraw [day]*/
	$('#cmcBtn0').bind("click", function()  {
		redawChart(0,4,true);
	});
	
	$('#cmcBtn1').bind("click", function()  {
		redawChart(24,4,true);
	});
		
	$('#cmcBtn2').bind("click", function()  {
		redawChart(24*7,4,true);
	});
	
	$('#cmcBtn3').bind("click", function()  {
		redawChart(24*14,4,true);
	}); 
	
	$('#cmcBtn4').bind("click", function()  {
		redawChart(24*30,4,true);
	}); 
	
	$('#cmcBtn5').bind("click", function()  {
		redawChart(24*30*3,4,true);
	}); 
			
	$('#cmcBtn6').bind("click", function()  {
		redawChart(24*30*6,4,true);
	}); 
	
	$('#cmcBtn7').bind("click", function()  {
		redawChart(24*365,4,true);
	}); 	
	
</script>
<script src="/monitor/static/js/combobox.js" type="text/javascript" charset="utf-8"></script> 
<script type="text/javascript" charset="utf-8">   
		
	$("#likeQuery").combobox();
 </script>
${commonfooter(messages)|n,unicode}
