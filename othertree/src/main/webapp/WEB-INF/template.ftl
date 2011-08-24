<!DOCTYPE script PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="StyleSheet" href="css.css" type="text/css" />
<script language="JavaScript">
function line(obj)//添加下划线
{
obj.style.textDecoration='underline';
}
function delline(obj)//取消下划线
{
obj.style.textDecoration='none';
}
function showtree(str)//展开|关闭子分支
{
	/*innerHTML设置或返回表格行的开始和结束标签之间的 HTML，将span标签变成另外的span标签
	这个里面，其实就是设置+号那个位置的字符*/
var eval1="span_"+str+".innerHTML=span_"+str+".innerHTML=='+'?'-':'+'";
	//OBJ.style.display，设定关闭某个元素。
var eval2=str+"_value.style.display="+str+"_value.style.display=='none'?'':'none'";
	
	/*函数可计算某个字符串，并执行其中的的 JavaScript 代码。通过计算 string 得到的值（如果有的话）。
	eval(x+17))
*/
eval(eval1);
eval(eval2);
}
</script>
</head>

<body>
   <table>
     <tr>
        <td width=80% style="padding: 30px">
        <#if tree??>
         <#list tree as x>
           <div class=tree_1 onmouseover="line(this)"onMouseOut="delline(this)" onClick="showtree('${x.name}')">
			  <img alt="database" src="folder.gif"></img>
			  <a href="database.do?database=${x.name}">${x.name}
			  <span id=span_${x.name} style="color: red">+</span>
		  </div >
		   <#if (x.child??)>
             <div id="${x.name}_value" class=tree_2 style="display:none">
		     <#list x.child as y>
		            <div class=tree_3 >
                    <img alt="table" src="page.gif"></img>
					<a href="table.do?database=${x.name}&table=${y}">${y}</a>
		            </div>
		     </#list>
           </div>
		  </#if>
         </#list>
        </#if>
        <#--最后添加一个可以执行SQL语句的页面链接-->
        <div class=tree_1 onmouseover="line(this)"onMouseOut="delline(this)" onClick="showtree('edit')">
			  <img alt="database" src="sql.png"></img>
			  <a href="edit.do">SQL编辑器</a>
		</div >
        </td>
     </tr>
   </table>


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
           <#elseif (x_index%2==1)>
               <tr >
               <#list x as y>
               <#if y??>
               <td>${y}</td>
               </#if>
               </#list>
               </tr>
           <#else>
           <#--如果行数时非0的偶数时，我就是用特殊的背景色才处理-->
            <tr class="alt">
              <#list x as y>
              <#if y??>
               <td>${y}</td>
              </#if>
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