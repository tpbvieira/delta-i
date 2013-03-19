<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
  // <![CDATA[
  var eeTimerStart = new Date().getTime();
  var eeTimerCnt = 0;
  var eeAdsLoaded = 0;
  var ourMs = 0;
  var adMs = 0;
  
  function eeEncode(str)
  {
     str = escape(str);
     str = str.replace('+', '%2B');
     str = str.replace('%20', '+');
     str = str.replace('*', '%2A');
     str = str.replace('/', '%2F');
     str = str.replace('@', '%40');
     return str;
  }
  
  function endEETimer()
  {
      if (++eeTimerCnt == 4) {
         ourMs = (new Date().getTime() - eeTimerStart);
         eeTimerStart = new Date().getTime();
      
      } 
      if (eeTimerCnt == 5 && eeAdsLoaded == 1) {
         adMs = (new Date().getTime() - eeTimerStart);
         eeTimerStart = new Date().getTime();
      }
      if (eeTimerCnt == 6) {
         var omnitureMs = (new Date().getTime() - eeTimerStart);
         var img = document.createElement("img");
         img.src="/pageLoaded.jsp?url=" + eeEncode(document.location.href) + 
                 "&isNew=0" +
                 "&adMs=" + adMs + "&ourMs=" + ourMs + "&omnitureMs=" + omnitureMs + 
                 "&isSecure=0" + 
                 "&isExpertSkin=0" + 
                 "&isVS=0" + 
                 "&isUsingCDN=0" +
                 "&isUsingEELevel3CDN=1" +
                 "&isUsingEEDigitalWestCDN=0" +
                 "&isConsolidatedCSS=1"; 
         

         document.body.appendChild(img)
 

      }
  }
 
  // ]]>
  </script>
  <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<link rel="shortcut icon" href="/images/ee.ico" type="image/x-icon" />

<style type="text/css">@import "http://images.experts-exchange.com/getCSS?key=/00214/ee_NS|--base,xp--base,--component,xp--component,--formFactory,xp--formFactory,xp--button,xp-include-infoBox&t=1305667763000";</style>
<style type="text/css">@import "http://images.experts-exchange.com/getCSS?key=/00214/ee_NS|-jsp-search,xp-jsp-search,-include-searchResults,xp-include-searchResults,-include-collapsibleList2,xp-include-collapsibleList2,-include-eeAd,xp-include-questionScore,xp-include-infoBox,-include-questionList,xp-include-questionList,-include-welcomeBottomOverlay,xp-include-welcomeBottomOverlay&t=1305667762000";</style>
<script src="http://images.experts-exchange.com/00214/scripts/eeSubs_8fd303295f70c1423099a23823d4f8ba.js" type="text/javascript"></script>
<meta name="robots" content="noindex, nofollow" />
<title>Experts Exchange Search Results</title>
<script src="http://images.experts-exchange.com/00214/scripts/s_code_0a522bfb0687449fe5b609a65bced569.js" type="text/javascript"></script> </head>
<body style="height: 100%;" class="Safari" >

 <div id="minWidth"></div>
<div class="outerBodyWrap searchJSP">
  <div class="bodyWrap">
  <div id="outerWrap">
   <div id="innerWrap" class="innerWrap" style="">
   <div id="adInNav"><div id="pageHeader"
       class="lo">
        <!-- top options -->
        <ul class="navTopOptions">
<li class="navTopOptButton">
    <span class="ntoR">
      <a href="https://secure.experts-exchange.com/register.jsp?rsid=60&redirectURL=/tag/pthread.h%3FgSearch%3D0%26q%3D%26sfTag%3Dpthread.h%26searchSubmit%3D1">
        Start FREE Trial</a>
    </span>
  </li>
  <li class="navTopOptButton">
    <span class="ntoR">
      <a href="/contactUs.jsp?cid=2372">
        Contact Us</a>
    </span>
  </li>
<li class="navTopOptButton" id="navLoginLink">
    <span class="ntoR">
      <a href="javascript:toggleLoginBox();" rel="nofollow noindex">Member Login</a>
    </span>
  </li>
</ul>
<div id="loginPopup">
<form method="post" action="/login.jsp" onsubmit="return submitOnce(this);" name="loginPopupForm">
  <input type="hidden" name="msuLoginSubmit" value="1" />
  <div><label for="lpumsuLoginName">Username or Email:</label></div>
  <div><input type="text" id="lpumsuLoginName" name="msuLoginName" value="" class="text" tabindex="1" /></div>
  <div><label for="lpumsuPassword">Password:</label><a href="/forgotPassword.jsp" rel="nofollow" style="margin-left: 48px; text-decoration: none;">Forgot your password?</a></div>
  <div><input type="password" id="lpumsuPassword" name="msuPassword" value="" class="text" tabindex="2"  /></div>
  <div class="rMeBox">
    <div class="rMeInput">
      <label for="lpumsuRememberMe">Remember Me</label>
      <input type="checkbox" id="lpumsuRememberMe" name="msuRememberMe" class="checkbox" tabindex="3" />
    </div>
    <div class="rMeShared">Do not use on any shared computer</div>
  </div>
  <div class="clear"></div>
  <div class="nvpButtons">
   <input type="hidden" name="redirectURL" value="/tag/pthread.h?gSearch=0&amp;q=&amp;sfTag=pthread.h&amp;searchSubmit=1" />
   <div class="nvCancel"><a href="javascript:toggleLoginBox();" tabindex="5">Cancel</a></div>
   <button type="submit" class="lpbSubmit" tabIndex="4">Login</button>
   <div class="clear"></div>
  </div>
</form>
</div>
<script type="text/javascript">
var loginVisible = false;

function toggleLoginBox()
{
   var loginLink = document.getElementById("navLoginLink");
   var loginBox = document.getElementById("loginPopup");
   var obj = document.getElementById('firstDisplayCount');
   
   if (loginVisible)
   {
      closeLoginPopup();
      loginLink.className = "navTopOptButton";
      loginVisible = false;

      if (obj)
         obj.style.display = "inline";
   }
   else
   {
      showLoginPopup(loginLink);
      loginLink.className = "navTopOptButtonL";
      loginVisible = true;
      document.getElementById('lpumsuLoginName').focus();

      if (obj)
         obj.style.display = "none";
   }    
}
</script><div id="compPgHrLo"><a href="http://www.experts-exchange.com"  style="background-image:url(/xp/images/newNavLogo.png);">Experts Exchange</a></div>
<div class="clear"></div><div id="compPgHrNav"><ul class="ulloggedOut">
<li class="compPgHrNavAsk"><a href="/newQuestionWizard.jsp">Ask</a></li>
<li class="compPgHrNavSol"><a href="/solutions.jsp">Solutions</a></li>
<li class="compPgHrNavArt"><a href="/articles/">Articles</a></li>
<li class="compPgHrNavT"><a href="/testimonials.jsp">Testimonials</a></li>
<li class="compPgHrNavBlogs"><a href="/blogs/">Blogs</a></li>
<li class="compPgHrNavHlp"><a href="/help.jsp"><span>Help</span></a></li>
</ul>
<div class="clearLeft"></div>
<div id="compPgHrSrch" class="LO">
     <form class="formFactory" action="/simpleSearch.jsp" method="get" id="topSearchbox" onsubmit="postAsycOmnitureGoogleSearch('gSearch_1');">
<div class="eeSrchRadio">
       <input type="radio" class="radio" name="gSearch" value="1" id="gSearch_1" /><label for="gSearch_1" class="lblGoogle">Google</label> 
       <input type="radio" class="radio" name="gSearch" value="0" id="gSearch_0" checked="checked" /><label for="gSearch_0" class="lblEE">EE</label>
       </div>
       <div class="clear"></div>
       <div class="eeSrchFrm">
<input type="hidden" name="sfFreshSearch" value="1" />
       <input type="hidden" name="omnitureSearchType" value="EE Search" />
       <input type="hidden" name="cid" value="315" />
       <input type="hidden" name="redirectURL" value="/" />
       <input type="text" name="q" value="Enter Your Search Terms" class="textInput" id="gsearchBox" onfocus="clearSearchBoxRecolor(gsearchBox, 'Enter Your Search Terms', '#667B8A');" />
<input type="submit" class="tsSubmit" value="1" name="searchSubmit" />
       <a href="http://www.experts-exchange.com/advancedSearch.jsp" rel="nofollow" class="advSrchLink" title="Advanced Search">Advanced</a>
       </div>
</form>
   </div>
 <div class="clear"></div>
</div>
<div id="navAdBanner">
<div class="ontopBanner" id="navTopBanner">
  <div id="headerAdvertisementDivNoJump">
<div id="headerAdvertisementDiv">
  <script type="text/javascript">
    document.write('<p>loading advertisement ...</p>');
  </script>
  </div>
</div>
</div>
</div><div class="clear"></div>
<div id="compZnNav"><ul>
<li class="first" id="znBrowseAll"><a href="/allTopics.jsp">Browse All</a></li>
<li id="znMicroSoft"><a href="/Microsoft/" onclick="createNavMenu(this, 14466); return false;">Microsoft</a></li>
<li id="znApple"><a href="/Apple/" onclick="createNavMenu(this, 14554); return false;">Apple</a></li>
<li id="znDigiLiv"><a href="/Digital_Living/" onclick="createNavMenu(this, 14718); return false;">Digital Living</a></li>
<li id="znHardware"><a href="/Hardware/" onclick="createNavMenu(this, 15284); return false;">Hardware</a></li>
<li id="znSoftware"><a href="/Software/" onclick="createNavMenu(this, 12771); return false;">Software</a></li>
<li id="znOs"><a href="/OS/" onclick="createNavMenu(this, 14725); return false;">OS</a></li>
<li id="znStorage"><a href="/Storage/" onclick="createNavMenu(this, 14724); return false;">Storage</a></li>
<li id="znDatabase"><a href="/Database/" onclick="createNavMenu(this, 14726); return false;">Database</a></li>
<li id="znSecurity"><a href="/Security/" onclick="createNavMenu(this, 14727); return false;">Security</a></li>
<li id="znProg"><a href="/Programming/" onclick="createNavMenu(this, 15287); return false;">Programming</a></li>
<li id="znWebDev"><a href="/Web_Development/" onclick="createNavMenu(this, 14728); return false;">Web Development</a></li>
<li id="znNetwork"><a href="/Networking/" onclick="createNavMenu(this, 15285); return false;">Networking</a></li>
<li id="znOther"><a href="/Other/" onclick="createNavMenu(this, 15286); return false;">Other</a></li>
</ul><div class="clear"></div></div>
<div id="navMenuViewport" class="navMenuViewport" onmouseout="setHidNavMenu();" onmouseover="zoneNavSlideMenu.dontHide();"><div id="navMenuSlider" class="navMenuSlider"></div></div>
<script type="text/JavaScript">
var zoneNavSlideMenu = null;
var currNavA = null;
function createNavMenu(obj, zoneID)
{
   if (zoneNavSlideMenu &&
       zoneNavSlideMenu.menuLevels[0] != zoneID)
   {
      zoneNavSlideMenu.destroy();
      currNavA.className = "";
      zoneNavSlideMenu = null;
   }
   
   if (zoneNavSlideMenu == null)
   {
      zoneNavSlideMenu = new slidingMenuObject('navMenuViewport', 'navMenuSlider', 'zoneNavSlideMenu', 200, 20, zoneID, '/shared/async/navigationMenu.jsp?menuName=', 'N');
      zoneNavSlideMenu.anchorObj = obj;
   }
   zoneNavSlideMenu.show(obj, true);
   obj.className = "znM";
   currNavA = obj;
}

function setHidNavMenu()
{
   zoneNavSlideMenu.delayHide(currNavA);
}
</script></div>
      
      </div><div class="bodyContentWrap" id="bodyContentWrap">
   <div id="breadCrumb">
              <ul>
<li><a href="/">Experts Exchange</a></li>
<li class="currentPage">Search Results</li>
</ul>
</div>
            <div id="pageMain">
        <div id="minWidthMain"></div>

<div id="pageMainHeader">
</div>
           <!-- end -->
<!-- close the body column -->
      </div>
<a id="searchResults"></a>
      
<div class="s shFFF5 sb expGray sectionTwo searchResults"><table class="h"><tr><td class="l"></td><td><span class="sectionHeaderExtra"><form action="/simpleSearch.jsp" method="POST" id="firstDisplayForm" class="listDisplayCountForm"><div class="formRow"><span class="formName"><label for="firstDisplayCount">Display:</label>
</span><span class="formValue"><select id="firstDisplayCount" name="sfDisplayCount" style="width: 50px; height: 20px;">
  <option value="10" selected="selected">10</option>
  <option value="25">25</option>
  <option value="50">50</option>
  <option value="100">100</option>
  <option value="200">200</option>
</select>
</span></div><input type="hidden" name="eeRefTID" value="0" />
<input type="hidden" name="filterTypeID1" value="10" />
<input type="hidden" name="sfTerms1" value="pthread.h" />
<input type="hidden" name="sfIncludes1" value="sfIncludesTrue" />
<input type="hidden" name="sfTermType1" value="1" />
<input type="hidden" name="sfTermScope1" value="16" />
<input type="hidden" name="numRows" value="1" />
<input type="hidden" name="searchSubmit" value="true" />
</form>
<script type="text/javascript">
var firstDisplayCountSelect = document.getElementById('firstDisplayCount');
var firstDisplayCountForm = document.getElementById('firstDisplayForm');
initfirst();
function initfirst() {
   firstDisplayCountSelect.onchange = function(){if(firstDisplayCountForm.onsubmit == null || firstDisplayCountForm.onsubmit()){firstDisplayCountForm.submit();}}}
</script>
</span><div class="t2i" style="background: url(http://images.experts-exchange.com/t/410795-50.png) no-repeat;width:117px;height:20px;"></div></td><td class="r"></td></tr></table><div class="bl"><div class="br"><div class="bbg"><div class="empty"></div><div class="bc"><script type="text/javascript">
   /* form fields */
   var SEARCH_FORM_VALUE = 'sfValue';
   var NUM_ROWS = 'numRows';
   
   /*Multipes*/
   var INCLUDES_PARAMETER_TRUE = 'sfIncludesTrue';
   var INCLUDES_PARAMETER_ZONE_TRUE = 'sfIncludesZoneTrue';
   var INCLUDES_PARAMETER_ZONE_FALSE = 'sfIncludesZoneFalse';
   var INCLUDES_PARAMETER = 'sfIncludes';
   var VALUE  = 'sfValue';
   var SECONDARY_TYPE_ID  = 'sfSecondaryTypeID';   
   var EE_REFERENCE_TYPE_ID  = 'eeRefTID';
   
   /* Terms */
   var TERM_TYPE_PARAMETER  = 'sfTermType';
   var TERMS_PARAMETER  = 'sfTerms';
   var TERM_SCOPE_PARAMETER  = 'sfTermScope';
   var CONTAINMENT  = 'sfContainment';

   /* Member Options */
   var MEMBER_ACTIVITY_PARAMETER = 'sfMemberActivity';
   var MEMBER_TYPE_PARAM = 'sfMemberType';
   var MEMBER_TEXT = 'sfMemberText';
   var MEMBER_SEARCH_TYPE_MEMBER_ID = '20';

   /* Content Types */
   var EE_REFERENCE_TYPE_QUESTION = '10';
   var EE_REFERENCE_TYPE_ARTICLE = '30';
   var EE_REFERENCE_TYPE_IDS = 'eeRefTypeIDs'; 
   var EE_REFERENCE_TYPE_UNKNOWN = '0';  
   var QUESTION_CONTENT_TYPE = 'qct';
   var ARTICLE_CONTENT_TYPE = 'act';     
   
   /* Filter Types */
   var FILTER_TYPE  = 'filterTypeID';
   var FILTER_EE_REFERENCE_TYPE = '5';
   var FILTER_TYPE_TERMS  = '10'; 
   var FILTER_TYPE_MEMBERS  = '20'; 
   var FILTER_TYPE_ACTIVITY  = '30'; 
   var FILTER_TYPE_RATINGS  = '50'; 
   var FILTER_TYPE_TYPE  = '60';   
   var FILTER_TYPE_CATEGORY  = '90'; 
   var FILTER_TYPE_DISPLAY  = '70'; 
   var FILTER_TYPE_CUSTOM  = '80'; 
   var FILTER_TYPE_ZONES  = '40'; 
   var FILTER_TYPE_OLD_FILTERS = '100'; 
   /* Display Options */
   var COLLAPSED_COMMENT_OPTION  = 'collapsedCommentOption';
    
   /* Activity Type */
   var ACTIVITY_PARAMETER = 'sfActivityType';
   var TIME_FRAME_PARAMETER = 'sfTimeFrame';
   var START_DATE_PARAMETER = 'sfStartDate';
   var END_DATE_PARAMETER = 'sfEndDate';
   var CUSTOM_DATES = '80';

   /* Ratings Type */
   var RATING_OPTION = 'sfRatingOptions';
   var RATING_TEXT_PARAM = 'sfRatingText';
   var COMPARISON = 'sfComparison';
   var GRADE = 'sfGrade';
   var POINTS = '10';

   /* Type Type */
   var TYPE_PARAM = 'sfTypeParam';
   var NEGLECTED_ON_TOP = 'sfNeglectedOnTop';

   var IN_PROGRESS = '20';
   var AWAITING_AUTHOR_RESPONSE = '30';
   var AWAITING_EXPERT_RESPONSE = '40';
   var CLOSURE_REQUESTED = '60';

   /* Category Type */
   var CATEGORY_PARAM = 'sfCategoryParam';
   
   /* Display Type */
   var NUM_RESULTS_PER_PAGE_PARAMETER = 'sfDisplayCount';
   var DISPLAY_LIST_PARAM = 'sfListDisplay'; 
   var DISPLAY_LIST_PARAM_CHECKED = '10';
   var LIST_DISPLAY_PARAM = 'sfListDisplay';

   /* Custom Type */
   var CUSTOM_QUERY_PARAMETER = 'ssfCustomQuery';

   /* TA Type */
   var ROOT_ZONE = '10';
   var TA = '20';
   var FAV_TA_PARAM = 'ssfFavTA';
   var TA_FILTER_PARAMETER = 'TAFilterID';
   var ZONE_ID_PARAMETER = 'sfZoneID';
   var ZONE_SELECTION_PARAMETER = 'zoneSelection';
   var ZONES_SEARCH = 'allZones';

   /* Saved Search */
   var TITLE_PARAMETER = 'ssfTitle';

   /* Sorting */
   var SORT_FIELD = 'sfSortField';
   var SORT_ORDER = 'sfSortOrder';
   var QUESTION_SCORE = '24';
   var SORT_DESCENDING = '-1';
   var ACCEPT_DATE_SORT = '25';
</script>
<script type="text/javascript">
     jsImport("com.ee.list.CollapsibleList");
     function collapseSection(elementID)
     {
        var section = document.getElementById(elementID);
        if (section.className.indexOf("collapsed") >= 0)
        {
           removeClass(section, "collapsed");
        } 
        else
        {
           addClass(section, "collapsed");
        }
     }
     
   </script>
   <script src="http://images.experts-exchange.com/00214/scripts/filterSelector_4d1557693f29439a3ee373d1625ea226.js" type="text/javascript"></script>
<form method="post" action="/simpleSearch.jsp" class="formFactory" onsubmit="return submitOnce(this);">
     <div class="simpleSearchBox">
       <div class="searchLabel">
            Enter Keywords:
       </div>
       <div class="clear"></div>
       <div class="searchInputs">
         <div id="dummyContentSelect" style="display: none;">
           <select id="dummySelect" name="dummySelect" class="dummySelect" disabled  style="width: 100px; height: 20px;">
  <option value="0" selected="selected">Everything</option>
  <option value="10">Questions</option>
  <option value="30">Articles</option>
</select>
</div>
         <div id="dummySearchInput" class="topSearch dummySearch" style="display: none;">
           <input type="text" readonly="readonly" id="dummyInput" name="dummyInput" value="pthread.h" class="text readOnly" style="width: 566px; height: 18px;" />
</div>
         
         <div id="searchContentSelect">
           <select id="contentTypeSelect" name="eeRefTID" class="contentTypeSelect"onchange="var eeRefType = document.getElementById('eeRefTID'); if (eeRefType != null) { eeRefType.value = this.value; } var dummyContentSelect = document.getElementById('dummySelect'); if (dummyContentSelect != null) {dummyContentSelect.selectedIndex = this.selectedIndex;} var advancedContentSelect = document.getElementById('contentTypeSelectAdvanced'); if (advancedContentSelect != null) { advancedContentSelect.selectedIndex = this.selectedIndex; }callAsync('/shared/async/filters/filterOptions.jsp?eeRefTID=' + this.value,'selectedFilters');" style="width: 100px; height: 20px;">
  <option value="0" selected="selected">Everything</option>
  <option value="10">Questions</option>
  <option value="30">Articles</option>
</select>
</div>
         <div id="searchQueryInput">
         <input type="text" id="q" name="q" value="pthread.h" class="text" style="width: 566px; height: 18px;" />
</div>
         <input type="hidden" id="simpleEERefTID" name="eeRefTID" value="0" />
</div>
       <div class="searchButtons">
       <a id="showSearchFilters" class="toggleFilterLink" href="#" onclick="clearFilters('0'); return false;">Advanced [+]</a>
         <div id="hideFilter" style="display: none;">
            <a class="hideSearchFilterLink toggleFilterLink" href="#" onclick="clearFilters('0'); return false;">Advanced [-]</a>
         </div>
         <input type="hidden" name="searchSubmit" value="1"/><div id="performSearchButton" class="performSearchButton bTypeButton bGlowSmall bGlowSmallLime"><button class="bTextSEARCH" type="submit"><div class="glowLeft"></div><div class="glowMiddle" style="padding-left: 26px; padding-right: 27px;"><div class="t2i" style="background: url(http://t2i.experts-exchange.com/t/414515-50.png) no-repeat;width:37px;height:12px;"></div></div><div class="glowRight"></div></button></div><input type="hidden" name="cid" value="322" />
<input type="hidden" name="gSearch" value="0" />
<input type="hidden" name="view" value="0" />
</div>
     </div>
   </form>
   
<form name="searchForm" method="post" action="/simpleSearch.jsp" class="formFactory" onsubmit="return submitOnce(this);">
<script type="text/javascript">setTextInput("pthread.h");</script>
         
         <div id="advOptions" style="display:none;">
         <div class="filterOptions">
    <span class="advancedFiltersLabel">
      Advanced Filters
    </span>
    <span class="includeResultsForLabel">
        (Include results for)
    </span>
    <div class="filterOptionsHeader">
      <span id="filterLinks">
        <select id="contentTypeSelectAdvanced" name="eeRefTID" class="contentTypeSelectAdvanced"onchange="callAsync('/shared/async/filters/filterOptions.jsp?eeRefTID=' + this.value,'selectedFilters');" style="width: 130px; height: 20px;">
  <option value="0" selected="selected">Everything</option>
  <option value="10">Questions</option>
  <option value="30">Articles</option>
</select>
</span>
    </div>
    <div id="selectedFilters">
      <input type="hidden" name="omnitureSearchType" value="Filter Search" />
<input type="hidden" id="eeRefTID" name="eeRefTID" value="0" />
<div id="rowsSection">
<div id="rowNumber1">
                     <div class="formRow">
       <div class="bTypeA bGlowSmall bGlowSmallRed"><a class="bTextX" href="javascript:deleteRow('rowNumber1');"><div class="glowLeft"></div><div class="glowMiddle" style="padding-left: 3px; padding-right: 2px;"><div class="t2i" style="background: url(http://t2i.experts-exchange.com/t/423101-50.png) no-repeat;width:7px;height:12px;"></div></div><div class="glowRight"></div></a></div><select name="options" onchange="changeRow(1,0, this.value);"; style="width: 130px; height: 20px;">
  <option value="0">Filter By</option>
  <option value="10" selected="selected">Terms</option>
  <option value="20">Members</option>
  <option value="30">Activity</option>
  <option value="40">Zones</option>
  <option value="70">Display</option>
  <option value="80">Custom Query</option>
</select>
<span id="innerRowNumber1">
        <input type="hidden" id="filterTypeID1" name="filterTypeID1" value="10" />
<select id="sfIncludes1" name="sfIncludes1" style="width: 70px; height: 20px;">
  <option value="sfIncludesTrue" selected="selected">Includes</option>
  <option value="sfIncludesFalse">Excludes</option>
</select>
<select id="sfTermType1" name="sfTermType1" style="width: 180px; height: 20px;">
  <option value="1" selected="selected">All these Terms</option>
  <option value="2">Any of these Terms</option>
  <option value="3">This Exact Phrase</option>
</select>
<input type="text" id="sfTerms1" name="sfTerms1" value="pthread.h" class="text termsInput" style="width: 280px; height: 18px;" />
<select id="sfTermScope1" name="sfTermScope1" class="rightEdge" style="width: 180px; height: 20px;">
  <option value="5">Any part</option>
  <option value="0">Title</option>
  <option value="16" selected="selected">Tags</option>
  <option value="1">Body</option>
  <option value="3">Zone Name</option>
  <option value="26">Comments</option>
  <option value="62">Admin Comments</option>
  <option value="22">Code Snippet</option>
  <option value="28">File Name</option>
</select>
</span>
   </div></div>
</div>

<input type="hidden" id="numRowsID" name="numRows" value="2" />
<div class="formRow">
      <div class="addNewFilterButton bTypeA bGlowSmall bGlowSmallBlue"><a class="bTextADDNEWFILTER" href="javascript:addRow(0);"><div class="glowLeft"></div><div class="glowMiddle" style="padding-left: -14px; padding-right: -14px;"><div class="t2i" style="background: url(http://t2i.experts-exchange.com/t/423102-50.png) no-repeat;width:78px;height:12px;"></div></div><div class="glowRight"></div></a></div><input type="hidden" name="searchSubmit" value="1"/><div class="runFilterButton bTypeButton bGlowSmall bGlowSmallLime"><button class="bTextRUNFILTER" type="submit"><div class="glowLeft"></div><div class="glowMiddle" style="padding-left: 15px; padding-right: 15px;"><div class="t2i" style="background: url(http://t2i.experts-exchange.com/t/423103-50.png) no-repeat;width:52px;height:12px;"></div></div><div class="glowRight"></div></button></div></div></div>
  </div>
</div>
<input type="hidden" name="view" value="0" />
</form>
   <div class="hr2 topHr"></div>
   <div class="allResults">
   
   <div class="clear"></div>
   <div class="rightSection">
      <div id="newQuestionHeader" class="rightSectionHeader">
      <span class="rightSectionTitle">Can't Find a Solution</span>
      <a href="javascript:void(0);" onclick="CL('AskNewQustion'); collapseSection('newQuestionHeader')" class="collapseButton" id="cCAskNewQustion"></a>
    </div>
    
    <div id="cAAskNewQustion" class="s sideSection shF5FF sgray expGray"><table class="h"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table><div class="bl"><div class="br"><div class="bbg"><div class="empty"></div><div class="bc"><div class="askNewQuestionButton bTypeA bGlowSmall bGlowSmallBlue"><a class="bTextAskANewQuestion" href="/newQuestionWizard.jsp" onclick="return anyAbandoned(false, false, false, 0, 'Safari'); "><div class="glowLeft"></div><div class="glowMiddle" style="padding-left: 2px; padding-right: 1px;"><div class="t2i" style="background: url(http://t2i.experts-exchange.com/t/423104-50.png) no-repeat;width:105px;height:12px;"></div></div><div class="glowRight"></div></a></div><div class="askANewQuestion"></div>
    <div class="clear"></div>
    </div><div class="empty"></div></div></div></div><table class="f"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table></div><div id="searchWithinHeader" class="rightSectionHeader">
      <span class="rightSectionTitle">Search Within Results</span>
      <a href="javascript:void(0);" onclick="CL('SearchWithin'); collapseSection('searchWithinHeader');" class="collapseButton" id="cCSearchWithin"></a>
    </div>
    <div id="cASearchWithin" class="s sideSection shF5FF sgray expGray searchWithin"><table class="h"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table><div class="bl"><div class="br"><div class="bbg"><div class="empty"></div><div class="bc"><form method="post" action="/simpleSearch.jsp" class="formFactory" onsubmit="return submitOnce(this);">

          <div class="formRow rightEdgeButton">
          <input type="text" id="delimitQuery" name="delimitQuery" value="" class="text" style="width: 86px; height: 18px;" />
<input type="hidden" name="searchSubmit" value="1"/><div class="searchWithinButton bTypeButton bGlowSmall bGlowSmallBlue"><button class="bTextSEARCH" type="submit"><div class="glowLeft"></div><div class="glowMiddle" style="padding-left: 15px; padding-right: 15px;"><div class="t2i" style="background: url(http://t2i.experts-exchange.com/t/414515-50.png) no-repeat;width:37px;height:12px;"></div></div><div class="glowRight"></div></button></div></div>
   
<input type="hidden" name="eeRefTID" value="0" />
<input type="hidden" name="filterTypeID1" value="10" />
<input type="hidden" name="sfTerms1" value="pthread.h" />
<input type="hidden" name="sfIncludes1" value="sfIncludesTrue" />
<input type="hidden" name="sfTermType1" value="1" />
<input type="hidden" name="sfTermScope1" value="16" />
<input type="hidden" name="numRows" value="1" />
<input type="hidden" name="cid" value="323" />
</form> 
     </div><div class="empty"></div></div></div></div><table class="f"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table></div><div id="narrowResultsHeader" class="rightSectionHeader">
      <span class="rightSectionTitle">Narrow Results</span>
      <a href="javascript:void(0);" onclick="CL('NarrowResults'); collapseSection('narrowResultsHeader');" class="collapseButton" id="cCNarrowResults"></a>
    </div>

<div id="cANarrowResults" class="s sideSection shF5FF sgray expGray"><table class="h"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table><div class="bl"><div class="br"><div class="bbg"><div class="empty"></div><div class="bc"><form method="POST" action="/simpleSearch.jsp" id="eeTagForm" class="formFactory"><input type="hidden" name="eeRefTID" value="0" />
<input type="hidden" name="filterTypeID1" value="10" />
<input type="hidden" name="sfTerms1" value="pthread.h" />
<input type="hidden" name="sfIncludes1" value="sfIncludesTrue" />
<input type="hidden" name="sfTermType1" value="1" />
<input type="hidden" name="sfTermScope1" value="16" />
<input type="hidden" name="numRows" value="1" />
</form><div class="searchTagCloud">
           <div class="rightSectionSubHeader tagHeader">Tag</div>
           <table class="cloudContainer"><tr><td><div class="cloud">
<span class="tag tagSize2"> <a class="tagColor 2"  rel="tag noindex nofollow"  href="javascript:void(0);" onclick="return submitSearchForm('eeTagForm',new Array('sfTag','searchSubmit'),new Array('download','1'));">download</a></span>
<div class="hr narrowBySectionHr"></div>
</div></td></tr></table></div>
<div class="zoneResults">
        <div class="rightSectionSubHeader zoneHeader">Zone</div>
<form method="POST" action="/simpleSearch.jsp" id="searchForm" class="formFactory"><input type="hidden" name="eeRefTID" value="0" />
<input type="hidden" name="filterTypeID1" value="10" />
<input type="hidden" name="sfTerms1" value="pthread.h" />
<input type="hidden" name="sfIncludes1" value="sfIncludesTrue" />
<input type="hidden" name="sfTermType1" value="1" />
<input type="hidden" name="sfTermScope1" value="16" />
<input type="hidden" name="numRows" value="1" />
<input type="hidden" name="searchSubmit" value="true" />
</form><div class="zoneResultColumn first">
<span class="zone">
              <a href="javascript:void(0);" onclick="return submitSearchForm('searchForm',new Array('sfWithinTA','searchSubmit','showFilters'),new Array('84','1','1'));">C++ Programming Language</a></span>
          
          <span class="results">2</span>
          <div class="clear"></div>
          <div class="hr"></div>
</div>
</div>
</div><div class="empty"></div></div></div></div><table class="f"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table></div><div class="s sideSection shF5FF sgray expGray eeAD"><table class="h"><tr><td class="l"></td><td><div class="t2i" style="background: url(http://images.experts-exchange.com/t/387127-50.png) no-repeat;width:16px;height:16px;"></div></td><td class="r"></td></tr></table><div class="bl"><div class="br"><div class="bbg"><div class="empty"></div><div class="bc"><div class="eeAd">
          <a href="/discoverBusinessAccounts.jsp?step=about&cid=894#top" class="eeAdSection corporate9"></a>
       </div>
       
       </div><div class="empty"></div></div></div></div><table class="f"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table></div></div>
   <a name="allResults"></a>
<div class="leftSection">
      <div class="resultSummary">
        <div class="resultCount">
          <span><span class="count">1 - 2</span> of <span class="count">2</span></span> <span class="searchTime">(0 seconds)</span>
        </div>
      <form method="POST" action="/simpleSearch.jsp" id="searchWithinForm" class="formFactory"><input type="hidden" name="eeRefTID" value="0" />
<input type="hidden" name="filterTypeID1" value="10" />
<input type="hidden" name="sfTerms1" value="pthread.h" />
<input type="hidden" name="sfIncludes1" value="sfIncludesTrue" />
<input type="hidden" name="sfTermType1" value="1" />
<input type="hidden" name="sfTermScope1" value="16" />
<input type="hidden" name="numRows" value="1" />
<input type="hidden" name="searchSubmit" value="true" />
</form><div class="formFactory sortBy">
          <span class="sortByTitle">Sort By: </span>
          <select id="sortBySelectID" name="sortBySelect" class="sortBySelect"onchange="return submitSearchForm('searchWithinForm', getSortNames(this.value), getSortValues(this.value));" style="width: 94px; height: 20px;">
  <option value="0" selected="selected">Relevance</option>
  <option value="24">Rating</option>
  <option value="25">Date</option>
</select>
</div> 
   </div>
    <div class="clear resultsClear"></div>
    <div class="searchResultList">
 <div class="s snhF5FF expGray"><table class="h"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table><div class="bl"><div class="br"><div class="bbg"><div class="empty"></div><div class="bc"><div class="searchResult on" onmouseover="addClass(this, 'articleIcon');" onmouseout="removeClass(this, 'articleIcon');">
<div class="searchTitle">
        <span class="article">
           <span class="check"></span>
        <div class="titleString ">
           <a href="/Programming/Languages/CPP/A_2616-POSIX-Threads-Programming-in-C.html?sfQueryTermInfo=1+10+30+pthread.h">
          POSIX Threads Programming in C++        </a><a href="/Programming/Languages/CPP/A_2616-POSIX-Threads-Programming-in-C.html?sfQueryTermInfo=1+10+30+pthread.h" class="icon"></a>
        </div>
        </span>
        <div class="tags">
              Tags: <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/C%2B%2B#allResults">C++</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/C#allResults">C</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/POSIX#allResults">POSIX</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/Threads#allResults">Threads</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/Linux#allResults">Linux</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/pthread_create#allResults">pthread_create</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/pthread.h#allResults"><span class="searchTerm">pthread.h</span></a></div>
        <div class="clearLeft"></div> 
      </div>
      <div class="description">
         Written by John Humphreys

C++ Threading and the POSIX Library

This article will cover the basic information that you need to know in order to make use of the POSIX threading library availabl...</div>
      <div class="link">
        <a href="/Programming/Languages/CPP/A_2616-POSIX-Threads-Programming-in-C.html?sfQueryTermInfo=1+10+30+pthread.h"/>http://www.experts-exchange.com/Programming/Languages/CPP/A_2616-POSIX-Threads-Programming...</a></div>
<div class="questionInfo">
        Zones: <span class="zones"><a href="/Programming/Languages/CPP/" title="C++">C++</a></span><span class="rightInfo">Date Created: <span class="info">03/05/2010</span> Comments: <span class="info">12</span></span></div>
    </div>
    <div class="hr2"></div>
    <div class="searchResult" onmouseover="addClass(this, 'solutionIcon');" onmouseout="removeClass(this, 'solutionIcon');">
<div class="searchTitle">
        <span class="solution">
           <span class="check"></span>
        <div class="titleString ">
           <a href="/Programming/Languages/CPP/Q_20722466.html?sfQueryTermInfo=1+10+30+pthread.h">
          <span class="searchTerm">pthread.h</span>        </a><a href="/Programming/Languages/CPP/Q_20722466.html?sfQueryTermInfo=1+10+30+pthread.h" class="icon"></a>
        </div>
        </span>
        <div class="tags">
              Tags: <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/download#allResults">download</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/pthread.h#allResults"><span class="searchTerm">pthread.h</span></a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/pthread#allResults">pthread</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/h#allResults">h</a>, <a link="rel" rel="tag noindex nofollow" href="http://www.experts-exchange.com/tag/.h#allResults">.h</a></div>
        <div class="clearLeft"></div> 
      </div>
      <div class="description">
        Hi All,

&nbsp; &nbsp;I have an application tha uses &lt;<span class="searchTerm">pthread.h</span>&gt; file. I do not have that file on my system. Can anybody give it to me... u can paste the code (if it not too big) here or you can mail it to...</div>
      <div class="link">
        <a href="/Programming/Languages/CPP/Q_20722466.html?sfQueryTermInfo=1+10+30+pthread.h"/>http://www.experts-exchange.com/Programming/Languages/CPP/Q_20722466.html</a></div>
<div class="questionInfo">
        Zones: <span class="zones"><a href="/Programming/Languages/CPP/" title="C++">C++</a></span><span class="rightInfo">Date Answered: <span class="info">02/01/2004</span> Rating: <span class="info">8.0</span> Views: <span class="info">175</span></span></div>
    </div>
    <div class="hr2"></div>
    </div><div class="empty"></div></div></div></div><table class="f"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table></div><div class="clear bottomClear"></div>
</div>
 <div class="views">
 <form action="/simpleSearch.jsp" method="POST" id="secondDisplayForm" class="listDisplayCountForm"><div class="formRow"><span class="formName"><label for="secondDisplayCount">Views per Page:</label>
</span><span class="formValue"><select id="secondDisplayCount" name="sfDisplayCount" style="width: 50px; height: 20px;">
  <option value="10" selected="selected">10</option>
  <option value="25">25</option>
  <option value="50">50</option>
  <option value="100">100</option>
  <option value="200">200</option>
</select>
</span></div><input type="hidden" name="eeRefTID" value="0" />
<input type="hidden" name="filterTypeID1" value="10" />
<input type="hidden" name="sfTerms1" value="pthread.h" />
<input type="hidden" name="sfIncludes1" value="sfIncludesTrue" />
<input type="hidden" name="sfTermType1" value="1" />
<input type="hidden" name="sfTermScope1" value="16" />
<input type="hidden" name="numRows" value="1" />
<input type="hidden" name="searchSubmit" value="true" />
</form>
<script type="text/javascript">
var secondDisplayCountSelect = document.getElementById('secondDisplayCount');
var secondDisplayCountForm = document.getElementById('secondDisplayForm');
initsecond();
function initsecond() {
   secondDisplayCountSelect.onchange = function(){if(secondDisplayCountForm.onsubmit == null || secondDisplayCountForm.onsubmit()){secondDisplayCountForm.submit();}}}
</script>
</div>
<div class="resultNavBottom">
  <div class="resultNav">
     <form method="POST" action="/simpleSearch.jsp" id="resultListPageForm" class="formFactory"><input type="hidden" name="eeRefTID" value="0" />
<input type="hidden" name="filterTypeID1" value="10" />
<input type="hidden" name="sfTerms1" value="pthread.h" />
<input type="hidden" name="sfIncludes1" value="sfIncludesTrue" />
<input type="hidden" name="sfTermType1" value="1" />
<input type="hidden" name="sfTermScope1" value="16" />
<input type="hidden" name="numRows" value="1" />
<input type="hidden" name="searchSubmit" value="true" />
</form><ul>
  <li class="currentPage first">1</li>
</ul>
</div>
</div>
</div>
 <div class="clear"></div>
</div>

 </div><div class="empty"></div></div></div></div><table class="f"><tr><td class="l"></td><td>&nbsp;</td><td class="r"></td></tr></table></div><div id="pageFooter">
<ul id="compFL">
<li><a href="/shortenURL.jsp">Shorten URL</a>|</li>
  <li><a href="/help.jsp">Help</a>|</li>
  <li><a href="/aboutUs.jsp">About Us</a>|</li>
  <li><a href="/contactUs.jsp">Contact Us</a>|</li>
  <li><a href="/termsOfUse.jsp">Terms of Use</a>|</li>
  <li><a href="/blogs/EE-Team">EE Blog</a>|</li>
<li><a href="http://www.alexa.com/siteinfo/experts-exchange.com" title="Internet Rank" target="_blank">Internet Rank</a>|</li>
  <li><a href="/privacyPolicy.jsp">Privacy Policy</a>|</li>
  <li class="lastInList"><a href="/siteMap.jsp">Site Map</a></li>
</ul>
<script type="text/javascript">
  if (hideCompFLOnCreate == true)
    document.getElementById('compFL').style.visibility = 'hidden';
</script><div id="compCpyr">
<div class="t2i" style="background: url(http://t2i.experts-exchange.com/t/443347-50.png) no-repeat;width:306px;height:11px;"></div></div>
<div id="compCpyrText"><span class="noWrap">Copyright © 1996 - 2011 Experts Exchange, LLC.</span> <span class="noWrap">All rights reserved.</span></div>
<div class="expertBaseFooter">
   <a target="_blank" href="http://www.expertbase.com?cid=2858">
   <div class="expertBaseFooterContentLeft"></div>
   </a>
   <div class="expertBaseFooterContentRight">
      <div class="anExpert">An <span class="expertBoldText">expert</span>base <a onmouseover="this.style.color='#FF7A00';" onmouseout="this.style.color='#454547';" class=onlineCommunity target="_blank" href="http://www.expertbase.com?cid=2858">online community</a>.</div>
      <div class="engageCustomers">ENGAGE CUSTOMERS. DRIVE TRAFFIC.</div>
   </div>
   <div class="clear"></div>
</div>
</div><div id="bodyFooterSpacer"></div></div></div></div></div></div>


<script type="text/javascript">eeAdsLoaded = 1</script>
<script type="text/javascript" src="http://images.experts-exchange.com/00214/timer/timer1.js"></script>
<script type="text/javascript" src="http://images.experts-exchange.com/00214/timer/timer2.js"></script>
<script type="text/javascript" src="http://images.experts-exchange.com/00214/timer/timer3.js"></script>
<script type="text/javascript" src="http://images.experts-exchange.com/00214/timer/timer4.js"></script>
<img src="/timer/timer1.gif" onload="endEETimer()" />
<img src="/timer/timer2.gif" onload="endEETimer()" />
<img src="/timer/timer3.gif" onload="endEETimer()" />
<img src="/timer/timer4.gif" onload="endEETimer()" />

<img src="/timer/timer5.gif" onload="endEETimer()" />
<img src="/timer/timer6.gif" onload="endEETimer()" />

   <script type="text/javascript">
       
      resizeFooter(false);
   </script>

<!-- SiteCatalyst code version: H.7.
Copyright 1997-2006 Omniture, Inc. More info available at
http://www.omniture.com -->
<script language="JavaScript"><!--
/* You may give each page an identifying name, server, and channel on
the next lines. */
var s = setUpOmnitureVariable();
s.pageName="LO-Search Page";
s.server="www1.experts-exchange.com";
s.channel="tag";
s.pageType="";
s.prop1=document.title;
s.prop2="";
s.prop3="";
s.prop4="";
s.prop5="";
s.prop6="";
s.prop7=""; // TODO THIS IS NO LONGER PROP 7, SHOULD BE BLOG AUTHOR
s.prop8="";
s.prop9="";
s.prop10="";
s.prop11="";
s.prop12="";
s.prop14="";
s.prop15="50";
s.prop16="";
s.prop17="";
s.prop18="";
s.prop19="";
s.prop20="";
s.prop21="";
s.prop22="";
s.prop23="";
s.prop24="";
s.prop25="";
s.prop26="";
s.prop27="";
s.prop28="";
s.prop29="";
s.prop30="";
s.prop31="";
s.prop32="";
s.prop33="";
s.prop34="";
s.prop35="";
s.prop36="";
s.prop37="";
s.prop38="";
s.prop39="";
s.prop40="";
s.prop41="";
s.prop42="";
s.prop43="";
s.prop44="";
s.prop45="";
s.prop46="";
s.prop47="";
s.prop48="";
s.prop49="";
s.prop50="";
/* E-commerce Variables */
s.campaign="";
s.events="event4";
s.products="";
s.state="";
s.zip="";
s.purchaseID="";
s.eVar1=document.title;
s.eVar2="";
s.eVar3="";
s.eVar4="";
s.eVar5="";
s.eVar6="Free Trial Guest";
s.eVar7="";
s.eVar8="";
s.eVar9="";
s.eVar10="";
s.eVar14="Refined Search";
s.eVar15="";
s.eVar16="";
s.eVar17="";
s.eVar18="";
s.eVar19="";
s.eVar20="";
s.eVar21="";
s.eVar22="";
s.eVar23="";
s.eVar24="";
s.eVar25="VM Text Ads";
s.eVar26="";
s.eVar27="";
s.eVar28="";
s.eVar29="4PM";
s.eVar30="Saturday";
s.eVar31="187.64.55.218";
s.eVar32="";
s.eVar33="";
s.eVar34="";
s.eVar35="";
s.eVar36="Subscription BP Update";
s.eVar37="Control"; 
s.eVar38="";
s.eVar39="";
s.eVar40="";
s.eVar41="";
s.eVar42="";
s.eVar43="";
s.eVar44="";
s.eVar45="EE_Level3 - 2";
s.eVar46="";
s.eVar47="";
s.eVar48="";
s.eVar49="";


/* Hierarchy Variables */
s.hier1="tag";
/************* DO NOT ALTER ANYTHING BELOW THIS LINE ! **************/
var s_code=s.t();if(s_code)document.write(s_code)//--></script>
<script language="JavaScript"><!--
if(navigator.appVersion.indexOf('MSIE')>=0)document.write(unescape('%3C')+'\!-'+'-')
//--></script><noscript><img
src="http://metrics.experts-exchange.com/b/ss/eexchangeprod/1/H.7--NS/0"
height="1" width="1" border="0" alt="" /></noscript><!--/DO NOT REMOVE/-->
<!-- End SiteCatalyst code version: H.7. -->
<script language="JavaScript">
   function getOmnitureFunction()
   {
      var s2 = setUpOmnitureVariable();
      s2.pageName="LO-Search Page";
      s2.server="www1.experts-exchange.com";
      s2.channel="tag";
      s2.pageType="";
      s2.prop1=document.title;
      s2.prop2="";
      s2.prop11="";
      s2.prop15="50";
      s2.prop31="";
      /* E-commerce Variables */
      s2.campaign="";
      s2.events="event4";
      s2.eVar1=document.title;
      s2.eVar6="Free Trial Guest";
      s2.eVar7="";
      s2.eVar20="";
      s2.eVar26="";
      s2.eVar29="4PM";
      s2.eVar30="Saturday";
      s2.eVar31="187.64.55.218";
      s2.eVar33="";
      s2.eVar34="";
      s2.eVar35="";
      s2.eVar36="Subscription BP Update";
      s2.eVar45="EE_Level3 - 2";
      s2.eVar47="";
      s2.eVar37="";
      
      return s2;
   }
   
   function postToOmniture(s2)
   {
      if (!s2)
         s2 = getOmnitureFunction();
      
      /************* DO NOT ALTER ANYTHING BELOW THIS LINE ! **************/
      var s_code=s2.t();
   }
   
   function postAsycOmnitureGoogleSearch(elementID)
   {
      var googleRadionButton = document.getElementById(elementID);
      if ((googleRadionButton.type == 'radio' && googleRadionButton.checked) ||
          (googleRadionButton.type == 'hidden' && googleRadionButton.value == '1'))
      {
         var s2 = getOmnitureFunction();
         s2.prop39 = 'Google Search';
         s2.evar14 = 'Google Search';
         postToOmniture(s2);
      }
   }

   function postAsyncOmnitureProp(prop, value)
   {
      var s2 = getOmnitureFunction();
      eval("s2." + prop + " = '" + value + "';");
      //alert("s2." + prop + " = '" + value + "';");
      postToOmniture(s2); 
   }

   function postAsyncOmnitureEvent(value)
   {
      var s2 = getOmnitureFunction();
      eval("s2.events = '"+value+"';");
      postToOmniture(s2); 
   }
   
   function postAsycOmniturePageName(pName)
   {
      if (pName != null)
      {
         var s2 = getOmnitureFunction();
         s2.pageName = pName;
         postToOmniture(s2);
      }
   }
   
   function postAsycOmnitureEVarAndProp(eVarName, eVarValue, propName, propValue, pName)
   {
      var s2 = getOmnitureFunction();
      var doPost = false;
      if ( (eVarName != null) && (eVarValue != null) )
      {
         eval("s2." + eVarName + " = '" + eVarValue + "';");
         doPost = true;
      }
      if ( (propName != null) && (propValue != null) )
      {
         eval("s2." + propName + " = '" + propValue + "';");
         doPost = true;
      }
      if (pName != null)
      {
         s2.pageName = pName;
         doPost = true;
      }

      if (doPost)
         postToOmniture(s2);
   }

   function postAsycOmnitureEVarAndEvent(eVarName, eVarValue, eventName, eventValue, pName)
   {
      var s2 = getOmnitureFunction();
      var doPost = false;
      if ( (eVarName != null) && (eVarValue != null) )
      {
         eval("s2." + eVarName + " = '" + eVarValue + "';");
         doPost = true;
      }
      if ( (eventName != null) && (eventValue != null) )
      {
         eval("s2." + eventName + " = '" + eventValue + "';");
         doPost = true;
      }
      if (pName != null)
      {
         s2.pageName = pName;
         doPost = true;
      }

      if (doPost)
         postToOmniture(s2);
   }
</script>
<script type="text/javascript">
// <![CDATA[

addCookie('EECC_0', 'CLOAD=2&EEMYI=true&EETEMPA=3f987d6af256d8e8b662e5c86a3e3d4cd06c26f8&EXP=SUB_BPf&SCMESS=0&EESESSIONS=1&EEPAGES=5&SEID=10', 2147483647);

addCookie('DLC', 'srp=10', 2147483647);

// ]]>
</script>

<script type="text/javascript">
  var fakeLocations = new Array();
  var realLocations = new Array();
</script>
<div id="headerAdvertisementDivTemp" style="display: none;">
    <script type="text/javascript">
      fakeLocations[fakeLocations.length] = 'headerAdvertisementDivTemp';
      realLocations[realLocations.length] = 'headerAdvertisementDiv';
      document.write('<scr'+'ipt type="text/javascript">\n//<![CDATA[\nord = window.ord || Math.floor(Math.random()*1E16);\ndocument.write( \'<scri\'+\'pt type="text/javascript" src="http://ad4.netshelter.net/adj/ns.expertsexchange/ros;ppos=ATF;kw=;tile=1;dcopt=ist;sz=728x90;ord=\' + ord + \'?"></scr\'+\'ipt>\');\n//]]>\n</scr'+'ipt>');
    </script>
  </div>

<script type="text/javascript">
for (var i=0; i<fakeLocations.length; i++)
{
   var fakeLocation = document.getElementById(fakeLocations[i]);
   var realLocation = document.getElementById(realLocations[i]);

   if (fakeLocation && realLocation)
   {
      for (index = fakeLocation.childNodes.length - 1; index >= 0; index--)
      {
         var element = fakeLocation.childNodes[index];
         if ((element.nodeName == 'SCRIPT') || (element.nodeName == 'NOSCRIPT'))
            fakeLocation.removeChild(element);
      }
   
      fakeLocation.parentNode.removeChild(fakeLocation);
      realLocation.parentNode.replaceChild(fakeLocation, realLocation);
      fakeLocation.style.display = 'block';
   }
}
</script>
</body>

</html>

<!-- www1.experts-exchange.com:80 -->
