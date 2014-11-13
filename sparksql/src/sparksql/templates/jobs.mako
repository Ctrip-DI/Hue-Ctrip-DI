<%!
from desktop.views import commonheader, commonfooter
from sparksql.conf import DATA_SERVICE
%>

<%namespace name="shared" file="shared_components.mako" />

${commonheader("Sparksql", "sparksql", user, "100px")|n,unicode}

## Use double hashes for a mako template comment
## Main body
<input id="spark_sql_url" type="hidden" value="${SPARK_SERVICE.SPARK_SQL_URL.get() | n,unicode }">

<div class="subnav subnav-fixed">
	<div class="container-fluid">
    	<ul class="nav nav-pills">
		<li class=""><a href="${url('sparksql.views.index')}">查询编辑器</a>				
	        <li class="active"><a href="${url('sparksql.views.jobs')}" id="historyList">历史记录</a></li>
	</ul>
	</div>
</div>

<br/>
<br/>

<div class="panel panel-default" style="width:97%;">
  <div class="panel-heading">用户历史查询记录</div>

	<div class="panel-body">
		<div id="pageSelect" style="float:left;margin-bottom:5px;margin-left:15px;width:100%">
			每页显示<select id="pageShow" style="width: 60px;">
				<option value="10">10</option>
				<option value="20">20</option>
				<option value="40">40</option>
				<option value="80">80</option>
				<option value="100">100</option>
			</select>条记录
		</div>
  		<table class="table table-bordered" id="sparksql-result">
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

<script src="/monitor/static/js/bootstrap.js" type="text/javascript" charset="utf-8"></script>
<script src="/sparksql/static/js/sparksql.js" type="text/javascript" charset="utf-8"></script>
<link type="text/css" rel="stylesheet" href="/monitor/static/css/bootstrap.css">
<link type="text/css" rel="stylesheet" href="/monitor/static/css/default.css">
<link type="text/css" rel="stylesheet" href="/sparksql/static/css/sparksql.css">

${commonfooter(messages)|n,unicode}
