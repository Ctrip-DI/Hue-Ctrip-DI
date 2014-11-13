<%!
from desktop.views import commonheader, commonfooter
from sparksql.conf import SPARK_SERVICE
 %> 
 
<%namespace name="shared" file="shared_components.mako" />

${commonheader("Sparksql", "sparksql", user, "100px")|n,unicode}

## Use double hashes for a mako template comment
## Main body
<input id="spark_sql_url" type="hidden" value="${SPARK_SERVICE.SPARK_SQL_URL.get() | n,unicode }">

<div class="subnav subnav-fixed">
	<div class="container-fluid">
    	<ul class="nav nav-pills">
        	<li class="active">
				<li class="active" id="query_edit"><a href="${url('sparksql.views.index')}">查询编辑器</a>				
			</li>
	        <li class="" id="history"><a href="javascript:void(0)" id="historyList">历史记录</a></li>
		</ul>
	</div>
</div>

<div class="input-group" style="width:97%;margin-left:5px;margin-top: 30px;" id="query_condition">
  	<span class="input-group-addon">
  		数据库:
		<select id="id_query_database" class="input-medium" name="query-database">
		</select><br>
		<span style="float:center;color:red;height:30px;" id="dbError"></span>
		<br>
		<button type="button" class="btn btn-default" id="sparksql-execute" style="background-color:rgb(230, 227, 227);">Execute</button>
	</span>
  	<textarea type="text" class="form-control" id="sql_context" placeholder="请输入Spark Sql"></textarea>  
  	<div class="alert alert-danger" role="alert" id="dangerMessage"></div>  
</div>

<div id="loadImage">
	<div class="loading-mask" style="position: absolute; z-index: 999; top: 0px; right: 0px; left: 0px; bottom: 0px;"></div>
	<div class="loading" style="position: absolute; z-index: 1000; margin-top: -31px; margin-left: -92.5px;">
		<img src="/sparksql/static/art/loads.gif"><span>数据加载中...</span>
	</div>
</div>

<div class="panel panel-info" style="width:98%;margin-left:5px;" id="execute_result">
  <div class="panel-heading">执行结果</div>

	<div class="panel-body">
		<div id="pageSelect" style="float:left;margin-bottom:5px;margin-left:15px;width:100%">
			每页显示<select id="pageShow" style="width:60px;">
				<option value=20>20</option>
				<option value=40>40</option>
				<option value=60>60</option>
				<option value=80>80</option>
				<option value=100>100</option>
			</select>条记录
		</div>
  		<table class="table table-bordered" id="sparksql-result">
 		</table>
		<table class="table table-bordered" id="sparksql_history">
			<thead class="thread_head">
				<tr>
				<th>启动时间 
				<th>结束时间 
				<th>执行时间(s) 
				<th>执行SQL 
				<th>执行用户 
				<th>Status 
				</tr>
			</thread>
			<tbody class="tbody_line" id="history_body">
			</tbody>
 		</table>
		
		<div class="alert alert-danger" role="alert" id="dangerTable"></div>  
		<div id="pageNodes" style="float:right;height:30px;z-index:10;margin-right:25px;width:220px;margin-left:3px;">
			<span style="float:right;color:red;height:30px;" id="pageError"></span>
			<a class="pages" href="javascript:void(0)" id="nextPage" >下一页</a>
			<a class="pages" href="javascript:void(0)" id="previousPage" >上一页</a>
			<a class="pages" href="javascript:void(0)" id="firstPage" >首页</a>
		</div>
 	</div>
</div>

<link type="text/css" rel="stylesheet" href="/monitor/static/css/bootstrap.css">
<link type="text/css" rel="stylesheet" href="/monitor/static/css/default.css">
<link type="text/css" rel="stylesheet" href="/sparksql/static/css/sparksql.css">
<script src="/monitor/static/js/bootstrap.js" type="text/javascript" charset="utf-8"></script>
<script src="/sparksql/static/js/sparksql.js" type="text/javascript" charset="utf-8"></script>

${commonfooter(messages)|n,unicode}
