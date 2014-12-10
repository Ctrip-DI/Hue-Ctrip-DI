	var today = new Date();
	var year = today.getFullYear(),
		month = today.getMonth()+1,
		day = today.getDate();
		
	currentPoint = 0 , tempPoint = 0 , days = 14;
	
	function getErrorInfo(days , KPI , option){
		if(option == 1)
			return "The users didn't run any "+KPI+" in last "+ days +" days, and you can click for longer time.";
		else if(option ==2 )
			return "Until now users unused any "+KPI+" , please check it!";
	}
		
	function appendData(path,series,mname , id){	
		var chart = $("#"+id).highcharts();	
		if(chart == null || "undefined" == typeof(chart)) return false ;		
		
		var series1 = chart.series[0];	
		if(series1 == null || "undefined" == typeof(series1)) return false ;
		
		var xDatas =  series1.xData ;
		if(xDatas == null || "undefined" == typeof(xDatas)) return false ;
		
		currentPoint =  xDatas[xDatas.length -1];		
		if(currentPoint == 0 ) currentPoint =  new Date().getTime() + 60*1000;
		
		var begin = new Date(currentPoint - 1000*60*1);
		seyear = begin.getFullYear(),
		semonth = begin.getMonth()+1,
		seday = begin.getDate(),
		sehour = begin.getHours(),
		semi = begin.getMinutes(); 
		
		var end = new Date(currentPoint + 1000*60*2);
		year = end.getFullYear(),
		month = end.getMonth()+1,
		day = end.getDate(),
		hour = end.getHours(),
		mi = end.getMinutes();
		
		var st = "cs=" + semonth +"/"+ seday +"/"+seyear+"+"+sehour+":"+semi;
			se = "ce=" +  month +"/"+ day +"/"+year+"+"+hour+":"+mi ;
			
		var param =  '&'+st+'&'+se;			
		var paths = path.split("&") , newPath = [];		
		
		if( path.indexOf("&cs=")   != -1  && path.indexOf("&ce=") != -1 ){			
			$.each(paths , function(index , ph){
				if(ph.indexOf("cs=")!= -1)	newPath[index] = st;	
				else if(ph.indexOf("ce=")!= -1)	newPath[index] = se;	
				else if(ph.indexOf("st=")== -1)  newPath[index] =ph;
			});
			
			path = newPath.join("&");
		}else if(path.indexOf("&st=")!=-1){
			$.each(paths , function(index , ph){
				if(ph.indexOf("st=")== -1)  newPath[index] =ph;
			});
			
			path = newPath.join("&") + param;
			
		}else path += param ;	
						
		getAppendData(path, mname , chart);	
    }
	
	function getAppendData(path , mname , chart){
		$.ajax({
            url: path,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
				
				if(result == null || "undefined" == typeof(result)) return false ;
				
				series1 = null ;
		
				$.each(result, function (index,itemData) {
					if(itemData == null || itemData == 'failed') return false;
					
					var metric_name = itemData.metric_name;
					var datapoints = itemData.datapoints;
					if(datapoints == null || typeof(datapoints) == 'undefined') return ;
					
					if (metric_name.trim() == "" || metric_name == null ) metric_name = mname ; 
					while(metric_name.indexOf("\\g") != -1){
						metric_name = metric_name.replace("\\g","");
					}	
					
					series1 = chart.get(metric_name);
					if(series1 == null || typeof(series1) == 'undefined' ) return ;
					
					$.each(datapoints,function(i,dt){
					    var data = [];
					 	var ds = dt[0];
						var times = dt[1]*1000;
							
						if(ds=='NaN') return ;
						if(times<currentPoint || times == currentPoint ) return ;
						
						data[0]=times;
						data[1]=ds;						
						
						series1.addPoint(data,true,true);
					});
				});	
            }
		}); 
	}
		
	function successRio(url,param){
		var maxValue = 100.1,minValue = 90 ;
		
		var flag = false ;
		$.ajax({
            url: url + param,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
				var rateList = new Array();
				
				var chart1 ={};
				var title = '' , divHeight = '420px' , isShowDiv = false ;
				
				if(result == null|| typeof(result) == 'undefined') {
					title = getErrorInfo(14 , 'job',1 );
					
					chart1 ={
						chart:{type:'spline'},
						title:{text:title,
							style: {"color":'red'}},
						scrollbar:{enabled:false},
						rangeSelector:{enabled:false},
						navigator: {enabled: false},
						exporting:{enabled:false},
						credits: {enabled: false}, 
						legend:{enabled:false}
					};
					flag = true ;
				}else{	
					var minList = [] ;
					$.each(result,function(index,itemData){
						var rate = [] ;
						
						rate[0] = itemData.dateTime;
						rate[1] = itemData.successRate;
						
						minList[index] = itemData.successRate ;
						rateList[index] = rate ;
						
					});
					
					minValue = minList[0];
					
					for(var i = 0 ;i<minList.length-1;i++){
						a = minList[i];
						
						if(a<minValue) minValue = a ;
					}
									
					if( rateList == null || rateList.length == 0 ){	
						if(param != '' && param != null ){
							days = param.substr(param.indexOf("=")+1);
							days = (days == '0') ? 14 : days ;
						};	
						
						title = getErrorInfo(days , 'job',1 );						
						divHeight = '45px' , isShowDiv = true ; 
					}				
					
					isShowPanel('job_success_rate','successRateError',divHeight,isShowDiv,title);
					
					chart1= {
						chart: {
							spacingTop:1,
							marginTop:1,
							zoomType: 'x'
						},						
						title:{
							text:''
						},
						scrollbar:{
							enabled:false
						},
						rangeSelector: {enabled:false},
						navigator: {enabled: false},
						legend:{enabled:false},
						exporting:{enabled:false},
						credits: {enabled: false}, 
						plotOption:{
							area:{
								stacking:'normal',
								lineColor:'gray',
								lineWidth:1,
								marker:{
									lineWidth:1,
									lineColor:'#CCFFCC'
								}
							}
						},				
						yAxis: {
							type: 'value',  
							title:{
								text:'jobSuccessRatio',
								style:{color:'black'}
							},
							labels:{
								format:'{value} %',
								style:{color:'black'}						
							},
							gridLineWidth:0,   
							min:minValue,
							max:maxValue,
							opposite: false,							
							plotLines: [{
								value: 100,
								color: 'red',
								width:1,
								label: {
									text: 'SuccessRio: 100%',
									align: 'center',
									style: {
										color: 'gray'
									}
								}
							}] 
						}, 
						series:[{
							type:'spline',
							color:'#4572A7',
							id:'jobSuccessRatio',
							name:'jobSuccessRatio',
							data:rateList,	 			
							pointStart: Date.UTC(year-1, month-1, day-1), 
							pointInterval: 24*3600 * 1000,
							tooltip:{valueSuffix:'%'}
						}]
					};
				}
				
				if(!flag)
					$("#aioinfo_monitor1").highcharts('StockChart',chart1);
				else $("#aioinfo_monitor1").highcharts(chart1);
			}
		});			
	}
	
	function jobCount(url,param){
		var flag = false ;
		$.ajax({
            url: url + param,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
				var totalJobList = new Array() ;				
				var chart2 ={};				
				var title = '' , divHeight = '420px' ,isShowDiv = false  ;
				
				if(result == null || typeof(result) == 'undefined' ){
					title = getErrorInfo(14 , 'job' ,1);
					
					chart2 ={
						chart:{type:'area'},
						title:{text:title,
							style: {"color":'red'}},
						scrollbar:{enabled:false},
						rangeSelector:{enabled:false},
						navigator: {enabled: false},
						exporting:{enabled:false},
						credits: {enabled: false}, 
						legend:{enabled:false}
					};
					flag = true ;	
				}else{				
					
					$.each(result,function(index,itemData){
						var count = [] ;
						
						count[0] = itemData.dateTime;
						count[1] = itemData.totalCount;
						
						totalJobList[index] = count ;					
					});
					
					if( totalJobList == null || totalJobList.length == 0 ){	
					
						if(param != '' && param != null ){
							days = param.substr(param.indexOf("=")+1);
							days = (days == '0') ? 14 : days;
						};
						
						title = getErrorInfo(days , 'job' ,1);						
						divHeight ='45px',isShowDiv = true ;
					}
										
					isShowPanel('job_count','jobCountError',divHeight,isShowDiv,title);
					
					chart2 ={
						chart: {
							spacingTop:1,
							marginTop:1,
							zoomType: 'x'
						},												
						title:{
							text:''
						},
						scrollbar:{
							enabled:false
						},
						rangeSelector: {enabled:false},
						navigator: {enabled: false},
						legend:{enabled:false},
						exporting:{enabled:false},
						credits: {enabled: false}, 							
						yAxis: [{
							type: 'value',     
							opposite: false,
							title:{
								text:'totalJob',
								style:{color:'#4572A7'}
							},
							labels:{
								format:'{value}',
								style:{color:'#4572A7'}
							}				
						}], 
						series:[{
							type:'area',
							color:'#4572A7',
							id:'totalJob',
							name:'totalJob',
							data:totalJobList,	 			
							pointStart: Date.UTC(year-1, month-1, day-1), 
							pointInterval: 24*3600 * 1000			
						}]
					};	
				}
				if(!flag)
					$("#aioinfo_monitor2").highcharts('StockChart',chart2);
				else{
					$("#aioinfo_monitor2").highcharts(chart2);
				}
			}
		});
	}
	
	function historyCount(url,param){
				
		$.ajax({
            url: url+param,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
				
				if(result == null || typeof(result) == 'undefined') return ;
				
				var succList = [] , userList = [] , failList = [] , userWightList = new Array() ,sumJob = 0;
				
				$.each(result,function(index,itemData){
					var itemData = result[index];
					sumJob += itemData.totalCount ;
				});
				
				$.each(result,function(i,itemData){
					var user = itemData.user;
					var total = itemData.totalCount;
					var success = itemData.successCount;
					var fail = itemData.failCount;
					
					succList[i] = success ;
					failList[i] = fail ;	
					userList[i] = user ;
					
					var userWight = [];
					
					userWight[0] = user ;
					userWight[1] = (total/sumJob) * 100;
					
					userWightList[i]=userWight;	
				});
				
				var title = '' ,divHeight = '700px',isShowDiv = false  ;
				
				if( (userList == null || userList.length == 0 ) 
					 || (succList == null || succList.length == 0 )  
					 || (failList == null || failList.length == 0 ) ){	
					
					if(param == '' || param == null )	days = 'last';
					else{
						days = param.substr(param.indexOf("=")+1);
						days  = (days == '0' || days == '1') ? 'last' : days ;				
					}
					
					title = getErrorInfo(days , 'job' ,1);
					divHeight = '45px', isShowDiv = true ;
				}
				
				isShowPanel('user_job_history','jobHistoryError',divHeight,isShowDiv,title);
				
				var chart1= {
					title:{text:''},
					scrollbar:{enabled:false},
					navigator: {enabled: false},
					rangeSelector: {enabled:false},				
					exporting:{enabled:false},
					credits: {enabled: false}, 			
					yAxis: {
						min:0 ,  
						title:{text:''},
						opposite: false
					}, 
					xAxis:{
						categories:userList,
						title:{text:'Users'},
						labels:{step:1}
					},
					plotOptions:{
						series:{stacking:'normal'}
					},
					series:[{
						type:'bar',
						id:'Fail',
						color:'red',
						name:'Fail',
						data:failList
					},{
						type:'bar',
						id:'Success',
						name:'Success',
						color:'green',
						data:succList
					}
					,{
						type:'pie',
						center:["80%","50%"],
						size:280,
						showInLegend:false,
						dataLabels:{enabled:true},
						name:'User Job Weight',						
						tooltip: {
							pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
						},
						data:userWightList
					}]
				};	 
				
				$("#aioinfo_monitor3").highcharts(chart1);	
			}
		});
	}

	function divAndFile(url){
		$.ajax({
            url: url,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
			
				if(result == null || typeof(result) == 'undefined') return ;
				
				var dirList = [] , userList = [] , fileList = [] , spaceList = new Array() ,sumJob = 0;
				
				$.each(result,function(index,itemData){
					sumJob += itemData.spaceConsumed ;
				});				
				
				$.each(result,function(i,itemData){
					var user = itemData.user;
					var space = itemData.spaceConsumed;
					var dir = itemData.directoryCount;
					var file = itemData.fileCount;
					
					dirList[i] = dir ;
					fileList[i] = file ;	
					userList[i] = user ;
					
					var userWight = [];
					
					userWight[0] = user ;
					userWight[1] = (space/sumJob) * 100;
					
					spaceList[i]=userWight;
				});
				
				var title = '', divHeight = '720px',isShowDiv = false  ;
				
				if( (userList == null || userList.length == 0 ) 
					 || (dirList == null || dirList.length == 0 )  
					 || (fileList == null || fileList.length == 0 ) ){
					
					title = getErrorInfo(days , 'files/dirctorys ' , 2);	
					divHeight = '45px',isShowDiv = true ; 
				}
				
				isShowPanel('user_fd_count','fileDirError',divHeight,isShowDiv,title);
				
				var chart1= {
					chart: {
						type:'bar'
					},
					title:{
						text:'' 
					},
					scrollbar:{enabled:false},
					navigator: {enabled: false},
					rangeSelector: {enabled:false},				
					exporting:{enabled:false},
					credits: {enabled: false}, 			
					yAxis: {
						min:0 ,  
						opposite: false,
						title:{text:''},
						labels:{step:1}
					}, 
					xAxis:{
						categories:userList,
						title:{text: 'Users'},
						labels:{step:1}
					},
					plotOptions:{
						series:{stacking:'normal'}
					},
					series:[{
						id:'dir',
						color:'#4EFEB3',
						name:'directoryCount',
						data:dirList
					},{
						id:'file',
						name:'FileCount',
						color:'#4572A7',
						data:fileList
					}]					
				};
				
				var chart2= {
					chart: {
						type:'pie'
					},
					title:{text:''},
					scrollbar:{enabled:false},
					navigator: {enabled: false},
					rangeSelector: {enabled:false},				
					exporting:{enabled:false},
					credits: {enabled: false}, 			
					yAxis: {
						min:0 ,  
						opposite: false
					}, 
					xAxis:{
						categories:userList,
						title:{text: 'Users'},
						labels:{step:1}
					},
					plotOptions:{
						series:{stacking:'normal'}
					},
					tooltip: {
						pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
					},
					series:[{
						center:["50%","55%"],
						size:500,
						showInLegend:false,
						dataLabels:{enabled:true},
						name:'User SpaceConsumed Weight',
						data:spaceList
					}]					
				};
				
				title = '', divHeight = '720px',isShowDiv = false  ;				
				if(spaceList == null || spaceList.length == 0 ){					
					title = getErrorInfo(days , 'space ' , 2);	
					divHeight = '45px',isShowDiv = true ; 					
				}
				
				isShowPanel('user_space_count','userSpaceError',divHeight,isShowDiv,title);
				
				$("#aioinfo_monitor5").highcharts(chart1);	
				$("#aioinfo_monitor6").highcharts(chart2);
			}
		});
	}	
	
	function hdfsInfo(url){
		$.ajax({
            url: url,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
				if(result == null || typeof(result) == 'undefined') return ;
				
				var fileList = [] ,  heapList = [] , nonHeapList = [] , dfsList = [] , nodeList = [] ;
				var	live_nodes_List = [] , dead_nodes_List = [] , decom_nodes_List = [] , 
					temp = 0 , data1 = 0 , data2 = 0 ;
				
				var usedFileDirCount = result.usedFileDirCount ,
					usedBlockCount = result.usedBlockCount ,
					totalBlockCount = result.totalBlockCount ,
					usedHeapMemory = result.usedHeapMemory ,
					commitedHeapMemory = result.commitedHeapMemory ,
					totalHeapMemory = result.totalHeapMemory ,
					usedNonHeapMemory = result.usedNonHeapMemory ,
					commitedNonHeapMemory = result.commitedNonHeapMemory ,
					totalNonHeapMemory = result.totalNonHeapMemory ,
					totalDfsCapacity = result.totalDfsCapacity , 
					usedDfs = result.usedDfs , 
					usedNonDfs = result.usedNonDfs , 
					liveNodes = result.liveNodes , 
					deadNodes = result.deadNodes , 
					decomNodes = result.decomNodes ;
				
				
				/**File Count*/	
				fileXDatas = ['Used File/Dir' ,'Used Block','Idle Block'] ;
				data1 = ((usedFileDirCount/totalBlockCount)*100) ,
				data2 = ((usedBlockCount/totalBlockCount)*100) ;
				temp = 100 - data1 - data2 ;
				fileList[0] = [ fileXDatas[0], data1] ;	
				fileList[1] = [fileXDatas[1], data2] ;
				fileList[2] = [fileXDatas[2] , temp] ;
				
				/**heap Count*/
				heapXDatas = ['Used' ,'Commited','Idle'] ;
				data1= ((usedHeapMemory/totalHeapMemory)*100) ,
				data2 =  ((commitedHeapMemory/totalHeapMemory)*100);
				temp = 100 - data1 - data2 ;		
				
				heapList[0] = [heapXDatas[0] , data1] ;
				heapList[1] = [heapXDatas[1] ,data2] ;
				heapList[2] = [heapXDatas[2] , temp] ;
				
				/**nonheap Count*/
				data1 = ((usedNonHeapMemory/totalNonHeapMemory)*100)  ,
				data2 = ((commitedNonHeapMemory/totalNonHeapMemory)*100) ;
				temp = 100 - data1 - data2 ;
				
				nonHeapList[0] = [heapXDatas[0], data1] ;
				nonHeapList[1] = [heapXDatas[1], data2] ;
				nonHeapList[2] = [heapXDatas[2], temp ] ;
				
				/**dfs Count*/
				dfsXDatas = ['Used Dfs' ,'Used NonDfs','Idle Dfs'] ;
				data1 = ((usedDfs/totalDfsCapacity)*100) ,				
				data2 = ((usedNonDfs/totalDfsCapacity)*100) ;
				temp = 100 - data1 - data2 ;
				
				dfsList[0] = [dfsXDatas[0] ,data1 ] ;
				dfsList[1] = [dfsXDatas[1] , data2 ] ;	
				dfsList[2] = [dfsXDatas[2] ,temp] ;	
				
				dataNodes(fileList, 'hdfs_files' , 'files' , fileXDatas,totalBlockCount+' Files ');		
				dataNodes(heapList,'hdfs_heap' , 'heap', heapXDatas ,(totalHeapMemory/(1024*1024)).toFixed(2)+'MB');				
				dataNodes(nonHeapList,'hdfs_nonheap' , 'nonheap', heapXDatas,(totalNonHeapMemory/(1024*1024)).toFixed(2)+'MB');
				dataNodes(dfsList,'hdfs_dfs' , 'dfs', dfsXDatas,(totalDfsCapacity/(1024*1024*1024*1024)).toFixed(2)+'TB');
												
				if(liveNodes == null || typeof(liveNodes) == 'undefined') data1 = 0 ;
				if(deadNodes == null || typeof(deadNodes) == 'undefined') data2 = 0 ;
				if(decomNodes == null || typeof(decomNodes) == 'undefined') temp = 0 ;
				
				/**node Count*/
				nodeXDatas = ['Live Nodes' ,'Dead Nodes','Decom Nodes'] ;
				live_nodes_List = $.parseJSON(liveNodes);
				var count = 0 ; 
				for(key in live_nodes_List ){
					count ++ ;
				}				
				data1 = count;
				
				
				dead_nodes_List = $.parseJSON(deadNodes);
				count = 0 ;
				for(key in dead_nodes_List ){
					count ++ ;
				}
				data2 = count;
				
				decom_nodes_List = $.parseJSON(decomNodes);
				count = 0 ;
				for(key in decom_nodes_List ){
					count ++ ;
				}
				temp = count;	
				
				var nodeSum = data1+data2+temp ;
				
				nodeList[0] = [ nodeXDatas[0] ,data1/nodeSum*100] ;
				nodeList[1] = [ nodeXDatas[1] ,data2/nodeSum*100] ;
				nodeList[2] = [ nodeXDatas[2] , temp/nodeSum*100] ;	
					
				dataNodes(nodeList,'hdfs_nodes' , 'nodes', nodeXDatas,nodeSum);					
				getNodesData(dead_nodes_List ,'dead');				
				getNodesData(decom_nodes_List ,'decom');				
				getNodesData(live_nodes_List ,'live');	
				getCurrentNodes(1);
			}
		});
	}	
	
	function dataNodes(yDatas,id , name ,xDatas ,total){

		var chart= {
			chart: {
				plotBackgroundColor: null,
				plotBorderWidth: null,
				plotShadow: false
			},
			title:{text:''},
			scrollbar:{enabled:false},
			navigator: {enabled: false},
			rangeSelector: {enabled:false},				
			exporting:{enabled:false},
			credits: {enabled: false}, 			
			yAxis: {
				min:0 ,  
				opposite: false
			}, 
			xAxis:{
				categories:xDatas,
				title:{text: ''},
				labels:{step:1}
			},
			plotOptions:{
				series:{stacking:'normal'},
				pie: {
					allowPointSelect: true,
					cursor: 'pointer',
					dataLabels: {
						enabled: true,
						color: '#000000',
						x:-1,
						y:-20,
						connectorColor: '#000000',
						format: '<b>{point.name}</b>: {point.percentage:.2f} %'
					}
				}
			},
			tooltip: {
    	  	  pointFormat: '{series.name}: <b>{point.percentage:.2f}%</b><br>Total:<b>'+total+'</b>'
       		},
			series:[{
				type:'pie',
				center:["50%","55%"],
				size:200,
				name:'Weight',
				showInLegend:false,
				dataLabels:{enabled:true},
				data:yDatas
			}]
		};
		
		title = '' ,divHeight = '370px' , isShowDiv = false ;		
		if(yDatas == null || yDatas.length == 0 ){				
			title = getErrorInfo(days , name , 2);	
			divHeight = '45px' , isShowDiv = true;
		}
		
		isShowPanel(id+"_status",id+"_error",divHeight,isShowDiv,title);
		
		$("#"+id).highcharts(chart);
	}

	nodesData = [] , nodesIndex = 0 , nodeDataShow = 2 , nodeDataPage = 1 ,currentNodes = [] ,totalPage = 1;
	function getNodesData(result,status  ){
		if(result ==  null || typeof(result )  == 'undefined') return false ;
		
		$.each(result,function(index,itemData){
			var data = [];
							   
			data[0] = index;
			data[1] = itemData.numBlocks;
			data[2] = (itemData.usedSpace/(1024*1024*1024)).toFixed(2);
			data[3] = itemData.lastContact;	
			data[4] = (itemData.capacity/(1024*1024*1024*1024)).toFixed(2);
			data[5] = (itemData.nonDfsUsedSpace/(1024*1024*1024)).toFixed(2);
			data[6] = itemData.adminState;
			data[7] = ((itemData.usedSpace/itemData.capacity)*100).toFixed(2);
			
			progress ='<div class="progress"><div class="progress-bar" role="progressbar" aria-valuenow="'+data[7]+'" aria-valuemin="0" aria-valuemax="100" style="width: '+data[7]+'%;"><span class="sr-only">'+data[7]+' Complete</span></div></div>';
			
			data[9] = progress;
			data[8] = status;
				
			nodesData[nodesIndex] = data ;
			nodesIndex ++ ;
		} );
	}
	
	function getCurrentNodes( option ){
		var pageShow = $("#pageLive").val();
		totalCount =  nodesData.length ;
		totalPage =  Math.round( totalCount/pageShow) ;
		
		totalPage = totalPage < 1 ? 1:totalPage ;
		
		$("#totalPage").text(totalStr +totalPage+pageStr);
		$("#totalPage").show();
		$("#pageLiveError").text('');
		$("#pageLiveError").hide();	
		
		if(option == 1 ){
			
			currentNodes = nodesData.slice(0 ,pageShow);
			nodesChart();
			$("#previousLive").hide();
			currentNodes = nodesData.slice(pageShow ,totalCount);
		}else if(option == 2 ){
			
			if(currentNodes == null || currentNodes.length == 0 || typeof(currentNodes) == 'undefined') {
				
				title = lastPageStr ;	
				$("#pageLiveError").text(title);
				$("#pageLiveError").show();
				$("#lastLive").hide();
				
				return false ;
			} 
			currentNodes = currentNodes.slice(0, pageShow);
			
			nodesChart();	
			nodeDataPage ++ ;
			currentNodes = nodesData.slice(nodeDataPage*pageShow,totalCount);
			
		}else if(option == 3 ){
			var end = (nodeDataPage - 1 ) * pageShow ,start = (nodeDataPage-2) * pageShow ;		
			currentNodes = [] ,  index = 0;
						
			for(var i = start ; i < end ;i ++){
				currentNodes[index] = nodesData[i];
				
				if(i> nodesData.length) break ;
				
				if(currentNodes[index] == null || typeof(currentNodes[index]) == 'undefined'){
					title = "已经是第一页." ;	
					$("#pageLiveError").text(title);
					$("#pageLiveError").show();
					$("#previousLive").hide();
					
					return false ;
				}				
				index ++ ;							
			}
			
			nodesChart();
			
			currentNodes = [] ,  count = 0 ;
			for(var i = 0 ; i <start;i++){
				currentNodes[count] = nodesData[i];
				count ++ ;
			}
			
			for(var i = end ; i<nodesData.length;i++){
				currentNodes[count] = nodesData[i];
				count ++ ;
			}	
			
			nodeDataPage -- ;
		}		
	}

	function goNextNode(){
		$("#pageLiveError").text("");	
		$("#pageLiveError").hide();
		$("#lastLive").show();
		$("#previousLive").show();
		
		if(nodeDataPage >totalPage ){
			$("#pageLiveError").text(lastPageStr);	
			$("#pageLiveError").show();
			$("#lastLive").hide();
			return false ;
		}
		
		getCurrentNodes(2);		
	}


	function goPreviousNode(){
		$("#lastLive").show();
		$("#previousLive").show();
		$("#pageLiveError").text("");	
		$("#pageLiveError").hide();
		
		if(nodeDataPage == 1 ){			
			$("#pageLiveError").text(firstPageStr);	
			$("#pageLiveError").show();
			$("#previousLive").hide();
			return false ;
		}		
		
		getCurrentNodes( 3 );				
	}
	
	function goFirstNode(){
		$("#lastLive").show();
		
		if(nodeDataPage == 1 ){
			
			$("#pageLiveError").text(firstPageStr);	
			$("#pageLiveError").show();
			$("#previousLive").hide();
			return false ;
		}
		
		$("#pageLiveError").text("");	
		$("#pageLiveError").hide();
		
		nodeDataPage = 1 ;
		getCurrentNodes( 1);
	}
	
	function showNodes(){
		$("#pageLiveError").text("");	
		$("#pageLiveError").hide();
		$("#previousLive").hide();
		$("#lastLive").show();
		
		nodeDataPage = 1 ;
		getCurrentNodes(1);
	}

	function nodesChart(){	
		if(currentNodes ==  null || typeof(currentNodes )  == 'undefined' || currentNodes.length == 0) {
			$("#lastLive").hide();
			foldPanel('live_nodes','110px' ,true );
			return false;	
		}
		
		$("#lastLive").show();
		foldPanel('live_nodes','600px' ,false );
			 			
		$('#live_nodes_details').dataTable().fnClearTable(false);
		$('#live_nodes_details').dataTable().fnDestroy();
			
		uJobsCountDataTable =  $('#live_nodes_details').dataTable({
			bJQueryUI:true, 
			sPaginationType: 'full_numbers', 
			bDeferRender: true,
			bProcessing: true,
			bPaginate:false, 
			bInfo:false ,
			sDom: '<filp>rt',
			aaData:currentNodes,
			aaSorting: [[0, 'desc']], 
//			asStripClasses:
			oLanguage:oLanguageStr,
			aoColumnDefs:[
				{'sType':'numeric',
				 'bSearchable': false, 
				 'aTargets': [1,2,3,4,5,6] 
				}
			]
		}).fnSetFilteringDelay(188);		
	
		var asInitVals = new Array();
		$('#liveNodes input').keyup(function () {
			uJobsCountDataTable.fnFilter(this.value, $('#liveNodes input').index(this));
		});
		$('#liveNodes input').each(function (i) {
			asInitVals[i] = this.value;
		});
		$('#liveNodes input').focus(function () {
			if (this.className == 'search_init') {
				this.className = '';
				this.value = '';
			}
		});
		$('#liveNodes input').blur(function (i) {
			if (this.value == '') {
				this.className = 'search_init';
				this.value = asInitVals[$('#liveNodes input').index(this)];
			}
		});			 
		
		$('#live_nodes_details').show();
	}
	
	function changeCharts(path,parent,mnames,prefix,months,columns, nameList,index){ 
		var percent = 100/columns - 2 ;
		var number = index+1 ,
			count = nameList.length ,
			mname = mnames[index ];
		var requestPath = path + nameList[index] ;		
		var types = 'spline';		
		var flag = false ;
		
		$.ajax({			   
            url: requestPath,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
				var dataList = new Array();		
							
				var metricsName = new Array(); 
				var colors = new Array(); 
				var chart1  = {};
				
				if(result == null || typeof(result) == 'undefined') {
					if(prefix == 'mtc_' || prefix == 'cmc_'){
						$("#metricsSearchError").text("the metric "+mname+" No data , please check it !");
						$("#metricsSearchError").show();
					}
					return false;
				}else{
					
					padding = 0  ,maxValue = null ;
					if(mname == 'cpu_report'){
						padding = 1 ;
						maxValue = 100.1 ;
					}
					
					$.each(result,function(index,itemData){
						var axisData =new Array();
						
						var metric_name = itemData.metric_name;
						var datapoints = itemData.datapoints;
						var color = itemData.color;  
						
						if(datapoints == null || typeof(datapoints) == 'undefined') return ;
					
						if (metric_name.trim() == "" || metric_name == null ) metric_name = mname ; 
						while(metric_name.indexOf("\\g") != -1) metric_name = metric_name.replace("\\g","");
									
						if (metric_name == 'Idle') return ;
						
						var indexx =0 ;					
						$.each(datapoints,function(i,dt){ 
							var data = [];
							
							var ds = dt[0];
							var times = dt[1]*1000;
							
							if(ds=="NaN") return;
							
							data[0]=times;
							data[1]=ds;					
							
							axisData[indexx]=data;
							indexx ++ ;
						});
											
						colors[index] = color ;
						metricsName[index]=metric_name;
						dataList[index] = axisData;						
					});
					
					if(colors.length ==1 )colors[0] ="#4572A7";
					if(metricsName.length ==1)types="area";					
					
					var option = {
						area:{
							stacking:'normal',
							lineWidth:1,
							marker:{
								lineWidth:1
							}
						}
					};
					
					if(mname.indexOf('cpu_report') != -1 || mname.indexOf('mem_report') != -1 ){
						types = "area";	
						option = {
							series: {
								stacking: 'normal'
							}
						};
					}					
						
					chart1 = {
						colors:colors,					 
						chart: {
							aniation:Highcharts.svg,		
							marginRight: 10,
							spacingRight: 20,
							events: {
								load: function() {  	
									chartAddPoint(requestPath,chart1.series,mname ,prefix+'charts'+number );								
								}
							},
							zoomType: 'x'
						},
						scrollbar:{enabled:false},						
						navigator: {enabled: false},
						rangeSelector: {enabled:false},	
						legend:{
							enabled:true,									 
							align: 'center',  
							layout: 'horizontal',
							verticalAlign: 'bottom', 
							itemMarginBottom:3,
							x:2,
							y:10, 
							symbolPadding:5,
							useHTML:true,
							borderWidth:1,
							labelFormatter:function(){
								return this.name;
							},
							backgroundColor:'rgb(249, 249, 249)',
							shadow: {
								color:'#000', 
								opacity:0.15,
								offsetY:2,
								offsetX:1
							}
						},
						exporting:{enabled:false},
						credits: {enabled: false}, 
						plotOptions: option,	
						yAxis: {
							type: 'value',    
							opposite: false,  
							minPadding:padding,
							max:maxValue, 
							plotLines:[{
								value: 0,
								width:2,
								color: '#4EFEB3'
							}] 
						},
						series:[]
					};							
								
					if(dataList != null && dataList.length >0 ){
						for(var i = 0 ;i<dataList.length;i++){
							var mtName = metricsName[i];
						
							if(mtName == 'Total' || mtName == 'Idle' ) types = "spline";
							
							var seriesObj = {
								type:types,
								id:mtName,
								name:mtName,
								data:dataList[i],	 			
								pointStart: Date.UTC(year-1, month-1, day-1), 
								pointInterval: 24*3600 * 1000								
							};
							chart1.series.push(seriesObj);
						}
					}			
				}	
				
				createCriticalPanel(number,percent,prefix,mname,count,parent,result);
					
				if(!flag)
					$('#'+prefix+'charts'+number).highcharts('StockChart', chart1);
				else{
					$('#'+prefix+'charts'+number).highcharts(chart1);
					
					panelDiv.style.height = "45px";
					panelBodyDiv.style.display = "none";
					aSpan.setAttribute('class','glyphicon glyphicon-chevron-down');
				}
				
				$("#metricsSearchError").text("");
				$("#metricsSearchError").hide();
				
				index ++ ;
				if(index<nameList.length)changeCharts(path,parent,mnames,prefix,months,columns, nameList,index);
  			},
			timeout:8000
		});
	}
	
	function createCriticalPanel(number,percent,prefix,mname,count,parent,result){
		var chartDiv = document.createElement('div');
		chartDiv.setAttribute('id',prefix+'charts'+number);
		chartDiv.setAttribute('class','widget-box clog-highchart-widget');				
		chartDiv.style.postion='relative';
															
		var panelDiv = document.createElement('div');
		panelDiv.setAttribute('id',prefix+'critical_metric_'+number);
		panelDiv.setAttribute('class','panel panel-info');		
		panelDiv.setAttribute('style','float:left;width:'+percent+'%;margin-left:10px;margin-right:10px;margin-top:10px;margin-bottom:10px;');
						
		var panelHeadDiv = document.createElement('div');
		panelHeadDiv.setAttribute('class','panel-heading');		
		
		var aFold = document.createElement('a');
		aFold.setAttribute('class','pages');
		aFold.title= foldStr ;
		aFold.style.float="right";
		aFold.href="javascript:void(0)";				
		
		var aSpan = document.createElement('span');
		aSpan.setAttribute('id',prefix+'critical_metric_'+number+'_icon');
		aSpan.setAttribute('class','glyphicon glyphicon-chevron-up');
		
		aFold.appendChild(aSpan);
				
		if(result == null){		
			panelDiv.setAttribute('class','panel panel-danger');
			panelHeadDiv.appendChild(document.createTextNode("No data for the metric of "+mname+" , please check!"));
		}else panelHeadDiv.appendChild(document.createTextNode(mname));						
		
		panelHeadDiv.appendChild(aFold);
	
		var panelBodyDiv = document.createElement('div');
		panelBodyDiv.setAttribute('id',prefix+'critical_metric_'+number+'_body');
		panelBodyDiv.setAttribute('class','panel panel-body');
		
		if(count == 1 ){
			chartDiv.style.height='450px';					
			panelBodyDiv.style.height = "470px";	
		
			aFold.setAttribute('onclick',
				"isFold('"+prefix+"critical_metric_"+number+"','"+prefix
				+"critical_metric_"+number+"_body','520px')");
				
			panelBodyDiv.appendChild(chartDiv);
			panelDiv.appendChild(panelHeadDiv);
			panelDiv.appendChild(panelBodyDiv);
		}else{				
			chartDiv.style.height='350px';
			panelBodyDiv.style.height = "370px";	
			
			aFold.setAttribute('onclick',
				"isFold('"+prefix+"critical_metric_"+number+"','"+prefix
				+"critical_metric_"+number+"_body','433px')");
				
			panelBodyDiv.appendChild(chartDiv);
			panelDiv.appendChild(panelHeadDiv);
			panelDiv.appendChild(panelBodyDiv);									
		}
				
		if(parent != null && typeof(parent) != 'undefined' ) parent.appendChild(panelDiv);
	}
	
	function serverChart(path,types,parent, metric,isPanel , columns, lines  , hosts , index ){ 
		var flag = false ;
		var percent = 100/columns - 2 ;
		var server = hosts[index ];
		var number = index+1 ;
		var hname = server.substr(0,server.indexOf(".")) + ' '+metric;
		var requestPath = path +'&h=' + server ;
		
		$.ajax({
            url: requestPath,
			type : "get",
			dataType:'jsonp',
			jsonp:"jsonpCallback",
            success: function(result) { 
				if(result == null || typeof(result) == 'undefined'){
					index ++ ;
					if(index<hosts.length && number < columns * lines ) 
						serverChart(path,types,parent, metric,isPanel , columns ,lines  , hosts , index );
					return  ;
				}
				
				var dataList = new Array() , colors = new Array();		
							
				var metricsName = new Array(); 
				var chart1  = {};

				$.each(result,function(index,itemData){ 
					var axisData =new Array();
					
					var metric_name = itemData.metric_name;
					var datapoints = itemData.datapoints;  
					var color = itemData.color; 
					if(datapoints == null || typeof(datapoints) == 'undefined') return ;
					
					if (metric_name.trim() == "" || metric_name == null ) metric_name = hname ; 
					while(metric_name.indexOf("\\g") != -1){
						metric_name = metric_name.replace("\\g","");
					}			
				
					if (metric_name == 'Idle') return ;
	
					var indexx =0 ;
					$.each(datapoints,function(i,dt){ 
						var data = [];
						
						var ds = dt[0];
						var times = dt[1]*1000;
						
						if(ds=="NaN") return;
						
						data[0]=times;
						data[1]=ds;					
						
						axisData[indexx]=data;
						indexx ++ ;
					});
					metricsName[index]=metric_name;
					dataList[index] = axisData;
					colors[index] = color;
				});
				
				if(dataList == null || typeof(dataList) == 'undefined') return ;					
				if(metricsName.length >1)types="spline";
				
				if(colors.length ==1 )colors[0] ="#81C0C0";				
				
				var option = {
					area:{
						stacking:'normal',
						lineWidth:1,
						marker:{
							lineWidth:1
						}
					}
				};
					
				if(metric.indexOf('cpu_report') != -1 || metric.indexOf('mem_report') != -1 ){
					types = "area";	
					option = {
						series: {
							stacking: 'normal'
						}
					};
				}	
										
				chart1 = {
					colors:colors,	
					chart: {
						zoomType: 'x'
					},
					scrollbar:{enabled:false },
					navigator: {enabled: false},
					rangeSelector: {enabled:false},
					legend:{
						enabled:true,									 
						align: 'center',  
						layout: 'horizontal',
						verticalAlign: 'bottom', 
						itemMarginBottom:3,
						x:2,
						y:10, 
						symbolPadding:5,
						useHTML:true,
						itemwidth:70,
						borderWidth:1,
						labelFormatter:function(){
							return this.name;
						},
						backgroundColor:'rgb(249, 249, 249)',
						shadow: {
							color:'#000',
							width:3,
							opacity:0.15,
							offsetY:2,
							offsetX:1
						}
					},
					exporting:{enabled:false},
					credits: {enabled: false}, 						
					plotOptions: option,	
					tooltip:{
						pointFormat:'<span style="color:{series.color}">\u25CF</span> {series.name}: <b>{point.y}</b><br/>.'
					},
					yAxis: {
						type: 'value',   
						opposite: false,
						plotLines:[{
							value: 0,
							width: 2,
							color: 'silver'
						}] 
					}, 
					series:[]
				};							
							
				if(dataList != null && dataList.length >0 )	{	
					for(var i = 0 ;i<dataList.length;i++){
					var mtName = metricsName[i];
					
						if(mtName == 'Total' || mtName == 'Idle' ){
							types = "spline";
						}
						
						var seriesObj = {
							type:types,		
							id:mtName,
							name:mtName,
							data:dataList[i],		 	
							pointStart: Date.UTC(year-1, month-1, day-1), 
							pointInterval: 24*3600 * 1000
						};
						chart1.series.push(seriesObj);
					}
				}
			
				createServerPanel(number,percent,hname , parent,result);
				
				if(!flag)
				$('#hostCharts'+number).highcharts('StockChart', chart1);	
				else{
					$('#hostCharts'+number).highcharts(chart1);
					
					panelDiv.style.height = "45px";
					panelBodyDiv.style.display = "none";
					aSpan.setAttribute('class','glyphicon glyphicon-chevron-down');
				}
				
				index ++ ;
				if(index<hosts.length && number < columns * lines ) serverChart(path,types,parent, metric,isPanel , columns ,lines  , hosts , index );
  			}
		});
	}
	
	function createServerPanel(number,percent,hname , parent,result){
		var chartDiv = document.createElement('div');
		chartDiv.setAttribute('id','hostCharts'+ number);
		chartDiv.setAttribute('class','widget-box server-highchart-widget');			
		chartDiv.style.postion='relative';		
		
		var panelDiv = document.createElement('div');
		panelDiv.setAttribute('id','server_metric_'+number);
		panelDiv.setAttribute('class','panel panel-info');
		panelDiv.setAttribute('style',
			'float:left;width:'+percent+'%;margin-left:10px;margin-right:10px;margin-top:10px;margin-bottom:10px;');
						
		var panelHeadDiv = document.createElement('div');
		panelHeadDiv.setAttribute('class','panel-heading');
						
		var aFold = document.createElement('a');
		aFold.setAttribute('class','pages');
		aFold.title= foldStr ;
		aFold.style.float="right";
		aFold.href="javascript:void(0)";
		aFold.setAttribute('onclick',
			"isFold('server_metric_"+number+"','server_metric_"+number+"_body','353px')");
		
		var aSpan = document.createElement('span');
		aSpan.setAttribute('id','server_metric_'+number+'_icon');
		aSpan.setAttribute('class','glyphicon glyphicon-chevron-up');				
		aFold.appendChild(aSpan);			
		
		if(result == null){					
			panelDiv.setAttribute('class','panel panel-danger');
			panelHeadDiv.appendChild(document.createTextNode("No data for the metric of "+hname+", please check!"));
		}else panelHeadDiv.appendChild(document.createTextNode(hname));		
						
		panelHeadDiv.appendChild(aFold);
		
		var panelBodyDiv = document.createElement('div');
		panelBodyDiv.setAttribute('id','server_metric_'+number+'_body');
		panelBodyDiv.setAttribute('class','panel panel-body');				
		panelBodyDiv.style.height = "290px";		
			
		panelBodyDiv.appendChild(chartDiv);
		panelDiv.appendChild(panelHeadDiv);
		panelDiv.appendChild(panelBodyDiv);		
		parent.appendChild(panelDiv);
	}
					
	function foldPanel(id,height ,flag ){ 
		var panel = document.getElementById(id);
		var pBody = document.getElementById(id+'_body');
		var icon = document.getElementById(id+'_icon');		
		
		panel.style.height = height;
		
		if(flag){
			pBody.style.display = "none";
			icon.setAttribute('class','glyphicon glyphicon-chevron-down');	
		}else{			
			pBody.style.display = "block";
			icon.setAttribute('class','glyphicon glyphicon-chevron-up');	
		}
	}
	

	var cancelID = [] , cancelCount = 0 ;
	
	function chartAddPoint(path,series,mname,id ,startPonit){
		
		cancelID[cancelCount] = setInterval(
			function(){
				appendData(path,series,mname,id , startPonit);
			},60000);
		cancelCount  ++ ;
	}

	function cancelInterval(){
		for(var index = 0 ; index<cancelCount ;index ++ ){
			window.clearInterval(cancelID[index]);
		}
		
		cancelID = [] , cancelCount = 0 ;
	}
	
	function isShowPanel(id,errorId,height,flag,title){
		
		foldPanel(id,height,flag);		
		$("#"+errorId).text(title);
		
		if(!flag) $("#"+errorId).hide();
		else $("#"+errorId).show();
		
	}