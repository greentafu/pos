const inputName = document.getElementById('name');
const selectJobTitle = document.getElementById('jobTitle');
const inputUserId = document.getElementById('userId');
const inputUserPw = document.getElementById('userPw');
const inputTelNumber = document.getElementById('telNumber');

export function getEmployeePage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/employeePage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText, searchCategory:searchCategory},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.number, content.name, content.jobTitle, content.regDate.split('T')[0], content.telNumber].forEach(text => {
                        const newTd = document.createElement('td');
                        newTd.textContent = text;
                        newTr.appendChild(newTd);
                    });

                    defaultInsertPoint.appendChild(newTr);
                });

                resolve();
            },
            error: function(xhr) { console.log('error'); }
        });
        page++;
    });
}

export function getEmployee(){
    $.ajax({
        url:'/api/getEmployee',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputName.value = data.name;
            selectJobTitle.value = data.job_titleDTO.id;
            inputUserId.textContent = data.loginDTO.userId;
            inputTelNumber.value = data.telNumber;
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveEmployee(){
    const id = Number(divId.dataset.number);
    const name = inputName.value;
    const jobTitle = Number(selectJobTitle.value);
    const screenAuthList = [];
    const userId = inputUserId.textContent;
    const userPw = inputUserPw.value;
    const telNumber = inputTelNumber.value;

    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveEmployee',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, name:name, jobTitle:jobTitle, userId:userId, userPw:userPw, telNumber:telNumber}),
            success:function(data){
                resolve();
            },
            error: function(xhr) {
                let response = JSON.parse(xhr.responseText);
                if (response.errors) alert(response.errors.join("\n"));
                else alert("알 수 없는 에러가 발생했습니다.");
            }
        });
    });
}

export function deleteEmployee(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteEmployee',
            method:'DELETE',
            data: {id:selectedRow},
            success:function(data){
                resolve();
            },
            error: function(xhr) {
                if (xhr.status === 404) alert("존재하지 않는 항목입니다.");
                else alert("삭제 실패: " + xhr.responseText);
            }
        });
    });
}

export function resetEmployee(){
    inputName.value=null;
    selectJobTitle.selectedIndex = 0;
    inputUserPw.value=null;
    inputTelNumber.value=null;

    makeEmployeeId();
}

export function makeEmployeeId(){
    $.ajax({
        url:'/api/makeEmployeeId',
        method:'GET',
        success:function(data){
            inputUserId.textContent = data;
        }
    });
}