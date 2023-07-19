$(document).ready(function () {
    setButtonColor();

    // sidebar button color
    function setButtonColor() {
        let id = $("#admin-type").val();

        $("a").css("background", "none").css("color", "black");
        $("#" + id).css("background", "linear-gradient(to right,#682CAD,#2FC0CC)").css("color", "white");

        if (id == "a-memberlist") {
            $("#btn-member-personal").css("background", "white").css("color", "rgba(57, 57, 57, 1)");
        }
    }

    // personal - company
    $('button[name = "btn-member"]').click(function () {
        $('button[name = "btn-member"]').css("background", "rgba(233, 233, 233, 1)").css("color", "rgba(110, 110, 110, 1)");
        $(this).css("background", "white").css("color", "rgba(57, 57, 57, 1)");
        $("#table-type").val($(this).val());
    });

    // show modal
    $("#btn-fk").click(function () {
        $("#modal-admin").css("display", "inline-block");
        $("button[name='company-list']").css("background", "none").css("font-weight", "normal").css("color", "black");
    });

    // hide modal
    $("#btn-modal-close").click(function () {
        $("#modal-admin").css("display", "none");
    });

    $("#btn-modal").click(function () {
        $("#modal-admin").css("display", "none");
        if ($("#company-selected-name").val() != 'undefined') {
            $("#company").val($("#company-selected-name").val());
        }
    });

    // modal - company select
    $(document).on("click", "button[name='company-list']", function () {
        $("button[name='company-list']").css("background", "none").css("font-weight", "normal").css("color", "black");
        $(this).css("background", "linear-gradient(to right, #682CAD, #2FC0CC)").css("font-weight", "bold").css("color", "white");

        var id = $(this).attr("id");
        var name = $(this).text();

        $("#cId").val(id);
        $("#company-selected-name").val(name);
    });

    // modal - search company
    $("#btn-search-company").click(function () {
        var keyword = $("#company-name").val();
        getResult(keyword)
    });

    function getResult(keyword = " ") {
        $.ajax({
            url: "/admin-company-selector",
            data: {keyword: keyword},
            success: function (result) {
                let returnData = JSON.parse(result);
                let output = "";

                if (returnData.nothing) {
                    alert("데이터가 존재하지 않습니다.");
                } else {
                    output += '<ul id="ul-company-list">';
                    for (let company of returnData.companyList) {
                        output += '<li>';
                        output += '<button type="button" name="company-list" id="' + company.id + '" value="' + company.name + '">' + company.name + '</button>';
                        output += '</li>';
                    }
                    output += '</ul>';

                    $("#ul-company-list").remove();
                    $("#hidden").after(output);
                }
            },
            error: function (xhr, status, error) {
                alert("error");
            }
        });
    }

    // go url page
    $("#btn-url").click(function () {
        let url = $("#url").val();
        window.open("http://localhost:9000" + url);
    });

    $("#btn-back").click(function () {
        window.history.back();
    });

    // file
    $('#upload-file').on('change', function () {
        let file = $("#upload-file")[0].files[0];
        let fileName = "../images/" + file.name;

        if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
            $('#imagePath').val(fileName);
            $('#path').val(fileName);
        } else {
            alert("이미지(jpg,png)만 선택 가능합니다.");
            $('#upload-file').val(null);
            $('#imagePath').val("");
        }
    });

    // game-register
    $("#btn-register").click(function (){
        if($("#name").val()==""){
            alert("게임 제목을 입력해주세요.");
        }
        else if($("#company").val()==""){
            alert("게임사를 선택해주세요.");
        }
        else if($("#genre").val()=="default"){
            alert("장르를 선택해주세요.");
        }
        else if($("#imagePath").val()==""){
            alert("게임 이미지를 선택해주세요.");
        }
        else if($("#description").val()==""){
            alert("상세설명을 작성해주세요.");
        }
        else {
            $("#form-register").submit();
        }
    });

    // game-update
    $("#btn-update").click(function (){
        if($("#name").val()==""){
            alert("게임 제목을 입력해주세요.");
        }
        else if($("#company").val()==""){
            alert("게임사를 선택해주세요.");
        }
        else if($("#genre").val()=="default"){
            alert("장르를 선택해주세요.");
        }
        else if($("#imagePath").val()==""){
            alert("게임 이미지를 선택해주세요.");
        }
        else if($("#description").val()==""){
            alert("상세설명을 작성해주세요.");
        }
        else {
            $("#form-update").submit();
        }
    });
});