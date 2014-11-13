// JavaScript Document

	function createTables(parent,chartDiv,number,prefix){	
	
		var cetFlag = false ,tabFlag = false , tbdFlag = false , trFlag = false ,tdFlag = false ;
		var count = number%2>0 ? Math.floor(number/2 +1) : number/2;		
		var center = null, table = null ,tbody = null, tr = null , td = null ;
		
		center = document.getElementById(prefix+"center");
		if(center == null || "undefined" == typeof(center)){
			center = document.createElement("center");
			center.setAttribute('id',prefix+'center');
			cetFlag = true;
		}
		
		table = document.getElementById(prefix+"table");
		if(table == null || "undefined" == typeof(table)){
			table = document.createElement("table");
			table.setAttribute('id',prefix+'table');
			table.style.tableLayout='fixed';
			table.style.width='96%';
			table.style.border='0';
			table.setAttribute('cellspacing','4');
			tabFlag = true;
		}
		
		tbody = document.getElementById(prefix+"tbody");
		if(tbody == null || "undefined" == typeof(tbody)){
			tbody = document.createElement("tbody");
			tbody.setAttribute('id',prefix+'tbody');
			tbdFlag = true;
		}
				
		tr = document.getElementById(prefix+'tr'+count);	
		td = document.getElementById(prefix+'td'+number);
		var td2 = null ;
			
		if(tr == null || "undefined" == typeof(tr)){
			tr = document.createElement("tr");
			tr.setAttribute('id',prefix+'tr'+count);
			trFlag = true;
			
			if(number%2 == 1){			
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id',prefix+'td'+number);
					td.style.verticalAlign="top";					
					tdFlag = true;					
					
					td2 = document.createElement('td');
					td2.setAttribute('id',prefix+'td'+(((count-1)*2+2)));
					td2.style.verticalAlign="top";				
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
					
					tr.appendChild(td2);
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}
			}else if(number%2 == 0){					
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id',prefix+'td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.setAttribute('id',prefix+'td'+(((count-1)*2+1)));
					td2.style.verticalAlign="top";					
					
					tr.appendChild(td2);
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}
			}		
		} else {
			if(number%2 == 1){			
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id',prefix+'td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;					
					
					td2 = document.createElement('td');
					td2.setAttribute('id',prefix+'td'+(((count-1)*2+2)));
					td2.style.verticalAlign="top";				
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
					
					tr.appendChild(td2);
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}
			}else if(number%2 == 0){					
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id',prefix+'td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.setAttribute('id',prefix+'td'+(((count-1)*2+1)));
					td2.style.verticalAlign="top";
					
					tr.appendChild(td2);					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}
			}	
		}
		
		
		if(trFlag)tbody.appendChild(tr);
		if(tbdFlag)table.appendChild(tbody);
		if(tabFlag)center.appendChild(table);
		if(cetFlag)parent.appendChild(center);				
	}
	
	function hostTables(chartDiv,number,parent){
		
		var cetFlag = false ,tabFlag = false , tbdFlag = false , trFlag = false ,tdFlag = false ;
		var count = number%4>0 ? Math.floor(number/4 +1) : number/4;		
		var center = null, table = null ,tbody = null, tr = null , td = null ;
		
		center = document.getElementById("center1");
		if(center == null || "undefined" == typeof(center)){
			center = document.createElement("center");
			center.setAttribute('id','center1');
			cetFlag = true;
		}
		
		table = document.getElementById("graph_metric");
		if(table == null || "undefined" == typeof(table)){
			table = document.createElement("table");
			table.setAttribute('id','graph_metric');
			table.style.tableLayout='fixed';
			table.style.width='96%';
			table.style.border='0';
			table.setAttribute('cellspacing','4');			
			tabFlag = true;
		}		
		
		tbody = document.getElementById("tbody1");
		if(tbody == null || "undefined" == typeof(tbody)){
			tbody = document.createElement("tbody");
			tbody.setAttribute('id','tbody1');
			tbdFlag = true;
		}
			
		tr = document.getElementById('tr'+count);	
		td = document.getElementById('td'+number); 
		
		var td2 = null ,td3 = null , td3 = null ;
		
		if(tr == null || "undefined" == typeof(tr)){
			tr = document.createElement("tr");
			tr.setAttribute('id','tr'+count);
			trFlag = true;
			
			if(number%4 == 1){			
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.style.verticalAlign="top";
					td2.setAttribute('id','td'+((count-1)*4+2));
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+3)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+4)));	
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);					
					
					tr.appendChild(td2);
					tr.appendChild(td3);
					tr.appendChild(td4);					
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}		
			}else if(number%4 == 2){				
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.style.verticalAlign="top";
					td2.setAttribute('id','td'+(((count-1)*4+1)));
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+3)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+4)));
					//
					tr.appendChild(td2);
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);	
					
					tr.appendChild(td3);
					tr.appendChild(td4);
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}				
			}else if(number%4 == 3){				
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.style.verticalAlign="top";
					td2.setAttribute('id','td'+(((count-1)*4+1)));
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+2)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+4)));	
					
					tr.appendChild(td2);
					tr.appendChild(td3);
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);	
					
					tr.appendChild(td4);		
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}			
			}else if(number%4 == 0){	
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.style.verticalAlign="top";
					td2.setAttribute('id','td'+(((count-1)*4+1)));
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+2)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+3)));
															
					tr.appendChild(td2);
					tr.appendChild(td3);
					tr.appendChild(td4);
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);					
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}
			}		
		} else {
			if(number%4 == 1){			
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.style.verticalAlign="top";
					td2.setAttribute('id','td'+(((count-1)*4+2)));
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+3)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+4)));
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);					
										
					tr.appendChild(td2);
					tr.appendChild(td3);
					tr.appendChild(td4);					
				}	else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}		
			}else if(number%4 == 2){				
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.style.verticalAlign="top";
					td2.setAttribute('id','td'+(((count-1)*4+1)));
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+3)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+4)));
					
					tr.appendChild(td2);
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);					
					
					tr.appendChild(td3);
					tr.appendChild(td4);
				}	else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);
				}			
			}else if(number%4 == 3){				
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.style.verticalAlign="top";
					td2.setAttribute('id','td'+(((count-1)*4+1)));
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+2)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+4)));
										
					tr.appendChild(td2);
					tr.appendChild(td3);
					
					td.appendChild(chartDiv);
					if(tdFlag)tr.appendChild(td);
					
					tr.appendChild(td4);
				}	else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);					
				}		
			}else if(number%4 == 0){					
				if(td == null || "undefined" == typeof(td)){
					td = document.createElement("td");
					td.setAttribute('id','td'+number);
					td.style.verticalAlign="top";
					tdFlag = true;
					
					td2 = document.createElement('td');
					td2.setAttribute('id','td'+(((count-1)*4+1)));
					td2.style.verticalAlign="top";
					
					td3 = document.createElement('td');
					td3.style.verticalAlign="top";
					td3.setAttribute('id','td'+(((count-1)*4+2)));
					
					td4 = document.createElement('td');
					td4.style.verticalAlign="top";
					td4.setAttribute('id','td'+(((count-1)*4+3)));
										
					tr.appendChild(td2);
					tr.appendChild(td3);
					tr.appendChild(td4);
					
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);	
				}else {
					td.appendChild(chartDiv);			
					if(tdFlag)tr.appendChild(td);					
				}
			}
		}
		
		if(trFlag)tbody.appendChild(tr);
		if(tbdFlag)table.appendChild(tbody);
		if(tabFlag)center.appendChild(table);
		if(cetFlag)parent.appendChild(center);	
		
	}