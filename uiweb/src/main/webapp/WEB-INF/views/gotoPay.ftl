<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Pragma" content="no-cache" />  
<meta http-equiv="Expires" content="-1" />  
<meta http-equiv="Cache-Control" content="no-cache" /> 
<title>pay</title>
<style>
.box{ width:450px; height:240px; margin:0 auto; position:absolute; top:50%; left:50%; margin-top:-99px; margin-left:-225px; text-align:center;}
</style>
</head>
<body>
        <form id="payBillForm" action=${req_url} method="post" >
                <input type="hidden" name="version" value=${version} />
                <input type="hidden" name="oid_partner" value=${oid_partner} />
                <input type="hidden" name="user_id" value=${user_id} />
                <input type="hidden" name="sign_type" value=${sign_type} />
                <input type="hidden" name="sign" value=${sign} />
                <input type="hidden" name="busi_partner" value=${busi_partner} />
                <input type="hidden" name="no_order" value=${no_order} />
                <input type="hidden" name="dt_order" value=${dt_order} />
                <input type="hidden" name="name_goods" value=${name_goods} />
                <input type="hidden" name="info_order" value=${info_order} />
                <input type="hidden" name="money_order" value=${money_order} />
                <input type="hidden" name="notify_url" value=${notify_url} />
                <input type="hidden" name="url_return" value=${url_return} />
                <input type="hidden" name="userreq_ip" value=${userreq_ip} />
                <input type="hidden" name="url_order" value='${url_order}' />
                <input type="hidden" name="valid_order" value=${valid_order} />
                <input type="hidden" name="risk_item" value=${risk_item} />
                <input type="hidden" name="timestamp" value=${timestamp} />
        </form>
</body>
</html>
 <script language="javascript" type="text/javascript">
 window.onload=function(){
 document.getElementById("payBillForm").submit();
 }
  </script>
