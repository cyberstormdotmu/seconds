<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

 <script type='text/javascript' src='http://code.jquery.com/jquery-1.6.2.js'></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/jquery-ui.js"></script>
    <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.14/themes/base/jquery-ui.css">
    <link rel="stylesheet" type="text/css" href="http://trirand.com/blog/jqgrid/themes/ui.jqgrid.css">
    <script type='text/javascript' src="http://trirand.com/blog/jqgrid/js/i18n/grid.locale-en.js"></script>
    <script type='text/javascript' src="http://trirand.com/blog/jqgrid/js/jquery.jqGrid.min.js"></script>

<style type="text/css">
	
	.ui-helper-clearfix
	{
		content: ".";
    	display: block;
    	height: 15px;
   		 visibility: hidden;
	}
		
</style>
<script type="text/javascript">
var row_selected;

$(document).ready(function(){
        $("#list").jqGrid({
            datatype: 'json',
            mtype: 'POST',
            height: 'auto',
            url:'/SpringHibernateWebAssignment/listUser',
            editurl:'/SpringHibernateWebAssignment/edit',
            colNames:['User ID','First Name','Last Name','User Name','Role Id'],
               colModel:[
                   {name:'userId',index:'userId', width:50, editable:false, editrules:{required:false,number:false,maxValue:200}, editoptions:{size:10}, formoptions:{elmprefix:'*'}},
                   {name:'firstName',index:'firstName', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}, formoptions:{elmprefix:'*'}},
                   {name:'lastName',index:'lastName', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}, formoptions:{elmprefix:'*'}},
                   {name:'userName',index:'userName', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}, formoptions:{elmprefix:'*'}},
                   {name:'roleId',index:'roleId', width:70, editable:true, editrules:{required:true,number:true}, editoptions:{size:10}, formoptions:{elmprefix:'*'}},
               ],
            gridview: true,
            toolbar: [false, "bottom"],
            pager: $('#pager'),
            rowNum:5,
            rowList:[1,2,3,4,5,6,7,8,9,10],
            sortname: 'userId',
            sortorder: "ASC",
            viewrecords: true,
            altRows: false,
            jsonReader : {
                root: "rows",
                page: "page",
                total: "total",
                records: "records", 
                repeatitems: false,
               },       
            caption: 'My JQGrid App',
            onSelectRow: function(row_id){
                            if(row_id != null) {
                                row_selected = row_id;
                                }
                            }
    }); 

    $("#list").jqGrid('navGrid','#pager',{edit:true,add:true,del:true,search:false,refresh:false},
            {
              beforeShowForm: function(form) {$('#tr_userId',form).hide();}
            },
            {
             beforeShowForm: function(form) {$('#tr_userId',form).show();}
            },
            {
            }
            );
});
 
$.jgrid.edit = {
    addCaption: "Add User",
    editCaption: "Edit User",
    bSubmit: "Submit",
    bCancel: "Cancel",
    bClose: "Close",
    bYes : "Yes",
    bNo : "No",
    bExit : "Cancel",
    closeAfterAdd:true,
    closeAfterEdit:true,
    reloadAfterSubmit:true,
    msg: {
            required: "is mandatory or required",
            number: "is a number field. Enter a valid number",
            minValue: "should not be less than ",
            maxValue: "should not be more than "
            },
    errorTextFormat: function (response) {
        if (response.status != 200) {
            return "Error encountered while processing. Please check the accuracy of data entered.";
        }
    },

    afterSubmit : function(response,postdata) {
                        return(true,"ok");
                        }
};

$.jgrid.del = {
    caption: "Delete User",
    msg: "Delete selected User?",
    bSubmit: "Delete",
    bCancel: "Cancel",
    reloadAfterSubmit:true,
    closeOnEscape:true,
    onclickSubmit : function(eparams) {
                            var rowData = $("#list").jqGrid('getRowData', row_selected);
                            var retarr = {'userId':rowData['userId']};
                            return retarr;
                            }
};
</script>
    
    
</head>
<body>
<center>
			<table id="list" class="scroll"></table>
		<div id="pager" class="scroll" style="text-align:center;"></div>
		</center>
</body>
</html>