/*1*/
select Firstname,lastname from TblUser;


/*2*/
select name from tblcountry;


/*3*/
select name from tblstate;

/*4*/
SELECT ts.Name as StateName,tc.Name as CountryName FROM TblState ts,TblCountry tc where (ts.CountryIDFK =tc.CountryID ) AND tc.CountryID IN 
('1' ,'2' , '3' ,'4' ,'5');
/*5*/
select name from tbldepartment;
/*6*/
select ta.streetname 'StreetName',ta.city 'CityName',ts.name 'StateName',ta.zipcode 'ZipCode' from tbladdress ta,tblstate ts where ts.stateid=ta.stateidfk;


/*7*/
select tu.Firstname from tbluser tu, tbladdress ta where ta.streetname=NULL;

/*8*/
SELECT TblUser.FirstName,TblUser.LastName,TblDepartment.Name
FROM TblUser
INNER JOIN TblDepartment
ON TblUser.DepartmentIDFK=TblDepartment.DepartmentId
where DepartmentID IN(select distinct top 5 departmentid from tbldepartment);

/*9*/
select firstname from tbluser where gender='male';

/*10*/

select firstname from tbluser where gender='female';

/*11*/
select firstname,lastname from tbluser where lastname=NULL;

/*12*/
select firstname,lastname from tbluser where lastname is NOT NULL;

/*13*/
SELECT TD.Name "Department",TS.Name "State", TC.Name "Country", TA.StreetName, TA.City, TA.ZipCode FROM TblState TS, TblDepartment TD, TblCountry TC, TblAddress TA, TblUser TU WHERE (TU.AddressIDFK=TA.AddressID) AND (TU.DepartmentIDFK=TD.DepartmentID) AND (TC.CountryID=TS.CountryIDFK) AND (TA.StateIDFK=TS.StateID) AND (TU.AddressIDFK=TA.AddressID) AND TD.DepartmentID IN (SELECT TOP 5 DepartmentID FROM TblDepartment);

/*14*/

SELECT TD.Name "Department",TS.Name "State", TC.Name "Country", TA.StreetName, TA.City, TA.ZipCode FROM TblState TS, TblDepartment TD, TblCountry TC, TblAddress TA, TblUser TU WHERE (TU.AddressIDFK=TA.AddressID) AND (TU.DepartmentIDFK=TD.DepartmentID) AND (TC.CountryID=TS.CountryIDFK) AND (TA.StateIDFK=TS.StateID) AND (TU.AddressIDFK=TA.AddressID) AND TS.stateID IN (SELECT TOP 5 stateID FROM Tblstate);

/*15*/

select tu.firstName,tu.LastName,tc.Name,ts.Name,td.Name,ta.city from TblUser tu inner join TblDepartment td on tu.DepartmentIdFk= td.DepartmentId join TblAddress ta on tu.AddressIdFk = ta.AddressId join TblState ts on ta.StateIdFk=ts.StateID join TblCountry tc on ts.CountryIdFk = tc.CountryId where Ta.City IN (select Distinct Top 5 city from TblAddress);

/*16*/

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where dateofbirth>='01/01/2000' And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);



/*17*/
select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where dateofbirth<='12/31/1999' And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);

/*18*/

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where dateofbirth='01/01/2000' And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);

/*19*/

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where dateofbirth Between '01/01/1990' And '01/01/1995' And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);

/*20*/

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid) order by dateofbirth;

/*21*/

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where (tu.isactive='true') And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where (tu.isactive='false') And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);


/*22*/
select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where (tu.firstname like 'a%') And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);

/*23*/
select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where (tu.emailaddress like '%gmail.com%') And (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);

/*24*/
select tu.firstname,tu.lastname from tbluser tu where tu.addressidfk is NULL; 

/*25*/

select tu.firstname,tu.lastname from tbluser tu,tbladdress ta,tblstate ts where tu.gender='female' and tu.addressidfk=ta.addressid and ta.stateidfk=ts.stateid and ts.name='gujarat';

/*26*/

select tu.firstname,tu.lastname from tbluser tu,tbladdress ta,tblstate ts where tu.gender='male' and tu.addressidfk=ta.addressid and ta.stateidfk=ts.stateid and ts.name='gujarat';

/*27*/

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where (tu.departmentidfk=td.departmentid and td.name='hr') And (tu.addressidfk=ta.addressid) And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);

/*28*/

select tu.firstname,tu.lastname,tu.gender,tu.username,tu.dateofbirth,tu.emailaddress,tu.mobilenumber,tu.phonenumber,tu.isactive,td.name 'Department Name',ta.streetname,ta.city,ts.name 'statename',tc.name 'countryname',ta.zipcode from tbluser tu,tbldepartment td,tbladdress ta,tblstate ts,tblcountry tc where  (tu.departmentidfk=td.departmentid) And (tu.addressidfk=ta.addressid and ta.zipcode='380063') And (ta.stateidfk=ts.stateid) And (ts.countryidfk=tc.countryid);






