<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk"> 
 <title>输入SQL语句</title>  
 <!--这里面的javascript确实可以验证你输入的是否为空，
 可是它提示你之后，那个代码还是传过去了，然后就出错 了，目前未解决-->
<script type="text/javascript">
  function getObj(id){
        var Obj = document.getElementById(id).value;
          return Obj;
   }  
   function check(){  
   if(getObj("sqlString")==""){  
   alert("文本框输入为空，不能提交表单！");  
   document.getElementById("test").focus;
   return false;//false:阻止提交表单
         }  
   }
 </script>
</head>

<body>
    <form action="query.do" method="post" onsubmit="return check()" >
		<p align="center">input SQL </p>
		<br/> 
		input the SQL : <br/>
		<input type="text" name="sqlString"  style= "width:800px;height=600px " />
		<br/>
		<input type="submit" value="SUBMIT" name="submit" /> 
		<input type="reset" value="RESET" name="reset" />
	</form>
	<div>
	 ${affect}
	</div>
</body>
</html>
