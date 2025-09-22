function openModal(){ document.getElementById("modalItem").style.display = "block"; }
function closeModal(){ document.getElementById("modalItem").style.display = "none"; }

function checkAdmin(){
    const passWord = document.getElementById('adminPassword').value;
    $.ajax({
        url:'/api/checkAdmin',
        method:'GET',
        data: {id:Number(selectedRow), pw:passWord},
        success:function(data){
            openAddPage();
            getPage2();
            document.getElementById('adminPassword').value = '';
            closeModal();
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}