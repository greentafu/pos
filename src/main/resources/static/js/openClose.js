const numberList = [50000, 10000, 5000, 1000, 500, 100, 50, 10];

document.addEventListener("DOMContentLoaded", () => {
    cashTable();
});

function cashTable(){
    const allInput = document.querySelectorAll('.autoComma');
    allInput.forEach(input => {
        input.addEventListener("input", (event) => {
            let total = 0;
            for(let i=0; i<8; i++){
                let number = document.getElementById('input'+numberList[i]);
                number = Number(number.value.replace(/\D/g, ''));
                if(number===0) document.getElementById('input'+numberList[i]).value=0;
                const multi = number*numberList[i];
                total+=multi;
                document.getElementById('td'+numberList[i]).textContent = (multi+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
            }
            document.getElementById('tdTotal').textContent = (total+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
        });
    });
}

function openStore(){
    const total = document.getElementById('tdTotal').textContent.replace(/\D/g, '');
    const pos = document.getElementById('pos').value;
    const employeeId = document.getElementById('employeeId').value;
    const employeePw = document.getElementById('employeePw').value;

    $.ajax({
        url:'/api/openStore',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({total:total, pos:pos, employeeId:employeeId, employeePw:employeePw}),
        success:function(data){ window.location.href = '/home/page1'; },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function closeStore(){
    const total = document.getElementById('tdTotal').textContent.replace(/\D/g, '');
    const recordId = document.getElementById('recordId').textContent;
    const employeeId = document.getElementById('employeeId').value;
    const employeePw = document.getElementById('employeePw').value;

    $.ajax({
        url:'/api/closeStore',
        method:'PUT',
        contentType: "application/json",
        data: JSON.stringify({total:total, recordId:recordId, employeeId:employeeId, employeePw:employeePw}),
        success:function(data){ window.location.href = '/home/page1'; },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
