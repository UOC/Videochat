<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Aula: 1234</title>
</head>
<body>
<h1></h1>

<h2>Launcher Version</h2>

<form action="../Front.launcher">
<div>
<fieldset>
<legend>Crear meeting</legend>
<label for="ids">session:</label> 
<input type="text" name="s" id="ids" value=""/><br/>
<label for="idcontext">context/Aula:</label> 
<input type="text" name="container" id="idcontext" value="DOMAIN.226458" size="8"/><br/>
</fieldset>
<input type="submit" name="crear_meeting" value="Crear meeting" />
</div>
<input type="hidden" name="control" value="view"/>
<input type="hidden" name="instanceId" value="EPC-REF.133"/>
</form>

<form action="../Front.launcher">
<div>
<fieldset>
<legend>Gravar meeting</legend>
<label for="ids">session:</label> 
<input type="text" name="s" id="ids" value=""/><br/>
<label for="idcontext">context/Aula:</label> 
<input type="text" name="container" id="idcontext" value="DOMAIN.226458" size="8"/><br/>
</fieldset>
<input type="submit" name="access_meeting" value="Accesss meeting" />
</div>
<input type="hidden" name="control" value="access"/>
<input type="hidden" name="instanceId" value="EPC-REF.133"/>
</form>

<form action="../Front.launcher">
<div>
<fieldset>
<legend>Cercar meeting</legend>
<label for="ids">session:</label> 
<input type="text" name="s" id="ids" value=""/><br/>
<label for="idcontext">context/Aula:</label> 
<input type="text" name="container" id="idcontext" value="DOMAIN.226458" size="8"/><br/>
</fieldset>
<input type="submit" name="cercar_meeting" value="Cercar meeting" />
</div>
<input type="hidden" name="control" value="search"/>
<input type="hidden" name="instanceId" value="EPC-REF.133"/>
</form>



</body>
</html>