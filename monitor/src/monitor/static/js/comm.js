
function getNameList(names){
	var nameList =[];
	
	for(var index =0 ;index<reports.length;index ++){
		var report = reports[index];
		nameList[index] = names+'&g='+report;
	}
	return nameList ;
}
	
function getHostList(hosts , host){
	var hostList = [] ;
	
	for(var index =0 ;index<hosts.length;index ++){
		var hh = hosts[index];
		hostList[index] = host+'&h='+hh;
	}
	return hostList;
}

function initAllMetrics(searchArray , clusterName){
	var number = searchArray.length ;
	$.ajax({
		url:  allMetricsPath ,
		type:'get',
		data:{
			clustername:clusterName
		},
		dataType:'jsonp',
		jsonp:'jsonpCallback',
		success:function(result){
			var metrics = result.metrics;		
			
			if(metrics == null || typeof(metrics) == 'undefined') return false ;
			
			$.each(metrics,function(index,metric){
				
				var isExist = $.inArray(metric,searchArray);
				
				if(isExist  == -1) {
					searchArray[number] = metric ;
					number ++ ;
					
					$("#likeQuery").append("<option value='"+metric+"'>"+metric+"</option>"); 
				}
			});
		}
	});			
}
	
function loadMonitor(url){
	$.ajax({
        url: url,
		type : "get",
		dataType:'jsonp',
		jsonp:"jsonpCallback",
        success: function(result) { 
			
			if(result == null || typeof(result) == 'undefined') return false ;
		
        	var datas = new Array();		
        	
			$.each(result,function(index,itemData){
				var data = [];
								   
				data[0] = itemData.dateStr;
				data[1] = itemData.successRate;
				data[2] = itemData.unsuccessRate;	
				data[3] = itemData.failRate;
				data[4] = itemData.killRate;
				data[5] = itemData.errorRate;
				data[6] = itemData.totalCount;
				data[7] = itemData.successCount;
				data[8] = itemData.unsuccessCount;
				data[9] = itemData.failCount;
				data[10] = itemData.errorCount;
				data[11] = itemData.killCount;
				
				datas[index] = data ;
			});
			
			$('#job_monitor_details').dataTable().fnClearTable(false);
			$('#job_monitor_details').dataTable().fnDestroy();
			
			jobsCountDataTable =  $('#job_monitor_details').dataTable({
				bJQueryUI:true, 
				sPaginationType: 'full_numbers', 
				aaData:datas,
				bDeferRender: true,
				bProcessing: true, 
				bPaginate:false,
				bInfo:false ,
				sDom:'<ilp>rt',
				aaSorting: [[0, 'desc']], 
				oLanguage:oLanguageStr,
				aoColumnDefs:[{'sType':'numeric',
					 'bSearchable': false, 
					'aTargets': [ 1,2,3,4,5,6] 
				}]
			}).fnSetFilteringDelay(188);	
		
			var asInitVals = new Array();
			$('#jobCount input').keyup(function () {
				jobsCountDataTable.fnFilter(this.value, $('#jobCount input').index(this));
			});
			$('#jobCount input').each(function (i) {
				asInitVals[i] = this.value;
			});
			$('#jobCount input').focus(function () {
				if (this.className == 'search_init') {
					this.className = '';
					this.value = '';
				}
			});
			$('#jobCount input').blur(function (i) {
				if (this.value == '') {
					this.className = 'search_init';
					this.value = asInitVals[$('#jobCount input').index(this)];
				}
			});	
			
			$("#lastC").show();
			if(datas == null || datas.length == 0 )$("#lastC").hide();
			$('#job_monitor_details').show();
		}
	});	
}		
		
function userJobMonitor(url ){
	$.ajax({
        url: url,
		type : "get",
		dataType:'jsonp',
		jsonp:"jsonpCallback",
        success: function(result) { 
			if(result ==  null || typeof(result) == 'undefined') return false ; 		
			
       		var datas = new Array();
			$.each(result,function(index,itemData){
				var data = [];
								   
				data[0] = itemData.dateStr;
				data[1] = itemData.user;
				data[2] = itemData.successRate;
				data[3] = itemData.unsuccessRate;	
				data[4] = itemData.failRate;
				data[5] = itemData.killRate;
				data[6] = itemData.errorRate;
				data[7] = itemData.totalCount;
				data[8] = itemData.successCount;
				data[9] = itemData.unsuccessCount;
				data[10] = itemData.failCount;
				data[11] = itemData.errorCount;
				data[12] = itemData.killCount;
				data[13] = (itemData.executionTime/(1000*60)).toFixed(2);//job executeion ms-> min
				data[14] = itemData.mapCount;//map
				data[15] = itemData.reduceCount;//reduce
					
				datas[index] = data ;			
			} );
			 			
			$('#user_monitor_details').dataTable().fnClearTable(false);
			$('#user_monitor_details').dataTable().fnDestroy();
			//$("#resultList").empty();

			uJobsCountDataTable =  $('#user_monitor_details').dataTable({
				bJQueryUI:true, 
				sPaginationType: 'full_numbers', 
				bDeferRender: true,
				bProcessing: true,
				bPaginate:false, 
				bInfo:false ,
				sDom:'<ilp>rt',
				aaData:datas,
				aaSorting: [[0, 'desc']], 
				oLanguage:oLanguageStr,
				aoColumnDefs:[
					{'sType':'numeric',
					 'bSearchable': false, 
					 'aTargets': [2,3,4,5,6,7,8,9,10,11] 
					}
				]
			}).fnSetFilteringDelay(188);
		
		
			var asInitVals = new Array();
			$('#ujobCount input').keyup(function () {
				uJobsCountDataTable.fnFilter(this.value, $('#ujobCount input').index(this));
			});
			$('#ujobCount input').each(function (i) {
				asInitVals[i] = this.value;
			});
			$('#ujobCount input').focus(function () {
				if (this.className == 'search_init') {
					this.className = '';
					this.value = '';
				}
			});
			$('#ujobCount input').blur(function (i) {
				if (this.value == '') {
					this.className = 'search_init';
					this.value = asInitVals[$('#ujobCount input').index(this)];
				}
			});					
			 
			$("#lastU").show();
			if(datas == null || datas.length == 0 ) $("#lastU").hide();		
			$('#user_monitor_details').show();
		}
	});
}

function createNodePanel(emptyId , parentId, isShowCMC){	
	var parentDiv = document.getElementById("host_metic_graphs"+parentId);
	var body3 = document.getElementById("host-panel-body");
	var body1 = document.getElementById("cmc-panel-body");	
	
	$("#cmc-panel-body").html();
	$("#host-panel-body").html();	
		
	if(isShowCMC){
		if(body1 == null || "undefined" == typeof(body1)){
			body1 = document.createElement('div');
			body1.setAttribute('class','panel-body');	
			body1.setAttribute('id','cmc-panel-body');	
		}
		
		var CMC = document.createElement('div');
		CMC.setAttribute('id','cMetricChart');
		
		body1.appendChild(CMC);
		parentDiv.appendChild(body1);
	}	
	if(body3 == null || "undefined" == typeof(body3)) {
		body3 = document.createElement('div');
		body3.setAttribute('class','panel-body');
		body3.setAttribute('id','host-panel-body');	
	}
	
	var hostCharts = document.createElement('div');
	hostCharts.setAttribute('id','hostCharts');
	
	body3.appendChild(hostCharts);			
	parentDiv.appendChild(body3);
			
	$("#host_metic_graphs"+emptyId).hide();
	$("#host_metic_graphs"+parentId).show();			
}

function isFold(pid,pBid,rsHeight){
	var panel = document.getElementById(pid);
	var pBody = document.getElementById(pBid);
	var icon = document.getElementById(pid+"_icon");
		
	if(pBody.style.display != 'none'){
		if(pid.indexOf("nodes")== -1 )
			panel.style.height = "45px";
		else panel.style.height = "110px";
		pBody.style.display = "none";
		icon.setAttribute('class','glyphicon glyphicon-chevron-down');
	}else{
		panel.style.height = rsHeight;	
		pBody.style.display = "block";
		icon.setAttribute('class','glyphicon glyphicon-chevron-up');
	}
}