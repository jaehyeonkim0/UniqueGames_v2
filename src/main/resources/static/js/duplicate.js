
$(document).ready(function(){
    let type2 = $("input[name='type2']");       let id = $("#id");
    let password = $("input[name='password']"); let newpassword = $("input[name='newpassword']");       let newpasswordCheck = $("input[name='newpassword-check']");
    let email = $("input[name='email']");       let name = $("input[name='name']");
    let email2 = $("#email");                   let name2 = $("#name");
    let tel = $(".tel");		                let phoneNum = $("input[name='phoneNum']");
    let memberId = $("#memberId");              let companyId = $("#companyId");
    let memberEmail = $("#memberEmail");        let companyEmail = $("#companyEmail");
    let memberPass = $("#memberPass");          let companyPass = $("#companyPass");
    let memberPhoneNum = $("#memberPhoneNum");  let companyPhoneNum = $("#companyPhoneNum");
    let modal = $(".modal2");                   let modaltype = $("input[name='modaltype']");

    /**이메일 중복체크*/
    $(email).change(function() {
        if(email.val() == "") {
            $("#emailMsg").text("필수항목입니다").css("color","red").css("font-size","11px").css("display","inline");
            return false;
        } else {
            $.ajax({
                url: "emailduplicatecheck",
                type: "POST",
                data: {
                    email: email.val(),
                    type2: type2.val(),
                },
                success : function (result) {
                    if(result==1) {
                        if(email.val() == memberEmail.val() || email.val() == companyEmail.val()) {
                            $("#emailMsg").css("color", "initial").css("display","none");
                        }else {
                            $("#emailMsg").text("중복된 이메일입니다").css("color","red").css("font-size","11px").css("display","inline");
                            $("#emailAuth").text("").css("display", "none");
                        }
                        return false;
                    }else {
                        $("#emailMsg").css("color", "initial").css("display","none");
                    }
                }
            });
        }
    });

    /**전화 중복체크*/
    $(tel)
        .add(phoneNum)
        .change(function (){
            if(tel.val() == "" || phoneNum.val() == "") {
                $("#phoneMsg").text("통신사, 휴대전화 전부 입력해주세요").css("color","red").css("font-size","11px").css("display","inline");
            }else {
                $.ajax({
                    url : "phonecheck1",
                    type: "POST",
                    data: {
                        phone_num : phoneNum.val(),
                        type2 : type2.val()
                    },
                    success : function (result) {
                        if(result==1) {
                            if(phoneNum.val() == memberPhoneNum.val() || phoneNum.val() == companyPhoneNum.val()) {
                                $("#phoneMsg").text("").css("color","initial").css("display","none");
                            }else {
                                $("#phoneMsg").text("이미 등록된 번호입니다").css("color","red").css("font-size","11px").css("display","inline");
                            }
                            return false;
                        }else {
                            $("#phoneMsg").css("color","initial").css("display","none");
                        }
                    }
                });
            }
        });

    /**mypage - newpass*/
    $("#mypageChangePass").click(function () {
        if (password.val() == "") {
            alert("현재 비밀번호를 입력하세요!");
            password.focus();
            return false;
        } else if (newpassword.val() == "") {
            alert("새 비밀번호를 입력하세요");
            newpassword.focus();
            return false;
        } else if (newpasswordCheck.val() == "") {
            alert("새 비밀번호 확인칸을 입력하세요");
            newpasswordCheck.focus();
            return false;
        } else if (newpassword.val() != newpasswordCheck.val()) {
            alert("비밀번호가 동일하지 않습니다");
            newpasswordCheck.focus();
            return false;
        } else if (password.val() != memberPass.val() && password.val() != companyPass.val()) {
            alert("현재 비밀번호가 일치하지 않습니다\n다시 입력해주세요");
            password.focus();
            return false;
        } else {
            $("#newpassForm").submit();
        }
    });

    /**delete account*/
    $("#deleteButton").click(function() {
        if (password.val() == "") {
            alert("비밀번호를 입력하세요");
            password.focus();
            return false;
        } else if(password.val() != memberPass.val() && password.val() != companyPass.val()){
            alert("비밀번호가 일치하지 않습니다");
            password.focus();
            return false;
        }else {
            $.ajax({
                url: "deletecheck1",
                type: "POST",
                data: {
                    id: id.val(),
                    password: password.val(),
                    type2 : type2.val()
                },
                success: function(result) {
                    if (result == 1) {
                        modal.show();
                        $(".delete-member-id").html(id.val());
                        $(".agreement-content1").html($(".deleteComplete").html());
                    }
                }
            });
        }
    });

    /** modal */
    $(document).on("click", "#gotoFind", function(event){
        modal.hide();
        email.val("").focus();
        id.val("");
        name.val("");
    });

    $(document).on("click", "#gotoPwd", function(event){
        modal.hide();
        location.href="/findMember?selectedTab=findPwd";
        if(modaltype.val() == 'member'){
            location.href="/findMember?selectedTab=findPwd";
        }else {
            location.href="/findCompany?selectedTab=findPwd";
        }
    });

    $(document).on("click", "#gotoLogin", function(event){
        modal.hide();
        if(modaltype.val() == 'member'){
            location.href="/login";
        }else {
            location.href="/login?selectedTab=company-login";
        }
    });

    $(document).on("click", "#gotoJoin", function(event){
        modal.hide();
        if(modaltype.val() == 'member'){
            location.href="/join";
        }else {
            location.href="/joincompany";
        }
    });

    $(document).on("click", "#home", function(event) {
        modal.hide();
        location.href="/";
    });
});