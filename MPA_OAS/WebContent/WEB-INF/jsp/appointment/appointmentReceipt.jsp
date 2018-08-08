<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">
function printFunction()
{
window.print();
}
</script>
<title><tiles:insertAttribute name="title" ignore="true"/></title>

<table width="100%" ><tr><td width="33%"></td>
<td width="33%">
<table width="100%" ><tr ><td></td></tr> </table>
<form:form action="${pageContext.request.contextPath}/report/appointmentReportReceipt.pdf" method="POST" name="receiptForm" commandName="appointmentForm" modelAttribute="appointmentForm" target="_blank">
<form:hidden path="nricPpassportNumber"/>
<form:hidden path="referenceNo"/>
<form:hidden path="name"/>
<form:hidden path="company"/>
<form:hidden path="appointmentType"/>
<form:hidden path="transactionType"/>
<form:hidden path="craftNumbers"/>
<form:hidden path="harbourCraftCheckBox"/>
<form:hidden path="pleasureCraftCheckBox"/>
<form:hidden path="portClearanceCheckBox"/>
<form:hidden path="othersCheckBox"/>
<form:hidden path="HCLNLSelect"/>
<form:hidden path="HCLADSelect"/>
<form:hidden path="HCLUCSelect"/>
<form:hidden path="HCLCOSelect"/>
<form:hidden path="HCLNMSelect"/>
<form:hidden path="HCLRHSelect"/>
<form:hidden path="PCLNLSelect"/>
<form:hidden path="PCLADSelect"/>
<form:hidden path="PCLUCSelect"/>
<form:hidden path="PCLNPSelect"/>
<form:hidden path="PCGDSelect"/>
<form:hidden path="PCALSelect"/>
<form:hidden path="PCABSelect"/>
<form:hidden path="OTHSSelect"/>
<form:hidden path="date"/>
<form:hidden path="time"/>
<form:hidden path="remark"/>
<form:hidden path="contactNumber"/>
<form:hidden path="emailAddress"/>
	<table width="100%"  class="dynamictable"   cellpadding="5" cellspacing="1">
					<tr>
					<td align="right"><label onclick="printFunction();">Printer Friendly</td>
					</tr>
					<tr>
						<td>
							<table class="nd1">
								<tr>
									<td width="119"></td>
									<td>
										<h2>Appintment Details</h2>
									</td>
									<td width="119"></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>

<table width="100%"  class="dynamictable"   cellpadding="5" cellspacing="1"
                                                                    border="1" >
                                                                    
                                    
                                                                    <tr class="rowodd">
                                                                    
                                                                        <td>
                                                                            <table height="40">
                                                                            <tr><td>NRIC Number/Passport Number/</td></tr>
                                                                            <tr><td>Craft Number</td></tr>
                                                                            </table>
                                                                        </td>
                                                                        <td>
                                                                           <table height="40">
                                                                            <tr><td>${appointmentForm.nricPpassportNumber}</td></tr>
                                                                            <tr><td>${appointmentForm.craftNumbers}</td></tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="roweven">
                                                                    
                                                                        <td>
                                                                           Name
                                                                        </td>
                                                                        <td>
                                                                           ${appointmentForm.name}
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="rowodd">
                                                                    
                                                                        <td>
                                                                             Company
                                                                            </td>
                                                                        <td>
                                                                            ${appointmentForm.company}
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="roweven">
                                                                    
                                                                        <td>
                                                                            Transaction Type
                                                                        </td>
                                                                        <td>
                                                                           <table height="100">
                                                                           <c:forEach  var="i" items="${appointmentForm.transactionType}">
                                                                	<c:choose>
                                                                		<c:when test="${i eq 'HCL'}">
                                                                		<tr>
                                                                		<td> Harbour Craft </td>
                                                                		<td>Qty</td>
                                                                		</tr>
                                                                			<c:forEach var = "i1" items="${appointmentForm.harbourCraftCheckBox}">
                                                                				<c:choose>
                                                                					
                                                                					<c:when test="${i1 eq 'HCLNL' }">
                                                                					<tr>
                                                                					<td>*New / Renewal of licence   &nbsp;</td>
                                                                					<td>${appointmentForm.HCLNLSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i1 eq 'HCLAD' }">
                                                                					<tr>
                                                                					<td>*Application to de-licence&nbsp;</td>
                                                                					<td>${appointmentForm.HCLADSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i1 eq 'HCLUC' }">
                                                                					<tr>
                                                                					<td>*Update of craft particulars   &nbsp;</td>
                                                                					<td>${appointmentForm.HCLUCSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i1 eq 'HCLCO' }">
                                                                					<tr>
                                                                					<td>*Change of ownership &nbsp;</td>
                                                                					<td>${appointmentForm.HCLCOSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i1 eq 'HCLNM' }">
                                                                					<tr>
                                                                					<td>*New/Renewal of Manning licence   &nbsp;</td>
                                                                					<td>${appointmentForm.HCLNMSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i1 eq 'HCLRH' }">
                                                                					<tr>
                                                                					<td>*Return of HARTS  &nbsp;</td>
                                                                					<td>${appointmentForm.HCLRHSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                				</c:choose>
                                                                			</c:forEach>		
                                                                		</c:when>
                                                                		
                                                                		<c:when test="${i eq 'PCL'}">
                                                                		<tr><td></td><td></td></tr>
                                                                		<tr>
                                                                		<td>Pleasure Craft</td>
                                                                		<td>Qty</td>
                                                                		</tr>
                                                                		
                                                                			<c:forEach var= "i2" items="${appointmentForm.pleasureCraftCheckBox}">
                                                                				<c:choose>
                                                                					<c:when test="${i2 eq 'PCLNL' }">
                                                                					<tr>
                                                                					<td>*New / Renewal of licence  &nbsp; </td>
                                                                					<td>${appointmentForm.PCLNLSelect}</td>
                                                                					</tr>                                                                					
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i2 eq 'PCLAD' }">
                                                                					<tr>
                                                                					<td>*Application to de-licence  &nbsp;</td>
                                                                					<td>${appointmentForm.PCLADSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i2 eq 'PCLUC' }">
                                                                					<tr>
                                                                					<td>*Update of craft particulars &nbsp;</td>
                                                                					<td>${appointmentForm.PCLUCSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i2 eq 'PCLNP' }">
                                                                					<tr>                                                           					<tr>
                                                                					<td>*New / Renewal of PPCDL or APPCDL  &nbsp; </td>
                                                                					<td>${appointmentForm.PCLNPSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                				
                                                                				</c:choose>
                                                                			</c:forEach>
                                                                		
                                                                		</c:when>
                                                                		
                                                                		<c:when test="${i eq 'PC'}">
                                                                		<tr><td></td><td></td></tr>
                                                                		<tr>
                                                                		<td>Port Clearance </td>
                                                                		<td>Qty</td>
                                                                		</tr>
                                                                		
                                                                		<c:forEach var = "i3" items="${appointmentForm.portClearanceCheckBox}"> 
                                                                				<c:choose>
                                                                					
                                                                					<c:when test="${i3 eq 'PCGD' }">
                                                                					<tr>
                                                                					<td>*General Declaration  &nbsp; </td>
                                                                					<td>${appointmentForm.PCGDSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                					
                                                                					<c:when test="${i3 eq 'PCAL' }">
                                                                					<tr>
                                                                					<td>*Application for launching permit &nbsp; </td>
                                                                					<td>${appointmentForm.PCALSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                
                                                                					<c:when test="${i3 eq 'PCAB' }">
                                                                					<tr>
                                                                					<td>*Application for Break-up permit  &nbsp;</td>
                                                                					<td>${appointmentForm.PCABSelect}</td>
                                                                					</tr>
                                                                					</c:when>
                                                                				</c:choose>
                                                                		</c:forEach>
                                                                		</c:when>
                                                                		<c:when test="${i eq 'OTHS'}">
                                                                		<tr>
                                                                		<td>Others</td>
                                                                		<td>Qty</td>
                                                                		</tr>
                                                                				<c:forEach var = "i4" items="${appointmentForm.othersCheckBox }">
                                                                					<c:choose>
                                                                						<c:when test="${i4 eq 'OTHS' }">
                                                                						<tr>
                                                                						<td>*Other SubTypes		&nbsp;</td>
                                                                						<td>${appointmentForm.OTHSSelect}</td>
                                                                						</tr>
                                                                						</c:when>
                                                                					</c:choose>
                                                                				</c:forEach>
                                                                		</c:when>
                                                                	</c:choose>
                                                                </c:forEach>
                                                                           </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="rowodd">
                                                                    
                                                                        <td>
                                                                            Date/Time
                                                                        </td>
                                                                        <td>
                                                                            ${appointmentForm.date}
                                                                            ${appointmentForm.time}
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="roweven">
                                                                    
                                                                        <td>
                                                                            Email-Address
                                                                        </td>
                                                                        <td>
																			${appointmentForm.emailAddress}
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="rowodd">
                                                                    
                                                                        <td>
                                                                            Contact Tel No:
                                                                        </td>
                                                                        <td>
                                                                            ${appointmentForm.contactNumber}
                                                                        </td>
                                                                    </tr>
                                                                    <tr class="rowodd">
                                                                    
                                                                        <td>
                                                                            Remarks
                                                                        </td>
                                                                        <td>
                                                                            ${appointmentForm.remark}
                                                                        </td>
                                                                    </tr>                                                                    
                                                                </table>
                                                                  <table width="100%"  cellpadding="5" cellspacing="1">
                                                                   <tr>
                                                                    <td>
                                                                    <input type="submit" class= "inputbutton btn_small"  value="PDF"/>
                                                                    </td>
                                                                    <td>
                                                                    </td>
                                                                   </tr>                                                                
                                                                   </table>
                                                                
                                                                </form:form>
                                                                </td>
                                                                <td></td>
                                                                </tr>
                                                                
                                                                
                                                                
                                                                </table>
                                                                
                                                                