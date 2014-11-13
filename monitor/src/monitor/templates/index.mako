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
<link type="text/css" rel="stylesheet" href="/monitor/static/css/bootstrap.css">

<input id="data_service_url" type="hidden" value="${DATA_SERVICE.DI_DATA_SERVICE_URL.get() | n,unicode }">

<div class="subnav subnav-fixed">
	<div class="container-fluid">
    	<ul class="nav nav-pills">
        	<li class="active">
				<li class="active"><a href="${url('monitor.views.index')}">统计报表</a>				
			</li>
	        <li class=""><a href="${url('monitor.views.metrics')}">Metrics监控</a></li>
		</ul>
	</div>
</div>

<div  class="panel panel-default" style="margin-top:30px;">
	<div class="panel-heading">	
		<ul class="nav nav-pills" role="tablist">
	        <li role="presentation" class="active" id="yarnChoosen"><a href="javascript:void(0);" onclick="showHdfs(false);">MapReduce</a></li>
        	<li role="presentation" class="" id="hdfsChoosen"><a href="javascript:void(0);" onclick="showHdfs(true);">HDFS</a></li>
		</ul>
		<div class="btns" style="margin-top:-30px;" id="timeSampsize">						
			<strong class="btnstyle">Zoom</strong>
			<button class="btnstyle" id="jobBtn0">reset</button>
			<button class="btnstyle" id="jobBtn1">1w</button>
			<button class="btnstyle" id="jobBtn2">2w</button>
			<button class="btnstyle" id="jobBtn3">1m</button>
			<button class="btnstyle" id="jobBtn4">3m</button>
			<button class="btnstyle" id="jobBtn5">6m</button>
			<button class="btnstyle" id="jobBtn6">1y</button>
		</div>
	</div>
	<div class="panel-body" id="yarn_hadoop_aioInfo" >
		<div class="panel panel-info" style="width:48%;float:left;" id="job_success_rate">
			<div class="panel-heading">Job 成功率统计	
				<span id="successRateError" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('job_success_rate','job_success_rate_body','420px')" title="折叠/展开" style="float:right;">
					<span id="job_success_rate_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" id="job_success_rate_body" style="height:370px;">
				<div class="widget-box clog-highchart-widget" style="height:350px;" id="aioinfo_monitor1"></div>
			</div>
		</div>
	
		<div class="panel panel-info" style="width:48%;float:left;" id="job_count">
			<div class="panel-heading">Job 总数量统计
				<span id="jobCountError" style="color:red;"></span>											
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('job_count','job_count_body','420px')" title="折叠/展开" style="float:right;">
					<span id="job_count_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" id="job_count_body" style="height:370px;">
				<div class="widget-box clog-highchart-widget" style="height:350px;" id="aioinfo_monitor2"></div>
			</div>
		</div>
						
		<div class="panel panel-info" style="width:99%;float:left;" id="user_job_history">
			<div class="panel-heading">用户Job 历史执行结果统计
				<span id="jobHistoryError" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('user_job_history','user_job_history_body','700px')" title="折叠/展开" style="float:right;">
					<span id="user_job_history_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body"  id="user_job_history_body" style="height:650px;">																	
				<div class="widget-box clog-highchart-widget" style="height:630px;"  id="aioinfo_monitor3"></div>							
			</div>
		</div>
			
		<div id="job_cluster_reports" class="panel panel-info" style="width:99%;float:left;"> 
			<div class="panel-heading">Job 数量统计报表								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('job_cluster_reports','job_cluster_reports_body','600px')" title="折叠/展开" style="float:right;">
					<span id="job_cluster_reports_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div> 
			<div class="panel-body" id="job_cluster_reports_body" style="width:99%;">			
				<div style="float:left;margin-left:15px;margin-bottom:5px;">
						每页显示<select id="pagesC" style="width: 75px;" onChange="showJob(1)">
							<option value="20">20</option>
							<option value="40">40</option>
							<option value="60">60</option>
							<option value="80">80</option>
							<option value="100">100</option>
						</select>条记录
					</div>
				<center>
						<table border="0" cellspacing="4" class="job_table"  id="job_monitor_details" style="table-layout:fixed;width:98%;">
							<thead class="thread_head">
								<tr>
								<th class="id">Job Started Day
								<th>Success Ratio (%)
								<th>Un-Success Ratio (%)
								<th>Failed Ratio (%)
								<th>Killed Ratio (%)
								<th>Error Ratio (%)
								<th>Total Job Count
								<th>Success Job Count
								<th>Un-Success Job Count
								<th>Failed Job Count
								<th>Error Job Count
								<th>Killed Job Count
								</tr>
							</thread>
							<tbody class="tbody_line">
							</tbody>
							<tfoot id="jobCount" class="tfoot_line">
								<tr>
									<th>
									<input class="search_init" type="text" name="started_dayC" placeholder="Started Day">
									<th>
									<input class="search_init" type="text" name="success_ratioC" placeholder="Success Ratio">
									<th>
									<input class="search_init" type="text" name="unsuccess_ratioC" placeholder="Un-Success Ratio">
									<th>
									<input class="search_init" type="text" name="failed_ratioC" placeholder="Failed Ratio">
									<th>
									<input class="search_init" type="text" name="killed_ratioC" placeholder="Killed Ratio">
									<th>
									<input class="search_init" type="text" name="error_ratioC" placeholder="Error Ratio">
									<th>
									<input class="search_init" type="text" name="total_job_countC" placeholder="Total Job Count">
									<th>
									<input class="search_init" type="text" name="success_job_countC" placeholder="Success Job Count">
									<th>
									<input class="search_init" type="text" name="unsuccess_job_countC" placeholder="Un-Success Job Count">
									<th>
									<input class="search_init" type="text" name="failed_job_countC" placeholder="Failed Job Count">
									<th>
									<input class="search_init" type="text" name="killed_job_countC" placeholder="Error Job Count">
									<th>
									<input class="search_init" type="text" name="error_job_countC" placeholder="Killed Job Count">
								</tr>
							</tfoot>
						</table>
					</center>				
				<div id="pages" style="float:right;height:30px;z-index:10;margin-right:25px;">
						<a class="pages" href="javascript:void(0)" id="lastC" onclick="goNextJob(1)">下一页</a>
						<a class="pages" href="javascript:void(0)" id="previousC" onclick="goPreviousJob(1)">上一页</a>
						<a class="pages" href="javascript:void(0)" id="firstC" onclick="goFirstJob(1)">首页</a>
					</div>
				<div id="pageCError"><span style="float:center;width:100%;color:red;height:30px;">已经是首页.</span></div>
			</div>
		</div>
				
		<div id="user_cluster_reports" class="panel panel-info" style="width:99%;float:left;"> 
			<div class="panel-heading">Job 历史执行结果统计报表
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('user_cluster_reports','user_cluster_reports_body','600px')" title="折叠/展开" style="float:right;">
					<span id="user_cluster_reports_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div> 
			<div class="panel-body" id="user_cluster_reports_body" style="width:99%;">		
				<div style="float:left;margin-bottom:5px;margin-left:15px;width:100%">
						每页显示<select id="pagesU" style="width: 75px;" onChange="showJob(2)">
							<option value="20">20</option>
							<option value="40">40</option>
							<option value="60">60</option>
							<option value="80">80</option>
							<option value="100">100</option>
						</select>条记录
						<input type="text" id="searchHistory" style="width:220px;float:right;margin-right:30px;" 
							placeholder="请输入started Day/User Name" onblur="go2search()">
						<strong style="float:right;margin-top:2px;">搜索&nbsp;&nbsp;</strong>
					</div>
				<center>
						<table border="0" cellspacing="4" class="job_table" id="user_monitor_details" style="table-layout:fixed;width:98%;">
							<thead class="thread_head">
								<tr>
								<th class="id">Started Day
								<th class="id">User Name
								<th>Success Ratio (%)
								<th>Un-Success Ratio (%)
								<th>Failed Ratio (%)
								<th>Killed Ratio (%)
								<th>Error Ratio (%)
								<th>Total Job Count
								<th>Success Job Count
								<th>Un-Success Job Count
								<th>Failed Job Count
								<th>Error Job Count
								<th>Killed Job Count
								<th>Job Execution Time(min)
								<th>Map Count
								<th>Reduce Count
								</tr>
							</thread>
							<tbody class="tbody_line">
							</tbody>
							<tfoot id="ujobCount" class="tfoot_line">
								<tr>
									<th>
									<input class="search_init" type="text" name="started_day" placeholder="Started Day">
									<th>
									<input class="search_init" type="text" name="user_name" placeholder="User Name">
									<th>
									<input class="search_init" type="text" name="success_ratio" placeholder="Success Ratio">
									<th>
									<input class="search_init" type="text" name="unsuccess_ratio" placeholder="Un-Success Ratio">
									<th>
									<input class="search_init" type="text" name="failed_ratio" placeholder="Failed Ratio">
									<th>
									<input class="search_init" type="text" name="killed_ratio" placeholder="Killed Ratio">
									<th>
									<input class="search_init" type="text" name="error_ratio" placeholder="Error Ratio">
									<th>
									<input class="search_init" type="text" name="total_job_count" placeholder="Total Job Count">
									<th>
									<input class="search_init" type="text" name="success_job_count" placeholder="Success Job Count">
									<th>
									<input class="search_init" type="text" name="unsuccess_job_count" placeholder="Un-Success Job Count">
									<th>
									<input class="search_init" type="text" name="failed_job_count" placeholder="Failed Job Count">
									<th>
									<input class="search_init" type="text" name="killed_job_count" placeholder="Error Job Count">
									<th>
									<input class="search_init" type="text" name="error_job_count" placeholder="Killed Job Count">
									<th>
									<input class="search_init" type="text" name="job_execution_time" placeholder="Job Execution Time">
									<th>
									<input class="search_init" type="text" name="map_count" placeholder="Map Count">
									<th>
									<input class="search_init" type="text" name="reduce_count" placeholder="Reduce Count">
								</tr>
							</tfoot>
						</table>
					</center>
				<div id="pages" style="float:right;height:30px;z-index:10;margin-right:25px;">
						<a class="pages" href="javascript:void(0)" id="lastU" onclick="goNextJob(2)">下一页</a>
						<a class="pages" href="javascript:void(0)" id="previousU" onclick="goPreviousJob(2)">上一页</a>
						<a class="pages" href="javascript:void(0)" id="firstU" onclick="goFirstJob(2)">首页</a>
					</div>
				<div id="pagePError"><span style="float:center;width:100%;color:red;height:30px;">已经是首页.</span></div>
			</div>
		</div>
	</div>
	
	<div class="penel-body" id="hdfs_hadoop_aioInfo">
		<div class="panel panel-info" style="width:48%;float:left;" id="user_fd_count">
			<div class="panel-heading">用户文件/目录数量统计																	
				<span id="fileDirError" style="color:red;"></span>	
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('user_fd_count','user_fd_count_body','720px')" title="折叠/展开" style="float:right;">
					<span id="user_fd_count_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:670px;" id="user_fd_count_body">
				<div class="widget-box clog-highchart-widget" style="height:650px;"  id="aioinfo_monitor5"></div>							
			</div>
		</div>
		
		<div class="panel panel-info" style="width:48%;float:left;" id="user_space_count">
			<div class="panel-heading">用户空间使用统计																	
				<span id="userSpaceError" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('user_space_count','user_space_count_body','720px')" title="折叠/展开" style="float:right;">
					<span id="user_space_count_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:670px;" id="user_space_count_body">
				<div class="widget-box clog-highchart-widget" style="height:650px;"  id="aioinfo_monitor6"></div>						
			</div>
		</div>
	
		<!--div class="panel panel-info" style="float:left;width:32%;" id="hdfs_info_status">
			<div class="panel-heading">HDFS集群各指标使用总数统计
				<span id="hdfs_info_error" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('hdfs_info_status','hdfs_info_status_body','370px')" title="折叠/展开" style="float:right;">
					<span id="hdfs_info_status_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:320px;" id="hdfs_info_status_body">
				<div class="widget-box clog-highchart-widget" style="height:300px;"  id="hdfs_info"></div>						
			</div>
		</div-->
			
		<div class="panel panel-info" style="float:left;width:32%;" id="hdfs_files_status">
			<div class="panel-heading">HDFS集群文件使用统计
				<span id="hdfs_files_error" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('hdfs_files_status','hdfs_files_status_body','370px')" title="折叠/展开" style="float:right;">
					<span id="hdfs_files_status_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:320px;" id="hdfs_files_status_body">
				<div class="widget-box clog-highchart-widget" style="height:300px;" id="hdfs_files"></div>						
			</div>
		</div>
				
		<div class="panel panel-info" style="float:left;width:32%;" id="hdfs_heap_status">
			<div class="panel-heading">HDFS集群堆内存使用统计
				<span id="hdfs_heap_error" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('hdfs_heap_status','hdfs_heap_status_body','370px')" title="折叠/展开" style="float:right;">
					<span id="hdfs_heap_status_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:320px;" id="hdfs_heap_status_body">
				<div class="widget-box clog-highchart-widget" style="height:300px;" id="hdfs_heap"></div>						
			</div>
		</div>
				
		<div class="panel panel-info" style="float:left;width:32%;" id="hdfs_nonheap_status">
			<div class="panel-heading">HDFS集群非堆内存使用统计
				<span id="hdfs_nonheap_error" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('hdfs_nonheap_status','hdfs_nonheap_status_body','370px')" title="折叠/展开" style="float:right;">
					<span id="hdfs_nonheap_status_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:320px;" id="hdfs_nonheap_status_body">
				<div class="widget-box clog-highchart-widget" style="height:300px;" id="hdfs_nonheap"></div>						
			</div>
		</div>
			
		<div class="panel panel-info" style="float:left;width:32%;" id="hdfs_dfs_status">
			<div class="panel-heading">HDFS集群容量使用统计
				<span id="hdfs_dfs_error" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('hdfs_dfs_status','hdfs_dfs_status_body','370px')" title="折叠/展开" style="float:right;">
					<span id="hdfs_dfs_status_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:320px;" id="hdfs_dfs_status_body">
				<div class="widget-box clog-highchart-widget" style="height:300px;"  id="hdfs_dfs"></div>						
			</div>
		</div>
			
		<div class="panel panel-info" style="float:left;width:32%;" id="hdfs_nodes_status">
			<div class="panel-heading">Data Nodes运行统计
				<span id="hdfs_nodes_error" style="color:red;"></span>								
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('hdfs_nodes_status','hdfs_nodes_status_body','370px')" title="折叠/展开" style="float:right;">
					<span id="hdfs_nodes_status_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div>
			<div class="panel-body" style="height:320px;" id="hdfs_nodes_status_body">
				<div class="widget-box clog-highchart-widget" style="height:300px;" id="hdfs_nodes"></div>						
			</div>
		</div>
		
		<div id="live_nodes" class="panel panel-info" style="float:left;width:99%;" > 
			<div class="panel-heading">运行节点详情
				<a class="pages" href="javascript:void(0);" 
					onclick="isFold('live_nodes','live_nodes_body','600px')" title="折叠/展开" style="float:right;">
					<span id="live_nodes_icon" class="glyphicon glyphicon-chevron-up"></span>
				</a>
			</div> 
			<div class="panel-body" id="live_nodes_body" style="width:99%;">
				<div style="z-index:1;position: absolute;float:left;margin-bottom:5px;margin-left:15px;width:80%">
					每页显示<select id="pageLive" style="width: 75px;" onChange="showNodes()">
						<option value=20>20</option>
						<option value=40>40</option>
						<option value=60>60</option>
						<option value=80>80</option>
						<option value=100>100</option>
					</select>条记录
					<!--input type="text" id="searchNodes" style="width:220px;float:right;margin-right:30px;"
						placeholder="请输入started Day/User Name" onblur="searchNodes()">
						<strong style="float:right;margin-top:2px;">搜索&nbsp;&nbsp;</strong-->
				</div>
					<table border="0" cellspacing="4" class="job_table" id="live_nodes_details" style="table-layout:fixed;width:98%;">
						<thead class="thread_head">
							<tr>
							<th class="id" style="width:305px;">Node
							<th>Num Blocks
							<th>Used Space(GB)
							<th>Last Contact
							<th>Capacity(TB)
							<th>NonDfs Used Space(GB)
							<th>Admin State
							<th>Used Space(%)						
							<th>Status						
							<th>Used Space(%)	
							</tr>
						</thread>
						<tbody class="tbody_line">
						</tbody>
						<tfoot id="liveNodes" class="tfoot_line">
							<tr>
								<th>
									<input class="search_init" type="text" name="node" placeholder="Node">
								<th>
									<input class="search_init" type="text" name="numBlocks" placeholder="Num Blocks">
								<th>
									<input class="search_init" type="text" name="usedSpace" placeholder="Used Space">
								<th>
									<input class="search_init" type="text" name="lastContact" placeholder="Last Contact">
								<th>
									<input class="search_init" type="text" name="capacity" placeholder="Capacity">
								<th>
									<input class="search_init" type="text" name="nonDfsUsedSpace" placeholder="NonDfs Used Space">
								<th>
									<input class="search_init" type="text" name="adminState" placeholder="Admin State">
								<th>
									<input class="search_init" type="text" name="usedRate" placeholder="Used Rate">
								<th>
									<input class="search_init" type="text" name="status" placeholder="status">
							</tr>
							</tfoot>
					</table>
				<div id="pageNodes" style="float:right;height:30px;magin-top:2px;margin-right:25px;">
					<a href="javascript:void(0)" id="firstLive" onclick="goFirstNode()">首页</a>
					<a href="javascript:void(0)" id="previousLive" onclick="goPreviousNode()">上一页</a>
					<a href="javascript:void(0)" id="lastLive" onclick="goNextNode()">下一页</a>
					<span id="totalPage"></span>
					<span style="color:red;height:30px;" id="pageLiveError"></span>
				</div>
			</div>			
		</div>
	</div>	
</div>
	

<div id="scrollUp" title="返回顶部" style="display: block;"></div>

<script src="http://svn.ui.sh.ctripcorp.com/istyle/code/istyle.30626.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/fnSetFilteringDelay.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/bootstrap.js" type="text/javascript" charset="utf-8"></script>
<script src="/monitor/static/js/highstock.js" type="text/javascript" charset="utf-8"></script> 
<script src="/monitor/static/js/exporting.js" type="text/javascript" charset="utf-8"></script>    
<script src="/monitor/static/js/charts.js" type="text/javascript" charset="utf-8"></script> 
<script src="/monitor/static/js/comm.js" type="text/javascript" charset="utf-8"></script>  
<script src="/monitor/static/js/date.js" type="text/javascript" charset="utf-8"></script>  
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
		showHdfs(false);
		
		//返回顶部按钮
		$("#scrollUp").click(function(){
			$('body,html').animate({scrollTop:0},500);  //返回顶部动画设置，500为返回时间，单位毫秒
		});
	});	
	
	var ds_url = $("#data_service_url").val(); 
	
	var jobDetailPath = ds_url+'/yarn/get/jobcountdetailbypage',
		userJobPath = ds_url + '/yarn/get/jobcountdetailbyuserandpage' ,
		hisPath = ds_url + '/yarn/get/jobnewestuserjobcount' ,
		sucPath = ds_url + '/yarn/get/jobsuccessrate' ,
		jobPath = ds_url + '/yarn/get/jobsuccesscountbydate' ,
		byDatePath = ds_url+'/yarn/get/jobuserjobcountbydate' ,
		resetPath = ds_url+'/yarn/get/jobnewestuserjobcount' ,
		
		filePath  = ds_url + '/hdfs/get/userfileinfo' , 
		hdfsPath = ds_url + '/hdfs/get/hdfsusageinfo' ,
		
		initDays = "?date=14" ,
		initPage = "?page=1",
		initShow = "&show=" ,
		first = initPage + initShow ,
		initFirst = first + 20 ;
		
	var months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
		
	Highcharts.setOptions({
		global: {
       		useUTC: false
     	  },
		lang:{
			months:months,
			shortMonths:months,
			weekdays:['星期一','星期二','星期三','星期四','星期五','星期六','星期七'],
			loading:"表捉急，我们正在努力加载数据..."
		},
		colors:[
		'#1569C7', '#6960EC', '#368BC1', '#6698FF', '#3BB9FF', '#82CAFA', '#ADDFFF', '#BCE954', '#357EC7',
		'#342D7E', '#151B54', '#2B547E', '#F70D1A', '#566D7E', '#98AFC7', '#B6B6B4', '#565051', '#726E6D',
		'#EDC9AF', '#990012', '#E67451', '#F75D59', '#C35817', '#6CC417', '#FF9655', '#7FFFD4', '#3090C7',
		'#2B60DE', '#1589FF', '#93FFE8', '#F88017', '#C47451', '#4CC552', '#CCFFFF', '#C2DFFF', '#736AFF',
		'#7F5A58', '#954535', '#F778A1', '#4863A0', '#E56717', '#437C17', '#78866B', '#00FFFF', '#438D80',
		'#7E354D', '#810541', '#DC381F', '#DDDF00', '#E77471', '#6AA121', '#008080', '#5E7D7E', '#3B9C9C',
		'#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4',
		'#151B8D', '#0041C2', '#2554C7', '#56A5EC', '#4EE2EC', '#7DFDFE', '#81D8D0', '#77BFC7', '#3EA99F']
	});	
	
	function showHdfs(isHdfs){
		
		$("#pageCError").hide();
		$("#pagePError").hide();	
		$("#pageLiveError").hide();
		
		
		$("#previousC").hide();	
		$("#previousU").hide();				
		$("#previousLive").hide();
		
		if(isHdfs){		
			$("#hdfsChoosen").attr('class','active');			
			$("#yarnChoosen").attr('class','');
			$("#timeSampsize").hide();
			
			divAndFile(filePath);
			hdfsInfo(hdfsPath);
				
			$("#yarn_hadoop_aioInfo").hide();
			$("#hdfs_hadoop_aioInfo").show();
		}else{
			$("#hdfsChoosen").attr('class','');			
			$("#yarnChoosen").attr('class','active');
			$("#timeSampsize").show();
					
			loadMonitor(jobDetailPath + initFirst );
			userJobMonitor(userJobPath + initFirst);
			
			successRio(sucPath  + initDays ,'');
			jobCount(jobPath + initDays,'');		
			historyCount(hisPath,'');			
						
			$("#yarn_hadoop_aioInfo").show();
			$("#hdfs_hadoop_aioInfo").hide();
		}
	}
	
	var jobCPage = 1 , jobUPage = 1 ;
	function showJob(option){
		var showC = $("#pagesC").val();
		var showU = $("#pagesU").val();
		
		if(option == 1 ){
			if(jobCPage == 1 )  $("#previousC").hide();	
			loadMonitor(jobDetailPath + "?page="+jobCPage + initShow + showC );
		}else if(option ==2 ){		
			if(jobUPage == 1 )  $("#previousU").hide();
			userJobMonitor(userJobPath + "?page="+jobUPage + initShow + showU);
		}
	}
	
	function goFirstJob(option){
		var showC = $("#pagesC").val();
		var showU = $("#pagesU").val();
		
		$("#pageCError").hide();
		$("#pagePError").hide();	
		$("#firstC").disabled="false";		
				
		if(option == 1 ){			
			$("#previousC").hide();
			if(jobCPage == 1 ) {
				$("#pageCError").show();	
				$("#firstC").disabled="disabled";	
			}else{
				jobCPage = 1 ;
				loadMonitor(jobDetailPath + first + showC );
			}
		}else if(option ==2 ){		
			$("#previousU").hide();
			if(jobUPage == 1 ) {
				$("#pagePError").show();
				$("#firstU").disabled="disabled";	
			}else{
				jobUPage = 1 ;
				userJobMonitor(userJobPath + first + showU);
			}
		}
	}
	
	function goPreviousJob(option){
		var showC = $("#pagesC").val();
		var showU = $("#pagesU").val();
				
		$("#pageCError").hide();
		$("#pagePError").hide();
		$("#firstC").disabled="false";	
		
		if(option == 1 ){
			if(jobCPage == 1 )  {				
				$("#pageCError").show();	
				$("#previousC").hide();
				$("#firstC").disabled="disabled";	
			}else {
				jobCPage -- ;
				$("#previousC").show();	
				if(jobCPage == 1 )	$("#previousC").hide();
				loadMonitor(jobDetailPath + "?page="+jobCPage + initShow + showC );
			}
		}else if(option ==2 ){	
		
			if(jobUPage == 1 ){
				$("#pagePError").show();	
				$("#previousU").hide();
				$("#firstU").disabled="disabled";				
			}else {
				jobUPage -- ;	
				
				$("#previousU").show();
				if(jobUPage == 1 )	$("#previousU").hide();
				userJobMonitor(userJobPath + "?page="+jobUPage + initShow + showU);
			}
		}
	}
	
	function goNextJob(option){
		var showC = $("#pagesC").val();
		var showU = $("#pagesU").val();
				
		$("#pageCError").hide();
		$("#pagePError").hide();
		
		if(option == 1 ){		
			jobCPage ++ ;
			$("#previousC").show();	
			loadMonitor(jobDetailPath + "?page="+jobCPage + initShow + showC );
		}else if(option ==2 ){	
			jobUPage ++ ;	
			$("#previousU").show();	
			userJobMonitor(userJobPath + "?page="+jobUPage + initShow + showU);
		}
	}	
	
	$("#searchHistory").bind('keypress',function(event){
		 if(event.keyCode == "13") {
			go2search();
		}
	});
	
	function go2search(){
		searchPath = ds_url + '/yarn/get/jobcountdetailbyuserordate';
		var condition = $("#searchHistory").val();
		
		temp = condition ;
		while(temp.indexOf("\\g") != -1){
			temp = temp.replace("\\g","");
		}
		if (temp.trim() == "" || temp == null ) {
			
			userJobMonitor(userJobPath + initFirst);
			return ;
		}
		
		searchPath += "?search=" + condition;
	
		userJobMonitor(searchPath);
	}
	
	function searchNodes(){}

	var today = new Date();
	var year = today.getFullYear();
	var month = today.getMonth()+1;
	var day = today.getDate(); 		
	var hour = today.getHours();
	var mi = today.getMinutes();		
			
	function redawChart(days){
		
		var param = '?date=';
		var resetP = param + 14 ;		
		param += days ;
		
		if(days == 1 ) {
			jobCount(jobPath ,resetP );
			successRio(sucPath , resetP);
		}else{
			successRio(sucPath , param);
			jobCount(jobPath, param );
		}
		
		historyCount(byDatePath,param);
	}
	
	$('#jobBtn0').bind("click", function()  {
		redawChart(1);
	});
	
	$('#jobBtn1').bind("click", function()  {
		redawChart(7);
	});
		
	$('#jobBtn2').bind("click", function()  {
		redawChart(7*2);
	});
	
	$('#jobBtn3').bind("click", function()  {
		redawChart(30);
	}); 
	
	$('#jobBtn4').bind("click", function()  {
		redawChart(30*3);
	}); 
	
	$('#jobBtn5').bind("click", function()  {
		redawChart(30*6);
	}); 
	
	$('#jobBtn6').bind("click", function()  {
		redawChart(365);
	}); 
 </script>
${commonfooter(messages)|n,unicode}
