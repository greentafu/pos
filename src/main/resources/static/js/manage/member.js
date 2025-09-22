const inputMemberNumber = document.getElementById('memberNumber');
const inputSend = document.getElementById('send');
const inputNotSend = document.getElementById('notSend');
const divRegDate = document.getElementById('regDate');
const divTotalPayment = document.getElementById('totalPayment');
const divHavingPoint = document.getElementById('havingPoint');

export function getMemberPage(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/memberPage',
            method:'GET',
            data: {page:page, size:size, searchText:searchText},
            success:function(data){
                totalPages=data.page.totalPages;
                if(totalPages<page) finPage=true;
                const contents = data.content;
                contents.forEach(content => {
                    const newTr = document.createElement('tr');
                    newTr.id = 'area1'+content.id;

                    [content.phoneNumber, content.modDate.split('T')[0], content.points].forEach(text => {
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

export function getMember(){
    $.ajax({
        url:'/api/getMember',
        method:'GET',
        data: {id:selectedRow},
        success:function(data){
            divId.dataset.number = data.id;
            inputMemberNumber.value = data.phoneNumber;
            inputSend.checked = data.mail;
            inputNotSend.checked = !data.mail;
            divRegDate.textContent = data.regDate.split('T')[0];
            divTotalPayment.textContent = (data.totalPayment+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            divHavingPoint.textContent = (data.points+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        error: function(xhr) { console.log('error'); }
    });
}

export function saveMember(){
    const id = Number(divId.dataset.number);
    const phoneNumber = inputMemberNumber.value;
    const mail = inputSend.checked;
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/saveMember',
            method:'POST',
            contentType: "application/json",
            data: JSON.stringify({id:id, phoneNumber:phoneNumber, mail:mail}),
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

export function deleteMember(){
    return new Promise((resolve) => {
        $.ajax({
            url:'/api/deleteMember',
            method:'DELETE',
            data: {id:selectedRow},
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

export function resetMember(){
    inputMemberNumber.value=null;
    inputSend.checked=true;
    inputNotSend.checked=false;
    divRegDate.textContent='';
    divTotalPayment.textContent='';
    divHavingPoint.textContent='';
}