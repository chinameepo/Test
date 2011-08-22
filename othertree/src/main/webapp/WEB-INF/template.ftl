<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title> 数据库结构&raquoTree</title>
	<link rel="StyleSheet" href="dtree.css" type="text/css" />
	<script type="text/javascript" src="dtree.js"></script>
	<style type="text/css">
#customers
  {
  font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
  width:100%;
  border-collapse:collapse;
  }

#customers td, #customers th 
  {
  font-size:1em;
  border:1px solid #98bf21;
  padding:3px 7px 2px 7px;
  }

#customers th 
  {
  font-size:1.1em;
  text-align:left;
  padding-top:5px;
  padding-bottom:4px;
  background-color:#A7C942;
  color:#ffffff;
  }

#customers tr.alt   td 
  {
  color:#000000;
  background-color:#EAF2D3;
  }
</style>
</head>


<body>
   <h2>导航</h2>
   <div class="dtree">
	  <p><a href="javascript: d.openAll();">展开</a> | <a href="javascript: d.closeAll();">关闭</a></p>
	 <script type="text/javascript">
	    d = new dTree('d');
		d.add(0,-1,'我的数据库结构');
		<#if tree??>
		<#list tree as x>
		  <#if (x.url??)>    
		  d.add(${x.id},${x.prentid},'${x.name}','${x.url}');
		  <#else >
		  d.add(${x.id},${x.prentid},'${x.name}');
		  </#if>
		</#list>
		<#else>
		<#--如果树为空，什么都不做-->
		</#if>
		d.add(99999,0,'执行SQL','sqlexe.do');
		document.write(d);
	 </script>
   </div>
   
   <div class ="show"  style= "overflow:auto " >
    <#if (tables??)>
       <table id="customers">
       <#list tables as x>
           <#--第一行是表头，特殊处理-->
           <#if (x_index==0)>
             <tr>
               <#list x as y>
               <th>${y}</th>
               </#list>
             </tr>
           <#elseif (x_index%2==0)>
               <tr >
               <#list x as y>
               <td>${y}</td>
               </#list>
               </tr>
           <#else>
           <#--如果行数时奇数，我就是用特殊的背景色才处理-->
            <tr class="alt">
              <#list x as y>
               <td>${y}</td>
              </#list>
              </tr>
           </#if>
          </#list>
      </table>
    <#else>
    <#--如果为空，什么都不做-->
    </#if>
   </div>
</body>
</html>