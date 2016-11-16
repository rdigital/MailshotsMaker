function showEnvironment()
{
	$("#tabs_plan").hide();
	$("#tabs_set").hide();
	$("#tabs_environment").show("slide",{},300,function(){});
	
	$("#plan").removeClass("pl");
	$("#set").removeClass("se");
	$("#environment").addClass("en");
	$("#en_s").hide();
	$("#pl_s").show();
	$("#se_s").show();
	
}
function showPlan()
{
	$("#tabs_environment").hide();
	$("#tabs_set").hide();
	$("#tabs_plan").show("slide",{},300,function(){});
	
	$("#environment").removeClass("en");
	$("#set").removeClass("se");
	$("#plan").addClass("pl");
	
	$("#pl_s").hide();
	$("#en_s").show();
	$("#se_s").show();

}
function showSet()
{
	$("#tabs_plan").hide();
	$("#tabs_environment").hide();
	$("#tabs_set").show("slide",{},300,function(){});
	
	$("#environment").removeClass("en");
	$("#plan").removeClass("pl");
	$("#set").addClass("se");
	
	$("#se_s").hide();
	$("#pl_s").show();
	$("#en_s").show();
}
function addEnClass(){
	$("#environment").addClass("environment");
}
function removeEnClass(){
	$("#environment").removeClass("environment");
}

function addPlanClass(){
	$("#plan").addClass("plan");
}
function removePlanClass(){
	$("#plan").removeClass("plan");
}

function addSetClass(){
	$("#set").addClass("set");
}
function removeSetClass(){
	$("#set").removeClass("set");
}
function showStore(){
	$("#dialog").dialog("open");
}






