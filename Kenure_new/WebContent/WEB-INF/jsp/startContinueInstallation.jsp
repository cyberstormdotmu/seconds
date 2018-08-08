<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" /> 
  <title>BLU Tower</title>
  <link href="css/style.css" rel="stylesheet" type="text/css" />
  <link href="css/dropkick.css" rel="stylesheet" type="text/css" />
  <!--[if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif]-->
  <meta http-equiv="X-UA-Compatible" content="IE=edge">


  <script src="js/jquery.min.js" type="text/javascript"></script>
  <script src="js/dropkick.js" type="text/javascript"></script>
  <script src="js/easy-tabs.js" type="text/javascript"></script>
  <script src="js/custom.js" type="text/javascript"></script>
</head>

<body>
 <div id="wrapper"> <!--wrapper Start-->
   <div id="header">
    <div class="logo"><a href="#"><img src="images/logo.jpg" alt="" /></a></div>
    <a class="menu-toggle" href="#menu">
      <i class="menu-icon">
        <span class="line1"></span>
        <span class="line2"></span>
        <span class="line3"></span>
      </i>
    </a>
    <div class="hd-right">
      <div class="user_info">
        <a href="#" class="user"><span class="user-img"><img alt="" src="images/user.jpg"></span> Sandip Trivedi</a>
        <ul class="user_link">
          <li><a href="#">My Profile</a></li>
          <li><a href="#">My Task</a></li>
          <li><a href="#">Logout</a></li>
        </ul>
      </div>
    </div>
  </div>
  <div class="leftbar customer">
   <ul class="menulist">
     <li class="has_dropdown"><a href="#"><i><img src="images/ic-control.png" alt="" /></i><p>Control</p></a>
       <ul class="sub-menu-nav">
        <li><a href="#" title="">Scheduler</a></li>
        <li><a href="#" title="" class="active">Consumer Management</a></li>
        <li><a href="#" title="">Site Management</a></li>
        <li><a href="#" title="">DataCollector Management</a></li>
        <li><a href="#" title="">User Management</a></li>
        <li><a href="#" title="">Meter Management</a></li>
      </ul>
    </li>
    <li class="has_dropdown"><a href="#"><i><img src="images/ic-analytics.png" alt="" /></i><p>Analytics</p></a>
      <ul class="sub-menu-nav">
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
      </ul>
    </li>
    <li class="has_dropdown"><a href="#"><i><img src="images/ic-setup.png" alt="" /></i><p>Setup</p></a>
      <ul class="sub-menu-nav">
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
      </ul>
    </li> 
    <li class="has_dropdown"><a href="#"><i><img src="images/ic-controlpanel.png" alt="" /></i><p>Maintenance</p></a>
      <ul class="sub-menu-nav">
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
        <li><a href="#" title="">Lorem ipsum</a></li>
      </ul>
    </li>
  </ul>
</div>
<div id="content">
 <!--Content Start-->
 <h1>Current Status</h1>
 <div class="middle">
   <div class="boxpanel clear-fix">

    <ul class="customer-details">
      <li><span>Customer Code <i>:</i></span> 123</li>
      <li><span>Installation <i>:</i></span>Aldershot</li>
      <li><span>SiteName <i>:</i></span>Northtown</li>
      <li><span>Site ID <i>:</i></span>1234</li>
      <li><span>Status <i>:</i></span>Commissioning 8</li>
    </ul>

    <ul class="customer-details pull-right">
      <li><span>NUmber of DataCollectorsvc <i>:</i></span>2</li>
      <li><span>Number of Endpoints <i>:</i></span>349</li>
      <li><span>Number of repeaters <i>:</i></span> 1</li>
    </ul>
  </div> 

  
  <div id="parentHorizontalTab" class="parent-tabs">
    <ul class="resp-tabs-list hor_1">
      <li>Planning</li>
      <li>Installation</li>
      <li>Commissioning</li>
      <li>Verification</li>
    </ul>
    <div class="resp-tabs-container hor_1">
      <div>
        <p>
          <!--vertical Tabs-->

          <div id="ChildVerticalTab_1" class="innertabs">


            <ul class="resp-tabs-list ver_1">
              <li>Endpoints</li>
              <li>Daet Collectors</li>
              <li>Repeaters</li>
            </ul>
            <div class="resp-tabs-container ver_1">
              <div class="boxpanel">
                <span class="heading">Commissioning Type</span>
                <div class="box-body">
                 <div class="filter_panel">
                   <div class="form-group">
                     <label class="checkbox"><input type="radio" name="Scheduled" value="option1"><span></span>Scheduled</label>
                     <label class="checkbox"><input type="radio" name="Scheduled" value="option2"><span></span>Monual</label>
                   </div>

                   <div class="form-group">
                     <input class="form-control calendar-input" type="text" placeholder="Active Date">
                   </div>
                   <div class="table-hint">
                     <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
                       tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
                       quis nostrud exercitation ullamco </p>
                       <span class="enable">1</span>
                     </div>
                   </div>

                   <span class="heading">Select Route File</span>
                   <div class="filter_panel uploadFile-box">
                    <div class="form-group">
                      <input id="uploadFile" class="form-control" placeholder="Choose File" readonly />
                    </div>
                    <div class="form-group">
                      <div class="fileUpload" style="">
                        <button class="default ic_browse">Browse</button>
                        <input id="uploadBtn" type="file" class="upload" />
                        <button class="primary ic_upload">Upload</button>
                      </div>
                      <script type="text/javascript">
                        document.getElementById("uploadBtn").onchange = function () {
                          document.getElementById("uploadFile").value = this.value;
                        };
                      </script>
                    </div>
                    <span class="disable">1</span>
                  </div>
                  <div class="optopn-box">
                    <a class="toggle"><span></span></a>
                    <ul class="optopn-link">
                      <li><a href="#">start/Continue Installation</a></li>
                      <li><a href="#">Re-schedule Commissioning</a></li>
                      <li><a href="#">Add more endpoints</a></li>
                      <li><a href="#">Set Operating Mode</a></li>
                      <li><a href="#">View Network Map</a></li>
                      <li><a href="#">Check DC Connectivity</a></li>
                      <li><a href="">Add/Edit Installation</a></li>
                      <li><a href="">Add/Edit Site</a></li>
                      <li><a href="">Assign/Configure DC</a></li>
                      <li><a href="">Add Endpoints</a></li>
                      <li><a href="">Add Repeaters</a></li>
                    </ul>
                  </div>

                  
                </div>
                <!-- ================================================ -->
                <div class="box-body">
                  <div class="grid-content">
                    <table class="grid">
                      <tr>
                        <th class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></th>
                        <th>Customer Name</th>
                        <th>Datplan (annual) </th>
                        <th>Plan Active Date</th>
                        <th>Plan Expiry Date</th>
                        <th>Action</th>
                      </tr>
                      <tr>
                        <td class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></td>
                        <td>John B Marshall</td>
                        <td>44.21</td>
                        <td>21/Jul/2016</td>
                        <td>21/Sep/2017</td>
                        <td class="action">
                          <a href="#" title="edit"><img alt="" src="images/edit-ic.png"></a>&nbsp;
                          <a href="#" title="delete"><img alt="" src="images/delete-ic.png"></a>
                        </td>
                      </tr>
                      <tr>
                        <td class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></td>
                        <td>John B Marshall</td>
                        <td>44.21</td>
                        <td>21/Jul/2016</td>
                        <td>21/Sep/2017</td>
                        <td class="action">
                          <a href="#" title="edit"><img alt="" src="images/edit-ic.png"></a>&nbsp;
                          <a href="#" title="delete"><img alt="" src="images/delete-ic.png"></a>
                        </td>
                      </tr>
                      <tr>
                        <td class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></td>
                        <td>John B Marshall</td>
                        <td>44.21</td>
                        <td>21/Jul/2016</td>
                        <td>21/Sep/2017</td>
                        <td class="action">
                          <a href="#" title="edit"><img alt="" src="images/edit-ic.png"></a>&nbsp;
                          <a href="#" title="delete"><img alt="" src="images/delete-ic.png"></a>
                        </td>
                      </tr>
                      <tr>
                        <td class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></td>
                        <td>John B Marshall</td>
                        <td>44.21</td>
                        <td>21/Jul/2016</td>
                        <td>21/Sep/2017</td>
                        <td class="action">
                          <a href="#" title="edit"><img alt="" src="images/edit-ic.png"></a>&nbsp;
                          <a href="#" title="delete"><img alt="" src="images/delete-ic.png"></a>
                        </td>
                      </tr>
                      <tr>
                        <td class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></td>
                        <td>John B Marshall</td>
                        <td>44.21</td>
                        <td>21/Jul/2016</td>
                        <td>21/Sep/2017</td>
                        <td class="action">
                          <a href="#" title="edit"><img alt="" src="images/edit-ic.png"></a>&nbsp;
                          <a href="#" title="delete"><img alt="" src="images/delete-ic.png"></a>
                        </td>
                      </tr>
                      <tr>
                        <td class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></td>
                        <td>John B Marshall</td>
                        <td>44.21</td>
                        <td>21/Jul/2016</td>
                        <td>21/Sep/2017</td>
                        <td class="action">
                          <a href="#" title="edit"><img alt="" src="images/edit-ic.png"></a>&nbsp;
                          <a href="#" title="delete"><img alt="" src="images/delete-ic.png"></a>
                        </td>
                      </tr>
                      <tr>
                        <td class="checkbox-col"><label class="checkbox"><input type="checkbox"><span></span></label></td>
                        <td>John B Marshall</td>
                        <td>44.21</td>
                        <td>21/Jul/2016</td>
                        <td>21/Sep/2017</td>
                        <td class="action">
                          <a href="#" title="edit"><img alt="" src="images/edit-ic.png"></a>&nbsp;
                          <a href="#" title="delete"><img alt="" src="images/delete-ic.png"></a>
                        </td>
                      </tr>
                    </table>
                    <div class="grid-bottom">
                      <div class="show-record col-sm-3">
                        <span class="records">Records:</span>
                        <select class="form-control">
                          <option>20</option>
                          <option>50</option>
                        </select>
                      </div>
                      <div class="paging">
                        <ul>
                          <li><a href="#" class="prevar"></a></li>
                          <li><a href="#">1</a></li>
                          <li><a href="#" class="active">2</a></li>
                          <li><a href="#">3</a></li>
                          <li><a href="#">4</a></li>
                          <li><a href="#">5</a></li>
                          <li><a href="#" class="nxtar"></a></li>
                        </ul>
                      </div> 
                    </div>
                  </div>
                  <div class="form-group align-right">
                    <button class="primary ic_save">Save File</button>
                  </div>
                </div>
              </div>
              <div>
                <p>Tab 1.2 Container</p>
              </div>
              <div>
                <p>Tab 1.3 Container</p>
              </div>
              <div>
                <p>Tab 1.4 Container</p>
              </div>
            </div>
          </div>
        </p>
      </div>
      <div class="Tab-2">
        <p>Tab 2 Container</p>
      </div>
      <div class="Tab-3">

        <p>Tab 3 Container</p>
      </div>
      <div class="Tab-4">

        <p>Tab 4 Container</p>
      </div>
    </div>
  </div>


</div>
<!--Content end-->   
</div>


<div id="footer">
  <!--footer start-->
  <p class="copyright">&copy; 2016 Blu Tower.&nbsp;&nbsp;<a href="#">Privacy</a><span>|</span><a href="#">Terms</a></p> 
  <!--footer end-->
</div>  
</div>
<!--wrapper end-->

</body>
</html>