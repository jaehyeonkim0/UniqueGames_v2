$(document).ready(function() {

	var memberId = $("input[name='memberId']");
	var memberPass = $("#mpass");
	var idMsg = $("#idMsg");


	$("#check-btn-style").click(function() {
		if (memberId.val() == "") {
			idMsg.text("아이디를 입력하세요").css("font-size","11px").css("color","red").css("display","inline");
			memberId.focus();
			return false;
		} else if (!idCheck1(memberId.val())){
			idMsg.text("영문 또는 숫자로 작성해주세요").css("color","red").css("font-size","11px").css("display","inline");
			memberId.focus();
			return false;
		} else if (!idCheck2(memberId.val())){
			idMsg.text("5~10자리로 작성해주세요").css("color","red").css("font-size","11px").css("display","inline");
		} else {
			const memberId = $("input[name='memberId']");

			$.ajax({
				url: "idcheck",
				type: "get",
				data: {
					member_id : memberId.val()
				},
				success: function(result) {
					if(result == 1){
						$("#idMsg").text("이미 사용중인 아이디 입니다. 다시 입력해주세요")
							.css("color","red").css("font-size","11px").css("display","inline");
						$("input[name='member_id']").val("").focus();
					}else if(result == 0){
						$("#idMsg").text("사용 가능한 아이디 입니다")
							.css("color","blue").css("font-size","11px").css("display","inline");
						$("#mpass").focus();
					}
				}
			});
		}
	});
	//법인 회원가입 시 추가
	$("input[name='password-check'], input[name='password']").blur(function(){

		if($("input[name='password-check']").val()=="") {

			$("#pwdMsg-check").text("필수항목입니다").css("color","red").css("font-size","11px").css("display","inline");

		}else if($("input[name='password']").val()!="" && $("input[name='password-check']").val()!="") {

			if($("input[name='password']").val() == $("input[name='password-check']").val()){
				$("#pwdMsg-check").text("비밀번호가 서로 동일합니다").css("color","blue").css("font-size","11px").css("display","inline");
			}else {
				$("#pwdMsg-check").text("비밀번호가 서로 동일하지 않습니다").css("color","red").css("font-size","11px").css("display","inline");
			}
		}

	});

	$("#selectbox-mobile, #selectbox-phone, input[name='phone2'], input[name='phone3']").blur(function() {
		var phoneNum = $("#selectbox-phone").val() + $("input[name='phone2']").val() + $("input[name='phone3']").val();
		if ($("#selectbox-mobile").val() == "default" || $("#selectbox-phone").val() == "default" || $("input[name='phone2']").val() == "" || $("input[name='phone3']").val() == "") {
			$("#phoneMsg").text("전부 입력해주세요").css("color","red").css("font-size","11px").css("display","inline");
		}else {
			$.ajax({
				url : "phonecheck",
				type: "POST",
				data: {
					phone_num : phoneNum
				},

				success : function(result) {
					if(result==1) {
						$("#phoneMsg").text("이미 등록된 휴대전화입니다").css("color","red").css("font-size","11px").css("display","inline");
						return false;
					}else {
						$("#phoneMsg").text("").css("display","none");
					}
				}
			});
		}
	});

	/**
	 * 이메일 중복확인
	 * */
	$("input[name='email']").blur(function(){

		if($("input[name='email']").val() == "") {
			$("#emailMsg").text("필수항목입니다").css("color","red").css("font-size","11px").css("display","inline");
			return false;
		}else {
			$.ajax({
				url : "emailcheck",
				type: "POST",
				data: {
					email : $("input[name='email']").val()
				},
				success : function(result) {
					if(result==1) {
						$("#emailMsg").text("중복된 이메일입니다").css("color","red").css("font-size","11px").css("display","inline");
						return false;
					}else {
						$("#emailMsg").text("").css("display","none");
					}
				}
			});
		}

	});

	$("#chk-circle").click(function(){

		if($("#chk-circle").is(":checked"))
			$("input[name='chk-agree']").prop("checked",true);
		else
			$("input[name='chk-agree']").prop("checked",false);
	});

	$("input[name='chk-agree']").click(function(){

		var total = $("input[name='chk-agree']").length;
		var checked = $("input[name='chk-agree']:checked").length;

		if(total != checked){
			$("#chk-circle").prop("checked",false);
		}else if(total == checked){
			$("#chk-circle").prop("checked",true);
		}
	});

	/** Agreement **/
// Agreement
	$("a").click(function(){
		$(".modal").css("display", "block");

		if($(this).attr("class") == "detail1"){
			$(".agreement-content").html($("div.detail1").html());
		}
		else if($(this).attr("class") == "detail2"){
			$(".agreement-content").html($("div.detail2").html());
		}else {
			$(".modal").css("display", "none");
		}
	});


// Agreement-btn
	$(document).on("click", "button[name='btn-agreement']", function(){
		$(".modal").css("display", "none");
	});

	$("#company").click(function(){
		$('input[name="email1"], input[name="email2"]').attr('readonly',false);
	});
	


});





















































function idCheck1(asValue) {
	var regex1 = /^[a-zA-Z0-9]*$/;
	return regex1.test(asValue);
}
function idCheck2(asValue) {
	var regex2 = /^[a-zA-Z0-9]{5,10}$/;
	return regex2.test(asValue);
}
function pwdCheck(asValue) {
	var regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/g;
	return regex.test(asValue);
}
function nameCheck(asValue) {
	var regex =  /^[가-힣]{2,4}$/;
	return regex.test(asValue);
}
function emailCheck(asValue){
	var regex = /[A-za-z0-9]{4,20}$/;
	return regex.test(asValue);
}
function phoneCheck(asValue) {
	var regex = /^\d{3,4}$/;
	return regex.test(asValue);
}