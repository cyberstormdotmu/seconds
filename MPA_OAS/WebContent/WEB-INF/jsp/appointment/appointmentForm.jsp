<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<script type="text/javascript" charset="utf-8">
 
 
 function reload(){
	 
	 location.href = "${pageContext.request.contextPath}/appointment/appointmentForm.mpa";
	 
 }
 
$(function(){
		
			//datepicker function to enable only 1 month..
			var today=new Date();
			var day=today.getDate()+1;
			var month=today.getMonth()+1;
			var year=today.getFullYear();
			var mm=today.getMinutes();
			var hour=today.getHours();
			var startDate = null ;
			if(year % 4 == 0){
				if(month == 2){
					if(day > 29){
						day = day -29;
						month = month +1;						
					}
					
				}
			}
			else{
				if(month == 2){
					if(day > 28){
						day = day - 28;
						month = month + 1;
						
						
					}
					
				}
				
			}
			if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ){
					if(day > 31){
						day = day - 31;
						month = month +1;	
					}
				}
				else if(month == 4 || month == 6 || month == 9 || month == 11){
					if(day >30){
						day = day - 30;
						month = month +1 ;
					}
				}
				else if (month == 12){
					if(day > 31){
						day = day - 31;
						month = month - 11;
						year = year + 1;
					}					
				}
	startDate = day+"/"+month+"/"+year;
	
		var monthLast= month + 1;
			if (monthLast == 2){
				if(year % 4 == 0){
					if(day > 29){
					day = day - 29;
					monthLast = monthLast + 1;
				}
				}
				else{
					if(day > 28){
					day = day - 28;
					monthLast = monthLast + 1;
					}
				}
				
			}
			
			else if (monthLast == 4 || monthLast == 6 || monthLast == 9 || monthLast == 11){
				
				if(day > 30){
				day = day - 30;
				monthLast = monthLast + 1;
				}
			}
			if(monthLast > 12){
				monthLast = monthLast - 12 ;
				year=year+1;
			}
			var lastDate = day+"/"+monthLast+"/"+year;
			
		//	$(".date-pick").datepicker({ beforeShowDay: $.datepicker.noWeekends });
			$('.date-pick').datePicker({startDate:startDate
										,endDate:lastDate
										});

		});
		
</script>
<script type="text/javascript" charset="utf-8">

	var counter=1;    //counter for craft numbers
	var transactionCounter=0;   //counter for transaction
	var hey=0;

	$(document).ready(function(){
		
		 $("#nric_passportNumber").focus();
		
		$("#addMore").click(function () {
			//add new text field for craft..
            counter++; 
			$('<div/>',{'id':'TextBoxDiv' + counter}).html('')
            .append( $('<input type="text"/>').attr({'id':'craftNumber'+counter,'name':'craftNumbers','class':'inp-form' , 'style':'margin-top:5px'}) )
            .appendTo( '#TextBoxesGroup' );
           
        });
 
        $("#removeLast").click(function () {
            //remove last added craft..
            if (counter == 1) {
                alert("No more textbox to remove");
                return false;
            }else{
            $("#TextBoxDiv" + counter).remove();
            counter--;

            }
        });

		//show and hide the lists...
		$("#hc").click(function(event) {

		     if ($(this).is(":checked"))
		        $("#hcList").show();
		     else{
		        $("#hcList").hide();
		        var temp1=document.getElementById("HCLNLChk").value;
		        var temp2=document.getElementById("HCLADChk").value;
		        var temp3=document.getElementById("HCLUCChk").value;
		        var temp4=document.getElementById("HCLCOChk").value;
		        var temp5=document.getElementById("HCLNMChk").value;
		        var temp6=document.getElementById("HCLRHChk").value;
		        
		        transactionCounter=parseInt(transactionCounter)-parseInt(temp1)-parseInt(temp2)-parseInt(temp3)-parseInt(temp4)-parseInt(temp5)-parseInt(temp6);
		        
		        document.getElementById("HCLNLChk").value =0;
		        document.getElementById("HCLNLChk").disabled=true;
		        document.getElementById("HCLNL").checked=false;
		        
		        document.getElementById("HCLADChk").value =0;
		        document.getElementById("HCLADChk").disabled =true;
		        document.getElementById("HCLAD").checked=false;
		        
		        document.getElementById("HCLUCChk").value =0;
		        document.getElementById("HCLUCChk").disabled =true;
		        document.getElementById("HCLUC").checked =false;
		        
		        document.getElementById("HCLCOChk").value =0;
		        document.getElementById("HCLCOChk").disabled =true;
		        document.getElementById("HCLCO").checked =false;
		
		        document.getElementById("HCLNMChk").value =0;
		        document.getElementById("HCLNMChk").disabled =true;
		        document.getElementById("HCLNM").checked =false;
		
		        document.getElementById("HCLRHChk").value =0;
		        document.getElementById("HCLRHChk").disabled =true;
		        document.getElementById("HCLRH").checked =false;
      		}
    	});

		$("#pc").click(function(event) {

		    if ($(this).is(":checked"))
		      $("#pcList").show();
		    else{
		      $("#pcList").hide();
		      var temp1=document.getElementById("PCLNLChk").value;
	          var temp2=document.getElementById("PCLADChk").value;
	          var temp3=document.getElementById("PCLUCChk").value;
	          var temp4=document.getElementById("PCLNPChk").value;

	          transactionCounter=parseInt(transactionCounter)-parseInt(temp1)-parseInt(temp2)-parseInt(temp3)-parseInt(temp4);

	          document.getElementById("PCLNLChk").value =0;
	          document.getElementById("PCLNLChk").disabled =true;
	          document.getElementById("PCLNL").checked =false;

	          document.getElementById("PCLADChk").value =0;
	          document.getElementById("PCLADChk").disabled =true;
	          document.getElementById("PCLAD").checked =false;

	          document.getElementById("PCLUCChk").value =0;
	          document.getElementById("PCLUCChk").disabled =true;
	          document.getElementById("PCLUC").checked =false;
	          
	          document.getElementById("PCLNPChk").value =0;
	          document.getElementById("PCLNPChk").disabled =true;
	          document.getElementById("PCLNP").checked =false;
		    }
		  });

		$("#port").click(function(event) {

		    if ($(this).is(":checked"))
		      $("#portList").show();
		    else{
		      $("#portList").hide();
		      var temp1=document.getElementById("PCGDChk").value;
	          var temp2=document.getElementById("PCALChk").value;
	          var temp3=document.getElementById("PCABChk").value;

	          transactionCounter=parseInt(transactionCounter)-parseInt(temp1)-parseInt(temp2)-parseInt(temp3);

	          document.getElementById("PCGDChk").value =0;
	          document.getElementById("PCGDChk").disabled =true;
	          document.getElementById("PCGD").checked =false;

	          document.getElementById("PCALChk").value =0;
	          document.getElementById("PCALChk").disabled =true;
	          document.getElementById("PCAL").checked =false;
	          
	          document.getElementById("PCABChk").value =0;
	          document.getElementById("PCABChk").disabled =true;
	          document.getElementById("PCAB").checked =false;
		    }
		    });

		$("#other").click(function(event) {

		    if ($(this).is(":checked"))
		      $("#otherList").show();
		    else{
		      $("#otherList").hide();
		      var temp1=document.getElementById("OTHSChk").value;
		      
		      transactionCounter=parseInt(transactionCounter)-parseInt(temp1);

		      document.getElementById("OTHSChk").value =0;
		      document.getElementById("OTHSChk").disabled =true;
		      document.getElementById("OTHS").checked =false;
		    }
		    });
		});

	function checkDate()
	{
		document.getElementById("time").disabled=false;

		var selectedDate=document.getElementById("date").value;

		var nextDay=new Date();
		var day=nextDay.getDate();
		var year=nextDay.getFullYear();
		var month=nextDay.getMonth()+1;

		if(month==0 || month==2 || month==4 || month==6 || month==7 || month==9 || month==11)
		{
			if(day==31)
				day=0;
		}
		else if(month==1)
		{
			if(year%4==0)
			{
				if(day==29)
					day=0;
			}

			else
			{
				if(day==28)
					day=0;
			}
		}
		else
		{
			if(day==30)
				day=0;
		}

		day++;

		var selectedDay=selectedDate.substring(0,2);

		var temp=parseInt(selectedDay)-parseInt(day);

		var hours=nextDay.getHours();
		
		if(temp==0 && hours>17)
		{
			alert("You Cant Make Appointment for tomorrow");
			document.getElementById("date").value="";
			document.getElementById("time").disabled=true;
			document.getElementById("time").value="select";			
		}
		
	}

	
	function checkCheckboxes(){

		  if(document.getElementById("hc").checked ==true) {
		   if(document.getElementById("HCLNL").checked == false &&
		     document.getElementById("HCLAD").checked == false &&
		     document.getElementById("HCLUC").checked == false &&
		     document.getElementById("HCLCO").checked == false &&
		     document.getElementById("HCLNM").checked == false &&
		     document.getElementById("HCLRH").checked == false )  {

		     document.getElementById("hc").checked =false;
		    }
		  }


		  if(document.getElementById("pc").checked ==true) {
		   if(document.getElementById("PCLNL").checked == false &&
		     document.getElementById("PCLAD").checked == false &&
		     document.getElementById("PCLUC").checked == false &&
		     document.getElementById("PCLNP").checked == false)  {

		     document.getElementById("pc").checked =false;
		    }
		  }

		  if(document.getElementById("port").checked ==true) {
		   if(document.getElementById("PCGD").checked == false &&
		     document.getElementById("PCAL").checked == false &&
		     document.getElementById("PCAB").checked == false)  {

		     document.getElementById("port").checked =false;
		    }
		  }

		  if(document.getElementById("other").checked ==true) {
		   if(document.getElementById("OTHS").checked == false)  {

		     document.getElementById("other").checked =false;
		    }
		  }
		 }
	
	
	
	function availableTimes(date){

		var appointmentType=document.getElementById("typeOfAppointment").value;
		var date1=date.value;
		
		$.ajax({
			url:"availableTimes.mpa?date="+date1+"&appointmentType="+appointmentType,
			type:"POST",
			contentType: "application/json; charset=utf-8",
            dataType:"json",
            success:function(result){
                var html="<option value=\"select\">----Select----</option>";
				for(var index=0;index<result.length;index++){
					html +="<option value="+result[index]+">"+ result[index]+"</option>";
				}
				$('#time').html(html);
            },
            error:function(errorThrown){
				
            }
		});		
	}

	function enableDisable(){
		document.getElementById("time").disabled=true;
		$( ".calendar-text" ).val(" ");
	}

	
	function PhoneNumber(phn) {
		//validate that albhabet can't be entered...
		
       var phnExp = /^[0-9]+$/;
        if (!(phn.value.match(phnExp))) {
        	 phn.value = phn.value.replace(/[^\0-9]+/g, '');
        }
    }

	function PhoneNumberLength(phn) {
		//validate the length of number
        var phoneNumber = phn.value.replace(/[\(\)\.\-\ ]/g, '');

        if (!($.trim(phoneNumber).length == 10)) {
            return false;
        }else {
			return true;
         }
    }
	
	function enable(t)
	{
		//enable the dropdown if user select checkbox..  
		
		var idSelect=t.id+"Chk";
		if(document.getElementById(t.id).checked==true){
			document.getElementById(idSelect).disabled=false;
		}
		else{
			document.getElementById(idSelect).disabled=true;
			var temp=document.getElementById(idSelect).value ;
			transactionCounter=parseInt(transactionCounter)-parseInt(temp);
			document.getElementById(idSelect).value =0;	
		}
	}

	function transactionCount(e)
	{
		//validate the number of transaction that user is allowed for appointment....
		var temp1=0;
		var temp2=0;
		var temp3=0;
		var temp4=0;
		var temp5=0;
		var temp6=0;
		var temp7=0;
		var temp8=0;
		var temp9=0;
		var temp10=0;
		var temp11=0;
		var temp12=0;
		var temp13=0;
		var temp14=0;

			if(document.getElementById("HCLNL").checked==true)
				temp1=document.getElementById("HCLNLChk").value;
			if(document.getElementById("HCLAD").checked==true)
				temp2=document.getElementById("HCLADChk").value;
			if(document.getElementById("HCLUC").checked==true)
				temp3=document.getElementById("HCLUCChk").value;
			if(document.getElementById("HCLCO").checked==true)
				temp4=document.getElementById("HCLCOChk").value;
			if(document.getElementById("HCLNM").checked==true)
				temp5=document.getElementById("HCLNMChk").value;
			if(document.getElementById("HCLRH").checked==true)
				temp6=document.getElementById("HCLRHChk").value;
			if(document.getElementById("PCLNL").checked==true)
				temp7=document.getElementById("PCLNLChk").value;
			if(document.getElementById("PCLAD").checked==true)
				temp8=document.getElementById("PCLADChk").value;
			if(document.getElementById("PCLUC").checked==true)
				temp9=document.getElementById("PCLUCChk").value;
			if(document.getElementById("PCLNP").checked==true)
				temp10=document.getElementById("PCLNPChk").value;
			if(document.getElementById("PCGD").checked==true)
				temp11=document.getElementById("PCGDChk").value;
			if(document.getElementById("PCAL").checked==true)
				temp12=document.getElementById("PCALChk").value;
			if(document.getElementById("PCAB").checked==true)
				temp13=document.getElementById("PCABChk").value;
			if(document.getElementById("OTHS").checked==true)
				temp14=document.getElementById("OTHSChk").value;
			
		

		transactionCounter =parseInt(temp1)+parseInt(temp2)+parseInt(temp3)+parseInt(temp4)+parseInt(temp5)+parseInt(temp6)+parseInt(temp7)+
							parseInt(temp8)+parseInt(temp9)+parseInt(temp10)+parseInt(temp11)+parseInt(temp12)+parseInt(temp13)+parseInt(temp14);
		
		var temp=document.getElementById(e.id).value;
		hey=temp;

		if(transactionCounter!=1 && document.getElementById("typeOfAppointment").value=="single"){
			alert("You Can't Select more than 1 transaction");
			transactionCounter=parseInt(transactionCounter)-parseInt(temp);
			document.getElementById(e.id).value=0;
		}
	}
	
	function mandatory()
	{
		//required field validations..
		var flag=false;
		var errors="Please Fill The Following Details<br/>";
		errors+="===========================<br/>"; 
		
		document.getElementById("errorDiv").innerHTML = " ";
		
		checkCheckboxes();

		if($.trim(document.getElementById("nric_passportNumber").value)== "")
		{
			errors +="<br/>NRIC Number/Passport Number";
			flag=true;
		}

		var craftFlag =false;
		for(var i=1;i<=counter;i++)
		{
			if($.trim(document.getElementById("craftNumber"+i).value) == "" || $.trim(document.getElementById("craftNumber"+i).value )== "undefiend")
			{
				craftFlag=true;
				break;
			}
		}

		if(craftFlag) {
			flag=true;
			errors+="<br/>Craft Number";
		}
		
		if($.trim(document.getElementById("name").value) == "")
		{
			errors +="<br/>Name";
			flag=true;
		}

		if($.trim(document.getElementById("companyName").value)== "")
		{
			errors +="<br/>Company Name";
			flag=true;
		}

		if(document.getElementById("typeOfAppointment").value=="select")
		{
			errors +="<br/>Select Type Of Appintment";
			flag=true;
		}

		if(document.getElementById("hc").checked==false && document.getElementById("pc").checked==false && document.getElementById("port").checked==false && document.getElementById("other").checked==false)
		{
			errors +="<br/>Select Transaction Type";
			flag=true;
		}

		if($.trim(document.getElementById("date").value)== "")
		{
			errors +="<br/>Date";
			flag=true;
		}

		if(document.getElementById("time").value=="select")
		{
			errors +="<br/>Time";
			flag=true;
		}

		if($.trim(document.getElementById("contactNumber").value) == "")
		{
			errors +="<br/>Contact Number";
			flag=true;
		}

		if($.trim(document.getElementById("contactNumber").value) != ""){
			if(!PhoneNumberLength(document.getElementById("contactNumber"))){
				errors +="<br/>Phone Number should contain only 10 digits";
				flag = true;
			}	
		}

		if($.trim(document.getElementById("emailAddress").value) == "")
		{
			errors +="<br/>Email Address";
			flag=true;
		}	

		if($.trim(document.getElementById("emailAddress").value) !="") {
			  var email=$.trim(document.getElementById("emailAddress").value);	
			  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			  if(!regex.test(email)){
				  errors +="<br/>Proper Email Address";
			    	flag=true;
			  }
		}

		if((transactionCounter>3 || transactionCounter<2) && document.getElementById("typeOfAppointment").value=="group"){
			errors +="<br/>You Can't Select more than 3 and less than 2 trasaction";
			flag = true;
		}			

		if(document.getElementById("typeOfAppointment").value=="single"){
				if(transactionCounter!=1){
					errors +="<br/>You Can Select Only 1 Transaction";
				}
		}

		if(flag)
		{
			$("#errorDiv").show();
			document.getElementById("errorDiv").innerHTML =errors;
			return false;
		}
	}	
</script>

<div id="wrapper">
<div id="middle">

	<h1>Create Appointment </h1>
	<div class="form_field top_pad">
	<div class="titlebg"><h2>Create Appointment Details</h2></div>
	<div class="content_block">
	<div id="errorDiv" style="border-color: red; border-style: dashed;border-width: 2px; margin-top: 20px; margin-bottom: 50px; display: none;padding:15px;color: red;">
	</div>
	<form:form method="post" action="${pageContext.request.contextPath}/appointment/appointmentForm.mpa" modelAttribute="appointmentForm" id="id-form">
		<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th align="right">NRIC Number/Passport Number:<span style=" color: red;"> *</span></th>
				<td><form:input path="nricPpassportNumber" maxlength="32" name="nricPassportNumber" id="nric_passportNumber" class="inp-form" /></td>
			</tr>
			
			<tr>
				<th align="right" valign="top">Craft Number:<span style=" color: red;"> *</span></th>
				<td>
					<div id='TextBoxesGroup'>
							 <div id="TextBoxDiv1">
 										 <input type="text" maxlength="32" id="craftNumber1" name="craftNumbers" class="inp-form"  />  
					 <div style="float: right;"><input type="button" class="inputbutton btn_small" style="font-size: 12px;" name="addMore" id="addMore" value="Add More"> 
				<input type="button" class="inputbutton btn_small" style="font-size: 12px;" name="removeLast" id="removeLast" value="Remove Last"> </div>

 							</div>
					</div>
				</td>
				
			</tr>
			
			<tr>
				<th align="right">Name:<span style=" color: red;"> *</span></th>
				<td><form:input path="name" maxlength="100" id="name" class="inp-form" /></td>
			</tr>
			
			<tr>
				<th align="right">Company Name:<span style=" color: red;"> *</span></th>
				<td><form:input path="company" maxlength="100" id="companyName" class="inp-form" /></td>
			</tr>
			
			<tr>
				<th align="right">Type Of Appointment:</th>
				<td>
					<form:select path="appointmentType" id="typeOfAppointment" class="inp-form" style="height:25px; width:139px;" onchange="enableDisable()">
							<form:option value="single">Single</form:option>
							<form:option value="group">Group</form:option>
					</form:select>
				</td>
			</tr>
			
			<tr>
				<th align="right">Transaction Type:<span style="color: red;"> *</span></th>
				<td>
					<span style="float: left;">
					<form:checkbox path="transactionType" name="transactionType" id="hc" value="HCL"/>&nbsp;&nbsp;Harbour Craft&nbsp;&nbsp;
					</span>
					<span style="float: left;">
					<form:checkbox path="transactionType" name="transactionType" id="pc" value="PCL"/>&nbsp;&nbsp;Pleasure Craft&nbsp;&nbsp;
					</span>
					<span style="float: left;">
					<form:checkbox path="transactionType" name="transactionType" id="port" value="PC"/>&nbsp;&nbsp;Port Clearance&nbsp;&nbsp;
					</span>
					<span style="float: left;">
					<form:checkbox path="transactionType" name="transactionType" id="other" value="OTHS"/>&nbsp;&nbsp;Others&nbsp;&nbsp;
					</span>
					
					<div id="hcList" style="display: none;padding-top: 25px;">
							-------Harbour Craft------<br/><br/>
						<table>
							<tr>
								<td>
						<span style="float: left;">
						
						 <form:checkbox path="harbourCraftCheckBox"  name="hcList" value="HCLNL" id="HCLNL" onchange="enable(this)"/>&nbsp; New / Renewal of licence
						</span>&nbsp;
								</td>
								<td>
						<form:select path="HCLNLSelect" id="HCLNLChk" disabled="true" onchange="transactionCount(this)">
							<form:option value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option>
							<form:option value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option>
							<form:option value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option>
						</form:select>
								</td>
							</tr>
							<tr>
								<td>						
						<span style="float: left;">
							<form:checkbox path="harbourCraftCheckBox" name="hcList" id="HCLAD" value="HCLAD" onchange="enable(this)"/>&nbsp;Application to de-licence
						</span>&nbsp;
								</td>
								<td>
						<form:select path="HCLADSelect" id="HCLADChk" disabled="true" onchange="transactionCount(this)">
							<form:option value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							</tr>
							<tr>
								<td>						
						<span style="float: left;">
							<form:checkbox path="harbourCraftCheckBox" name="hcList" id="HCLUC" value="HCLUC" onchange="enable(this)"/>&nbsp;Update of craft particulars
						</span>&nbsp;
								</td>
								<td>
						<form:select path="HCLUCSelect" id="HCLUCChk" disabled="true" onchange="transactionCount(this)">
							<form:option value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							<tr>
								<td>								
						<span style="float: left;">
							<form:checkbox path="harbourCraftCheckBox" name="hcList" id="HCLCO" value="HCLCO" onchange="enable(this)"/>&nbsp;Change of ownership
						</span>&nbsp;
								</td>
								<td>
						<form:select path="HCLCOSelect" id="HCLCOChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							<tr>
								<td>
						<span style="float: left;">
							<form:checkbox path="harbourCraftCheckBox" name="hcList" id="HCLNM" value="HCLNM" onchange="enable(this)"/>&nbsp;New / Renewal of Manning licence
						</span>&nbsp;
								</td>
								<td>
						<form:select path="HCLNMSelect" id="HCLNMChk" disabled="true" onchange="transactionCount(this)">
							<form:option value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							<tr>
								<td>
						<span style="float: left;">
							<form:checkbox path="harbourCraftCheckBox" name="hcList" id="HCLRH" value="HCLRH" onchange="enable(this)"/>&nbsp;Return of HARTS
						</span>&nbsp;
								</td>
								<td>
						<form:select path="HCLRHSelect" id="HCLRHChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							</tr>
						</table>
					</div>
					
					
					<div id="pcList" style="display: none;padding-top: 25px;">
							-------Pleasure Craft------<br/><br/>
						<table>
							<tr>
								<td>
						<span style="float: left;">
							<form:checkbox path="pleasureCraftCheckBox" name="pcList" id="PCLNL" value="PCLNL" onchange="enable(this)"/> &nbsp;New / Renewal of licence
						</span>&nbsp;
								</td>
								<td>
						<form:select path="PCLNLSelect"  id="PCLNLChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							<tr>
								<td>
						<span style="float: left;">
							<form:checkbox path="pleasureCraftCheckBox"  name="pcList" id="PCLAD" value="PCLAD" onchange="enable(this)"/>&nbsp;Application to de-licence
						</span>&nbsp;
								</td>
								<td>
						<form:select path="PCLADSelect" id="PCLADChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							</tr>
							<tr>
								<td>
						<span style="float: left;">
							<form:checkbox path="pleasureCraftCheckBox" name="pcList" id="PCLUC" value="PCLUC" onchange="enable(this)"/>&nbsp;Update of craft particulars
						</span>&nbsp;
								</td>
								<td>
						<form:select path="PCLUCSelect" id="PCLUCChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							</tr>
							<tr>
								<td>
						<span style="float: left;">
							<form:checkbox path="pleasureCraftCheckBox" name="pcList" id="PCLNP" value="PCLNP" onchange="enable(this)"/>&nbsp;New / Renewal of PPCDL or APPCDL
						</span>&nbsp;
								</td>
								<td>
						<form:select path="PCLNPSelect" id="PCLNPChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
								</td>
							</tr>
						</table>
					</div>
					
					
					
					<div id="portList" style="display: none;padding-top: 25px;">
							-------Port Clearance------<br/><br/>
							<table>
								<tr>
									<td>
						<span style="float: left;">
							<form:checkbox path="portClearanceCheckBox" name="portList" id="PCGD" value="PCGD" onchange="enable(this)"/>&nbsp;General Declaration
						</span>&nbsp;
									</td>
									<td>
						<form:select path="PCGDSelect" id="PCGDChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
									</td>
								</tr>
								<tr>
									<td>
						<span style="float: left;">
							<form:checkbox path="portClearanceCheckBox" name="portList" id="PCAL" value="PCAL" onchange="enable(this)"/>&nbsp;Application for launching permit
						</span>&nbsp;
									</td>
									<td>
						<form:select path="PCALSelect" id="PCALChk" disabled="true" onchange="transactionCount(this)">
							<form:option value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
									</td>
								</tr>
								<tr>
									<td>
						<span style="float: left;">
							<form:checkbox path="portClearanceCheckBox" name="portList" id="PCAB" value="PCAB" onchange="enable(this)"/>&nbsp;Application for Break-up permit
						</span>&nbsp;
									</td>
									<td>
						<form:select path="PCABSelect" id="PCABChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option >
							<form:option  value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option >
							<form:option  value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option >
						</form:select>
									</td>
								</tr>
							</table>					
					</div>
					
					<div id="otherList" style="display: none;padding-top: 25px;">
							-------Others------<br/><br/>
							<table>
								<tr>
									<td>
						<span style="float: left;">
							<form:checkbox path="othersCheckBox" name="otherList" id="OTHS" value="OTHS" onchange="enable(this)"/>&nbsp;Others
						</span>&nbsp;
									</td>
									<td>
						<form:select path="OTHSSelect" id="OTHSChk" disabled="true" onchange="transactionCount(this)">
							<form:option  value="0">&nbsp;&nbsp;0&nbsp;&nbsp;</form:option  >
							<form:option   value="1">&nbsp;&nbsp;1&nbsp;&nbsp;</form:option  >
							<form:option   value="2">&nbsp;&nbsp;2&nbsp;&nbsp;</form:option  >
						</form:select>
									</td>
								</tr>
							</table>
					</div>
					
				</td>
			</tr>
			
			<tr>
				<th align="right">Date/Time:<span style="color: red;"> *</span></th>
				<td><span id="temp11"><form:input path="date" readonly="true" name="date" class="date-pick inp-form calendar-text" id="date" style="width:133px;" onfocus="checkDate()" onchange="availableTimes(this)"/></span>
				
				<form:select path="time" id="time"  class="inp-form" style="margin:0px;padding: 0px;height:25px;padding-left: 3px;">
					<form:option value="select">----SELECT----</form:option>
					<form:options items="${timeList}"/>
				</form:select>
				
				</td>
			</tr>
			
			<tr>
				<th align="right">Remark:</th>
				<td><form:input path="remark" name="remark" class="inp-form" /></td>
			</tr>
			
			<tr>
				<th align="right">Contact No:<span style=" color: red;"> *</span></th>
				<td><form:input path="contactNumber" maxlength="18" id="contactNumber" name="contactNumber" class="inp-form" onblur="javascript:return PhoneNumberLength(this)" onkeyup="PhoneNumber(this)"/></td>
			</tr>
			
			<tr>
				<th align="right">Email Address:	<span style="color: red;"> *</span> </th>
				<td><form:input path="" id="emailAddress" maxlength="100" name="emailAddress" class="inp-form" /></td>
			</tr>
			
			<tr>
				<td></td>
				<td>
					<input class = "inputbutton btn_small" type="submit" value="Submit" onclick="javascript:return mandatory()">

					<input type="button" value="Cancel" onclick="reload();"  class="inputbutton btn_small">
				</td>
			</tr>
		</table>
		</form:form>
		</div>
		</div>
		</div>
		</div>
		