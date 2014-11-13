var initLines = 0 , initPage = 1  , currentData = [] , initData = [] , totalPage = 0 ,
	uname = $("#uname").val() , hideID = '' ;
	
var path = $("#spark_sql_url").val() +"/spark/";

$("#dangerMessage").hide(); 
$("#dangerTable").hide();
$("#previousPage").hide();
$("#dbError").hide();
$("#pageError").hide();
$("#firstPage").hide();
$("#nextPage").hide();
$("#query_condition").show();
$("#pageSelect").hide();
$("#sparksql_history").hide();
$("#sparksql-result").show();	
$("#loadImage").hide();

$(window).load(function(){		
	$("#execute_result").css('margin-top','10px');
	$("#query_edit").attr('class','active');
	$("#history").attr('class','');
	
	loadDB();
	loadDateJS();
});

function loadDateJS(){
	var dateJS = document.createElement('script');
	dateJS.setAttribute('type','text/javascript');
	dateJS.setAttribute('charset','utf-8');
	dateJS.setAttribute('src','/monitor/static/js/date.js');
	
	document.body.appendChild(dateJS);
	
	loadingJS = document.createElement('script');
	loadingJS.setAttribute('type','text/javascript');
	loadingJS.setAttribute('charset','utf-8');
	loadingJS.setAttribute('src','/sparksql/static/js/loading.js');
	
	document.body.appendChild(loadingJS);	
}

function loadDB(){
	var spark_url = path + '/execute' , sql = 'show databases;';
	$.ajax({
    	url:spark_url,
	    type:"get",
		data:{
			sql:sql
		},
    	dataType:'jsonp',
	    jsonp:"jsonpCallback",
   	 	success: function(result) {
			
			if(!isError(result , 'dbError')) return false ;
			
			$.each(result,function(index , itemData){
				$("#id_query_database").append("<option value='"+itemData.result+"'>"+itemData.result+"</option>"); 			
			});
		}
	});
}

$("#sparksql-execute").click(function(){
	var sql = $("#sql_context").val();

	var db = $("#id_query_database").val();
	
  	if(sql == null || sql.trim() == ''){
		$("#dangerMessage").text('请输入Spark Sql...');
		$("#dangerMessage").show();	
		return false ;
	}else {		
		$("#dangerMessage").text('');
		$("#dangerMessage").hide();
	}
    
 	var spark_url= path + '/execute';
	datas = {sql:sql,database:db,user:uname}; 
	queryData(spark_url , datas,'sparksql-result' );
  	
});

//option:1-next;2-previous;
function renderTable(result ,option ){
	getHideID() ;
	
	$("#"+hideID).empty();	
	$("#pageError").text('');
	$("#pageError").hide();
	
	initLines = $("#pageShow").val();	
	
	if(option == 1 ){		
		currentData = result.slice(0,initPage * initLines );
		createTable(currentData);
		
		currentData = result.slice(initPage * initLines , result.length);
	}else if(option == 2 ){			
		var end = (initPage - 1 ) * initLines ,start = (initPage-2) * initLines ;		
		
		end = end < initData.length ? end : initData.length; 
		
		var index = 0 ;
		for(var i = start ;i<end;i++){
			result[index] = initData[i] ;
			index ++ ;
		}
		createTable(result);
		
		var count = 0 ;		
		for(var i = 0 ; i <start;i++){
			currentData[count] = initData[i];
			count ++ ;
		}
		
		for(var i = end ; i<initData.length;i++){
			currentData[count] = initData[i];
			count ++ ;
		}
	}
}

function createTable(result){
	var columns = [];
    var columnCount = 0;
    var head = "<tr>";
	
	flag = hideID == 'sparksql-result' ? true :false ;
	var first = result[0];
	
	if(flag ){
		for(var key in first ) {
			if(key == 'id') continue;
			
			columns[columnCount]=key;			
			columnCount++;
		
			head = head+"<th class='th_title'>"+key+"</th>"; 
		}

		head = head + "</tr><tbody class='tbody_line'>";
		$("#"+hideID).append(head);
	}else{
		columnCount = 6;
		columns = ['startTime','finishTime','executeTime','sql','user','status'];
	}
	
    $.each(result, function(i, resultJson){
		tr_css = (i%2 == 0 ) ? 'single':'double';
		
    	var valueRow = "<tr class='"+tr_css+"'>";
        for(var index = 0;index < columnCount; index++){
			var colsValue = columns[index] ;
			
			tdValue = getTdValue(colsValue, resultJson);
			
        	valueRow = valueRow + "<td>" + tdValue + "</td>";			
       	}
		
        valueRow = valueRow + "</tr>";
       	$("#"+hideID).append(valueRow);
		
		if(i>initPage * initLines) return ;
	});			
	
	$("#"+hideID).append('</tbody>');
	
}

function getTdValue(columns , resultJson){
	if(columns =='' || typeof(columns) == 'undefined' ) return false ;
	
	value = resultJson[columns]  ;
	
	if(columns == 'finishTime' || columns == 'startTime' ){
		value = getSmpFormatDateByLong(value/1000 , true );
	}else if(columns == 'executeTime' ){
		value = resultJson['finishTime'] -  resultJson['startTime'] ;
		value = value/1000;
	}
	
	return value ;
}

function queryData(spark_url,datas , id){
	$.ajax({
    	url:spark_url,
	    type:"get",
		data:datas,
    	dataType:'jsonp',
	    jsonp:"jsonpCallback",
		beforeSend:function(){
			$("#loadImage").show();
		},
   	 	success: function(result) {
			
			$("#"+id).empty();
	
			if(!isError(result , 'dangerTable')) return false ;
			
			initData = result ;
			
			initLines = $("#pageShow").val();
			
			totalPage = Math.round(result.length/initLines) ;
			renderTable(result , 1);			
		},
		complete:function(){
			$("#loadImage").hide();
		}
	});	
}

function isError(result , id){
	$("#nextPage").hide();
	$("#pageSelect").hide();
	
	if(result == null || typeof(result) == 'undefined' || result.length == 0 ){
		$("#"+id).text('查询不到相关数据！');
		$("#"+id).show();
		$("#sparksql-result").empty();
		
		return false ;
	}
		
	var status = result.status;
	if( typeof(status) != 'undefined' && status == 'error'){
		$("#"+id).text(result.message);
		$("#"+id).show();	
		$("#sparksql-result").empty();
		return false ;
	}
	
	$("#"+id).text('');
	$("#"+id).hide();
	
	if( id != 'dbError'){
		$("#firstPage").show();
		$("#nextPage").show();
		$("#pageSelect").show();
	}	
	return true ;
}


$("#historyList").click(function(){
	$("#query_condition").hide();
	$("#sparksql-result").empty();
	
	$("#sparksql_history").show();
	$("#sparksql-result").hide();
	
	initPage = 1 ;

	spark_url= path + '/getjobs';	
	datas = {user:uname};
	queryData(spark_url , datas ,'history_body');
	$("#execute_result").css('margin-top','30px');
	$("#query_edit").attr('class','');
	$("#history").attr('class','active');
  	
});

$("#nextPage").click(function(){
	$("#previousPage").show();
	$("#firstPage").show();
	
	if(initPage == totalPage) {
		$("#pageError").text('已经是最后一页');
		$("#pageError").show();
		$("#nextPage").hide();
		return false ;
	}
	
	if(!isError(currentData , 'dangerTable')){
		initPage ++ ;
		return false ;
	}
	
	renderTable(currentData ,1 );
	initPage ++ ;
});

$("#previousPage").click(function(){
	$("#nextPage").show();
	$("#firstPage").show();
	
	if(initPage == 1 ) {
		$("#pageError").text('已经是第一页');
		$("#pageError").show();
		$("#previousPage").hide();
		return false ;
	}
	
	//isError(currentData , 'dangerTable');
	$("#dangerTable").text('');
	$("#dangerTable").hide();
	
	renderTable(currentData ,2);
	initPage -- ;
	if(initPage == 1 ) $("#previousPage").hide();
});

$("#firstPage").click(function(){		
	$("#firstPage").show();
	$("#previousPage").hide();
	$("#nextPage").show();
	
	if(initPage == 1 ) {
		$("#pageError").text('已经是首页');
		$("#pageError").show();
		return false ;
	}
	
	if(!isError(initData , 'dangerTable')) return fasle ;
	
	initPage = 1;	
	renderTable(initData ,1);
});

$("#pageShow").change(function(){
	initPage = 1;
	$("#previousPage").hide();
	$("#nextPage").show();
	$("#firstPage").show();
	
	if(!isError(initData , 'dangerTable') ) return false ;
	
	renderTable(initData ,1);
});

function getHideID(){	
	hideID = $("#sparksql-result").is(":visible") ? 'sparksql-result' :'history_body' ;
}
