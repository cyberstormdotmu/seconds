package com.kenure.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kenure.entity.Customer;
import com.kenure.entity.TariffPlan;
import com.kenure.entity.TariffTransaction;
import com.kenure.entity.User;
import com.kenure.model.TariffModel;
import com.kenure.model.TariffTransactionModel;
import com.kenure.service.ITariffPlanService;
import com.kenure.service.IUserService;
import com.kenure.utils.DateTimeConversionUtils;
import com.kenure.utils.KenureUtilityContext;
import com.kenure.utils.LoggerUtils;

@Controller
@RequestMapping(value="/customerOperation/tarrifManagementOperation")
public class TariffController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ITariffPlanService tariffService;

	private org.slf4j.Logger log = LoggerUtils.getInstance(TariffController.class);

	private boolean isError;

	/*@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(TariffAddModel.class, new TariffAddModel());
	}*/


	@RequestMapping(value="/addNewTariff",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> addNewTariff(HttpServletRequest request,HttpServletResponse response,
			@RequestBody TariffModel tariffArray){

		isError = false;

		// Tariff Transaction is mapped with TariffPlan with Many to One Relation

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(currentUser.getUserId());


		// First Lets check name already exist for current customer or not !

		if(tariffService.isNameTariffNameAlreadyExist(currentCustomer, tariffArray.getTariffName())){
			return new ResponseEntity<String>(new Gson().toJson("nameexist"), HttpStatus.OK);
		}else{

			TariffPlan tariffPlan = new TariffPlan();
			tariffPlan.setCustomer(currentCustomer);
			tariffPlan.setTarrifName(tariffArray.getTariffName());
			tariffPlan.setCreatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
			tariffPlan.setCreatedBy(currentCustomer.getUser().getUserId());
			// Set tariifplan object in all tariff transaction

			TariffTransaction tariffTrans = new TariffTransaction();	

			tariffArray.getTariffArray().stream().forEach(x -> {
				tariffTrans.setEndBand(x.getEnd());
				tariffTrans.setStartBand(x.getStart());
				tariffTrans.setRate(x.getCost().doubleValue());
				tariffTrans.setTariffPlan(tariffPlan);

				try{
					userService.saveTariffTransaction(tariffTrans);
				}catch (Exception e) {
					isError = true;
					log.warn("Error while inserting record of {} tariff transaction ",x);
					log.error(e.getMessage());
				}

			});

			if(isError){
				return new ResponseEntity<String>( new Gson().toJson("error"), HttpStatus.OK);
			}else{
				return new ResponseEntity<String>( new Gson().toJson("added"), HttpStatus.OK);
			}
		}
	}

	@RequestMapping(value="/getTariffData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getTariffData(HttpServletRequest request,HttpServletResponse response){
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(currentUser.getUserId());

		List<TariffPlan> list = userService.getTariffDataByCustomer(currentCustomer);
		List<Object> responseList = new ArrayList<Object>();
		List<JsonObject> jsonObjList = new ArrayList<JsonObject>();

		list.stream().forEach(n -> {
			JsonObject jObject = new JsonObject();
			jObject.addProperty("id", n.getTariffPlanId());
			jObject.addProperty("name", n.getTarrifPlanName());
			jsonObjList.add(jObject);
		});
		responseList.add(jsonObjList);
		Boolean isNormalCustomer = (Boolean) request.getSession().getAttribute("isNormalCustomer"); 
		JsonObject jObject = new JsonObject();
		if(isNormalCustomer != null && isNormalCustomer){
			responseList.add(Boolean.FALSE);
		}else{
			responseList.add(Boolean.TRUE);
		}		
		return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
	}

	@RequestMapping(value="/tarrifSearch",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> searchTariffByName(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="name") String tariffName){

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(currentUser.getUserId());

		List<TariffPlan> list = userService.searchTariffByNameAndCustomer(currentCustomer,tariffName);

		List<JsonObject> responseList = new ArrayList<JsonObject>();

		if(list != null && list.size() > 0){
			list.stream().forEach(n -> {
				JsonObject jObject = new JsonObject();
				jObject.addProperty("id",n.getTariffPlanId());
				jObject.addProperty("name", n.getTarrifPlanName());
				responseList.add(jObject);
			});

			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}else{
			return new ResponseEntity<>(new Gson().toJson("nodata"),HttpStatus.OK);
		}
		
		
	}

	@RequestMapping(value="/tariffDelete",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> deleteTariff(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id") String id,
			@RequestParam(value="name") String name){
		try{
			if(userService.deleteTariffwithId(id)){
				return new ResponseEntity<>( new Gson().toJson("deleted"),HttpStatus.OK);
			}else{
				return new ResponseEntity<>( new Gson().toJson("error"),HttpStatus.OK);
			}
		}catch(Exception e){
			return new ResponseEntity<>( new Gson().toJson("error"),HttpStatus.OK);
		}

	}

	@RequestMapping(value="/tariffEdit",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> tariffEdit(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="id") String tariffId,
			@RequestParam(value="name") String tariffName){

		// We have to get Tarrifplan by its id 

		TariffPlan tariffPlan = tariffService.getTariffPlanById(Integer.parseInt(tariffId));

		TariffModel tariffArray = new TariffModel();

		if(tariffPlan != null && tariffPlan.getTariffTransaction() != null){

			tariffArray.setTariffName(tariffName);

			log.info("Transaction {}",tariffPlan.getTariffTransaction().size());

			List<TariffTransactionModel> tariffTransactionList = new ArrayList<TariffTransactionModel>();

			tariffPlan.getTariffTransaction().forEach(n -> {
				TariffTransactionModel tModel = new TariffTransactionModel();
				tModel.setCost(n.getRate());
				tModel.setEnd(n.getEndBand());
				tModel.setStart(n.getStartBand());
				tariffTransactionList.add(tModel);
			});

			// Sorting list so that we can get appropriate order at UI side 
			tariffTransactionList.sort((TariffTransactionModel t1, TariffTransactionModel t2) -> t1.getStart() - t2.getStart());
			tariffArray.setTariffArray(tariffTransactionList);

			List<Object> responseList = new ArrayList<Object>();
			responseList.add(tariffId);
			responseList.add(tariffArray);

			request.getSession().setAttribute("editTariffData", responseList);

			return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);

		}

		return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getEditTariffData",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> getEditTariffData(HttpServletRequest request,HttpServletResponse response){

		List<Object> responseList = new ArrayList<>();
		responseList = (List<Object>) request.getSession().getAttribute("editTariffData");
		request.getSession().removeAttribute("editTariffData");

		if(responseList != null){
			return new ResponseEntity<>(new Gson().toJson(responseList),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Gson().toJson("error"),HttpStatus.OK);
	}
	
	@RequestMapping(value="/editCurrentTariff",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<String> getEditTariffData(HttpServletRequest request,HttpServletResponse response,
			@RequestBody TariffModel tariffArray){
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Customer currentCustomer = userService.getOnlyCustomerByUserId(currentUser.getUserId());

		// Again first check if new edited name exist or not but first we have to check name is changed or not
		TariffPlan tariffPlan = tariffService.getTariffPlanById(Integer.parseInt(tariffArray.getTariffId()));
		
		User loggedUser = null;
		if((loggedUser = KenureUtilityContext.isNormalCustomer(request)) != null){
			tariffPlan.setUpdatedBy(loggedUser.getUserId());
			tariffPlan.setUpdatedTs(DateTimeConversionUtils.getDateInUTC(new Date()));
		}
		
		if(!tariffPlan.getTarrifPlanName().equals(tariffArray.getTariffName()) && tariffService.isNameTariffNameAlreadyExist(currentCustomer, tariffArray.getTariffName())){
			// Name is changed.Lets check for its existence
				return new ResponseEntity<>(new Gson().toJson("nameexist"),HttpStatus.OK);
		}else{
			// Updating new values
			tariffPlan.setTarrifName(tariffArray.getTariffName());

			tariffService.deleteTariffTransaction(tariffPlan,tariffArray);

			return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);
		}
		/*TariffPlan tariffPlan = tariffService.getTariffPlanById(Integer.parseInt(tariffArray.getTariffId()));
		// Updating new values
		tariffPlan.setTarrifName(tariffArray.getTariffName());

		tariffService.deleteTariffTransaction(tariffPlan,tariffArray);

		return new ResponseEntity<>(new Gson().toJson("success"),HttpStatus.OK);*/
	}

}
