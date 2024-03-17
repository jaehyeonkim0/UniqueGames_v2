let code = "";
let output = "";
let request = new XMLHttpRequest();
function memberIdPassCheck() {
	var memberId = document.querySelector("input[name='memberId']");
	var password = document.querySelector("input[name='password']");
	var memberForm = document.getElementById("individual-loginForm");
	if(memberId.value === "") {
		alert("아이디는 필수입니다");
		memberId.focus();
		return false;
	}else if(password.value=="") {
		alert("비밀번호는 필수입니다");
		password.focus();
		return false;
	}
	if(memberForm != null) {
		memberForm.submit();
	}
	return true;
}

function memberPassVisible() { //회원가입, 비밀번호 변경 페이지
	var isChecked = document.getElementById("pwd-check-img").checked;
	var password = document.querySelector("input[name='password']");

	if (!isChecked) {
		password.type = "password";
	} else {
		password.type = "text";
	}
}

function memberPassCheckVisible() { //회원가입, 비밀번호 변경 페이지
	var isChecked = document.getElementById("pwd-check-img-1").checked;
	var passwordCheck = document.querySelector("input[name='password-check']");

	if (!isChecked) {
		passwordCheck.type = "password";
	} else {
		passwordCheck.type = "text";
	}
}

function mypagePassVisible() {
	var isChecked = document.getElementById("mypage-pwd-check-img").checked;
	var mypageNewpass = document.querySelector("input[name='newpassword']");

	if(!isChecked) {
		mypageNewpass.type = "password";
	}else {
		mypageNewpass.type = "text";
	}
}

function mypagePassCheckVisible() {
	var isChecked = document.getElementById("mypage-pwd-check-img-1").checked;
	var mypageNewpassCheck = document.querySelector("input[name='newpassword-check']");

	if(!isChecked) {
		mypageNewpassCheck.type = "password";
	} else {
		mypageNewpassCheck.type = "text";
	}
}


function sendEmail() { //회원가입, 개인, 법인 마이페이지
	let email = document.querySelector("input[name='email']").value;
	request.open("POST", "mailCheck", true);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	request.onreadystatechange = function () {
		if (request.readyState === 4 && request.status === 200) {
			alert("인증번호가 전송되었습니다.");
			code = request.responseText;

			var emailAuthCheckInput = document.getElementById('email-auth-check');
			if (!emailAuthCheckInput) { // 입력 필드가 존재하지 않을 때만 생성
				output += "<li>";
				output += "<input id='email-auth-check' name='email-auth-check' placeholder='인증번호 6자리'>";
				output += "</li>";

				var nameInput = document.querySelector(".button-style");
				nameInput.insertAdjacentHTML("afterend", output);

				emailAuthCheckInput = document.getElementById('email-auth-check');
				emailAuthCheckInput.addEventListener('input', checkEmailAuth);
			}
		}
	};
	request.send("email=" + email);
}

function checkEmailAuth() { //회원가입, 개인, 법인 마이페이지
	var inputCode = document.querySelector('#email-auth-check').value;
	var resultMsg = document.querySelector('.messageStyle2');

	if (inputCode === code) {
		resultMsg.textContent = '인증번호가 일치합니다.';
		resultMsg.style.fontSize = '11px';
		resultMsg.style.color = 'blue';
		resultMsg.style.display = 'inline';
		document.querySelector('#email-auth-check').disabled = true;
		document.querySelector('input[name="email"]').readOnly = true;
	} else {
		resultMsg.textContent = '인증번호가 불일치합니다. 다시 확인해주세요!';
		resultMsg.style.fontSize = '11px';
		resultMsg.style.color = 'rgb(255, 0, 0)';
		resultMsg.style.display = 'inline';
	}
}


function sendIdToEmail() {
	let email = document.querySelector("input[name='email']").value;
	let type2 = document.querySelector("input[name='type2']").value;
	let emailAuth = document.querySelector("#emailAuth");
	console.log(email);
	console.log(type2);

	request.open("POST", "sendIdToEmail", true);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	request.onreadystatechange = function () {
		if (request.readyState === 4) {
			if (request.status === 200) {
				alert("귀하의 이메일 주소로 가입된 아이디를 발송 하였습니다");
				console.log(request.responseText);
			} else if (request.status === 404) {
				emailAuth.innerHTML = "입력한 회원정보로 가입한 계정이 없습니다";
				emailAuth.style.color = "red";
				emailAuth.style.fontSize = "11px";
			}
		}
	};
	request.send("email=" + encodeURIComponent(email) + "&type2=" + encodeURIComponent(type2));
}

function sendPassToEmail() {
	let email = document.getElementById("email").value;
	let id = document.getElementById("id").value;
	let emailAuth = document.querySelector("#emailAuth2");
	console.log(email);
	let type2 = document.querySelector("input[name='type2']").value;
	request.open("POST", "sendPassToEmail", true);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	request.onreadystatechange = function () {
		if (request.readyState === 4) {
			if (request.status === 200) {
				alert("귀하의 이메일 주소로 임시 비밀번호를 발송 하였습니다");
				console.log(request.responseText);
			} else if (request.status === 404) {
				emailAuth2.innerHTML = "입력한 회원정보로 가입한 계정이 없습니다";
				emailAuth2.style.color = "red";
				emailAuth2.style.fontSize = "11px";
			}
		}
	};
	request.send("email=" + encodeURIComponent(email) + "&id=" + encodeURIComponent(id) + "&type2=" + encodeURIComponent(type2));
}

function companyIdPassCheck() {
	var companyId = document.querySelector("input[name='companyId']");
	var password = document.getElementById("companyPass");
	var companyForm = document.getElementById("company-loginForm");

	if(companyId.value === "") {
		alert("아이디는 필수입니다");
		companyId.focus();
		return false;
	}else if(password.value === "") {
		alert("비밀번호는 필수입니다");
		password.focus();
		return false;
	}
	if(companyForm != null) {
		companyForm.submit();
	}
	return true;
}
function changePassword() {
	var changePassForm = $("#changePassForm");

	if ($("input[name='newpassword']").val() == "") {
		alert("새 비밀번호를 입력하세요");
		$("input[name='newpassword']").focus();
		return false;
	} else if ($("input[name='newpassword']").val() != $("input[name='newpassword-check']").val()) {
		alert("비밀번호가 일치하지 않습니다");
		$("input[name='newpassword-check']").focus();
		return false;
	} else {
		changePassForm.submit();
	}
}
function findAddress() {
	new daum.Postcode({
		oncomplete: function(data) {
			$("input[name='addr1']").val("("+data.zonecode+") "+data.address);
			$("input[name='addr2']").focus();
		}
	}).open();
}
document.addEventListener('DOMContentLoaded', function() {
	var urlParams = new URLSearchParams(window.location.search);
	var selectedTab = urlParams.get('selectedTab');
	if (selectedTab) {
		document.getElementById(selectedTab).checked = true;
	}
});